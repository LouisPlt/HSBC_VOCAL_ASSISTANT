package Answer;

import googleApi.Place;

public class AddressOfNearestAgency implements Answer {
	
	@Override
	public String getTextResponse(Place place) {
		return "There is one agency at " + place.getVicinity();
	}
}
