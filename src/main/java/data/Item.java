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
    private int status=STANDARD;
    private int shoppingListId = -1;
    private int disbursementId = -1;

    public static final int PURCHASED = 0;
    public static final int GOING_TO_BE_BOUGHT = 2;
    public static final int STANDARD = 1;

    public Item(){
    }

    public Item(int id, String name, int status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Item(int id, String name, int status, int shoppingListId, int disbursementId) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.shoppingListId = shoppingListId;
        this.disbursementId = disbursementId;
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

    public int getDisbursementId() { return disbursementId; }

    public void setDisbursementId(int disbursementId) { this.disbursementId = disbursementId; }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", shoppingListId=" + shoppingListId +
                ", disbursementId=" + disbursementId +
                '}';
    }
}
