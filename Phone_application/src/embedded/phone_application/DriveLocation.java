package embedded.phone_application;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;

public class DriveLocation {// extends AsyncTask<Void, String, Void>{

	Context c;
	Queue<Location> waypoints;
	LocationManager locationManager;
	
	public DriveLocation(Context c){
		this.c = c;
		locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
		waypoints = new ConcurrentLinkedQueue<Location>();
	}
	
	public void driveTo(Location loc){
		waypoints.add(loc);
	}
	
	private int getBearing(Location loc) {
	    Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	    if (locationGps == null) return (int)locationNet.bearingTo(loc);
	    else return (int)locationGps.bearingTo(loc);
	}
	
	public void test(){
	//@Override
	//protected Void doInBackground(Void... params) {
		while (true){
			if (waypoints.size() > 0){ 
				Location destination = waypoints.remove();
				Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (location == null) location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				Boolean atLocation = areWeThereYet(location, destination);
				System.out.println("dest" + destination);
				System.out.println("curLoc" + location);
				System.out.println("there yet?" + atLocation);
				while(!atLocation){
					String text = "BEARING: " + Integer.toString(getBearing(destination));
					System.out.println(text);
					Message.sendMessage(text);
					try {
						Thread.sleep(100);
					} catch (Exception e){
						System.out.println(e.toString());
					}
				}
			}
		}
	}

	private Boolean areWeThereYet(Location location, Location destination) {
		if (!compareDouble(location.getLatitude(), destination.getLatitude(), 0.00000001)) return false;
		if (!compareDouble(location.getLongitude(), destination.getLongitude(), 0.00000001)) return false;
		return true;
	}
	
	private Boolean compareDouble(double a, double b, double precision){
		return Math.abs(a-b) < precision;
	}
}
	
	
