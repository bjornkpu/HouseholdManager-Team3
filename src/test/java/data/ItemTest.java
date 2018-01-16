/*
package data;

import db.ItemDao;
import db.ShoppingListDao;
import db.UserDao;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ItemTest {
    private static Item itemTest;
    private static ShoppingList shoppingListTest;
    private static int itemId;
    private static int shoppingListId;
    private static int dipsId;
    private static String userId;

    @BeforeClass
    public static void setUp() throws SQLException {
        itemId = 67;
        shoppingListId = 67;
        dipsId = 67;
        userId = "itemTestUser@test.no";

        User u = new User(userId, "itemTestUser", "", "");

        ArrayList<User> userList = new ArrayList<User>();
        userList.add(u);

        shoppingListTest = new ShoppingList(shoppingListId, "ItemTest", 2, null, userList);
        itemTest = new Item(itemId, "TestItem", 0, shoppingListId, 1);

        UserDao.addUser(u);
        ShoppingListDao.addShoppingList(shoppingListTest);
        ItemDao.addItem(itemTest);
    }

    @Test
    public void testGetItem() {
        Item i = new Item();

        try {
            i = ItemDao.getItem(itemTest.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(i.getId(), itemTest.getId());
    }

    @Test
    public void testUpdateItem() {
        Item i = new Item();
        Item newItemTest = new Item(itemId, "AnnetNavn", 1, shoppingListId, 1);

        try {
            ItemDao.updateItem(newItemTest);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            i = ItemDao.getItem(itemId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(newItemTest.getId(), i.getId());
        assertEquals(newItemTest.getName(), i.getName());
        assertEquals(newItemTest.getStatus(), i.getStatus());
    }

    @Test
    public void testGetItemsInShoppingList() {
        ArrayList<Item> items = new ArrayList<Item>();

        try {
            items = ItemDao.getItemsInShoppingList(shoppingListId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertNotNull(items);
        assertEquals(items.get(0).getId(), itemTest.getId());
    }


    @AfterClass
    public static void tearDown() throws SQLException{
        //ItemDao.delItem(itemTest);
        ShoppingListDao.delShoppingList(shoppingListTest.getId());
        UserDao.delUser(userId);
    }
}*/
