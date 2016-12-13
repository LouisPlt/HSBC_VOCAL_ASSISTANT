package amazon;

import answer.BankAdvisor;
import answer.BankBalance;
import answer.BankCeiling;
import answer.MaxBankOverdraft;
import answerNearestPlace.AddressOfNearestAgency;
import answerNearestPlace.NumOfNearestAgency;
import answerNearestPlace.OpeningHoursOfNearestAgency;
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

    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        initializeComponents();

    }

    public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        initializeComponents();

        return hSBCManager.getOnLaunchResponse(request, session);
    }

    public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        initializeComponents();

        Intent intent = request.getIntent();
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
                	return hSBCManager.getGenericIntentResponse(new BankBalance());
                case "GetMaxOverdraftIntent":
                	return hSBCManager.getGenericIntentResponse(new MaxBankOverdraft());
                case "GetBankCeilingIntent":
                	return hSBCManager.getGenericIntentResponse(new BankCeiling());
                case "GetAdvisorInfoIntent":
                    return hSBCManager.getGenericIntentResponse(new BankAdvisor());
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

    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {

    }

    private void initializeComponents() {
        hSBCManager = new HSBCManager();
    }
}