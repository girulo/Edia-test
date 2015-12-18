package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Document;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import play.Play;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.*;

import utils.ElasticsearchTransportClient;
import utils.TextScore;
import views.html.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static play.data.Form.form;

public class AppController extends Controller {

    public Result index() {

        List<Document> documentList = Document.find.where().orderBy().desc("createdDate").findList();

        return ok(index.render(documentList));
    }

    public Result search() {

        String searchTerms = Form.form().bindFromRequest().get("searchBox");

//        SearchResponse response = client.prepareSearch("document", "description")
//                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                .setQuery(termQuery("description", "*name*"))
//                .setFrom(0).setSize(60).setExplain(true)
//                .execute()
//                .actionGet();

        SearchResponse response;
        try {
            response = ElasticsearchTransportClient.getClient()
                    .prepareSearch("edia", "document")
                    .setQuery(QueryBuilders.matchQuery("description", searchTerms)).execute().actionGet();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //TODO: Feed the list witht eh results of Elasticsearch
        List<Document> documentList = Document.find.where().orderBy().desc("createdDate").findList();


        return ok(index.render(documentList));
    }

    public Result edit(Long docId){

        Document doc = Document.find.byId(docId);

        Form<Document> documentForm = form(Document.class).fill(doc);
        return ok(editDocument.render(documentForm));

    }

    /*We can implement this method together with save(), so basically they do the same function with the different that
    one executes save() and the other one update()
    The condition will be the next after doing the Document doc = documentForm.get();"
    if (doc.getId() == null) we will do doc.save(), otherwise we will do doc.update().

    But i made 2 methods only to show the differencies
     */
    public Result update(Long docId){

        //We fill the documentForm (we create it like in the method create) but now we fill it with the form we receive from the view.
        Form<Document> documentForm = form(Document.class).bindFromRequest();

        //Check that the form doesn't contains errors
        if(documentForm.hasErrors()) {
            List<ValidationError> list = new ArrayList<ValidationError>();
            list.add(new ValidationError("GLOBAL", "The form contains errors. Please fix them before continue"));
            documentForm.errors().put("GLOBAL", list);

            return badRequest(createDocument.render(documentForm));
        }

        //Parsing del form to a Document Object
        Document doc = documentForm.get();

        //Store in DDBB
        doc.update();
        return redirect(routes.AppController.index());
    }

    public Result create(){

        //Here we create a form from the class Document. This is a play feature. We pass the form to the view to fill it up
        Form<Document> documentForm = form(Document.class);

        return ok(createDocument.render(documentForm));
    }

    public Result save() throws IOException {

        //We fill the documentForm (we create it like in the method create) but now we fill it with the form we receive from the view.
        Form<Document> documentForm = form(Document.class).bindFromRequest();

        //Check that the form doesn't contains errors
        if(documentForm.hasErrors()) {
            List<ValidationError> list = new ArrayList<ValidationError>();
            list.add(new ValidationError("GLOBAL", "The form contains errors. Please fix them before continue"));
            documentForm.errors().put("GLOBAL", list);

            return badRequest(createDocument.render(documentForm));
        }

        //Parsing the form to a Document Object
        Document doc = documentForm.get();
        doc.setCreatedDate(new Date());

        doc.setTextScore(TextScore.calculateScore(doc));

        //Store in DDBB
        doc.save();

        //Put in the Elasticsearch index (index=edia, type=document

        String indexName = Play.application().configuration().getString("index.name");

        ObjectMapper mapper = new ObjectMapper();
        byte[] jsonToIndex = mapper.writeValueAsBytes(doc);
        System.out.println(mapper.toString());

        IndexResponse response = ElasticsearchTransportClient.getClient()
                .prepareIndex("edia", "document")
                .setSource(jsonBuilder()
                                .startObject()
                                .field("title", doc.getTitle())
                                .field("description", doc.getDescription())
                                .field("createdDate", doc.getCreatedDate())
                                .field("textScore", doc.getTextScore())
                                .endObject()
                )
                .get();

        return redirect(routes.AppController.index());
    }

}
