package data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoginDataTest {


    @Test
    public void testGetEmail() throws Exception {
        LoginData loginData = new LoginData();
        String name = "Kristian@mail.no";
        loginData.setEmail("Kristian@mail.no");
        assertEquals(loginData.getEmail(),name);
    }
    @Test
    public void testGetPassword() throws Exception {
        LoginData log = new LoginData();
        String password = "1234b";
        log.setPassword("1234b");
        assertEquals(log.getPassword(),password);
    }
}
