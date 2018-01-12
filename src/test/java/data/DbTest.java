package data;
import db.Db;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertNotNull;

public class DbTest {
    private static Connection connection;

    @Before
    public void open_connection() {
        try {
            connection = Db.instance().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_connection() {
        assertNotNull(connection);
    }

    @After
    public void close_connection() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}