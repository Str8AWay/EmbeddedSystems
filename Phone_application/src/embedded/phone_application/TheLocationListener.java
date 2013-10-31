package embedded.phone_application;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class TheLocationListener implements LocationListener{

	public Location loc = null;
	
	@Override
	public void onLocationChanged(Location location) {
		loc = location;
		System.out.println("miracles have occurred");
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
