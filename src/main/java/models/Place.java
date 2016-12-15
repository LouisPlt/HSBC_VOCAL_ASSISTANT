package models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import application.Util;
import googleApi.APIConnector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Place {
	
	private Point coordinate;
    private String vicinity;
    private String placeId;
    private String name;
    private String phoneNumber;
    private Map<String, List<String>> openingHours = new LinkedHashMap<>();

	public Place(JSONObject place) throws JSONException {
        this.placeId        = place.getString("place_id");
        this.name           = place.getString("name");
        this.coordinate     = new Point(place);
		this.vicinity       = place.getString("vicinity");
	}

	public void findPhoneNumber(){
        try {    
            JSONObject details = findDetails();
            if(details != null){
                phoneNumber = details.getString("formatted_phone_number");
            }
        } catch (JSONException e ) {
            e.printStackTrace();
        }
    }
	
	public void findOpeningHours(){
		// Init :
		for(int i = 0; i<7; i++){
			openingHours.put(Util.DAYS[i], new ArrayList<>());
		}
		
        try {
            JSONObject details = findDetails();
            if(details != null){
                JSONArray periodTab = details.getJSONObject("opening_hours").getJSONArray("periods");
                
                for (int i = 0 ; i < periodTab.length(); i++) {
                    JSONObject open  = periodTab.getJSONObject(i).getJSONObject("open");
                    JSONObject close = periodTab.getJSONObject(i).getJSONObject("close");
                    String day = Util.DAYS[open.getInt("day")-1];
                    List<String> hours = openingHours.get(day);
                    hours.add(open.getString("time"));
                    hours.add(close.getString("time"));
                    openingHours.put(day, hours);
                }
            }
        } catch (JSONException e ) {
            e.printStackTrace();
        }
    }
	
	public JSONObject findDetails(){
        try {
            JSONObject details  = APIConnector.getPlaceDetails(placeId);     
            return details.getJSONObject("result");
        } catch (IOException | JSONException e ) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, List<String>> getOpeningHours() {
        return openingHours;
    }
    
    public void setOpeningHours(Map<String, List<String>> openingHours){
    	this.openingHours = openingHours;
    }

	public Point getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Point coordinate) {
		this.coordinate = coordinate;
	}

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getPhoneNumber() {
    	return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber){
    	this.phoneNumber = phoneNumber;
    }
}
