package db;

import data.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Logger;
import util.LoginCheck;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ItemDaoTest {
    private static Connection connection;
    private static ItemDao itemDao;
    private static UserDao userDao;
    private static GroupDao groupDao;
    private static ShoppingListDao shoppingListDao;
    private static MemberDao memberDao;


    private static Item itemTest;
    private static ShoppingList shoppingListTest;
    private static int itemId;
    private static int shoppingListId;
    private static int disbursementId;
    private static String userId;
    private static int groupId;



    private static final Logger log = Logger.getLogger();
    private static PreparedStatement ps;

    private static String name1 = "Vennegjengen";
    private static String email1 = "en@test1.no";
    private static String email2 = "to@test1.no";
    private static String email3 = "tre@test1.no";
    private static String email4 = "fire@test1.no";
    private static int groupId1 = 1001;
    private static int groupId2 = 1002;

    @BeforeClass
    public static void setUp() throws SQLException {
        connection = Db.instance().getConnection();
        itemDao = new ItemDao(connection);
        userDao = new UserDao(connection);
        groupDao = new GroupDao(connection);
        shoppingListDao = new ShoppingListDao(connection);
        memberDao = new MemberDao(connection);

        itemId = 66;
        shoppingListId = 68;
        userId = "itemTestUser8@test.no";
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
        disbursement.setDate(new Timestamp(System.currentTimeMillis()));
        disbursement.setName("name");
        disbursement.setDisbursement(0);
        disbursement.setPayer(u);
        try {
            userDao.addUser(u);
            groupId = groupDao.addGroup(g);
//            disbursementDao.addDisbursement(disbursement,groupId);
            itemDao.addItem(itemTest);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection = Db.instance().getConnection();
        memberDao = new MemberDao(connection);

        Group group1 = new Group();
        group1.setId(groupId1);
        group1.setName(name1);

        try {
            ps = connection.prepareStatement("INSERT INTO shoppinglist(id,name,party_id) VALUES(68,'testlist',1);");
            int result = ps.executeUpdate();
            log.info("Added group " + (result == 1 ? "ok" : "failed"));
        }catch (SQLException e) {
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

    //Works thorugh disbursementDaoTest...
    @Test
    public void testGetItemsInDisbursment(){
        ArrayList<Item> items = new ArrayList<>();
        itemTest.setDisbursementId(disbursementId);

        try {
            itemDao.updateItem(itemTest);
            items = itemDao.getItemsInDisbursement(disbursementId);
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
           shoppingListDao.delShoppingList(shoppingListId);
           memberDao.deleteMember(userId,groupId);
           groupDao.deleteGroup(groupDao.getGroupByName("testgroup").get(0).getId());
           userDao.delUser(userId);
       }finally {
           Db.close(connection);
       }
    }
}
