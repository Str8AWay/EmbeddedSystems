package embedded.tablet_application;

import java.net.DatagramSocket;
import java.net.InetAddress;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class ControlActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		Bundle extras = getIntent().getExtras();
		String[] connectionInfo = extras.getStringArray("connection");
		Message.ip = connectionInfo[0];
		
		setupUDP();
	}
	
	public void setupUDP(){
		try{
			Message.outSocket = new DatagramSocket();
			Message.local = InetAddress.getByName(Message.ip);
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	public void stopRobot(View view){
		Message.sendMessage("STOP");
	}
	public void leftRobot(View view){
		Message.sendMessage("LEFT");
	}
	public void rightRobot(View view){
		Message.sendMessage("RIGHT");
	}
	public void forwardsRobot(View view){
		Message.sendMessage("FORWARDS");
	}
	public void backwardsRobot(View view){
		Message.sendMessage("BACKWARDS");
	}
	public void homeRobot(View view){
		LocationManager location = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Location myLoc = location.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location myLocNet = location.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (myLoc != null) Message.sendMessage(myLoc.toString());
		else Message.sendMessage(myLocNet.toString());
	}
	public void waypointRobot(View view){
		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control, menu);
		return true;
	}
}
