package models;

/**
 * Created by hugo on 12/12/15.
 */

import com.avaje.ebean.Model;
import com.representqueens.lingua.en.Fathom;
import com.representqueens.lingua.en.Readability;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Document extends Model {


    //Finder to interect with the database and make sql querys
    public static Finder<Long, Document> find = new Finder<>(Long.class, Document.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Required(message = "ERROR: Please fill the Title.")
    private String title;

    @Required(message = "ERROR: Please fill the Description.")
    private String description;

    private Date createdDate;

    private float textScore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public float getTextScore() {
        return textScore;
    }

    public void setTextScore(float textScore) {
        this.textScore = textScore;
    }

}

