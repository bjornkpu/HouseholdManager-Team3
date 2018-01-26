package db;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import data.Group;
import data.Item;
import data.ShoppingList;
import data.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Logger;
import util.LoginCheck;

import java.sql.Connection;
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
    private static final Logger log = Logger.getLogger();
    private static Connection connection;
    private static ShoppingListDao shoppingListDao;
    private static UserDao userDao;
    private static GroupDao groupDao;

    private static int slId;
    private static int shopListId = -1;
    private static int smallShopListID = -1;
    private static int groupId;
    private static User testUser;
    private static Group testGroup;
    private static ShoppingList shoppingListTest, testSmallShoppingList;
    private static ArrayList<User> userList;

    @BeforeClass
    public static void setUp(){
        try{
            connection=Db.instance().getConnection();
        }catch(SQLException e){
            log.error("Could not get connection", e);
        }
        shoppingListDao= new ShoppingListDao(connection);
        userDao= new UserDao(connection);
        groupDao = new GroupDao(connection);

        slId = 45;

        try{
            testUser = new User("shoppinglistTest@user.no", "shoppinglistTestUser", "", "", LoginCheck.getSalt());
            testGroup = new Group(groupId, "shoppingListTestGroup", "", testUser.getEmail());

            userDao.addUser(testUser);
            groupId = groupDao.addGroup(testGroup);

            //groupId = groupDao.getGroupByName("shoppingListTestGroup").get(0).getId();
            testGroup.setId(groupId);

            userList = new ArrayList<User>();
            userList.add(testUser);

            shoppingListTest = new ShoppingList(groupId,"shoppingListTestRegular",
                    groupId, null, userList);

            testSmallShoppingList = new ShoppingList(groupId, "shoppingListTestSmallShoppingList");
            testSmallShoppingList.setGroupId(groupId);

            shopListId = shoppingListDao.addShoppingList(shoppingListTest);
            smallShopListID = shoppingListDao.addShoppingList(testSmallShoppingList);
            shoppingListTest.setId(shopListId);
            testSmallShoppingList.setId(smallShopListID);
        }catch(MySQLIntegrityConstraintViolationException e) {
            log.error("Constraint error", e);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    public void addShoppingList() throws SQLException{
        ShoppingList sl = new ShoppingList();
        sl.setId(384);
        sl.setName("ARNEUSERLIST123");
        sl.setGroupId(2);
        int ok = shoppingListDao.addShoppingList(sl);
        assertTrue(ok != -1);
        shoppingListDao.delShoppingList(ok);

    }

    @Test
    public void testGetShoppingList(){
        ShoppingList sl = new ShoppingList();

        try {
            sl = shoppingListDao.getShoppingList(shopListId, testUser.getEmail());
        } catch(SQLException e){
            e.printStackTrace();
        }

        assertEquals(shoppingListTest.getId(), sl.getId());
    }

    @Test
    public void testGetShoppingListByGroupId(){
        ArrayList<ShoppingList> slList = new ArrayList<ShoppingList>();
        ShoppingList testGetByGroup = new ShoppingList(46, "getByGroupIdTest", groupId, null, userList);
        int shop = -1;

//        adds new shoppinglist to database
        try {
            shop = shoppingListDao.addShoppingList(testGetByGroup);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        testGetByGroup.setId(shop);

//      gets both shoppinglists from database connected to groupId
        try {
            slList = shoppingListDao.getShoppingListByGroupid(groupId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//      deletes extra added shoppinglist from database
        try {
            shoppingListDao.delShoppingList(testGetByGroup.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertNotNull(slList);

        //assertEquals(shoppingListTest.getId(), slList.get(0).getId());

        //assertEquals(testGetByGroup.getId(), slList.get(1).getId());
    }

    @Test
    public void testGetShoppingListByUser(){
        ArrayList<ShoppingList> slList = new ArrayList<ShoppingList>();

        try {
            slList = shoppingListDao.getShoppingListByUser(testUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertNotNull(slList);
        assertEquals(shoppingListTest.getId(), slList.get(0).getId());
    }

    @Test
    public void testUpdateShoppingList(){
        ShoppingList updatedSl = new ShoppingList(shopListId, "nyttNavn", groupId, null, userList);
        ShoppingList updateTest = new ShoppingList();

        try {
            shoppingListDao.updateShoppingList(updatedSl);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            updateTest = shoppingListDao.getShoppingList(shopListId, testUser.getEmail());
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
    	int slId = smallShopListID;
    	int disId = 467;

	    Item smallItem = new Item(itemId, itemName, itemStatus);
	    Item mediumItem = new Item(itemId+1, itemName+"1", itemStatus);
	    Item longItem = new Item(itemId+2, itemName+" 2", itemStatus, slId, disId+1);

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
			   "id="+smallShopListID+", " +
			   "name='shoppingListTestSmallShoppingList', " +
			   "groupId="+groupId+", " +
			   "itemList=[" +
				   "Item{" +
					   "id=453, " +
					   "name='test item 2', " +
					   "status=0, " +
					   "shoppingListId="+smallShopListID+", " +
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
        try{
            shoppingListDao.delShoppingList(shopListId);
            shoppingListDao.delShoppingList(smallShopListID);
            groupDao.deleteGroup(testGroup);
            userDao.delUser(testUser.getEmail());
        }finally {
            Db.close(connection);
        }
    }

}
