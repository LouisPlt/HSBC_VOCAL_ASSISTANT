package amazon;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;


import answerPrivateQuestion.*;
import answerPublicQuestion.*;

import application.Util;
import config.DatabaseConnector;


/**
 * Created by louis on 05/11/16.
 */
public class HSBCSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(HSBCSpeechlet.class);
    private HSBCManager hSBCManager;
    private static final List<String> PRIVATE_QUESTIONS = Arrays.asList("GetBalanceIntent", "GetMaxOverdraftIntent","GetBankCeilingIntent","GetAdvisorInfoIntent");
    private static String token;
    
    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        initializeComponents();
    }
    
    public boolean getTokenFromDB(String token) throws SQLException
    {
    	ResultSet result = DatabaseConnector.getConnection().createStatement().executeQuery("SELECT token FROM clients WHERE token = " + token);
		if(result.next())
		{
			return true;
		} else {
			return false;
		}
    }
    
//    public String getInfoClient(String token)
//    {
//    	ResultSet result = DatabaseConnector.getConnection().createStatement().executeQuery("SELECT client FROM clients WHERE token = " + token);
//    	result.next();
//    	return result.getString("state");
//    }
   

    @Override
    public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        initializeComponents();
        return hSBCManager.getOnLaunchResponse(request, session);
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        initializeComponents();

        Intent intent = request.getIntent();

        // Start authentification if needed
        if(PRIVATE_QUESTIONS.contains(intent.getName())){
            token = session.getUser().getAccessToken();
            try {
    			if (getTokenFromDB(token)){
    				System.out.println("Token in DB !");
    			} else {
    				// LinkedCard
    				return hSBCManager.getAuthentificationIntentResponse(session, request);
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
        }

        try {
            switch (intent.getName()) {
                case "AddressNearestPlaceIntent":
                    return hSBCManager.getNearestPlaceGenericIntentResponse(new AddressOfNearestAgency());
                case "PhoneNumberNearestPlaceIntent":
                    return hSBCManager.getNearestPlaceGenericIntentResponse(new NumOfNearestAgency());
                case "OpeningHoursNearestPlaceIntent":
                	return hSBCManager.getNearestPlaceGenericIntentResponse(new OpeningHoursOfNearestAgency());
                case "DayOpeningHoursNearestPlaceIntent":
                	return hSBCManager.getDayOpeningHoursNearestPlaceIntentResponse(request);
                case "GetBalanceIntent":
                	return hSBCManager.getGenericIntentResponse(new BankBalance(), session);
                case "GetMaxOverdraftIntent":
                	return hSBCManager.getGenericIntentResponse(new MaxBankOverdraft(), session);
                case "GetBankCeilingIntent":
                	return hSBCManager.getGenericIntentResponse(new BankCeiling(), session);
                case "GetAdvisorInfoIntent":
                    return hSBCManager.getGenericIntentResponse(new BankAdvisor(),session);
                case "LoginIntent" :
                	return hSBCManager.getLoginIntentResponse(request, session);
                case "PasswordIntent" :
                	return hSBCManager.getPasswordIntentResponse(request, session);
                default:
                    return hSBCManager.nothingFoundResponse();
            }
        } catch (IOException | JSONException | SQLException e){
            return hSBCManager.getTellSpeechletResponse(e.toString());
        }
    }

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
    }
    
    private void initializeComponents() {
        hSBCManager = new HSBCManager();
    }
}