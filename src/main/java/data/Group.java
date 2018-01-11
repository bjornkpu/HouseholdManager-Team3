package data;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class for the Group-object
 */
public class Group {
    private int groupId;
    private String groupName;
    private String description;
    private String admin;
    private ArrayList<User> userList = new ArrayList<>();
    private ArrayList<ShoppingList> shoppingListList = new ArrayList<>();
    private ArrayList<Todo> todoList = new ArrayList<>();
    private ArrayList<WallPost> wallPostList = new ArrayList<>();

    public Group(){
    }

    public Group(int groupId, String groupName, String description, String admin) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.description = description;
        this.admin = admin;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void addUser(User u){
        userList.add(u);
    }

    public void removeUser(String email){
        userList.remove(email);
    }

    public ArrayList<ShoppingList> getShoppingListList() {
        return shoppingListList;
    }

    public ShoppingList getShoppingListFromList(int shoppingListId){
        return shoppingListList.get(shoppingListId);
    }

    public void createShoppingList(int id, String name, User u){ //create a ShoppingList object and add it to the list
        ShoppingList sh = new ShoppingList(id, name);
        sh.getUserList().add(u); //add the creator of the list as a user in the created object
        shoppingListList.add(sh);
    }

    public void addShoppingList(ShoppingList sl){
        shoppingListList.add(sl);
    }

    public void removeShoppingList(ShoppingList sl){
        shoppingListList.remove(sl);
    }

    public void removeShoppingList(int shoppingListId){
        shoppingListList.remove(shoppingListId);
    }

    public ArrayList<Todo> getTodoList() {
        return todoList;
    }

    public Todo getTodoFromList(int todoId){
        return todoList.get(todoId);
    }

    public void createTodo(int id, String desc){
        todoList.add(new Todo(id, desc));
    }

    public void createTodo(int id, String desc, Date refreshDate){
        todoList.add(new RepeatedTodo(id, desc, refreshDate));
    }

    public void addTodo(Todo t){
        todoList.add(t);
    }

    public void removeTodo(Todo t){
        todoList.remove(t);
    }

    public void removeTodo(int todoId){
        todoList.remove(todoId);
    }

    public ArrayList<WallPost> getWallPostList() {
        return wallPostList;
    }

    public WallPost getWallPostFromList(int wallPostId){
        return wallPostList.get(wallPostId);
    }

    public void createWallPost(int id, String desc, Date d){
        wallPostList.add(new WallPost(id, desc, d));
    }

    public void addWallpost(WallPost wp){
        wallPostList.add(wp);
    }

    public void removeWallPost(WallPost wp){
        wallPostList.remove(wp);
    }

    public void removeWallPost(int wallPostId){
        wallPostList.remove(wallPostId);
    }

    public void changeAdmin(String u){
        //TODO method to safely switch admin rights between users
        admin = u;
    }

    public void closeGroup(){
        //TODO delete any information in group and close it
    }
}
