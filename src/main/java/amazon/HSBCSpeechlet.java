package amazon;

import answerPrivateQuestion.BankAdvisor;
import answerPrivateQuestion.BankBalance;
import answerPrivateQuestion.BankCeiling;
import answerPrivateQuestion.MaxBankOverdraft;
import answerPublicQuestion.AddressOfNearestAgency;
import answerPublicQuestion.NumOfNearestAgency;
import answerPublicQuestion.OpeningHoursOfNearestAgency;
import application.Util;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


/**
 * Created by louis on 05/11/16.
 */
public class HSBCSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(HSBCSpeechlet.class);
    private HSBCManager hSBCManager;
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
        return hSBCManager.getOnLaunchResponse(request, session);
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        initializeComponents();

        Intent intent = request.getIntent();

        // Start authentification if needed
        if(PRIVATE_QUESTIONS.contains(intent.getName()) && Util.sessionEnded(session)){
                return hSBCManager.getAuthentificationIntentResponse(session, request);
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