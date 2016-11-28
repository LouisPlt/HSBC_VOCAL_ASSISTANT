package googleApi;

import config.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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

    public static void main(String[] args) throws IOException, JSONException {
        List<Place> places = APIConnector.getPlaces(getCoordinatesFromAddress("Paris"), Util.DEFAULT_RADIUS);
        Place place = places.get(1);
    	place.findPhoneNumber();
    	place.findOpeningHours();
    	System.out.println(place.getCoordinate());
    	System.out.println(place.getPhoneNumber());
    	
    	// TODO : create UT!
    	// get and test similar days (based on their opening hours) :
    	List<List<String>> similarDays = getSimilarOpeningHoursDays(place);
    	for(List<String> days : similarDays){
    		System.out.print("Groupe : ");
    		for(String day : days){
    			System.out.print(day + " ");
    		}
    		System.out.println();
    	}
    	
    	// test openingHours :
    	StringBuilder result = new StringBuilder("The agency is open ");
    	
    	for(List<String> days : similarDays){	
    		for(String day : days){
    			List<String> hours = place.getOpeningHours().get(day);
        		if(hours.isEmpty()){
        			break;
        		}
    			if(day.equals(days.get(days.size()-1))){
        			result.append("and ");
        			result.append(day);
            		result.append(" from ");
                	for(int i = 0; i < hours.size(); i++){
                		if(i%2 == 1){
                			result.append(" to ");
                		} else if(i != 0){
                			result.append(" then from ");
                		}
                		result.append(hours.get(i).substring(0,2));
                		if(!hours.get(i).substring(2).equals("00")){
                    		result.append(" ");
                    		result.append(hours.get(i).substring(2));
                		}
            		}
                	result.append(", ");
            	} else {
            		result.append(day);
            		result.append(", ");
    			}
    		}
    	}
    	result.delete(result.length() - 2, result.length() - 1);
    	System.out.println(result);
    }
    
    // TODO : delete it after moving the previous test
    public static List<List<String>> getSimilarOpeningHoursDays(Place place){
    	List<List<String>> similarDays = new ArrayList<>(); 
    	
    	for(String day : place.getOpeningHours().keySet()){
    		boolean matchingExistingHours = false;
    		List<String> hours = place.getOpeningHours().get(day);
    		
    		int i = 0;
    		while(!matchingExistingHours && i < similarDays.size()){
    			String previousDay = similarDays.get(i).get(0);
    			if(place.getOpeningHours().get(previousDay).equals(hours)){
    				matchingExistingHours = true;
    				List<String> days = similarDays.get(i);
    				days.add(day);
    				similarDays.set(i, days);
    			}
    			i++;
    		}    		
    		// if there is no match :
    		if(!matchingExistingHours){
	    		List<String> days = new LinkedList<>();
	    		days.add(day);
	    		similarDays.add(days);
    		}
    	}
    	return similarDays;
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




