package data;

import db.ShoppingListDao;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class ShoppingListTest {
    private static ShoppingListDao slDao;
    private static ShoppingList shoppingListTest;
    private static int id;

    @BeforeClass
    public static void setUp(){
        slDao = new ShoppingListDao();
        shoppingListTest = new ShoppingList(0, "shoppingListTest")
        id = 0;
    }

    @Test
    public void testGetShoppingList(){
        ShoppingList sl = new ShoppingList();

        try {
            sl = slDao.getShoppingList(id);
        } catch(SQLException e){
            e.printStackTrace();
        }

        assertEquals(shoppingListTest.getId(), sl.getId());
    }

}
