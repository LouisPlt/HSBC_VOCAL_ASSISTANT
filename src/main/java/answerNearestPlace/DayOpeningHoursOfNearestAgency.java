package answerNearestPlace;

import java.util.List;

import models.Place;

public class DayOpeningHoursOfNearestAgency {

	public String getTextResponse(Place place, String day) {
		place.findOpeningHours();
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
	  	return response.toString();
	}
}
