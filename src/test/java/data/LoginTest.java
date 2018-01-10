package data;
import db.UserDao;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginTest {
    private static UserDao u;

    @BeforeClass
    public static void setUp() throws SQLException {
        u = new UserDao();
    }

    @Test
    public void get_user(){
        User uu = new User();
        try{
            uu = u.getUser("tre@h.no");
        }catch(SQLException e){
            e.printStackTrace();
        }

        assertEquals("Knat Waag", uu.getName());
    }

    @Test
    public void insert_user(){
        User user1 = new User("user1@gmail.com", "User1", "90706060", "123");
        User user2 = new User();

        try {
            u.addUser(user1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            user2 = u.getUser("user1@gmail.com");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(user2.getEmail() + " " + user2.getName() + " " + user2.getPhone() + " ");

        assertEquals(user1.getEmail(), user2.getEmail());
    }

    @Test
    public void update_user(){
        User user1 = new User("user1@gmail.com", "UserA", "292929", "1321");

        try {
            u.updateUser(user1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User test = new User();

        try {
            test = u.getUser("user1@gmail.com");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals("UserA", test.getName());
        assertEquals("292929", test.getPhone());
        assertEquals("1321", test.getPassword());
    }
    /**
     * Deleting the user made in insert_user to prevent the sql error the next time we run the insert_user-test
     */
    @AfterClass
    public void delete_user() throws SQLException {
        u.delUser("user1@gmail.com");
    }
}
