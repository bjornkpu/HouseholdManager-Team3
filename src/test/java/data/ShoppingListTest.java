/**
package data;

import db.ShoppingListDao;
import db.UserDao;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * -Description of the class-
 * @author enoseber
 */
/**
public class ShoppingListTest {
    private static int slId;
    private static int groupId;
    private static User u;
    private static ShoppingList shoppingListTest;
    private static ArrayList<User> userList;

    @BeforeClass
    public static void setUp() throws SQLException{
        slId = 45;
        groupId = 2;

        u = new User("shoppinglistTest@user.no", "shoppinglistTestUser", "", "");

        UserDao.addUser(u);

        userList = new ArrayList<User>();
        userList.add(u);

        shoppingListTest = new ShoppingList(slId, "shoppingListTest",
                groupId, null, userList);

        ShoppingListDao.addShoppingList(shoppingListTest);
    }

    @Test
    public void testGetShoppingList(){
        ShoppingList sl = new ShoppingList();

        try {
            sl = ShoppingListDao.getShoppingList(slId);
        } catch(SQLException e){
            e.printStackTrace();
        }

        assertEquals(shoppingListTest.getId(), sl.getId());
    }

    @Test
    public void testGetShoppingListByGroupid(){
        ArrayList<ShoppingList> slList = new ArrayList<ShoppingList>();

        try {
            slList = ShoppingListDao.getShoppingListByGroupid(groupId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertNotNull(slList);
        assertEquals(slList.get(0).getId(), shoppingListTest.getId());
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
        assertEquals(slList.get(0).getId(), shoppingListTest.getId());
    }

    @Test
    public void testUpdateShoppingList(){
        ShoppingList updatedSl = new ShoppingList(slId, "nyttNavn", groupId, null, null);
        ShoppingList updateTest = new ShoppingList();

        try {
            ShoppingListDao.updateShoppingList(updatedSl);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            updateTest = ShoppingListDao.getShoppingList(slId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(updateTest.getId(), updatedSl.getId());
        assertEquals(updateTest.getName(), updatedSl.getName());

    }

    @AfterClass
    public static void tearDown() throws SQLException{
        ShoppingListDao.delShoppingList(slId);
        UserDao.delUser(u.getEmail());
    }

}
*/