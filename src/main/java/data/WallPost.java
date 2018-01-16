package data;
import java.util.Date;
/**
 * -Description of the class-
 *
 * @author
 */
public class WallPost {
    private int id;
    private Date datePosted; //id
    private String message;
    private String postedBy;
    private int postedTo;

    //TODO what needs to be here?

    public WallPost() {
    }

    public WallPost(int id, Date datePosted, String message, String postedBy, int postedTo) {
        this.id =id;
        this.datePosted = datePosted;
        this.message = message;
        this.postedBy = postedBy;
        this.postedTo = postedTo;
    }

    public WallPost( Date datePosted, String message, String postedBy, int postedTo) {
        this.datePosted = datePosted;
        this.message = message;
        this.postedBy = postedBy;
        this.postedTo = postedTo;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String description) {
        this.message = description;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public int getPostedTo() {
        return postedTo;
    }

    public void setPostedTo(int postedTo) {
        this.postedTo = postedTo;
    }

    public int getId(){
        return id;
    }
}
