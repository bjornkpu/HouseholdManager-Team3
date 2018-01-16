package data;
import db.UserDao;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
/**
 * Tests for UserDao
 *
 * @author enoseber
 */
public class UserTest {
    private static User user;
    private static final String email="LoginTestEmailATemailDOTcom";


    @BeforeClass
    public static void setUp() throws SQLException {

        user = new User(email, "User1", "90706060", "123");
        UserDao.addUser(user);
    }

    @Test
    public void get_user(){
        User uu = new User();
        try{
            uu = UserDao.getUser(email);
        }catch(SQLException e){
            e.printStackTrace();
        }

        assertEquals(user.getName(), uu.getName());
    }

    @Test
    public void testGetUsersInShoppingList(){
        ArrayList<User> ulist = new ArrayList<User>();

        try {
            ulist = UserDao.getUsersInShoppingList(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(2, ulist.size());
    }

    @Test
    public void update_user(){
        user.setPhone("newphone");
//        user.setName("newname");
        user.setPassword("newpw");

        try {
            UserDao.updateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User test = new User();

        try {
            test = UserDao.getUser(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        assertEquals("newname", test.getName());
        assertEquals(user.getPhone(), test.getPhone());
        assertEquals(user.getPassword(), test.getPassword());
    }
    /**
     * Deleting the user made in insert_user to prevent the sql error the next time we run the insert_user-test
     */
    @AfterClass
    public static void tearDown() throws SQLException {
        UserDao.delUser(email);
    }
}
