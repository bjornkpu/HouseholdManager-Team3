package data;
/**
 * Class for the Item-object
 *
 * @author BK
 * @author jmska
 */
public class Item {
    private int id;
    private String name;
    private int status;
    private int shoppingListId;
    private int dispId;

    public Item(){
    }

    public Item(int id, String name, int status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Item(int id, String name, int status, int shoppingListId, int dispId) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.shoppingListId = shoppingListId;
        this.dispId = dispId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() { return status; }

    public void setStatus(int status) { this.status = status; }

    public int getShoppingListId() { return shoppingListId; }

    public void setShoppingListId(int shoppingListId) { this.shoppingListId = shoppingListId; }

    public int getDispId() { return dispId; }

    public void setDispId(int dispId) { this.dispId = dispId; }
}
