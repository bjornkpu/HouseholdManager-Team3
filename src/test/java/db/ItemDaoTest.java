package db;

import data.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import util.LoginCheck;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ItemDaoTest {
    private static Connection connection;
    private static ItemDao itemDao;
    private static UserDao userDao;
    private static GroupDao groupDao;
    private static DisbursementDao disbursementDao;
    private static ShoppingListDao shoppingListDao;
    private static MemberDao memberDao;


    private static Item itemTest;
    private static ShoppingList shoppingListTest;
    private static int itemId;
    private static int shoppingListId;
    private static int disbursementId;
    private static String userId;
    private static int groupId;

    @BeforeClass
    public static void setUp() throws SQLException{
        connection = Db.instance().getConnection();
        itemDao = new ItemDao(connection);
        userDao = new UserDao(connection);
        groupDao = new GroupDao(connection);
        disbursementDao = new DisbursementDao(connection);
        shoppingListDao = new ShoppingListDao(connection);
        memberDao = new MemberDao(connection);

        itemId = 67;
        shoppingListId = 67;
        userId = "itemTestUser5@test.no";
        itemTest = new Item();
        itemTest.setId(itemId);
        itemTest.setName("TestItem");
        itemTest.setStatus(0);

        ArrayList<User> userList = new ArrayList<>();
        User u = new User(userId, "itemTestUser", "", "", LoginCheck.getSalt());
        userList.add(u);
        Group g = new Group();
        g.setName("testgroup");
        g.setAdmin(userId);
        Disbursement disbursement = new Disbursement();
        disbursement.setDate(new Date(System.currentTimeMillis()));
        disbursement.setName("name");
        disbursement.setDisbursement(0);
        disbursement.setPayer(u);
        shoppingListTest = new ShoppingList(shoppingListId, "ItemTest",groupId, null, userList);
        try{
            userDao.addUser(u);
            groupId = groupDao.addGroup(g);
//            disbursementDao.addDisbursement(disbursement,groupId);
            shoppingListDao.addShoppingList(shoppingListTest);
            itemDao.addItem(itemTest);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testGetItem() throws SQLException {
        Item i = new Item();

        try {
            i = itemDao.getItem(itemTest.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(i.getId(), itemTest.getId());
    }

    @Test
    public void testUpdateItem(){
        Item i = new Item();
        itemTest.setShoppingListId(shoppingListId);

        try {
            itemDao.updateItem(itemTest);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            i = itemDao.getItem(itemId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(itemTest.getId(), i.getId());
        assertEquals(itemTest.getShoppingListId(), i.getShoppingListId());
        assertEquals(itemTest.getStatus(), i.getStatus());
    }

    @Test
    public void testGetItemsInShoppingList(){
        ArrayList<Item> items = new ArrayList<>();
        itemTest.setShoppingListId(shoppingListId);

        try {
            itemDao.updateItem(itemTest);
            items = itemDao.getItemsInShoppingList(shoppingListId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertNotNull(items);
        assertEquals(items.get(0).getId(), itemTest.getId());
    }
    @Test
    public void testGetItemsInDisbursment(){
        ArrayList<Item> items = new ArrayList<>();
        itemTest.setDisbursementId(disbursementId);

        try {
            itemDao.updateItem(itemTest);
            items = itemDao.getItemsInShoppingList(shoppingListId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertNotNull(items);
        assertEquals(items.get(0).getId(), itemTest.getId());
    }

    @Test
    public void testToString() {
        String expected = "Item{" +
	    "id=" + 67 +
	    ", name='" + "TestItem" + '\'' +
	    ", status=" + 0 +
	    ", shoppingListId=" + -1 +
	    ", disbursementId=" + -1 +
	    '}';
        assertEquals(expected, itemTest.toString());
    }

    @AfterClass
    public static void tearDown() throws SQLException{
       try{
           itemDao.delItem(itemTest.getId());
           shoppingListDao.delShoppingList(shoppingListTest.getId());
           memberDao.deleteMember(userId,groupId);
           groupDao.deleteGroup(groupDao.getGroupByName("testgroup").get(0).getId());
           userDao.delUser(userId);
       }finally {
           Db.close(connection);
       }
    }
}
