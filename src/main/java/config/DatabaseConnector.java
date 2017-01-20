package config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by louis on 13/11/16.
 */
public class DatabaseConnector {

    private static Connection connection = null;
    private static Statement stat = null;
    private static String host;
    private static String port;
    private static String db;
    private static String user;
    private static String password;

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
            stat = connection.createStatement();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
    public static boolean isTokenInDB(String userToken)
    {
		try {
			ResultSet result = stat.executeQuery("SELECT token FROM clients WHERE token='" + userToken + "';");
			if(result.next())
			{
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
    }
    
    public static ResultSet getInfoFromToken(String userToken)
    {
		try {
			ResultSet result = stat.executeQuery("SELECT * FROM clients WHERE token='" + userToken + "';");
			result.next();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    public static String getClientPassword(String login) throws SQLException{
		ResultSet result = stat.executeQuery("SELECT password\n" +
				"FROM clients\n" +
				"WHERE CAST( login AS INTEGER) = " + login);
		result.next();
		
		return result == null ?  null : result.getString("password");
		
    }
    
    public static String getClientName(String login) throws SQLException{
		ResultSet result = stat.executeQuery("SELECT lastname, firstname\n" +
				"FROM clients\n" +
				"WHERE CAST( login AS INTEGER) = " + login);
		result.next();
		return result == null ?  null :  result.getString("firstname") +" "+ result.getString("lastname");
		
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
