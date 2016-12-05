package config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by louis on 13/11/16.
 */
public class DatabaseConnector {

    private static Connection connection = null;
    private static String host;
    private static String port;
    private static String db;
    private static String user;
    private static String password;


    public static void main(String[] args) {
        System.out.println(getConnection());
    }

    static {
        try {
            host = Configuration.getDBhost();
            port = Configuration.getDBport();
            db = Configuration.getDBname();
            user = Configuration.getDBuser();
            password = Configuration.getDBpassword();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            connection = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + db + "?sslmode=require", user, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        DatabaseConnector.connection = connection;
    }
}
