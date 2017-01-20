package answerPublicQuestion;

import models.Place;

public class AddressOfNearestAgency implements AnswerPublicQuestion {
	
	@Override
	public String getTextResponse(Place place) {
		return "There is one agency at " + place.getVicinity();
	}
}
