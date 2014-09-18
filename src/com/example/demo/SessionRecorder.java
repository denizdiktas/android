package com.example.demo;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import math.Vec3;

import android.util.Log;

public class SessionRecorder {
	public static final String TAG = "SessionRecorder";
	
	private FileOutputStream  mFileOutputStream = null;
	private DataOutputStream  mDataOutputStream = null;
	
	
	public void open( String fileName )  {
		File file = new File( fileName );
		try {
			file.createNewFile();
		} catch (IOException e) {
			Log.d(TAG, "could not create the session file");
			e.printStackTrace();
			file = null;
			return;
		}
		
		if( file.exists() )
		{
			try {
				mFileOutputStream = new FileOutputStream( file );
			} catch (FileNotFoundException e) {
				Log.d(TAG, "failed: file output stream");
				e.printStackTrace();
			}
			mDataOutputStream = new DataOutputStream( mFileOutputStream );
		}
	}
	
	public void write( LLA camPos, Vec3 camRot ) {
		if ( mDataOutputStream == null )
			return;
		
		try {
			mDataOutputStream.writeDouble( camPos.latitude );
			mDataOutputStream.writeDouble( camPos.longitude );
			mDataOutputStream.writeDouble( camPos.altitude );
			mDataOutputStream.writeDouble( camRot.x );
			mDataOutputStream.writeDouble( camRot.y );
			mDataOutputStream.writeDouble( camRot.z );
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void close() {
		try {
			mDataOutputStream.close();
			mFileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
