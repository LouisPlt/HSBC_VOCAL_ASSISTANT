package answerNearestPlace;

import models.Place;

public class AddressOfNearestAgency implements AnswerNearestPlace {
	
	@Override
	public String getTextResponse(Place place) {
		return "There is one agency at " + place.getVicinity();
	}
}