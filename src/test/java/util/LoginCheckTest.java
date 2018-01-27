package util;
import data.LoginData;
import data.User;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static util.LoginCheck.correctLogin;
import static util.LoginCheck.getHash;
/**
 * -Description of the class-
 *
 * @author
 */
public class LoginCheckTest {
    private final static String email= "abcATemailDOTcom";
    private final static String pw= "qwe";
    private final static String salt= "123";
    private final static String hashedPw= "18138372fad4b94533cd4881f03dc6c69296dd897234e0cee83f727e2e6b1f63";
    private static LoginData data;
    private static LoginData data1;
    private static LoginData data2;
    private static User user;

    @BeforeClass
    public static void buildUp(){
        data = new LoginData();
        data.setEmail(email);
        data.setPassword(pw);
        data1 = new LoginData();
        data1.setEmail("WRONGEMAIL");
        data1.setPassword(pw);
        data2 = new LoginData();
        data2.setEmail(email);
        data2.setPassword("WRONGPW");
        user = new User();
        user.setEmail(email);
        user.setPassword(hashedPw);
        user.setSalt(salt);
    }

    @Test
    public void testThatCorrectPwGivesCorrectHash(){
        assertEquals(hashedPw,getHash(pw+salt));
    }

    @Test
    public void testThatWrongPwGivesWrongHash(){
        System.out.println(pw+salt);
        assertNotEquals(hashedPw,getHash(pw+"122"));
    }

    @Test
    public void testThatCorrectLoginChecksUsernames(){
        assertTrue(correctLogin(data,user));
        assertFalse(correctLogin(data1,user));
    }
    @Test
    public void testThatCorrectLoginChecksPasswords(){
        assertTrue(correctLogin(data,user));
        assertFalse(correctLogin(data2,user));
    }

}
