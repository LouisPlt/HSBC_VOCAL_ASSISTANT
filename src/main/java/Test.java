import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;

import googleApi.APIConnector;
import googleApi.Place;
import googleApi.Util;

public class Test {

	public static void main(String[] args) throws IOException, JSONException {
      List<Place> places = APIConnector.getPlaces(APIConnector.getCoordinatesFromAddress("Paris"), Util.DEFAULT_RADIUS);
      Place place = places.get(0);
  	//place.findPhoneNumber();
// 	place.findOpeningHours();
//  	System.out.println(place.getCoordinate());
//  	System.out.println(place.getPhoneNumber());
//  	
//  	// TODO : create UT!
//  	// get and test similar days (based on their opening hours) :
//  	List<List<String>> similarDays = getSimilarOpeningHoursDays(place);
//  	for(List<String> days : similarDays){
//  		System.out.print("Groupe : ");
//  		for(String day : days){
//  			System.out.print(day + " ");
//  		}
//  		System.out.println();
//  	}
//  	
//  	// test openingHours :
//  	StringBuilder result = new StringBuilder("The agency is open ");
//  	
//  	for(List<String> days : similarDays){	
//  		for(String day : days){
//  			List<String> hours = place.getOpeningHours().get(day);
//      		if(hours.isEmpty()){
//      			break;
//      		}
//  			if(day.equals(days.get(days.size()-1))){
//      			result.append("and ");
//      			result.append(day);
//          		result.append(" from ");
//              	for(int i = 0; i < hours.size(); i++){
//              		if(i%2 == 1){
//              			result.append(" to ");
//              		} else if(i != 0){
//              			result.append(" then from ");
//              		}
//              		result.append(hours.get(i).substring(0,2));
//              		if(!hours.get(i).substring(2).equals("00")){
//                  		result.append(" ");
//                  		result.append(hours.get(i).substring(2));
//              		}
//          		}
//              	result.append(", ");
//          	} else {
//          		result.append(day);
//          		result.append(", ");
//  			}
//  		}
//  	}
//  	result.delete(result.length() - 2, result.length() - 1);
//  	System.out.println(result);
	place.findOpeningHours();
  	String date = "2016-11-25";
  	String day = Util.getDayOfTheWeekFromDate(date);
  	System.out.println(day);
  	List<String> hours = place.getOpeningHours().get(day);
  	StringBuilder response = new StringBuilder("The agency is open ");
  	response.append(day);
  	response.append(" from ");
  	for(int i = 0; i < hours.size(); i++){
		if(i%2 == 1){
			response.append(" to ");
		} else if(i != 0){
			response.append(" then from ");
		}
		response.append(hours.get(i).substring(0,2));
		if(!hours.get(i).substring(2).equals("00")){
			response.append(" ");
			response.append(hours.get(i).substring(2));
		}
	}
  	System.out.println(response);
  	
  }
  
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
}
