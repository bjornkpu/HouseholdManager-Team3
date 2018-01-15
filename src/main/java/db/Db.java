package db;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import util.Logger;

import java.sql.Connection;
import java.sql.SQLException;
/** This is a class to establish a connection pool
 * @author BK
 */
public class Db {
    private static Db datasource;
    private ComboPooledDataSource cpds;
    private static final Logger log = Logger.getLogger();
    private static final String DB_URL = "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/";
    private static final String DB_USER_NAME = "g_tdat2003_t3";
    private static final String DB_PW = "Xq6ksy8X";

	/** constructor that sets up the connection pool.
	 * logs an exception error if it fails to initialize the connection pool to the database.
	 */
    private Db() {
        try {
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass("com.mysql.jdbc.Driver"); //loads the jdbc driver
            cpds.setJdbcUrl(DB_URL+DB_USER_NAME);
            cpds.setUser(DB_USER_NAME);
            cpds.setPassword(DB_PW);

            // the settings below are optional -- c3p0 can work with defaults
            cpds.setMinPoolSize(0);
            cpds.setAcquireIncrement(5);
            cpds.setMaxPoolSize(20);
            cpds.setMaxStatements(180);

            log.info("DB initialized!");

        } catch (Exception exception) {
            log.error("Failed to start DB", exception);
        }
    }

	/** Method to instance the datasource. If a connection is already established it will return the connection.
	 * This will prevent need to establish a new connection every time you need to make a query.
	 */
    public static Db instance() {
        if (datasource == null) {
            datasource = new Db();
            return datasource;
        } else {
            return datasource;
        }
    }

	/** Method to get a connection from the connection pool.
	 * @return a connection ready for use.
	 */
    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }
}
