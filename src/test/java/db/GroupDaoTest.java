package db;

import org.junit.Before;

public class GroupDaoTest {
    private static GroupDao gr;

    @Before
    public static void setUp() throws Exception {
        gr = new GroupDao();
    }
}
