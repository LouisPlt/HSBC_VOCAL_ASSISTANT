package answerPublicQuestion;

import java.util.ArrayList;
import java.util.List;

import application.Util;
import models.Place;

public class OpeningHoursOfNearestAgency implements AnswerPublicQuestion {

	@Override
	public String getTextResponse(Place place) {
		place.findOpeningHours();
		List<List<String>> similarDays = getSimilarOpeningHoursDays(place);
    	StringBuilder response = new StringBuilder("The agency is open ");
    	
    	for(List<String> days : similarDays){	
    		for(String day : days){
    			List<String> hours = place.getOpeningHours().get(day);
    			
    			// If the agency is closed that day we go to the next day :
        		if(hours.isEmpty()){
        			break;
        		}
        		
        		// If this is the last day of the similar days we print all the details :
    			if(day.equals(days.get(days.size()-1))){
    				response.append("and ");
    				response.append(day);
    				response.append(" from ");
                	response = Util.buildResponseFromOpeningHours(response, hours);
                	response.append(", ");
            	} else {
            		response.append(day);
            		response.append(", ");
    			}
    		}
    	}
    	// To remove the last comma :
    	response.delete(response.length() - 2, response.length() - 1);
    	
    	return response.toString();
	}
	
	/*
	 *  Method used to group days with the same hours
	 *  @return the list of the similar days groups
	 */
    List<List<String>> getSimilarOpeningHoursDays(Place place){
    	List<List<String>> similarDays = new ArrayList<>(); 
    	
    	for(String day : place.getOpeningHours().keySet()){
    		boolean matchingExistingHours = false;
    		List<String> hours = place.getOpeningHours().get(day);
    		
    		int i = 0;
    		while(!matchingExistingHours && i < similarDays.size()){
    			String previousDay = similarDays.get(i).get(0);
    			// If the day has the same opening hours of a group already existing, add it to this group
    			if(place.getOpeningHours().get(previousDay).equals(hours)){
    				matchingExistingHours = true;
    				List<String> days = similarDays.get(i);
    				days.add(day);
    				similarDays.set(i, days);
    			}
    			i++;
    		}    		
    		// Else, create another group :
    		if(!matchingExistingHours){
	    		List<String> days = new ArrayList<>();
	    		days.add(day);
	    		similarDays.add(days);
    		}
    	}
    	return similarDays;
    }

}
