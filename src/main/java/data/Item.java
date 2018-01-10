package data;

/**
 * Class for the Item-object
 */
public class Item {
    int itemId;
    String description;
    double price;
    int userId;

    public Item(){
    }

    public Item (int elementId, String description, double price, int userId) {
        this.itemId = elementId;
        this.description = description;
        this.price = price;
        this.userId = userId;
    }

    public int getElementId() {
        return itemId;
    }

    public void setElementId(int elementId) {
        this.itemId = elementId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
