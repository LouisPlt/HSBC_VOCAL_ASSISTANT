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
         //InputStream resourceAsStream = Configuration.class.getResourceAsStream("/config.properties");
         prop = new Properties();
         try {
             prop.load(new FileInputStream("config.properties"));
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
}
