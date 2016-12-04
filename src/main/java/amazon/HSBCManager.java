package amazon;

import static googleApi.APIConnector.getCoordinatesFromAddress;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import org.json.JSONException;

import answer.Answer;
import answer.AnswerNearestPlace;
import answer.BankBalance;
import answer.DayOpeningHoursOfNearestAgency;
import googleApi.Place;
import googleApi.Point;
import googleApi.Util;

/**
 * Created by louis on 05/11/16.
 */

public class HSBCManager {

	private static final String SLOT_DATE = "Date";
			
    public SpeechletResponse getOnLaunchResponse(LaunchRequest request, Session session) {
        String speechText;

        speechText = "HSBC, What can I do for you?";
        return getTellSpeechletResponse(speechText);
    }
    
    public SpeechletResponse getNearestPlaceGenericIntentResponse(AnswerNearestPlace answer) throws IOException, JSONException {   	

        // TODO : To be replaced with the location of alexa
        Point coordinates = getCoordinatesFromAddress("Paris");

        List<Place> places = Util.getAllPlacesNear(coordinates);

        if(places.size() != 0 ){
            Place nearestPlace = Util.findNearestPlace(coordinates, places);
            String responseText = answer.getTextResponse(nearestPlace);
            return getTellSpeechletResponse(responseText);
        }
        else {
            return nothingFoundResponse();
        }
    }
    
    public SpeechletResponse getDayOpeningHoursNearestPlaceIntentResponse(IntentRequest request) throws IOException, JSONException {   	
        
    	Intent intent = request.getIntent();
    	String date = intent.getSlot(SLOT_DATE).getValue();
    	String day = Util.getDayOfTheWeekFromDate(date);
    	if(day == null){
    		return nothingFoundResponse();
    	}
    	
        // TODO : To be replaced with the location of alexa
        Point coordinates = getCoordinatesFromAddress("Paris");
        List<Place> places = Util.getAllPlacesNear(coordinates);     
        if(places.size() != 0 ){
            Place nearestPlace = Util.findNearestPlace(coordinates, places);
            String responseText = new DayOpeningHoursOfNearestAgency().getTextResponse(nearestPlace, day);
            return getTellSpeechletResponse(responseText);
        }
        else {
            return nothingFoundResponse();
        }
    }

    public SpeechletResponse nothingFoundResponse(){
        String speechText = "No result matches your request";
        return getTellSpeechletResponse(speechText);
    }

    private SpeechletResponse getTellSpeechletResponse(String speechText) {
        SimpleCard card = new SimpleCard();
        card.setTitle("Session");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

	public SpeechletResponse getGenericIntentResponse(Answer answer) {
		try {
			String responseText = answer.getTextResponse();
			return getTellSpeechletResponse(responseText);
		}catch (Exception e){
			return nothingFoundResponse();
		}
	}
}
