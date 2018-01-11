package data;

import java.util.Date;

public class WallPost {
    private Date datePosted; //id
    private String message;

    public WallPost() {
    }

    public WallPost(String message, Date datePosted) {
        this.message = message;
        this.datePosted = datePosted;
    }

    public String getDescription() {
        return message;
    }

    public void setDescription(String description) {
        this.message = description;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }
}
