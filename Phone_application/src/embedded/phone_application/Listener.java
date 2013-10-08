package embedded.phone_application;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


public class Listener extends AsyncTask<Void, String, Void>{
	
	Context c;
	String ip;
	DatagramSocket inSocket;
	DatagramPacket inPacket;
	InetAddress local;
	DatagramSocket outSocket;
	byte[] message = new byte[1500];
	
	public Listener(Context c, String ip){
		this.c = c;
		this.ip = ip;
	}
	
	public void setupUdp(){
		try{
			inSocket = new DatagramSocket(8000);
			outSocket = new DatagramSocket(8888);
			local = InetAddress.getByName(ip);
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		while(!inSocket.isClosed()){
			try{
				inPacket = new DatagramPacket(message, message.length);
				inSocket.receive(inPacket);
				String text = new String(message, 0, inPacket.getLength());
				publishProgress(text);
				sendMessage(text);
			}
			catch (Exception e){
				System.out.println(e.getMessage());
			}
		}
		return null;
	}	
	
	public void onProgressUpdate(String... params) {
		System.out.println(params[0]);
		Toast.makeText(c, params[0], Toast.LENGTH_SHORT).show();
	}

	public void sendMessage(String messageStr){
		System.out.println("sending");
		byte[] message = messageStr.getBytes();
		int msg_length=messageStr.length();
		DatagramPacket packet = new DatagramPacket(message, msg_length, local, 8888);
		System.out.println(message);
		System.out.println(msg_length);
		System.out.println(local);
		System.out.println(8888);
		
		try{
			outSocket.send(packet);
			System.out.println("sent");
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
}
