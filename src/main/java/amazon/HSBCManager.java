package amazon;

import googleAPI.APIConnector;
import googleAPI.Place;
import googleAPI.Point;
import googleAPI.Util;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by louis on 05/11/16.
 */

public class HSBCManager {

    private static final String SLOT_CITY = "City";
    private static int RADIUS_MAX = 10000;
    private static int DEFAULT_RADIUS = 1000;


    public SpeechletResponse getOnLaunchResponse(LaunchRequest request, Session session) {
        String speechText;

        speechText = "HSBC, What can I do for you?";
        return getTellSpeechletResponse(speechText);
    }


    public SpeechletResponse getAddressNearestPlaceIntentResponse(IntentRequest request, Session session) throws IOException, JSONException {

        // Get the address input
        Intent intent = request.getIntent();
        String input_address = intent.getSlot(SLOT_CITY ).getValue();

        // Get the coordinates of the address input
        Point coordinates =  APIConnector.getCoordinatesFromAddress(input_address);

        // Get the list of all places near the coordinates
        // While there is no result new request with a larger radius is send
        ArrayList<Place> places ;
        int radius = DEFAULT_RADIUS;
        do {
            places = APIConnector.getPlaces(coordinates, radius);
            radius += 2000;
        } while (places.size() == 0 && radius < RADIUS_MAX);

        if(places.size() != 0 ){
            // Find the nearest place
            Place nearestPlace = Util.findNearestPlace(coordinates, places );

            // Return the address of the nearest place
            String responseText = "There is one agency at "+ nearestPlace.getVicinity();
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
