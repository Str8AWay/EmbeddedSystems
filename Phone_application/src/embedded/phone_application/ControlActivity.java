package embedded.phone_application;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

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
	DatagramSocket inSocket;
	DatagramPacket inPacket;
	byte[] message = new byte[1500];
	
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
		sleep(500);
		listenForMessage();
	}
	
	public void sleep(int milliseconds){
		try {
			Thread.sleep(milliseconds);
		}
		catch (Exception e){
			System.out.println("Sleep Failed");
		}
	}
	
	public void listenForMessage(){
			try{
				inSocket.receive(inPacket);
				String text = new String(message, 0, inPacket.getLength());
				inSocket.close();
				if (text.length() > 0){
					showMessage(text);
				}
				Arrays.fill(message, (Byte) null);
			}
			catch (Exception e){
				System.out.println("socket error");
				System.out.println(e.toString());
				sleep(250);
			}
	}
	
	public void showMessage(String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Message");
		alertDialog.setMessage(message);
		alertDialog.show();
	}
	
	public void setupUDP(){
		try{
			outSocket = new DatagramSocket();
			local = InetAddress.getByName(ip);
			inPacket = new DatagramPacket(message, message.length);
			inSocket = new DatagramSocket(Integer.parseInt(port));
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	public void stopRobot(View view){
		sendMessage("STOP");
	}
	public void leftRobot(View view){
		sendMessage("LEFT");
	}
	public void rightRobot(View view){
		sendMessage("RIGHT");
	}
	public void forwardRobot(View view){
		sendMessage("BACKWARDS");
	}
	public void backwardsRobot(View view){
		sendMessage("FORWARDS");
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
