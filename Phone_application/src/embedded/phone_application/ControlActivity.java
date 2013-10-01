package embedded.phone_application;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.View;

public class ControlActivity extends Activity {

	String ip;
	String port;
	InetAddress local;
	DatagramSocket outSocket;
	byte[] message = new byte[1500];
	Listener listener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		Bundle extras = getIntent().getExtras();
		String[] connectionInfo = extras.getStringArray("connection");
		//ip = connectionInfo[0];
		port = connectionInfo[1];
		
		System.out.println(port);
		listener = new Listener(this, Integer.parseInt(port));
		listener.setupUdp();
	}
	
	public void startListening(View view){
		listener.execute();
	}
	
	public void showMessage(String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Message");
		alertDialog.setMessage(message);
		alertDialog.show();
	}
	
	public void stopListening(View view){
		listener.cancel(true);
	}
	
	public void sendMessage(String messageStr){
		byte[] message = messageStr.getBytes();
		int msg_length=messageStr.length();
		DatagramPacket packet = new DatagramPacket(message, msg_length,local,Integer.parseInt(port));
		try{
			outSocket.send(packet);
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control, menu);
		return true;
	}
}
