package com.example.demo;

import java.util.Observable;

import math.Vec3;

public class Camera extends Observable {

	private LLA mPos;
	private Vec3 mRot;
	
	public Camera() {
		mPos = new LLA();
		mRot = new Vec3();
	}
	
	public void set( LLA pos, Vec3 rot ) {
		mPos.set(pos);
		mRot.set(rot); 
		notifySubscribers();
	}
	
	public void setPos( LLA pos ) { 
		mPos.set(pos);
		notifySubscribers();
	}
	
	public void setRot( Vec3 rot ) { 
		mRot.set(rot); 
		notifySubscribers();
	}

	private void notifySubscribers() {
		setChanged();
		notifyObservers();
	}
	
	
	public LLA getPos() { return mPos; }
	public Vec3 getRot() { return mRot; }
	
	
	public double getLatitude()  { return mPos.latitude; }
	public double getLongitude() { return mPos.longitude; }
	public double getAltitude()  { return mPos.altitude; }
	public double getYaw()   { return mRot.z; }
	public double getPitch() { return mRot.y; }
	public double getRoll()  { return mRot.x; }
	
	
	public void setLatitude( double latitude )  {
		mPos.latitude = latitude;
		notifySubscribers();
	}
	public void setLongitude( double longitude ) { 
		mPos.longitude = longitude;
		notifySubscribers();
	}
	public void setAltitude( double altitude ) { 
		mPos.altitude = altitude;
		notifySubscribers();
	}
	public void setYaw( double yaw ) { 
		mRot.z = yaw;
		notifySubscribers();
	}
	public void setPitch( double pitch ) { 
		mRot.y = pitch;
		notifySubscribers();
	}
	public void setRoll( double roll ) { 
		mRot.x = roll;
		notifySubscribers();
	}
	
	
	public void incLatitude( double diff )  {
		mPos.latitude += diff;
		notifySubscribers();
	}
	public void incLongitude( double diff ) { 
		mPos.longitude += diff;
		notifySubscribers();
	}
	public void incAltitude( double diff ) { 
		mPos.altitude += diff;
		notifySubscribers();
	}
	public void incYaw( double diff ) { 
		mRot.z += diff;
		notifySubscribers();
	}
	public void incPitch( double diff ) { 
		mRot.y += diff;
		notifySubscribers();
	}
	public void incRoll( double diff ) { 
		mRot.x += diff;
		notifySubscribers();
	}
}






















