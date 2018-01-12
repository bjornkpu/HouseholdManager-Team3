package data;

import db.ShoppingListDao;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ShoppingListTest {
    private static ShoppingList shoppingListTest;
    private static int id;
    private static ArrayList<User> userList;

    @BeforeClass
    public static void setUp() throws SQLException{
        userList = new ArrayList<User>();

        userList.add(new User("shoppingListTest@sl.no", "ShoppingListUser", "", ""));

        shoppingListTest = new ShoppingList(45, "shoppingListTest",
                2, null, userList);
        id = 45;

        ShoppingListDao.addShoppingList(shoppingListTest);
    }

    @Test
    public void testGetShoppingList(){
        ShoppingList sl = new ShoppingList();

        try {
            sl = ShoppingListDao.getShoppingList(id);
        } catch(SQLException e){
            e.printStackTrace();
        }

        assertEquals(shoppingListTest.getId(), sl.getId());
    }

    @AfterClass
    public static void delete_shoppingList() throws SQLException{
        ShoppingListDao.delShoppingList(id);
    }

}
