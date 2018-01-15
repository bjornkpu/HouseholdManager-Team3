package data;
import db.UserDao;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
/**
 * -Description of the class-
 *
 * @author
 */
public class LoginTest {
    private static UserDao userDao;
    private static User user;
    private static final String email="LoginTestEmailATemailDOTcom";


    @BeforeClass
    public static void setUp() throws SQLException {
        userDao = new UserDao();
        user = new User(email, "User1", "90706060", "123");
        userDao.addUser(user);

    }

    @Test
    public void get_user(){
        User uu = new User();
        try{
            uu = userDao.getUser(email);
        }catch(SQLException e){
            e.printStackTrace();
        }

        assertEquals("User1", uu.getName());
    }


    @Test
    public void update_user(){
        user.setPhone("newphone");
//        user.setName("newname");
        user.setPassword("newpw");

        try {
            userDao.updateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User test = new User();

        try {
            test = userDao.getUser(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        assertEquals("newname", test.getName());
        assertEquals("newphone", test.getPhone());
        assertEquals("newpw", test.getPassword());
    }
    /**
     * Deleting the user made in insert_user to prevent the sql error the next time we run the insert_user-test
     */
    @AfterClass
    public static void delete_user() throws SQLException {
        userDao.delUser(email);
    }
}
