package db;

import data.Group;
import data.ShoppingList;
import data.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import util.Logger;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * -Description of the class-
 * @author enoseber
 */
public class ShoppingListDaoTest {
    private static int slId;
    private static int groupId;
    private static User u;
    private static Group g;
    private static ShoppingList shoppingListTest;
    private static ArrayList<User> userList;

    @BeforeClass
    public static void setUp() throws SQLException{
        slId = 45;
        groupId = 1;


        u = new User("shoppinglistTest@user.no", "shoppinglistTestUser", "", "");
        g = new Group(groupId, "shoppingListTestGroup", "", u.getEmail());

        UserDao.addUser(u);
        GroupDao.addGroup(g);

        groupId = GroupDao.getGroupByName("shoppingListTestGroup").get(0).getId();
        g.setId(groupId);

        userList = new ArrayList<User>();
        userList.add(u);

        shoppingListTest = new ShoppingList(slId, "shoppingListTest",
                groupId, null, userList);

        ShoppingListDao.addShoppingList(shoppingListTest);
    }

    @Test
    public void addShoppingList() throws SQLException{
        ShoppingList sl = new ShoppingList();
        sl.setId(slId);
        ShoppingListDao.addShoppingList(sl);
    }

    @Test
    public void testGetShoppingList(){
        ShoppingList sl = new ShoppingList();

        try {
            sl = ShoppingListDao.getShoppingList(slId, u.getEmail());
        } catch(SQLException e){
            e.printStackTrace();
        }

        assertEquals(shoppingListTest.getId(), sl.getId());
    }

    @Test
    public void testGetShoppingListByGroupId(){
        ArrayList<ShoppingList> slList = new ArrayList<ShoppingList>();
        ShoppingList testGetByGroup = new ShoppingList(46, "getByGroupIdTest", groupId, null, userList);

//        adds new shoppinglist to database
        try {
            ShoppingListDao.addShoppingList(testGetByGroup);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//      gets both shoppinglists from database connected to groupId
        try {
            slList = ShoppingListDao.getShoppingListByGroupid(groupId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//      deletes extra added shoppinglist from database
        try {
            ShoppingListDao.delShoppingList(testGetByGroup.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertNotNull(slList);

        assertEquals(shoppingListTest.getId(), slList.get(0).getId());

        assertEquals(testGetByGroup.getId(), slList.get(1).getId());
    }

    @Test
    public void testGetShoppingListByUser(){
        ArrayList<ShoppingList> slList = new ArrayList<ShoppingList>();

        try {
            slList = ShoppingListDao.getShoppingListByUser(u);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertNotNull(slList);
        assertEquals(shoppingListTest.getId(), slList.get(0).getId());
    }

    @Test
    public void testUpdateShoppingList(){
        ShoppingList updatedSl = new ShoppingList(slId, "nyttNavn", groupId, null, userList);
        ShoppingList updateTest = new ShoppingList();

        try {
            ShoppingListDao.updateShoppingList(updatedSl);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            updateTest = ShoppingListDao.getShoppingList(slId, u.getEmail());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(updatedSl.getId(), updateTest.getId());
        assertEquals(updatedSl.getName(), updateTest.getName());

    }

    @AfterClass
    public static void tearDown() throws SQLException{
        ShoppingListDao.delShoppingList(slId);
        GroupDao.deleteGroup(g);
        UserDao.delUser(u.getEmail());
    }

}
