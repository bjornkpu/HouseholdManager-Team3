package data;

/**
 * Class for the Item-object
 */
public class Item {
    private int itemId;
    private String name;
    private int status;

    public Item(){
    }

    public Item (int itemId, String name, int status) {
        this.itemId = itemId;
        this.name = name;
        this.status = status;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
