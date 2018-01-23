package db;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import util.Logger;

import java.sql.*;

/** This is a class to establish a connection pool
 *
 * @author BK
 */
public class Db {
    private static Db datasource;
    private static Logger logger= new Logger();
    private static ComboPooledDataSource cpds;
    private static final Logger log = Logger.getLogger();
    private static final String DB_URL = "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/";
    private static final String DB_USER_NAME = "g_tdat2003_t3";
    private static final String DB_PW = "Xq6ksy8X";
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";


	/**
	 * constructor that sets up the connection pool.
	 * logs an exception error if it fails to initialize the connection pool to the database.
	 */
    private Db() {
        try {
//            Class.forName(DB_DRIVER);
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass(DB_DRIVER); //loads the jdbc driver
            cpds.setJdbcUrl(DB_URL+DB_USER_NAME);
            cpds.setUser(DB_USER_NAME);
            cpds.setPassword(DB_PW);

            // the settings below are optional -- c3p0 can work with defaults
            cpds.setInitialPoolSize(1);
            cpds.setMinPoolSize(1);
            cpds.setAcquireIncrement(2);
            cpds.setMaxPoolSize(4);
            cpds.setMaxIdleTimeExcessConnections(120);
            cpds.setMaxStatements(180);

            cpds.setCheckoutTimeout(300000); //30sek
            cpds.setTestConnectionOnCheckout(true);
            cpds.setUnreturnedConnectionTimeout(20);
            cpds.setDebugUnreturnedConnectionStackTraces(true);

            log.info("DB initialized!");

        } catch (Exception exception) {
            log.error("Failed to start DB", exception);
        }
    }

	/** Method to instance the datasource. If a connection is already established it will return the connection.
	 * This will prevent need to establish a new connection every time you need to make a query.
	 * @return an instance of the datasource
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
     *
	 * @return A {@link Connection}.
	 * @throws SQLException
	 */
    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
//        return DriverManager.getConnection(DB_URL+DB_USER_NAME, DB_USER_NAME,DB_PW);
    }
    /** Closes a {@linkplain Connection}.
     *
     * @param conn the Connection to close.
     */
    public static void close(Connection conn){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) { /* ignored */}
        }
    }

    /** Closes a {@linkplain PreparedStatement}.
     *
     * @param ps The PreparedStatement to close.
     */
    public static void close(PreparedStatement ps){
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) { /* ignored */}
        }
    }

    /** Closes a {@linkplain Statement}.
     *
     * @param s The PreparedStatement to close.
     */
    public static void close(Statement s){
        if (s != null) {
            try {
                s.close();
            } catch (SQLException e) { /* ignored */}
        }
    }

    /** Closes a {@linkplain ResultSet}.
     *
     * @param rs The ResultSet to close.
     */
    public static void close(ResultSet rs){
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) { /* ignored */}
        }
    }

    public void destroy(){
        cpds.close();
    }
}
