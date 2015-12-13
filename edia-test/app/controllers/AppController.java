package controllers;

import models.Document;
import play.*;
import play.data.Form;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.mvc.*;

import views.html.*;

import java.util.ArrayList;
import java.util.List;

import static play.data.Form.form;

public class AppController extends Controller {

    public Result index() {

        List<Document> documentList = new ArrayList();
        documentList = Document.find.all();

        return ok(index.render(documentList));
    }


    public Result edit(Long id){

        return TODO;
    }

    public Result update(Long id){

        return TODO;
    }

    public Result create(){

        //Here we create a form from the class Document. This is a play feature. We pass the form to the view to fill it up
        Form<Document> documentForm = form(Document.class);

        return ok(createDocument.render(documentForm));
    }

    public Result save(){

        //We fill the documentForm (we create it like in the method create) but now we fill it with the form we receive from the view.
        Form<Document> documentForm = form(Document.class).bindFromRequest();

        //Check that the form doesn't constain errors
        if(documentForm.hasErrors()) {
            List<ValidationError> list = new ArrayList<ValidationError>();
            list.add(new ValidationError("GLOBAL", "The form contains errors. Please fix them before continue"));
            documentForm.errors().put("GLOBAL", list);

            return badRequest(createDocument.render(documentForm));
        }

        //Parsing del form to a Document Object
        Document doc = documentForm.get();

        //Store in DDBB
        doc.save();
        return redirect(routes.AppController.index());
    }

}
