package embedded.tablet_application;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.app.Application;

public class Message extends Application{

	static String ip;
	static int port = 8000;
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
}
