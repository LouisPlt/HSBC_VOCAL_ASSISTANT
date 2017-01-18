import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONException;

import answerPrivateQuestion.BankBalance;
import config.DatabaseConnector;

public class Test {

	/*
	 * TODO : Replace it with UT!
	 */
	public static void main(String[] args) throws IOException, JSONException, SQLException {
		//System.out.println( "Your maximum allowed overdraft is "+ result.getString("overdraft_value_max")+" euros");
		DatabaseConnector.getConnection().createStatement().executeQuery("UPDATE clients SET token = '0' ;");
		
		System.out.println(new BankBalance().getTextResponse("11445113"));
		
		
		
}
      //Place place = places.get(0);
//		place.findPhoneNumber();
// 		place.findOpeningHours();
//  	System.out.println(place.getCoordinate());
//  	System.out.println(place.getPhoneNumber());
//  	
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
//  	// testing openingHoursOfNearestAgency :
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
      
    // testing DayOpeningHoursOfNearestAgency
//	place.findOpeningHours();
//  	String date = "2016-11-25";
//  	String day = Util.getDayOfTheWeekFromDate(date);
//  	System.out.println(day);
//  	List<String> hours = place.getOpeningHours().get(day);
//  	StringBuilder response = new StringBuilder("The agency is open ");
//  	response.append(day);
//  	response.append(" from ");
//  	for(int i = 0; i < hours.size(); i++){
//		if(i%2 == 1){
//			response.append(" to ");
//		} else if(i != 0){
//			response.append(" then from ");
//		}
//		response.append(hours.get(i).substring(0,2));
//		if(!hours.get(i).substring(2).equals("00")){
//			response.append(" ");
//			response.append(hours.get(i).substring(2));
//		}
//	}
//  	System.out.println(response);
  	
 // }

}
