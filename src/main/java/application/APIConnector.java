package application;

import config.*;
import models.Place;
import models.Point;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public final class APIConnector {
    private static String API_KEY;

    static {
        try {
            API_KEY = Configuration.getAPIKey();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getPlaceDetails(String placeId) throws IOException, JSONException {
        String urlString =  "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&key=" + API_KEY;
        URL url = new URL(urlString);
        return getJsonFromUrl(url);
    }
    
    public static ArrayList<Place> getPlaces(Point coordinates, int radius) throws IOException, JSONException {
        ArrayList<Place> arrayOfPlaces = new ArrayList<Place>();
        String urlString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
                                                                                         + "?location="+  coordinates
                                                                                         + "&radius="+    radius
                                                                                         + "&name=hsbc"
                                                                                         + "&types=bank"
                                                                                         + "&key=" +      API_KEY;
        URL url = new URL(urlString);
        JSONArray results = getJsonFromUrl(url).getJSONArray("results");

        for (int i = 0 ; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            if (result.getString("name").toLowerCase().contains("hsbc")){
                arrayOfPlaces.add(new Place(result));
            }
        }
        return arrayOfPlaces;
    }

    public static Point getCoordinatesFromAddress(String address) throws JSONException, IOException {
        String urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + API_KEY;
        URL url = new URL(urlString);
        JSONObject res = getJsonFromUrl(url).getJSONArray("results").getJSONObject(0); // TODO : check null??
        return new Point(res);
    }

    public static JSONObject getJsonFromUrl(URL url) throws IOException, JSONException {
        // read from the URL
        //System.out.println("Nouvelle requete ? "+ url);
        Scanner scan = new Scanner(url.openStream());
        String str = new String();
        while (scan.hasNext())
            str += scan.nextLine();
        scan.close();

        // build a JSON object
        JSONObject obj = new JSONObject(str);
        if (! obj.getString("status").equals("OK")){
            return null;
        }
        else {
            return obj;
        }

    }
}




