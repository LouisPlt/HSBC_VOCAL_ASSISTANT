package googleApi;
import org.json.JSONException;

import java.util.ArrayList;

public class Util {
	
	public static int DEFAULT_RADIUS = 1000;
	public static String[] DAYS = {"Lundi", "Mardi" ,"Mecredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};


	// d(a,b) = sqrt((xb - xa)^2 + (yb - ya)^2)
	public static double distance(Point p1, Point p2){
		double a = p2.getLat() - p1.getLat();
		double b = p2.getLng() - p1.getLng();
		return Math.sqrt(a*a + b*b);
	}
	
	public static Place findNearestPlace(Point location, ArrayList<Place> places) throws JSONException {
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
}
