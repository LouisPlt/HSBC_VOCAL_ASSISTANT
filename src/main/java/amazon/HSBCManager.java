package amazon;

import static application.APIConnector.getCoordinatesFromAddress;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONException;

import answerPrivateQuestion.AnswerPrivateQuestion;
import answerPrivateQuestion.BankAdvisor;
import answerPrivateQuestion.BankBalance;
import answerPrivateQuestion.BankCeiling;
import answerPrivateQuestion.MaxBankOverdraft;
import answerPublicQuestion.AnswerPublicQuestion;
import answerPublicQuestion.DayOpeningHoursOfNearestAgency;
import application.Authentification;
import application.Slots;
import application.Util;
import config.DatabaseConnector;
import models.Place;
import models.Point;

/**
 * Created by louis on 05/11/16.
 */

public class HSBCManager {
	
    public SpeechletResponse getOnLaunchResponse(LaunchRequest request, Session session) {
        return getTellSpeechletResponse("HSBC, What can I do for you?");
    }
    
    public SpeechletResponse getNearestPlaceGenericIntentResponse(AnswerPublicQuestion answer) throws IOException, JSONException {

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
            String clientLogin = (String) session.getAttribute("login");
			String responseText = answer.getTextResponse(clientLogin);
			SpeechletResponse response = getTellSpeechletResponse(responseText);
			response.setShouldEndSession(false);
			return response;
		} catch (Exception e){
            System.out.println(e);
            return nothingFoundResponse();
		}
	}
	
	public SpeechletResponse getLoginIntentResponse(IntentRequest request, Session session) throws SQLException{
        String speechText;
        SpeechletResponse response;
		Intent intent = request.getIntent();
	    String login = intent.getSlot(Slots.SLOT_LOGINPONE).getValue() + intent.getSlot(Slots.SLOT_LOGINPTWO).getValue();	    
		
//		Vérification de la présence de l'user en Database	
	    String name = DatabaseConnector.getClientName(login);
	    if(name != null){
	    	speechText = "Welcome "+ DatabaseConnector.getClientName(login) + ". Please give me your password.";
	    	response = getTellSpeechletResponse(speechText);
		    response.setShouldEndSession(false);
		    session.setAttribute("login", login);
	    }
	    else{
	    	speechText = "Your login is incorrect.";
	    	response = getTellSpeechletResponse(speechText);
	    }
	    
        return response;
	}

	public SpeechletResponse getPasswordIntentResponse(IntentRequest request, Session session) throws SQLException {
		String speechText;
		Intent intent = request.getIntent();
	    String password = intent.getSlot(Slots.SLOT_PASSWORD).getValue();
	    String login = (String) session.getAttribute("login");
    
	    Authentification auth = new Authentification(login, password);
	    auth.checkPassword();
		if(	auth.isSucceeded()){
			speechText = "You're successfully logged in. What can I do for you ?";
			session.setAttribute("sessionStartTime", auth.getSessionStartTime());
            String intentBeforeAuth = (String) session.getAttribute("intentBeforeAuth");
			if(intentBeforeAuth != null){
                return getPrivateQuestionByIntent(intentBeforeAuth, session);
            }
        }else{
			speechText = auth.getReasonOfFailure(); 
		}
		SpeechletResponse response = getTellSpeechletResponse(speechText);

        return response;
	}

	public SpeechletResponse getPrivateQuestionByIntent(String intentName, Session session){
        switch (intentName){
            case "GetBalanceIntent":
                return getGenericIntentResponse(new BankBalance(), session);
            case "GetMaxOverdraftIntent":
                return getGenericIntentResponse(new MaxBankOverdraft(), session);
            case "GetBankCeilingIntent":
                return getGenericIntentResponse(new BankCeiling(), session);
            case "GetAdvisorInfoIntent":
                return getGenericIntentResponse(new BankAdvisor(), session);
            default:
                return getTellSpeechletResponse("You're successfully logged in. What can I do for you ?");
        }
    }


    public SpeechletResponse getAuthentificationIntentResponse(Session session, IntentRequest request) {
		String speechText = "You need to log in first. What's your login ?";
		Intent intent = request.getIntent();

		SpeechletResponse response = getTellSpeechletResponse(speechText);
		response.setShouldEndSession(false);
		session.setAttribute("intentBeforeAuth", intent.getName());

		return response;
	}
}
