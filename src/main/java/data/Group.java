package data;

import java.util.ArrayList;

/**
 * Class for the Group-object
 */
public class Group {
    int groupId;
    String groupName;
    String description;
    User admin;
    ArrayList<User> userList;
    ArrayList<ShoppingList> shoppingListList;
    ArrayList<Todo> todoList;
    ArrayList<WallPost> wallPostList;

    public Group(){
    }

    public Group(int groupId, String groupName, String description, User admin) {
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

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public User getUserFromList(int userId){
        return userList.get(userId);
    }

    public void addUser(User u){
        userList.add(u);
    }

    public void removeUser(User u){
        userList.remove(u);
    }

    public ArrayList<ShoppingList> getShoppingListList() {
        return shoppingListList;
    }

    public ShoppingList getShoppingListFromList(int shoppingListId){
        return shoppingListList.get(shoppingListId);
    }

    public void addShoppingList(ShoppingList sl){
        shoppingListList.add(sl);
    }

    public void removeShoppingList(ShoppingList sl){
        shoppingListList.remove(sl);
    }

    public ArrayList<Todo> getTodoList() {
        return todoList;
    }

    public Todo getTodoFromList(int todoId){
        return todoList.get(todoId);
    }

    public void addTodo(Todo t){
        todoList.add(t);
    }

    public void removeTodo(Todo t){
        todoList.remove(t);
    }

    public ArrayList<WallPost> getWallPostList() {
        return wallPostList;
    }

    public WallPost getWallPostFromList(int wallPostId){
        return wallPostList.get(wallPostId);
    }

    public void addWallpost(WallPost wp){
        wallPostList.add(wp);
    }

    public void removeWallPost(WallPost wp){
        wallPostList.remove(wp);
    }

    public void changeAdmin(User u){
        admin = u;
    }

    public void closeGroup(){
        //TODO delete any information in group and close it
    }
}
