package com.example.demo;

import com.google.android.gms.maps.model.LatLng;

public class LLA {
	public double latitude;
	public double longitude;
	public double altitude;

	public LLA() {}
	public LLA( LatLng gps, double height ) { set(gps, height); }
	
	public void set(LatLng gps, double height) {
		this.latitude = gps.latitude;
		this.longitude = gps.longitude;
		this.altitude = height;
	}
	
	public LatLng getLatLng()
	{
		return new LatLng(this.latitude, this.longitude);
	}
	
	public double getPhi()    { return Math.toRadians(latitude); }
	public double getLambda() { return Math.toRadians(longitude); }
	
	@Override
	public String toString() {
		return latitude + ", " + longitude + ", " + altitude;
	}
	public void set(LLA lla) {
		this.latitude = lla.latitude;
		this.longitude = lla.longitude;
		this.altitude = lla.altitude;
	}
}
