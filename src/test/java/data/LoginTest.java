package data;
package search;

import db.UserDao;
import org.junit.BeforeClass;
import org.junit.Test;


import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class LoginTest {
    private static UserDao mockedUserDao;

    @BeforeClass
    public static void setUp() {
        // Set up mock
        mockedUserDao = mock(UserDao.class);

        when(mockedUserDao.readPage("url1")).thenReturn(new String[]{"hasdadsei", "ho", "bi", "bo"});
    }

    @Test
    public void teste_funksjonaliteten_i_SearchEngine_java() {
        // Test
        mockedSearchEngine.indexPage("url1");

        List<String> url2forst = List.of("url2", "url1");

        assertEquals(url2forst , mockedSearchEngine.search("hei"));

    }
}
