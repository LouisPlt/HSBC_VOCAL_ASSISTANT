package googleAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class Place {
	
	private Point coordinate;

    private String vicinity;

    private String placeId;
    private String name;
	public Place(JSONObject place) throws JSONException {

		this.placeId = place.getString("place_id");
        this.name = place.getString("name");
        this.coordinate = new Point(place);
		this.vicinity = place.getString("vicinity");
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
}
