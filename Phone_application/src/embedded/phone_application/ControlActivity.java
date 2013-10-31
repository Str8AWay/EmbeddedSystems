package embedded.phone_application;

import java.net.DatagramSocket;
import java.net.InetAddress;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;

public class ControlActivity extends Activity {
	
	String ip;
	byte[] message = new byte[1500];
	Listener listener;
	Boolean listening = false;
	LocationManager locationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		Bundle extras = getIntent().getExtras();
		String[] connectionInfo = extras.getStringArray("connection");
		ip = connectionInfo[0];
		
		//DriveLocation drive = new DriveLocation(this);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		TheLocationListener mlocListener = new TheLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
		            0, mlocListener);
		listener = new Listener(this, ip, new DriveLocation(this, locationManager));
		
		setupUdp();	
	}
	
	public void setupUdp(){
		listener.setupUdp();
		try{
			Message.outSocket = new DatagramSocket();
			Message.ip = ip;
			Message.local = InetAddress.getByName(Message.ip);
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	public void startListening(View view){
		if (!listening) {
			listening = true;
			listener.execute();
		}
	}
	
	public void stopListening(View view){
		try {
			listener.cancel(true);
			listening = false;
		}
		catch (Exception e) {
			System.out.println("stopped failed");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control, menu);
		return true;
	}
}
