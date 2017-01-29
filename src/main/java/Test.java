import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;

import config.DatabaseConnector;

public class Test {

	/*
	 * TODO : Replace it with UT!
	 */
	public static void main(String[] args) throws IOException, JSONException, SQLException {
		//System.out.println( "Your maximum allowed overdraft is "+ result.getString("overdraft_value_max")+" euros");
//		String query = "UPDATE clients SET token = '0' ;";
//		DatabaseConnector.getConnection().createStatement().executeQuery(query);
		
			String query =  "SELECT t.amount, t.description, a.account_type_id, a.id, t.sender_account_id " +
					"FROM transfers t " +
					"JOIN accounts a " +
					"ON t.sender_account_id = a.id OR t.receiver_account_id = a.id " +
					"WHERE a.client_id IN (SELECT id FROM clients WHERE CAST(login AS INTEGER) = 17602697)" +
					"ORDER BY t.date DESC";
			ResultSet result = DatabaseConnector.getConnection().createStatement().executeQuery(query);
			
			try {
				System.out.println(buildResponse(result, "", 10));
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	
//				
}
	private static String buildResponse(ResultSet result, String response, int nbOfTransfers) throws Exception {		
		int i = 0;
		while(result.next() && (i < nbOfTransfers) && (i < 10)){	
			String[] description = result.getString("description").split(" ");
			String transfertType = description[0];
		
			switch(transfertType){
				case "CB": 
					response += "Your account has been debited of " + result.getString("amount") + " euros from " +  description[1];
					break;
				case "PRLV": 
					response += "You have been charged " + result.getString("amount") + " euros from " +  description[1];
					break;
				case "RETRAIT":
					response += "You have withdrawn " + result.getString("amount") + " euros";
					break;
				case "VIREMENT":
					if(result.getString("sender_account_id") == result.getString("id")){
						response += "You have received a transfer of  " + result.getString("amount") + " euros";
					}
					else {
						response += "You have made a transfer of " + result.getString("amount") + " euros";
					}
					break;
				default: 
					response += "Your account has been charged of " + result.getString("amount") + " euros";
					break;
			}
			i++;
			response+=". ";
		}
		if(i >= 10){
			response += "If you want to have more than 10 reponses, go check your transfers on the HSBC website.";
		}
		return response;
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
