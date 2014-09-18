package com.example.demo;

import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import math.Vec3;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

public class DebugSessionActivity extends Activity {
	public static final String TAG = "DebugSessionActivity";
	
	private GoogleMap	mGoogleMap;
	private Renderer	mRenderer;

	
	Camera mCamera = new Camera();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug_session_activity);
		
		mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	
		double initialCamHeight = 1000;
		LatLng initialCamPos = new LatLng(41.07763, 29.02812);
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialCamPos, 12));

		mCamera.setPos( new LLA(initialCamPos, initialCamHeight) );
		mCamera.setRot( new Vec3(0, Math.toRadians(-90), 0) );
		startRenderingLoop( 20 );
		
		initGUI();
	}
	
	private void startRenderingLoop( long redrawSleepTime ) {
		mRenderer = new Renderer( mGoogleMap );
		mRenderer.startRenderingLoop(mCamera.getPos(), mCamera.getRot(), redrawSleepTime);
		
	}
	
	Button	mButtonLatDec, mButtonLatInc;
	Button	mButtonLonDec, mButtonLonInc;
	Button	mButtonAltDec, mButtonAltInc;
	private double mLatDiff = 0.0001;
	private double mLongDiff = 0.0001;
	private double mAltDiff = 10;
	private double mYawDiff = Math.toRadians(1);
	private double mPitchDiff = Math.toRadians(1);
	private double mRollDiff = Math.toRadians(1);
	
	
	EditText mEditTextLat, mEditTextLon, mEditTextAlt;
	EditText mEditTextYaw, mEditTextPitch, mEditTextRoll;
	
	private void updateEditTextFields() {
		LLA pos = mCamera.getPos();
		Vec3 rot = mCamera.getRot();
		DecimalFormat df = new DecimalFormat("#.####");		
		mEditTextLat.setText( df.format(pos.latitude) );
		mEditTextLon.setText( df.format(pos.longitude) );
		mEditTextAlt.setText( df.format(pos.altitude) );
		mEditTextYaw.setText( df.format(Math.toDegrees(rot.z)) );
		mEditTextPitch.setText( df.format(Math.toDegrees(rot.y)) );
		mEditTextRoll.setText( df.format(Math.toDegrees(rot.x)) );
	}
	
	
	private boolean mButtonLatDecDown = false;
	private boolean mButtonLatIncDown = false;
	private boolean mButtonLongDecDown = false;
	private boolean mButtonLongIncDown = false;
	private boolean mButtonAltDecDown = false;
	private boolean mButtonAltIncDown = false;
	//
	private boolean mButtonYawDecDown = false;
	private boolean mButtonYawIncDown = false;
	private boolean mButtonPitchDecDown = false;
	private boolean mButtonPitchIncDown = false;
	private boolean mButtonRollDecDown = false;
	private boolean mButtonRollIncDown = false;
	class ButtonTouchListener implements View.OnTouchListener {

		public boolean onTouch(View v, MotionEvent event) {
			boolean buttonDown = (event.getAction() == MotionEvent.ACTION_DOWN);
			switch( v.getId() ) {
			case R.id.button_lat_dec:	mButtonLatDecDown = buttonDown; break;
			case R.id.button_lat_inc:	mButtonLatIncDown = buttonDown; break;
			case R.id.button_long_dec:	mButtonLongDecDown = buttonDown; break;
			case R.id.button_long_inc:	mButtonLongIncDown = buttonDown; break;
			case R.id.button_alt_dec:	mButtonAltDecDown = buttonDown; break;
			case R.id.button_alt_inc:	mButtonAltIncDown = buttonDown; break;
			//
			case R.id.button_yaw_dec:	mButtonYawDecDown = buttonDown; break;
			case R.id.button_yaw_inc:	mButtonYawIncDown = buttonDown; break;
			case R.id.button_pitch_dec:	mButtonPitchDecDown = buttonDown; break;
			case R.id.button_pitch_inc:	mButtonPitchIncDown = buttonDown; break;
			case R.id.button_roll_dec:	mButtonRollDecDown = buttonDown; break;
			case R.id.button_roll_inc:	mButtonRollIncDown = buttonDown; break;
			default: break;
			}
			return false;
		}
	}
	
	
	private void initGUI() {
		getWindow().setSoftInputMode(EditorInfo.IME_ACTION_DONE);
		
		mEditTextLat = (EditText) findViewById(R.id.edit_lat);  
		mEditTextLon = (EditText) findViewById(R.id.edit_long);
		mEditTextAlt = (EditText) findViewById(R.id.edit_alt);
		mEditTextYaw = (EditText) findViewById(R.id.edit_yaw);  
		mEditTextPitch = (EditText) findViewById(R.id.edit_pitch);
		mEditTextRoll = (EditText) findViewById(R.id.edit_roll);
		
		ButtonTouchListener touchListener = new ButtonTouchListener(); 
		findViewById(R.id.button_lat_dec).setOnTouchListener( touchListener );
		findViewById(R.id.button_lat_inc).setOnTouchListener( touchListener );
		findViewById(R.id.button_long_dec).setOnTouchListener( touchListener );
		findViewById(R.id.button_long_inc).setOnTouchListener( touchListener );
		findViewById(R.id.button_alt_dec).setOnTouchListener( touchListener );
		findViewById(R.id.button_alt_inc).setOnTouchListener( touchListener );
		//
		findViewById(R.id.button_yaw_dec).setOnTouchListener( touchListener );
		findViewById(R.id.button_yaw_inc).setOnTouchListener( touchListener );
		findViewById(R.id.button_pitch_dec).setOnTouchListener( touchListener );
		findViewById(R.id.button_pitch_inc).setOnTouchListener( touchListener );
		findViewById(R.id.button_roll_dec).setOnTouchListener( touchListener );
		findViewById(R.id.button_roll_inc).setOnTouchListener( touchListener );
	
		final Handler handler = new Handler(Looper.getMainLooper());
		
		mCamera.addObserver( new Observer() {
			public void update(Observable observable, Object data) {
				handler.post( new Runnable() {
					public void run() {
						updateEditTextFields(); 
					}
				} );
			}
		} );
	
		new Thread( new Runnable() {
			public void run() {
				while(true) {
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					if (mButtonLatDecDown)	{ mCamera.incLatitude( -mLatDiff ); }
					if (mButtonLatIncDown)	{ mCamera.incLatitude(  mLatDiff ); }
					if (mButtonLongDecDown)	{ mCamera.incLongitude( -mLongDiff ); }
					if (mButtonLongIncDown)	{ mCamera.incLongitude(  mLongDiff );  }
					if (mButtonAltDecDown)	{ mCamera.incAltitude( -mAltDiff );  }
					if (mButtonAltIncDown)	{ mCamera.incAltitude(  mAltDiff ); }
					
					if (mButtonYawDecDown)	{ mCamera.incYaw( -mYawDiff ); }
					if (mButtonYawIncDown)	{ mCamera.incYaw(  mYawDiff ); }
					if (mButtonPitchDecDown){ mCamera.incPitch( -mPitchDiff ); }
					if (mButtonPitchIncDown){ mCamera.incPitch(  mPitchDiff );  }
					if (mButtonRollDecDown)	{ mCamera.incRoll( -mRollDiff ); }
					if (mButtonRollIncDown)	{ mCamera.incRoll(  mRollDiff );  }
				}
			}
		} ).start();
	}

}
