package googleAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class Point {
	
	private double lat, lng;
	
	public Point (double lat, double lng) {
        this.setLat(lat);
        this.setLng(lng);
    }
	public Point (JSONObject location) throws JSONException {
		JSONObject coordinates = location.getJSONObject("geometry").getJSONObject("location");

		this.lat = Double.parseDouble(coordinates.getString("lat"));
		this.lng = Double.parseDouble(coordinates.getString("lng"));
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String toString(){
		return String.valueOf(getLat())+','+String.valueOf(getLng());
	}
	
}
