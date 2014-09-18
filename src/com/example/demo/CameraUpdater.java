package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import math.Vec3;

public class CameraUpdater {
	List<CameraUpdateListener>	mListeners;
	
	public CameraUpdater() {
		mListeners = new ArrayList<CameraUpdateListener>();
	}
	
	public void addListener( CameraUpdateListener listener ) {
		mListeners.add(listener);
	}
	
	public void notifyListeners(LLA pos, Vec3 rot) {
		for (CameraUpdateListener listener : mListeners) {
			listener.onCameraChanged(pos, rot);
		}
	}

}
