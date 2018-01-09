package sql;

import com.mysql.cj.jdbc.*;
import data.dao.tablemodel.LogTableModel;
import util.Singleton;

import java.io.IOException;
import java.io.InputStream;

import java.net.UnknownHostException;
import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.net.InetAddress;

public final class SQLManager implements Singleton, AutoCloseable {

    private static final int    DEFAULT_TRIES   =   3;
    private static final int    DEFAULT_TIMEOUT = 500;
    private static final String PROPERTY_FILE   = "db.properties";

    private Connection connection;
    private String     url;
    private String     user;
    private String     password;

    // Logging
    private LogTableModel ltm;
    private InetAddress   IP;

    private List<PreparedStatement> statements = new ArrayList<>();

    @Override
    public void close() {
        for (PreparedStatement stmt : statements) {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace();}
        }
        if (connection != null) try { connection.close(); } catch (SQLException e) { e.printStackTrace();}

        instance = null;
    }

    /**
     * <p>Tells if the connection needs to be refreshed before it is used.</p>
     * If this is {@code true} the SQLManager will refresh its {@code Connection} the next time it is accessed.
     * @see #getConnection()
     * @see #refreshConnection()
     */
    private boolean    notRefreshed = true;





    /**
     * <p>Gets a {@code Connection} to the database.</p>
     * <p>Does a weak check to see if the {@code connection} needs to be refreshed before it is returned.
     * If you want to be certain the connection is valid, call #checkConnection before calling this.</p>
     *
     * @return the {@code Connection}
     */

    protected InetAddress getIP() {
        if (IP == null) {
            try {
                IP = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
        return IP;
    }


    public boolean isStatementConnected(Statement stmt) {
        try {
            return stmt != null && stmt.getConnection().equals(getConnection()) && !stmt.isClosed();
        } catch (SQLException e) { e.printStackTrace();
            return false;
        }
    }

    public Connection getConnection() {
        boolean ok;
        try {
            ok = !(notRefreshed || connection == null || connection.isClosed());
        } catch (SQLException e) { e.printStackTrace();
            ok = false;
        }
        if (!ok) {
            try {
                refreshConnection();
            } catch (SQLException e) { e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    /**
     * Does a thorough check to see if the connection is still valid.
     * @return {@code true} if the connection is valid, {@code false} otherwise
     *
     * @see java.sql.Connection#isValid(int)
     */
    public boolean checkConnection() {
        boolean ok;
        try {
            ok = connection != null && connection.isValid(DEFAULT_TIMEOUT);
        } catch (SQLException e) { e.printStackTrace();
            ok = false;
        }
        if (!ok) notRefreshed = true;
        return ok;
    }

    /**
     * Creates a unique {@code Connection} and returns it.
     *
     * @return a unique {@code Connection}
     * @throws SQLException if a {@code Connection} could not be created
     * @see #createConnection()
     */
    public Connection getUniqueConnection() throws SQLException {
        return createConnection();
    }

    /**
     * Refreshes the {@code Connection}.
     *
     * @return {@code true} if the {@code Connection} could be refreshed, {@code false} otherwise
     * @throws SQLException if a {@code Connection} could not be created
     * @see #createConnection()
     */
    public boolean refreshConnection() throws SQLException {
        connection = createConnection();
        notRefreshed = false;
        return connection != null;
    }

    /**
     * Creates a new {@code Connection} and returns it.
     * It will try {@link #DEFAULT_TRIES} times to create the {@code Connection}.
     * @return a new {@code Connection}
     * @throws SQLException if a {@code Connection} could not be created after {@link #DEFAULT_TRIES} attempts
     */
    private Connection createConnection() throws SQLException {
        int tries = DEFAULT_TRIES;
        while (true) {
            try {
                return DriverManager.getConnection(url, user, password);
            } catch (SQLException e) { e.printStackTrace();
                if(--tries <= 0) {
                    throw new SQLException(
                            "Could not establish connection to database.\n"+
                                    "url=" + url + "\n" +
                                    "user=" + user + "\n" +
                                    "password=" + password + "\n",
                            e);
                }
            }
        }
    }

    /**
     * Loads properties into the SQLManager from an {@code InputStream} formatted with properties.
     *
     * @param is the {@code InputStream} to the properties.
     * @return {@code true} if the action was successful, {@code false} otherwise
     *
     * @see #arePropertiesLoaded()
     * @see java.util.Properties
     */
    private boolean loadProperties(InputStream is) {
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            return false;
        }
        String _url      = properties.getProperty("url");
        String _user     = properties.getProperty("user");
        String _password = properties.getProperty("password");

        if (_url == null || _user == null || _password == null) return false;

        url      = _url;
        user     = _user;
        password = _password;

        notRefreshed = true;

        return true;
    }

    /**
     * Checks if any of the db-properties are null.
     *
     * @return {@code true} if none of the properties are {@code null}, {@code false} otherwise
     */
    public boolean arePropertiesLoaded() {
        return url != null && user != null && password != null;
    }


    // Singleton
    /**
     * The {@code Singleton} instance
     */
    private static SQLManager instance;

    /**
     * Gets the instance of this class.
     * @return the instance
     * @see util.Singleton
     */
    public static SQLManager get() {
        if(instance == null) {
            instance = new SQLManager();
        }
        return instance;
    }

    /**
     * Protected constructor so this {@code Singleton} cannot be instantiated from the outside.
     */
    protected SQLManager() {
        loadProperties(getClass().getResourceAsStream(PROPERTY_FILE));
        ltm = LogTableModel.get();
    }


    /**
     * <p>Takes a {@code StatementPreparer} and attempts to execute the {@code PreparedStatement} prepared.
     * Note that the query and its result will be logged.</p>
     *
     * <p>If either the statement itself or the logging is unsuccessful the database the query will not be committed.</p>
     *
     * @param stmt
     * @param tries
     * @return
     * @throws SQLException
     *
     * @see #log(Statement, String)
     */
    public ResultSet query(PreparedStatement stmt, int tries) throws SQLException {
        boolean   ok = false;
        ResultSet rs = null;

        boolean oldCommit = getConnection().getAutoCommit();
        getConnection().setAutoCommit(false);

        // Retry loop
        do {
            try {
                rs = stmt.executeQuery();
                rs.last();
                ok = log(stmt,String.valueOf(rs.getRow())); //TODO find better result-string?
                rs.beforeFirst();
            } catch (SQLException e) {
                e.printStackTrace();
                if (!isStatementConnected(stmt)) {
                    throw e;
                }
                ok = false;
            }
            if (!ok) {
                if (--tries <= 0) {
                    getConnection().rollback();
                    getConnection().setAutoCommit(oldCommit);
                    throw new SQLException("could not execute query");
                }
                getConnection().rollback();
            }
        } while (!ok);

        getConnection().commit();
        getConnection().setAutoCommit(oldCommit);
        return rs;
    }
    public ResultSet query(PreparedStatement stmt) throws SQLException {
        return query(stmt, DEFAULT_TRIES);
    }



    public int update(PreparedStatement stmt, int tries) throws SQLException {
        boolean   ok = false;
        int      res = -1;

        boolean oldCommit = getConnection().getAutoCommit();
        getConnection().setAutoCommit(false);

        // Retry loop
        do {
            try {
                res = stmt.executeUpdate();
                ok = log(stmt,String.valueOf(res));

            } catch (SQLException e) { e.printStackTrace();
                if (!isStatementConnected(stmt)) {
                    throw e;
                }
                ok = false;
            }
            if (!ok) {
                if (--tries <= 0) {
                    getConnection().rollback();
                    getConnection().setAutoCommit(oldCommit);
                    throw new SQLException("could not execute update");
                }
                getConnection().rollback();
            }
        } while (!ok);

        getConnection().commit();
        getConnection().setAutoCommit(oldCommit);
        return res;
    }
    public int update(PreparedStatement stmt) throws SQLException {
        return update(stmt, DEFAULT_TRIES);
    }


    public boolean execute(PreparedStatement stmt, int tries) throws SQLException {
        boolean   ok  = false;
        boolean   res = false;

        boolean oldCommit = getConnection().getAutoCommit();
        getConnection().setAutoCommit(false);

        // Retry loop
        do {
            try {
                res = stmt.execute();
                ok = log(stmt,String.valueOf(res));

            } catch (SQLException e) { e.printStackTrace();
                if (!isStatementConnected(stmt)) {
                    throw e;
                }
                ok = false;
            }
            if (!ok) {
                if (--tries <= 0) {
                    getConnection().rollback();
                    getConnection().setAutoCommit(oldCommit);
                    throw new SQLException("could not execute");
                }
                getConnection().rollback();
            }
        } while (!ok);

        getConnection().commit();
        getConnection().setAutoCommit(oldCommit);
        return res;
    }

    public boolean execute(PreparedStatement stmt) throws  SQLException {
        return execute(stmt, DEFAULT_TRIES);
    }

    private PreparedStatement logstmt;

    private boolean log(Statement stmt, String result) throws SQLException {
        if (!isStatementConnected(logstmt)) {
            String query = "insert into " + ltm.TABLE_NAME + "(" + ltm.COLUMN_NAME_ISSUER + ", " + ltm.COLUMN_NAME_QUERY + ", " + ltm.COLUMN_NAME_RESULT + ") VALUES(?, ?, ?)";
            logstmt = getConnection().prepareStatement(query);
        }
        String stmtquery;
        if (stmt instanceof com.mysql.cj.jdbc.PreparedStatement) {
            stmtquery = ((com.mysql.cj.jdbc.PreparedStatement) stmt).asSql();
        } else {
            stmtquery = stmt.toString();
        }

        logstmt.setString(1, getIP().toString());
        logstmt.setString(2, stmtquery);
        logstmt.setString(3, result);

        return logstmt.executeUpdate() == 1;
    }



    public PreparedStatement prepareStatement(String sql) {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement(sql);
        } catch (SQLException e) { e.printStackTrace();
            if (!checkConnection()) {
                try {
                    stmt = getConnection().prepareStatement(sql);
                } catch (SQLException f) {
                    throw new IllegalStateException("could not prepare statement", f);
                }
            }
        }
        if (stmt != null) statements.add(stmt);
        return stmt;
    }
}
