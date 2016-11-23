package googleApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Place {
	
	private Point coordinate;
    private String vicinity;
    private String placeId;
    private String name;
    private String phoneNumber;

    private ArrayList<String []> openingHours  = new ArrayList<>();

	public Place(JSONObject place) throws JSONException {
        System.out.println(place);
        this.placeId        = place.getString("place_id");
        this.name           = place.getString("name");
        this.coordinate     = new Point(place);
		this.vicinity       = place.getString("vicinity");
	}

	public void findDetails(){
        try {
            JSONObject details  = APIConnector.getPlaceDetails(placeId);

            this.phoneNumber = details.getString("formatted_phone_number");
            JSONArray periodTab = details.getJSONObject("opening_hours").getJSONArray("periods");

            for (int i = 0 ; i < periodTab.length(); i++) {
                JSONObject open     = periodTab.getJSONObject(i).getJSONObject("open");
                JSONObject close    = periodTab.getJSONObject(i).getJSONObject("close");
                String day = Util.DAYS[open.getInt("day")-1];
                String hours[] = {day, open.getString("time"),close.getString("time")};
                openingHours.add(hours);

                periodTab.getJSONObject(i);
            }


        } catch (IOException | JSONException e ) {
            e.printStackTrace();
        }
    }


    public ArrayList<String[]> getOpeningHours() {
        return openingHours;
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
