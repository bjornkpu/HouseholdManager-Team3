package db;

import data.Group;
import data.ShoppingList;
import data.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import util.LoginCheck;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * -Description of the class-
 * @author enoseber
 * @author bk
 */
public class ShoppingListDaoTest {
    private static int slId;
    private static int groupId;
    private static User testUser;
    private static Group testGroup;
    private static ShoppingList shoppingListTest, testSmallShoppingList;
    private static ArrayList<User> userList;

    @BeforeClass
    public static void setUp() throws SQLException{
        slId = 45;
        groupId = 1;


        testUser = new User("shoppinglistTest@user.no", "shoppinglistTestUser", "", "", LoginCheck.getSalt());
        testGroup = new Group(groupId, "shoppingListTestGroup", "", testUser.getEmail());

        UserDao.addUser(testUser);
        GroupDao.addGroup(testGroup);

        groupId = GroupDao.getGroupByName("shoppingListTestGroup").get(0).getId();
        testGroup.setId(groupId);

        userList = new ArrayList<User>();
        userList.add(testUser);

        shoppingListTest = new ShoppingList(slId, "shoppingListTest",
                groupId, null, userList);

	    testSmallShoppingList = new ShoppingList(46, "shoppingListTest");

        ShoppingListDao.addShoppingList(shoppingListTest);
        ShoppingListDao.addShoppingList(testSmallShoppingList);
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
            sl = ShoppingListDao.getShoppingList(slId, testUser.getEmail());
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
            slList = ShoppingListDao.getShoppingListByUser(testUser);
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
            updateTest = ShoppingListDao.getShoppingList(slId, testUser.getEmail());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(updatedSl.getId(), updateTest.getId());
        assertEquals(updatedSl.getName(), updateTest.getName());

    }

   /* @Test
    public void testItemInShoppingList(){
	    ItemDao.addItem(itemId,  desc, status)
    }
	    addItem(Item i)

	    removeItem(Item i){
		    removeItem(int itemId){
	    }
		    getItemList()
		    getItemFromList(int itemId)
    }*/

    @Test
    public void testUserInShoppingList(){

    }

    /*@Test
    public void testToString() {
	    String expected = "ShoppingList{" +
	    "id=" + 45 +
	    ", name='" + "testSmallShoppingList" + '\'' +
	    ", groupId=" + 1 +
	    ", itemList=" + ""+
	    ", userList=" + "User{" +
			    "email='" + "LoginTestEmailATemailDOTcom" + '\'' +
			    ", name='" + "User1" + '\'' +
			    ", phone='" + "90706060" + '\'' +
			    ", password='" + "123" + '\'' +
			    ", salt='" + testUser.getSalt() + '\'' +
			    '}' +
	    '}';
	    assertEquals(expected, testSmallShoppingList.toString());
    }*/

    @AfterClass
    public static void tearDown() throws SQLException{
        ShoppingListDao.delShoppingList(slId);
        GroupDao.deleteGroup(testGroup);
        UserDao.delUser(testUser.getEmail());
    }

}
