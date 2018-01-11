package data;

/**
 * Class for the Item-object
 */
public class Item {
    private int id;
    private String description;
    private int status;
    private int userId;

    public Item(){
    }

    public Item (int id, String description, int status, int userId) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int elementId) {
        this.id = elementId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
