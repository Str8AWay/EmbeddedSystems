package embedded.phone_application;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
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
				if (text.startsWith("Location")) sendLocation(text);
				else sendMessage(text);
				
			}
			catch (Exception e){
				System.out.println(e.getMessage());
			}
		}
		return null;
	}	
	
	private void sendLocation(String text) {
		Location destination = getLocationFromPacket(text);
		System.out.println(destination);
		double bearing = getBearing(destination);
		System.out.println(bearing);
		String message = String.format("Lat: %d Long %d Bearing %d", destination.getLatitude(), destination.getLongitude(), bearing);
		Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
		sendMessage(message);
	}

	private Location getLocationFromPacket(String text) {
		Location dest = new Location("dest");
		String[] coords = text.split(",");
		dest.setLatitude(Double.parseDouble(coords[0]));
		dest.setLongitude(Double.parseDouble(coords[1]));
		return dest;
	}

	private double getBearing(Location loc) {
		LocationManager locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
		
	    Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	    if (locationGps == null) return locationNet.bearingTo(loc);
	    else return locationGps.bearingTo(loc);
	}

	public void onProgressUpdate(String... params) {
		String message = params[0];
		System.out.println(message);
		Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
	}

	public void sendMessage(String messageStr){
		System.out.println("sending");
		byte[] message = messageStr.getBytes();
		int msg_length=messageStr.length();
		DatagramPacket packet = new DatagramPacket(message, msg_length, local, 8888);
		
		try{
			outSocket.send(packet);
			System.out.println("sent");
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
}
