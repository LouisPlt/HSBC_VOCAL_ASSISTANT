package amazon;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

import googleApi.APIConnector;
import googleApi.Place;
import googleApi.Point;
import googleApi.Util;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import static googleApi.APIConnector.getCoordinatesFromAddress;

/**
 * Created by louis on 05/11/16.
 */

public class HSBCManager {

    private static final String SLOT_CITY = "City";
    private static int RADIUS_MAX = 10000;


    public SpeechletResponse getOnLaunchResponse(LaunchRequest request, Session session) {
        String speechText;

        speechText = "HSBC, What can I do for you?";
        return getTellSpeechletResponse(speechText);
    }
    
    public SpeechletResponse getAddressNearestGenericIntentResponse(IntentRequest request, Session session, Answer answer) throws IOException, JSONException {
    	
        // Get the address input
        Intent intent = request.getIntent();

        // TODO : To be replaced with the location of alexa
        Point coordinates = getCoordinatesFromAddress("Paris");

        // Get the list of all places near the coordinates
        // While there is no result new request with a larger radius is send
        ArrayList<Place> places ;
        int radius = Util.DEFAULT_RADIUS;
        do {
            places = APIConnector.getPlaces(coordinates, radius);
            radius += 2000;
        } while (places.size() == 0 && radius < RADIUS_MAX);

        if(places.size() != 0 ){
            // Find the nearest place
            Place nearestPlace = Util.findNearestPlace(coordinates, places);

            // Return the address of the nearest place
            String responseText = answer.getTextResponse(nearestPlace);
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
}
