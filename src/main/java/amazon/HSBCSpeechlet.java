package amazon;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;

import answerPrivateQuestion.BankAdvisor;
import answerPrivateQuestion.BankBalance;
import answerPrivateQuestion.BankCeiling;
import answerPrivateQuestion.MaxBankOverdraft;
import answerPublicQuestion.AddressOfNearestAgency;
import answerPublicQuestion.NumOfNearestAgency;
import answerPublicQuestion.OpeningHoursOfNearestAgency;
import application.Util;
import config.DatabaseConnector;


/**
 * Created by louis on 05/11/16.
 */
public class HSBCSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(HSBCSpeechlet.class);
    private HSBCManager _HSBCManager;
    private static final List<String> PRIVATE_QUESTIONS = Arrays.asList("GetBalanceIntent", "GetMaxOverdraftIntent","GetBankCeilingIntent","GetAdvisorInfoIntent");
    
    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        initializeComponents();
    }
    

    @Override
    public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        initializeComponents();
        return _HSBCManager.getOnLaunchResponse(request, session);
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        initializeComponents();

        Intent intent = request.getIntent();
        
        // Start authentification if needed
        if(PRIVATE_QUESTIONS.contains(intent.getName())){
        	String token = session.getUser().getAccessToken();
        	// Si le token est invalid on renvoie l'intent pour s'authentifier :
			if (token == null || token.isEmpty() || !DatabaseConnector.isTokenInDB(token)){
				return _HSBCManager.getLinkIntentResponse(request, session);
			} else if(Util.sessionEnded(session)){
				return _HSBCManager.getAuthentificationIntentResponse(request, session);
			}
        }

        try {
            switch (intent.getName()) {
                case "AddressNearestPlaceIntent":
                    return _HSBCManager.getNearestPlaceGenericIntentResponse(new AddressOfNearestAgency());
                case "PhoneNumberNearestPlaceIntent":
                    return _HSBCManager.getNearestPlaceGenericIntentResponse(new NumOfNearestAgency());
                case "OpeningHoursNearestPlaceIntent":
                	return _HSBCManager.getNearestPlaceGenericIntentResponse(new OpeningHoursOfNearestAgency());
                case "DayOpeningHoursNearestPlaceIntent":
                	return _HSBCManager.getDayOpeningHoursNearestPlaceIntentResponse(request);
                case "GetBalanceIntent":
                	return _HSBCManager.getGenericIntentResponse(new BankBalance(), session);
                case "GetMaxOverdraftIntent":
                	return _HSBCManager.getGenericIntentResponse(new MaxBankOverdraft(), session);
                case "GetBankCeilingIntent":
                	return _HSBCManager.getGenericIntentResponse(new BankCeiling(), session);
                case "GetAdvisorInfoIntent":
                    return _HSBCManager.getGenericIntentResponse(new BankAdvisor(),session);
                case "PasswordIntent" :
                	return _HSBCManager.getPasswordIntentResponse(request, session);
                default:
                    return _HSBCManager.nothingFoundResponse();
            }
        } catch (IOException | JSONException e){
        	log.error(e.getMessage());
            return _HSBCManager.getTellSpeechletResponse("An error occured during the request.");
        }
    }

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
    }
    
    private void initializeComponents() {
    	_HSBCManager = new HSBCManager();
    }
}