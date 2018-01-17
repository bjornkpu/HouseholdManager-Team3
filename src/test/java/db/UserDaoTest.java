package db;
import data.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import util.LoginCheck;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
/**
 * Tests for UserDao
 *
 * @author enoseber
 */
public class UserDaoTest {
    private static User user;
    private static String salt = LoginCheck.getSalt();
    private static final String email="LoginTestEmailATemailDOTcom";


    @BeforeClass
    public static void setUp() throws SQLException {
        user = new User(email, "User1", "90706060", "123", salt);
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

    @Test
    public void testToString() {
        String expected = "User{" +
        "email='" + "LoginTestEmailATemailDOTcom" + '\'' +
        ", name='" + "User1" + '\'' +
        ", phone='" + "90706060" + '\'' +
        ", password='" + "123" + '\'' +
        ", salt='" + user.getSalt() + '\'' +
        '}';
        assertEquals(expected, user.toString());
    }


    /**
     * Deleting the user made in insert_user to prevent the sql error the next time we run the insert_user-test
     */
    @AfterClass
    public static void tearDown() throws SQLException {
        UserDao.delUser(email);
    }
}
