package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class newSimpleDB {

    private static String dbUsername = "root"; // mysql user name
    private static String dbPassword = "ibran2892001"; // mysql password
    private static String URL = "127.0.0.1"; // location of db server
    private static String port = "3306"; // constant
    private static String dbName = "supplierdb"; // most likely will not change
    private static Connection con;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DBConn a = new DBConn(URL, port, dbName, dbUsername, dbPassword);
        /*
         * con = a.connectDB(); System.out.println("Connection established");
         * con.close();
         */
        con = a.connectDB();
        String mystring = "select * from supplier";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(mystring);
        while (rs.next()) // one tuple at a time
            System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4)
                    );

        rs.close();
        stmt.close();

        con.close();
        System.out.println("Connection closed");

    }

}

class DBConn {

    private Connection con;
    private String dbURL;
    private String dbUsername;
    private String dbPassword;
    private String URL;
    private String port;
    private String dbName;
    DBConn(){
    	this.dbUsername = "root"; // mysql user name
	    this.dbPassword = "ibran2892001"; // mysql password
	    this.URL = "127.0.0.1"; // location of db server
	    this.port = "3306"; // constant
	    this.dbName = "supplierdb"; // most likely will not change
    }

    DBConn(String URL, String port, String dbName, String dbUsername, String dbPassword) {

        this.URL = URL;
        this.port = port;
        this.dbName = dbName;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }

    public Connection connectDB() throws ClassNotFoundException, SQLException {

        // dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName +
        // "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=GMT";
        // dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName +
        // "?verifyServerCertificate=false";
        dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName
                + "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=ConvertToNull&serverTimezone=GMT";

        System.out.println(dbURL);
        Properties p = new Properties();
        p.setProperty("user", dbUsername);
        p.setProperty("password", dbPassword);
        p.setProperty("useSSL", "false");
        p.setProperty("autoReconnect", "true");

        // Class.forName("com.mysql.cj.jdbc.Driver");
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(dbURL, p);
        // new com.mysql.jdbc.Driver();
        // con = DriverManager.getConnection(dbURL,p);

        return con;
    }

}
