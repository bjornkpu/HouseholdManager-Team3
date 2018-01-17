package db;

import data.Group;
import data.Item;
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
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testItemInShoppingList() throws SQLException {
    	int itemId = 451;
    	String itemName = "test item";
    	int itemStatus = 0;
    	int slId = 946;
    	int disId = 467;

	    Item smallItem = new Item(itemId, itemName, itemStatus);
	    Item mediumItem = new Item(itemId+1, itemName+"1", itemStatus);
	    Item longItem = new Item(itemId+2, itemName+" 2", itemStatus, slId+1, disId+1);

	    ArrayList<Item> itemList = new ArrayList<>();
	    itemList.add(smallItem);
	    itemList.add(mediumItem);
	    itemList.add(longItem);

	    testSmallShoppingList.addItem(itemList.get(0));
	    testSmallShoppingList.addItem(itemList.get(1));
	    testSmallShoppingList.addItem(itemList.get(2));

	    assertEquals(testSmallShoppingList.getItemList(), itemList);
	    assertEquals(testSmallShoppingList.getItemFromList(1), itemList.get(1));

	    testSmallShoppingList.removeItem(smallItem);
	    testSmallShoppingList.removeItem(mediumItem);
	    testSmallShoppingList.removeItem(itemId+2);


    }

    @Test
    public void testUserInShoppingList(){
    	User testUser = new User( "b@k.com",  "bk",  "123",  "456",  "789");
	    testSmallShoppingList.addUser(testUser);
	    User testUser2 = testUser;
	    testUser2.setEmail("b@k.no");
	    testSmallShoppingList.addUser(testUser2);
    	assertTrue(testSmallShoppingList.getUserList().size() == 2);
    	testSmallShoppingList.removeUser(testUser);
	    assertTrue(testSmallShoppingList.getUserList().size() == 1);
	    testSmallShoppingList.removeUser("b@k.no");

    }

    @Test
    public void testToString() {
	    ArrayList<Item> itemList = new ArrayList<Item>();
	    itemList.add(new Item(8574, "toStringTest", 0));
	    testSmallShoppingList.addItem(itemList.get(0));

	   String expected = "ShoppingList{" +
			   "id=46, " +
			   "name='shoppingListTest', " +
			   "groupId=0, " +
			   "itemList=[" +
				   "Item{" +
					   "id=453, " +
					   "name='test item 2', " +
					   "status=0, " +
					   "shoppingListId=947, " +
					   "disbursementId=468}, " +
				   "Item{" +
					   "id=8574, " +
					   "name='toStringTest', " +
					   "status=0, " +
					   "shoppingListId=-1, " +
					   "disbursementId=-1}], " +
			   "userList=[]}";
	    assertEquals(expected, testSmallShoppingList.toString());
	    testSmallShoppingList.removeItem(8574);
    }

    @AfterClass
    public static void tearDown() throws SQLException{


        ShoppingListDao.delShoppingList(slId);
        GroupDao.deleteGroup(testGroup);
        UserDao.delUser(testUser.getEmail());
    }

}
