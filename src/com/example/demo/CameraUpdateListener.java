package com.example.demo;

import math.Vec3;


public interface CameraUpdateListener {
	
	public void onCameraChanged( LLA pos, Vec3 rot );
	
}
