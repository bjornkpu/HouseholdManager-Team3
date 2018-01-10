package data;

import java.util.Date;

public class WallPost {
    int wallPostId;
    String description;
    Date datePosted;

    public WallPost() {
    }

    public WallPost(int wallPostId, String description, Date datePosted) {
        this.wallPostId = wallPostId;
        this.description = description;
        this.datePosted = datePosted;
    }

    public int getWallPostId() {
        return wallPostId;
    }

    public void setWallPostId(int wallPostId) {
        this.wallPostId = wallPostId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }
}
