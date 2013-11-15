package embedded.tablet_application;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MyLocation implements LocationListener {

	public Location myLoc;
	
	LocationManager locationManager;
	
	public MyLocation(LocationManager locationManager){
		this.locationManager = locationManager;
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
		myLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}
	
	public Location getLocation(){
		return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		myLoc = location;
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

}
