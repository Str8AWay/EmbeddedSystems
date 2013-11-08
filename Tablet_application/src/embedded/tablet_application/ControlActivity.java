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
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ControlActivity extends Activity {
	
	ToggleButton toggle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		Bundle extras = getIntent().getExtras();
		String[] connectionInfo = extras.getStringArray("connection");
		Message.ip = connectionInfo[0];
		
		toggle = (ToggleButton) findViewById(R.id.tiltbutton);
		
		setupUDP();
		setupSensor();
		setupTilt();
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

    final float[] mValuesMagnet      = new float[3];
    final float[] mValuesAccel       = new float[3];
    final float[] mValuesOrientation = new float[3];
    final float[] mRotationMatrix    = new float[9];
	
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

	volatile boolean tiltControl = false;
	
	public void setupUDP(){
		try{
			Message.outSocket = new DatagramSocket();
			Message.local = InetAddress.getByName(Message.ip);
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	volatile int prevDir = 0;
	
	public void tiltRobot(){
			SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
	        SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);
	        final CharSequence test;
	        test = "results: " + mValuesOrientation[0] +" "+mValuesOrientation[1]+ " "+ mValuesOrientation[2];
	        //System.out.println(test);
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
		LocationManager location = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Location myLoc = location.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (myLoc != null) myLoc = location.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (myLoc != null) Message.sendLocation(myLoc);
		else Message.sendMessage("tablet location unknown");
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
