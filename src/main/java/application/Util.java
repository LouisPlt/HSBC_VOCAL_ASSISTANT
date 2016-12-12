package googleApi;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;

public class Util {
	
	public static int DEFAULT_RADIUS = 1000;
	public static int RADIUS_MAX = 10000;
	
	public static String[] DAYS = {"Monday", "Tuesday" ,"Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};


	// d(a,b) = sqrt((xb - xa)^2 + (yb - ya)^2)
	public static double distance(Point p1, Point p2){
		double a = p2.getLat() - p1.getLat();
		double b = p2.getLng() - p1.getLng();
		return Math.sqrt(a*a + b*b);
	}
	
	public static Place findNearestPlace(Point location, List<Place> places) throws JSONException {
		Place closestPlace = places.get(0);
		double minDistance = distance(location, closestPlace.getCoordinate());
	
		for (Place place : places){
			double distance = distance(location, place.getCoordinate());
			if(distance < minDistance){
				closestPlace = place;
				minDistance = distance;
			}
		}
		return closestPlace;
	}
	
	/*
	 *  Get the list of all places near the coordinates
     *  While there is no result new request with a larger radius is send
	 */
	public static List<Place> getAllPlacesNear(Point location) throws IOException, JSONException{
		List<Place> places = new ArrayList<>();
		int radius = Util.DEFAULT_RADIUS;
        do {
            places = APIConnector.getPlaces(location, radius);
            radius += 2000;
        } while (places.size() == 0 && radius < RADIUS_MAX);
        return places;
	}
	
	public static String getDayOfTheWeekFromDate(String date){
		Calendar cal = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		try {
			cal.setTime(format.parse(date));
			int day = cal.get(Calendar.DAY_OF_WEEK);
			return DAYS[(day - 2 + DAYS.length) % DAYS.length];
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
}
