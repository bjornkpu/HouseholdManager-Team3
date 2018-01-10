package data;

import db.Db;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DbTest {
    private static Db mockedDb;

    @BeforeClass
    public static void setUp() {
        mockedDb = mock(Db.class);
    }

    @Test
    public void teste_onnection() throws SQLException {
        assertNotEquals(null , mockedDb.getConnection());
    }
}
