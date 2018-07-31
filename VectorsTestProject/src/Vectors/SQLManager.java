package Vectors;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLManager {
    private String dbURL = "jdbc:mysql://localhost:3306/fypj";
    private String dbUserName = "root";
    private String dbPassword = "root";

    public SQLManager() {
   
    }

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

    public String getDbURL() {
        return dbURL;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    /*
     * Open database connection
     */
    
    public Connection getConnection() {
        Connection conn = null;
        try {
            String connectionURL = "jdbc:mysql://localhost:3306/fypj";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(connectionURL, "root", "root");
        } catch (SQLException e) {
            System.out.println("Could not connect to DB");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.print(e);
        } catch (InstantiationException e) {
            System.out.print(e);
        } catch (IllegalAccessException e) {
            System.out.print(e);
        }
        return conn;
    }

    /*
     * Close open database connection
     */
    public void putConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Enable to close DB connection");
                e.printStackTrace();
            }
        }
    }

}
