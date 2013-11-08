package embedded.phone_application;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class DriveLocation implements Runnable {// extends AsyncTask<Void, String, Void>{
	
	GeomagneticField geoField;
	Context c;
	Queue<Location> waypoints;
	LocationManager locationManager;
	volatile Boolean running = true;
	Location dest;
	Location curLoc;
	Heading h;
	
	public DriveLocation(Context c, LocationManager locationManager){
		this.c = c;
		waypoints = new ConcurrentLinkedQueue<Location>();
		this.locationManager = locationManager;
	}
	
	public void driveTo(Location loc){
		waypoints.add(loc);
	}
	
	private double getBearing(Location loc) {
	    Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	    if (locationGps == null) return locationNet.bearingTo(loc);
	    else return locationGps.bearingTo(loc);
	}
	
	public Location getCurrentLocation(){
		
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			//System.out.println("network");
		}
		return location;
	}
	
	public void run(){
		while(running && !areWeThereYet(getCurrentLocation(), dest)){
			geoField = new GeomagneticField((float)getCurrentLocation().getLatitude(), (float)getCurrentLocation().getLongitude(),
					(float)getCurrentLocation().getAltitude(), System.currentTimeMillis());
			sendCorrection();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendCorrection(){
		double bearing = getBearing(dest);
		double heading = h.heading + geoField.getDeclination();
		heading = heading > 180 ? -(360 - heading) : heading;
		double correction = (bearing - heading) * -1;
		correction = correction > 180 ? -(360 - correction) : heading;
		correction = correction < -180 ? (360 + correction) : heading;
		System.out.println("heading " + heading);
		//System.out.println("correction: " + correction);
		Message.sendMessage(Double.toString(correction));
	}
	
	public void test(){
	//@Override
	//protected Void doInBackground(Void... params) {
		while (true){
			if (waypoints.size() > 0){
				Location destination = waypoints.remove();
				Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (location == null) location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				
				System.out.println("dest" + destination);
				System.out.println("curLoc" + location);
				
				while(!areWeThereYet(location, destination)){
					
					String text = "BEARING: " + Double.toString(0.0);
					System.out.println(text);
					Message.sendMessage(text);
					try {
						Thread.sleep(100);
					} catch (Exception e){
						System.out.println(e.toString());
					}
					location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (location == null) location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
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
	
	
