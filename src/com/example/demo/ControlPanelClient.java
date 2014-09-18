package com.example.demo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import math.Vec3;

import android.os.AsyncTask;
import android.util.Log;

public class ControlPanelClient extends CameraUpdater {

	public static final String TAG = "ControlPanelClient";
	
	private boolean mContinue = true;
	private Socket mConnection = null;
	private ObjectInputStream mInput = null;
	
	public void stop() {
		cleanUp();
	}
	
	public void start() {
		new AsyncTask<Void, Void, Void>() { 
			protected Void doInBackground(Void... params) {
				final int port = 6789;
				final String serverIP = "10.30.22.65";
				Log.d( TAG, "Attempting connection..." );
				try {
					mConnection = new Socket(InetAddress.getByName(serverIP), port);
					Log.d( TAG, "Connected to: " + mConnection.getInetAddress().getHostName() );
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
					mInput = new ObjectInputStream( mConnection.getInputStream() );
				} catch (Exception e) {
					e.printStackTrace();
				}

				// listen to the server
				final String tag = "ControlPanel";
				mContinue = true;
				LLA camLLA = new LLA();
				Vec3 camRot = new Vec3();
				do {
					try {
						String message = (String) mInput.readObject();
						Log.d(tag, message);
						
						StringTokenizer st = new StringTokenizer(message, ",");
						camLLA.latitude  = Double.parseDouble( (String) st.nextElement() );
						camLLA.longitude = Double.parseDouble( (String) st.nextElement() );
						camLLA.altitude  = Double.parseDouble( (String) st.nextElement() );
						camRot.x = Math.toRadians( Double.parseDouble((String) st.nextElement()) );
						camRot.y = Math.toRadians( Double.parseDouble((String) st.nextElement()) );
						camRot.z = Math.toRadians( Double.parseDouble((String) st.nextElement()) );
						Log.d(tag, "cam-pos = " + camLLA);
						Log.d(tag, "cam-rot = " + camRot);
						
						notifyListeners(camLLA, camRot);
					} catch (Exception e) {
						e.printStackTrace();
						cleanUp();
						return null;
					}
					
				} while( mContinue );
				
				cleanUp();
				
				return null;
			}
		}.execute();
	}
	
	private void cleanUp() {
		try {
			mInput.close();
			mConnection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
