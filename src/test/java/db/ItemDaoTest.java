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
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ItemDaoTest {
    private static Item itemTest;
    private static ShoppingList shoppingListTest;
    private static int itemId;
    private static int shoppingListId;
    private static int disbursementId;
    private static String userId;
    private static int groupId;

    @BeforeClass
    public static void setUp() throws SQLException{
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
        UserDao.addUser(u);
        groupId = GroupDao.addGroup(g);
        Disbursement disbursement = new Disbursement(0,"name",new Date(),u);
        disbursementId = DisbursementDao.addDisbursement(disbursement,groupId);

        shoppingListTest = new ShoppingList(shoppingListId, "ItemTest",groupId, null, userList);


        ShoppingListDao.addShoppingList(shoppingListTest);
        ItemDao.addItem(itemTest);
    }

    @Test
    public void testGetItem() throws SQLException {
        Item i = new Item();

        try {
            i = ItemDao.getItem(itemTest.getId());
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
            ItemDao.updateItem(itemTest);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            i = ItemDao.getItem(itemId);
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
            ItemDao.updateItem(itemTest);
            items = ItemDao.getItemsInShoppingList(shoppingListId);
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
            ItemDao.updateItem(itemTest);
            items = ItemDao.getItemsInShoppingList(shoppingListId);
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
        ItemDao.delItem(itemTest.getId());
        ShoppingListDao.delShoppingList(shoppingListTest.getId());
        MemberDao.deleteMember(userId,groupId);
        GroupDao.deleteGroup(GroupDao.getGroupByName("testgroup").get(0).getId());
        UserDao.delUser(userId);
    }
}
