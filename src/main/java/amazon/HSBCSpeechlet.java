package amazon;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Created by louis on 05/11/16.
 */
public class HSBCSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(HSBCSpeechlet.class);
    private HSBCManager hSBCManager;

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
        switch (intent.getName()){
            case "AddressNearestPlaceIntent" :
                try {
                    return hSBCManager.getAddressNearestPlaceIntentResponse(request, session);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            default:
                return hSBCManager.nothingFoundResponse();
        }
    }

    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {

    }

    private void initializeComponents() {
        hSBCManager = new HSBCManager();

    }
}
