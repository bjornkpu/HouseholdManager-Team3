package data;
import db.Db;
import org.junit.BeforeClass;
import org.junit.Test;
import java.sql.SQLException;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class DbTest {
    private static Db mockedDb;

    @BeforeClass
    public static void setUp() {
        mockedDb = mock(Db.class);
    }

    @Test
    public void test_connection() throws SQLException {
        assertNotNull(mockedDb.getConnection());
    }
}
