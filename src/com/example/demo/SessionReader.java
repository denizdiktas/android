package com.example.demo;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import math.Vec3;

public class SessionReader {
	public static final String TAG = "SessionReader";
	
	private FileInputStream  mFileInputStream = null;
	private DataInputStream  mDataInputStream = null;
	
	
	public void open( String fileName )  {
		try {
			mFileInputStream = new FileInputStream( fileName );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		mDataInputStream = new DataInputStream( mFileInputStream );
	}
	
	public void read( LLA camPos, Vec3 camRot ) throws IOException {
		if ( mDataInputStream == null )
			return;
		
		camPos.latitude = mDataInputStream.readDouble();
		camPos.longitude = mDataInputStream.readDouble();
		camPos.altitude = mDataInputStream.readDouble();
		camRot.x = mDataInputStream.readDouble();
		camRot.y = mDataInputStream.readDouble();
		camRot.z = mDataInputStream.readDouble();
	}
	
	public void close() {
		try {
			mDataInputStream.close();
			mFileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
