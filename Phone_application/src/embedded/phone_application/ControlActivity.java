package embedded.phone_application;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class ControlActivity extends Activity {

	String ip;
	String port;
	InetAddress local;
	DatagramSocket socket;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		Bundle extras = getIntent().getExtras();
		String[] connectionInfo = extras.getStringArray("connection");
		ip = connectionInfo[0];
		port = connectionInfo[1];
		
		System.out.println(ip);
		System.out.println(port);
		
		setupUDP();
	}
	
	public void setupUDP(){
		try{
			socket = new DatagramSocket();
			local = InetAddress.getByName(ip);
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	public void stopRobot(View view){
		String messageStr = "STOP";
		byte[] message = messageStr.getBytes();
		int msg_length=messageStr.length();
		DatagramPacket packet = new DatagramPacket(message, msg_length,local,Integer.parseInt(port));
		try{
			socket.send(packet);
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
