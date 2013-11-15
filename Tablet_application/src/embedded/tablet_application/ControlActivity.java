package embedded.tablet_application;

import java.net.DatagramSocket;
import java.net.InetAddress;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ControlActivity extends Activity {

    final float[] mValuesMagnet      = new float[3];
    final float[] mValuesAccel       = new float[3];
    final float[] mValuesOrientation = new float[3];
    final float[] mRotationMatrix    = new float[9];
    
    volatile int prevDir = 0;
    volatile boolean tiltControl = false;
    
	ToggleButton toggle;
	MyLocation myLoc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		//Bundle extras = getIntent().getExtras();
		//String[] connectionInfo = extras.getStringArray("connection");		
		Message.ip = "192.168.2.11";
		
		toggle = (ToggleButton) findViewById(R.id.tiltbutton);
		
		setupUDP();
		setupSensor();
		setupTilt();
		setupLocation();
	}
	
	public void setupLocation(){
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		myLoc = new MyLocation(locationManager);
	}
	
	public void setupTilt(){
		Thread tilt = new Thread(){
			@Override
			public void run(){
				while (tiltControl){
					tiltRobot();
				}
			}
		};
		tilt.start();
	}
	
	public void setupSensor(){
		SensorManager sensor = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		SensorEventListener listener = new SensorEventListener() {
			@Override
			public void onSensorChanged(SensorEvent event) {
				switch (event.sensor.getType()) {
	            case Sensor.TYPE_ACCELEROMETER:
	                System.arraycopy(event.values, 0, mValuesAccel, 0, 3);
	                break;
	
	            case Sensor.TYPE_MAGNETIC_FIELD:
	                System.arraycopy(event.values, 0, mValuesMagnet, 0, 3);
	                break;
				}	
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		};
		registerListeners(sensor, listener);
	}
	
	private void registerListeners(SensorManager sensor, SensorEventListener listener) {
		sensor.registerListener(listener, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
                SensorManager.SENSOR_DELAY_NORMAL);
        sensor.registerListener(listener, sensor.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 
                SensorManager.SENSOR_DELAY_NORMAL);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				tiltControl = isChecked;
				if (tiltControl) setupTilt();
			}
		});
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
	
	
	
	public void tiltRobot(){
			SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
	        SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);
	        if (mValuesOrientation[2] > -1) {
	        	if (prevDir != 1){
	        		prevDir = 1;
	        		System.out.println("FORWARDS");
	        		Message.sendMessage("FORWARDS");
	        	}
	        }
	        else if (mValuesOrientation[2] < -2) {
	        	if (prevDir != 2){
		        	prevDir = 2; 
		        	System.out.println("BACKWARDS");
		        	Message.sendMessage("BACKWARDS");
	        	}
	        }
	        else if (mValuesOrientation[1] > .5) {
	        	if (prevDir != 3){
		        	prevDir = 3; 
		        	System.out.println("LEFT");
		        	Message.sendMessage("LEFT");
	        	}
	        }
	        else if (mValuesOrientation[1] < -.5) {
	        	if (prevDir != 4){
		        	prevDir= 4; 
		        	System.out.println("RIGHT");
		        	Message.sendMessage("RIGHT");
	        	}
	        }
	        else{ 
	        	if (prevDir != 0){
		        	prevDir = 0;
		        	System.out.println("STOP");
		        	Message.sendMessage("STOP");
	        	}
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
		Location location = myLoc.getLocation();
		Toast toast;
		if (location != null) toast = Toast.makeText(this, location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT); //Message.sendLocation(location);
		else toast = Toast.makeText(this, "location not found", Toast.LENGTH_SHORT); //Message.sendMessage("tablet location unknown");
		toast.show();
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
