package data;

import java.util.Date;

public class Notification {
    private int id;
    private String userEmail;
    private String text;
    private Date date;
    private int seen;

    public Notification() {}

    public Notification(int id, String userEmail, String text, Date date) {
        this.id = id;
        this.userEmail = userEmail;
        this.text = text;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }
}
