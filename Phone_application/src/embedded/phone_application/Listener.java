package embedded.phone_application;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.Toast;

public class Listener extends AsyncTask<Void, String, Void>{
	
	private DriveLocation drive;
	Context c;
	String ip;
	DatagramSocket inSocket;
	DatagramPacket inPacket;
	InetAddress local;
	byte[] message = new byte[1500];
	
	public Listener(Context c, String ip, DriveLocation drive){
		this.c = c;
		this.ip = ip;
		this.drive = drive;
	}
	
	public void setupUdp(){
		try{
			inSocket = new DatagramSocket(8000);
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
				System.out.println(text);
				Message.sendMessage(text);
				if (Character.isDigit(text.charAt(0))) driveToLocation(text);
			}
			catch (Exception e){
				System.out.println(e.getMessage());
			}
		}
		return null;
	}	
	
	private void driveToLocation(String text) {
		Location destination = getLocationFromPacket(text);
		System.out.println(destination);
		drive.driveTo(destination);
		drive.test();
	}

	private Location getLocationFromPacket(String text) {
		Location dest = new Location("dest");
		String[] coords = text.split(",");
		dest.setLatitude(Double.parseDouble(coords[0]));
		dest.setLongitude(Double.parseDouble(coords[1]));
		return dest;
	}

	public void onProgressUpdate(String... params) {
		String message = params[0];
		Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
	}
}
