package config;

import java.io.*;
import java.util.Properties;

/**
 * Created by louis on 08/11/16.
 */
public class Configuration {


    public static void main(String[] args) throws IOException {
        // To test that this class correctly retrieves te properties
        getAPIKey();
        getAPPID();
    }

    private static Properties prop;

     static {
         prop = new Properties();
         try {
             File file = new File("config.properties");

             // For local test
             if (!file.exists()) {
                 file = new File("src/main/resources/config.properties");
             }
             prop.load(new FileInputStream(file));
         } catch (IOException e) {
             e.printStackTrace();
         }
     }


    public static String getAPIKey() throws IOException {
        System.out.println("API_KEY = "+ prop.getProperty("apikey"));
        return  prop.getProperty("apikey");
    }

    public static String getAPPID() throws IOException {

        System.out.println("APP_ID = "+ prop.getProperty("appid"));
        return  prop.getProperty("appid");

    }

    public static String getDBname() throws IOException {
        return  prop.getProperty("db");
    }
    public static String getDBhost() throws IOException {
        return  prop.getProperty("host");
    }

    public static String getDBport() throws IOException {
        return  prop.getProperty("port");
    }

    public static String getDBuser() throws IOException {
        return  prop.getProperty("user");
    }
    public static String getDBpassword() throws IOException {
        return  prop.getProperty("password");
    }




}
