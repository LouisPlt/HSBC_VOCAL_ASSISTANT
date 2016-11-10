package googleAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.*;

public class Test {

	public static void main(String[] args) throws JSONException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File("test.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Lecture du fichier json
		Scanner scan = new Scanner(fis);
	    String str = new String();
	    while (scan.hasNext()){
	        str += scan.nextLine();	
	    }
	    scan.close();
	    
	    // Creation de l'objet json
	    JSONObject obj = new JSONObject(str);
	    if (! obj.getString("status").equals("OK"))
	        return;
	 
	    // get results
	    JSONObject res = obj.getJSONObject("result");
	    System.out.println(res.getString("formatted_address"));
	    JSONObject loc = res.getJSONObject("geometry").getJSONObject("location");
	    System.out.println("lat: " + loc.getDouble("lat") + ", lng: " + loc.getDouble("lng"));
	    
	    Point p1 = new Point(loc.getDouble("lat"), loc.getDouble("lng"));
	    Point p2 = new Point(loc.getDouble("lat") + 5, loc.getDouble("lng") + 2);
	    Point p3 = new Point(loc.getDouble("lat") + 7, loc.getDouble("lng") + 4);
	    
	    System.out.println(Util.distance(p1, p2));
	    System.out.println(Util.distance(p1, p3));
	    
	    Point[] points = {p2, p3};
	    //System.out.println(Util.findClosestOffice(p1, offices).getLat());
	}
	
}
