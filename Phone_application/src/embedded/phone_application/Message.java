package embedded.phone_application;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.app.Application;
import android.location.Location;

public class Message extends Application{

	static String ip;
	static int port = 8888;
	static InetAddress local;
	static DatagramSocket outSocket;
	byte[] message = new byte[1500];
	
	public static void sendMessage(String messageStr){
		byte[] message = messageStr.getBytes();
		int msg_length=messageStr.length();
		DatagramPacket packet = new DatagramPacket(message, msg_length,local,port);
		try{
			outSocket.send(packet);
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	public static void sendLocation(Location myLoc){
		double lat = myLoc.getLatitude();
		double lon = myLoc.getLongitude();
		String message = lat + "," + lon;
		sendMessage(message);
	}
}
