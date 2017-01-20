package amazon;

import static application.APIConnector.getCoordinatesFromAddress;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.LinkAccountCard;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

import answerPrivateQuestion.AnswerPrivateQuestion;
import answerPublicQuestion.AnswerPublicQuestion;
import answerPublicQuestion.DayOpeningHoursOfNearestAgency;
import application.Slots;
import application.Util;
import config.DatabaseConnector;
import models.Place;
import models.Point;

/**
 * Created by louis on 05/11/16.
 */

public class HSBCManager {
    private static final Logger log = LoggerFactory.getLogger(HSBCManager.class);

    public SpeechletResponse getOnLaunchResponse(LaunchRequest request, Session session) {
        return getTellSpeechletResponse("HSBC, What can I do for you?");
    }
    
    public SpeechletResponse getNearestPlaceGenericIntentResponse(AnswerPublicQuestion answer) throws IOException, JSONException {

        // TODO : To be replaced with the location of alexa
        //Point coordinates = getCoordinatesFromAddress("La Defense");
    	Point coordinates = new Point(48.891370, 2.241579);
        
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
    	String date = intent.getSlot(Slots.SLOT_DATE).getValue();
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
        return getTellSpeechletResponse("No result matches your request");
    }

    SpeechletResponse getTellSpeechletResponse(String speechText) {
        SimpleCard card = new SimpleCard();
        card.setTitle("Session");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }
    
	public SpeechletResponse getGenericIntentResponse(AnswerPrivateQuestion answer, Session session) {

		try {
		    String token = session.getUser().getAccessToken();
		    ResultSet result = DatabaseConnector.getInfoFromToken(token);
			String responseText = answer.getTextResponse(result.getString("login"));
			SpeechletResponse response = getTellSpeechletResponse(responseText);
			response.setShouldEndSession(false);
			return response;
		} catch (Exception e){
            System.out.println(e);
            return nothingFoundResponse();
		}
	}
	
	public SpeechletResponse getPasswordIntentResponse(IntentRequest request, Session session) {
		String speechText;
		Intent intent = request.getIntent();
	    String password = intent.getSlot(Slots.SLOT_PASSWORD).getValue();
	    String token = session.getUser().getAccessToken();
	    SpeechletResponse response;
	    try {
	    	String login = DatabaseConnector.getInfoFromToken(token).getString("login");
	        
			if(Util.isPasswordCorrect(login, password)){
				speechText = "You're successfully logged in. What can I do for you ?";
				String sessionStartTime = DateTime.now().getYear() + "." + DateTime.now().getDayOfYear() + "." + DateTime.now().getSecondOfDay();
				session.setAttribute("sessionStartTime", sessionStartTime);
	        }else{
				speechText = "Your password is wrong, please try again.";
			}
	    } catch (SQLException e){
	    	log.error(e.getMessage());
	    	speechText = "An error occured during the request.";
	    }
	    response = getTellSpeechletResponse(speechText);
		response.setShouldEndSession(false);
		return response;
	}
    
    public SpeechletResponse getLinkIntentResponse(IntentRequest request, Session session) {
		String speechText = "You need to link your HSBC account to your amazon echo first. Please check your phone and click on the link to log in.";
		SpeechletResponse response = getTellSpeechletResponse(speechText);
		response.setShouldEndSession(false);
		
		LinkAccountCard card = new LinkAccountCard();
	    card.setTitle("Link your HSBC account");
	    PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
	    speech.setText(speechText);

	    return SpeechletResponse.newTellResponse(speech, card);
	}
    
    public SpeechletResponse getAuthentificationIntentResponse(IntentRequest request, Session session) {
		String speechText = "You need to log in first : what is your HSBC password?";
		return getTellSpeechletResponse(speechText);
	}
}
