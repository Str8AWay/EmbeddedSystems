package embedded.phone_application;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class ControlActivity extends Activity {
	
	String ip;
	byte[] message = new byte[1500];
	Listener listener;
	Boolean listening = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		Bundle extras = getIntent().getExtras();
		String[] connectionInfo = extras.getStringArray("connection");
		ip = connectionInfo[0];
		
		listener = new Listener(this, ip);
		listener.setupUdp();
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
