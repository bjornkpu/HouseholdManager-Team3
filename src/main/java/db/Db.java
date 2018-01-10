package db;

import util.MethodTimer;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import util.Logger;

/**
 * Singleton for DB creation and connection creation
 * @author nilstes
 */
//Connection driver etc.
public class Db {

    private static final Logger log = Logger.getLogger();
    private static final String DB_NAME = "g_tdat2003_t3";
    private static Db instance = new Db();

    private Db() {
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection("jdbc:h2:~/" + DB_NAME, DB_NAME, "Xq6ksy8X");
            try {
                Statement statement = connection.createStatement();
                statement.close();
            } finally {
                connection.close();
            }
            log.info("DB initialized!");
        } catch (Exception exception) {
            log.error("Failed to start DB", exception);
        }
    }

    public static Db instance() {
        return instance;
    }

    Connection getConnection() throws SQLException {
        // todo FIXME This is not a good way to get connections
        // Use a pool instead
        try (MethodTimer timer = new MethodTimer(Db.class, "getConnection")) {
            return DriverManager.getConnection("jdbc:h2:~/" + DB_NAME, DB_NAME, "Xq6ksy8X");
        }
    }
}

