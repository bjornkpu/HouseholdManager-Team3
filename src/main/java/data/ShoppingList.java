package data;
import java.util.ArrayList;
/**
 * Class for the ShoppingList-object
 *
 * @author
 */
public class ShoppingList {
    private int id;
    private String name;
    private int groupId;
    private ArrayList<Item> itemList;
    private ArrayList<User> userList;

    public ShoppingList() {
    }

    public ShoppingList(int id, String name){
        this.id = id;
        this.name = name;
        itemList = new ArrayList<>();
        userList = new ArrayList<>();
    }

    public ShoppingList(String name, int groupId,
                        ArrayList<User> userList) {
        this.name = name;
        this.groupId = groupId;
        this.itemList = new ArrayList<Item>();
        this.userList = userList;
    }

    public ShoppingList(int id, String name, int groupId,
                        ArrayList<Item> itemList, ArrayList<User> userList) {
        this.id = id;
        this.name = name;
        this.groupId = groupId;
        this.itemList = itemList;
        this.userList = userList;
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

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public Item getItemFromList(int itemId){
        return itemList.get(itemId);
    }

    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    public void addItem(Item i){
        itemList.add(i);
    }

    public void addItem(int itemId, String desc, int status){
        itemList.add(new Item(itemId, desc, status));
    }

    public void addItem(int id, String name, int status, int shoppingListId, int disbursementId){
        itemList.add(new Item(id, name, status, shoppingListId, disbursementId));
    }

    public void removeItem(Item i){
        itemList.remove(i);
    }

    public void removeItem(int itemId){
    	while(itemList.contains(itemId)){
    		int i = 0;
    		if(itemList.get(i).getId() == itemId){
    			itemList.remove(i);
    			break;
		    }
	    }
    }

    public void addUser(User u){
        userList.add(u);
    }

    public void removeUser(User u){
        userList.remove(u);
    }

    public void removeUser(String userId){
        userList.remove(userId);
    }

    @Override
    public String toString() {
        return "ShoppingList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", groupId=" + groupId +
                ", itemList=" + itemList.toString() +
                ", userList=" + userList.toString() +
                '}';
    }
}
