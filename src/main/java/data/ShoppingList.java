package data;

import java.util.ArrayList;

/**
 * Class for the ShoppingList-object
 */
public class ShoppingList {
    private int shoppingListId;
    private String shoppingListName;
    private ArrayList<Item> itemList;
    private ArrayList<User> userList;

    public ShoppingList() {
    }

    public ShoppingList(int shoppingListId, String shoppingListName){
        this.shoppingListId = shoppingListId;
        this.shoppingListName = shoppingListName;
    }

    public ShoppingList(int shoppingListId, String shoppingListName, ArrayList<Item> itemList, ArrayList<User> userList) {

        this.shoppingListId = shoppingListId;
        this.shoppingListName = shoppingListName;
        this.itemList = itemList;
        this.userList = userList;
    }

    public int getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(int shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public String getShoppingListName() {
        return shoppingListName;
    }

    public void setShoppingListName(String shoppingListName) {
        this.shoppingListName = shoppingListName;
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

    public void addItem(int itemId, String desc, double price, int userId){
//        itemList.add(new Item(itemId, desc, price, userId));
    }

    public void removeItem(Item i){
        itemList.remove(i);
    }

    public void removeItem(int itemId){
        itemList.remove(itemId);
    }

    public void addUser(User u){
        userList.add(u);
    }

    public void removeUser(User u){
        userList.remove(u);
    }

    public void removeUser(int userId){
        userList.remove(userId);
    }

    public double getSumOfItems(){
        //TODO calculate sum price of items
        return 0;
    }
}
