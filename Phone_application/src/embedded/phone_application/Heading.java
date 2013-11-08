package embedded.phone_application;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Heading implements Runnable, SensorEventListener{

	private SensorManager mSensorManager;
	volatile Boolean running = true;
	Context c;
	volatile double heading = 0;
	
	public Heading(Context c){
		this.c = c;
		mSensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	@Override
	public void run() {
		while(running){
			//System.out.println(heading);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	float[] mGravity;
	float[] mGeomagnetic;
	@Override
	public void onSensorChanged(SensorEvent event) {
	    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
	        mGravity = event.values;
	    if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
	        mGeomagnetic = event.values;
	    if (mGravity != null && mGeomagnetic != null) {
	        float R[] = new float[9];
	        float I[] = new float[9];
	        boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
	                mGeomagnetic);
	        if (success) {
	            float orientation[] = new float[3];
	            SensorManager.getOrientation(R, orientation);
	            double azimut = Math.toDegrees(orientation[0]);
	            this.heading = azimut;
	            //System.out.println(this.heading);
	        }
	    }
	}

}
