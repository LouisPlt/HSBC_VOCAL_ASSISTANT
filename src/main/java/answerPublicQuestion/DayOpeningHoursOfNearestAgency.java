package answerPublicQuestion;

import java.util.List;

import application.Util;
import models.Place;

public class DayOpeningHoursOfNearestAgency {

	public String getTextResponse(Place place, String day) {
		place.findOpeningHours();
	  	List<String> hours = place.getOpeningHours().get(day);
	  	
	  	StringBuilder response = new StringBuilder("The agency is open ");
	  	response.append(day);
	  	response.append(" from ");
	  	response = Util.buildResponseFromOpeningHours(response, hours);
	  	return response.toString();
	}
}
