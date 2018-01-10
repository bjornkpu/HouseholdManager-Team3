package data;
import db.UserDao;
import org.junit.BeforeClass;
import org.junit.Test;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginTest {
    private static UserDao mockedUserDao;

    @BeforeClass
    public static void setUp() throws SQLException {
        // Set up mock
        mockedUserDao = mock(UserDao.class);

//        when(mockedUserDao.getUser("bk@asd.no"))
//                .thenReturn(new User("bk@asd.no","bk","123","pass123"));
    }

    @Test
    public void teste_user() throws SQLException {

        assertEquals(new User("bk@asd.no","bk","123","pass123")
                , mockedUserDao.getUser("bk@asd.no"));

    }

    @Test
    public void hent_user(){
        UserDao u = new UserDao();
        String s;
        try{
            s = u.getUser("tre@h.no").toString();
            System.out.println(s);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
