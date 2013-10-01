package embedded.phone_application;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


public class Listener extends AsyncTask<Void, String, Void>{
	
	Context c;
	int port;
	DatagramSocket inSocket;
	DatagramPacket inPacket;
	byte[] message = new byte[1500];
	
	public Listener(Context c, int portNum){
		this.c = c;
		port = portNum;
	}
	
	public void setupUdp(){
		try{
			inSocket = new DatagramSocket(port);
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
				System.out.println(text);
				publishProgress(text);
			}
			catch (Exception e){
				System.out.println(e.getMessage());
			}
		}
		return null;
	}	
	
	public void onProgressUpdate(String... params) {
		Toast.makeText(c, params[0], Toast.LENGTH_SHORT).show();
	}
}
