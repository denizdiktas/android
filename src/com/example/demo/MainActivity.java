package com.example.demo;

import java.io.IOException;

import math.Vec3;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends Activity {
	public static final String TAG = "MainActivity";

	GoogleMap mGoogleMap;
	
	long mReplaySleepTime;
	int mThreadSleepTime;
	int mNumIters;
	
	String			 mSessionFileName;
	SessionReader    mSessionReader;
	SessionRecorder  mSessionRecorder;
	
	Renderer	mRenderer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// immediately switch to the debug session
		Intent intent = new Intent(this, DebugSessionActivity.class);
		startActivity(intent);
		
		mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		// mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		// mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
		mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		// mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); 
		
		mNumIters = 50;
		mThreadSleepTime = 200;
		mReplaySleepTime = 20;
	

		double initialCamHeight = 1000;
		LatLng initialCamPos = new LatLng(41.07763, 29.02812);
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialCamPos, 12));
		
		mCamPos.set(initialCamPos, initialCamHeight);
		mCamRot.set(0, Math.toRadians(-90), 0);
		startRenderingLoop( 20 );
		
		mSessionFileName = Environment.getExternalStorageDirectory() + "/session.dat";
		//initRecordingSession();
		initPlaybackSession();
		//connectToControlPanel(); // uncomment this only if you want to connect to the control server
	}

	private void startRenderingLoop( long redrawSleepTime ) {
		mRenderer = new Renderer( mGoogleMap );
		mRenderer.startRenderingLoop(mCamPos, mCamRot, redrawSleepTime);
	}
	
	
	private void initPlaybackSession() {
		mSessionReader = new SessionReader();
		mSessionReader.open( mSessionFileName );
		
		TimedThreadLoop loop = new TimedThreadLoop();
		loop.set( mReplaySleepTime, loop.new LoopBody() {
			public void run() {
				try {
					mSessionReader.read( mCamPos, mCamRot );
				} catch (IOException e1) {
					e1.printStackTrace();
					mSessionReader.close();
					exitLoop();
				}
			}
		} );
		loop.start();
	}
	
	private void initRecordingSession() {
		mSessionRecorder = new SessionRecorder();
		mSessionRecorder.open( mSessionFileName );
		
		Thread update = new Thread() {
			public void run() {
				while(true) {					
					mSessionRecorder.write(mCamPos, mCamRot);
					try {
						Thread.sleep(mThreadSleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		update.start();
	}
	
	LLA mCamPos = new LLA();
	Vec3 mCamRot = new Vec3();
	


	@Override
	protected void onDestroy() {
		//mControlPanelClient.stop();
		super.onDestroy();
	}
	
	
	private ControlPanelClient		mControlPanelClient;
	private void connectToControlPanel() {
		mControlPanelClient = new ControlPanelClient();
		CameraUpdateListener listener = new CameraUpdateListener() {
			public void onCameraChanged(LLA pos, Vec3 rot) {
				mCamPos = pos;
				mCamRot.set(rot);
			}
		};
		mControlPanelClient.addListener( listener );
		mControlPanelClient.start();
	}
}








