package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import math.Line3;
import math.Mat3;
import math.Ray3;
import math.Vec3;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class Renderer {
	static public final String TAG = "Renderer";
	
	private GoogleMap	mGoogleMap;
	private Handler		mHandler;
	private Polygon		mPolygon;
	private Marker[]	mMarkers;
	
	public LLA mCamPos;
	public Vec3 mCamRot;
	public long mRedrawSleepTime;
	public LatLngBounds	mLatLngBounds;
	
	public Renderer( GoogleMap map ) {
		mGoogleMap = map;
		mHandler = new Handler(Looper.getMainLooper());	
	
		initViewFrustumPolygon();
	}
	
	public void set( LLA camPos, Vec3 camRot ) {
		mCamPos = camPos;
		mCamRot = camRot;
	}
	
	public void startRenderingLoop( LLA camPos, Vec3 camRot, long renderSleepTime ) {
		mCamPos = camPos;
		mCamRot = camRot;
		mRedrawSleepTime = renderSleepTime;
		TimedThreadLoop loop = new TimedThreadLoop();
		loop.set(mRedrawSleepTime, loop.new LoopBody() {
			public void run() {
				render( mCamPos, mCamRot );
			}
		});
		loop.start();
	}
	
	private void initViewFrustumPolygon() {
		final PolygonOptions po = new PolygonOptions();
		po.add( new LatLng(0,0) ); // we need to add at least one point to avoid crash	
		mPolygon = mGoogleMap.addPolygon(po);
		mPolygon.setStrokeWidth(3);
		mPolygon.setFillColor(Color.BLUE);
	}
	
	private void initMarkers() {
		mMarkers = new Marker[4];
		LatLng initDummyPos = new LatLng(0, 0);
		MarkerOptions moRED = new MarkerOptions().position( initDummyPos ).draggable(false)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		MarkerOptions moGREEN = new MarkerOptions().position( initDummyPos ).draggable(false)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		MarkerOptions moBLUE = new MarkerOptions().position( initDummyPos ).draggable(false)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		MarkerOptions moYELLOW = new MarkerOptions().position( initDummyPos ).draggable(false)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
		mMarkers[0] = mGoogleMap.addMarker( moRED );
		mMarkers[1] = mGoogleMap.addMarker( moGREEN );
		mMarkers[2] = mGoogleMap.addMarker( moBLUE );
		mMarkers[3] = mGoogleMap.addMarker( moYELLOW );
	}
	
	
	private double haversin( double theta )		{ return (1 - Math.cos(theta)) / 2.0; }
	private double inverseHaversin( double h )	{ return Math.acos(1 - 2*h); }
	private LatLng xyToLatLong( double px, double py, LLA camPosLLA ) {
		final double r = 6367444.6571;
		double hi, lat1, lat2, d, long1, long2;
	
		lat1 = Math.toRadians( camPosLLA.latitude );
		lat2 = lat1 + (px / r);
		d = Math.sqrt( px*px + py*py );
		hi = inverseHaversin( (haversin(d/r) - haversin(lat2 - lat1)) / (Math.cos(lat1) * Math.cos(lat2)) );
		long1 = Math.toRadians( camPosLLA.longitude );
		long2 = long1 + Math.abs(hi) * Math.signum(py);
		return new LatLng(Math.toDegrees(lat2), Math.toDegrees(long2));
		
	}
	
	public void render( LLA camPosLLA, Vec3 camRot ) {
		// R = rotation matrix for the line of sight
		Mat3 Rz = Mat3.rotZ( camRot.z );	// YAW
		Mat3 Ry = Mat3.rotY( -camRot.y );	// PITCH
		Mat3 Rx = Mat3.rotX( camRot.x );	// ROLL
		Mat3 R = Mat3.mul(Mat3.mul(Rz, Ry), Rx);

		final double FOV = Math.toRadians(50.0);
		final double halfFOV = FOV * 0.5;

		// generate points around the line-of-sight, forming a cone.
		Vec3 pi = new Vec3();
		final List<LatLng> newPoints = new ArrayList<LatLng>();
		
		double ti;
		double tn = Math.tan( halfFOV );
		Vec3[] corners = new Vec3[4];
		corners[0] = new Vec3(1, -tn, -tn);
		corners[1] = new Vec3(1,  tn, -tn);
		corners[2] = new Vec3(1,  tn,  tn);
		corners[3] = new Vec3(1, -tn,  tn);
		
		Ray3 ray = new Ray3();
		ray.o.set(0, 0, camPosLLA.altitude);
		
		// find first intersection point
		double timax = 0;
		for ( int j=0; j<4; j++ ) {
			Mat3.mul(ray.u, R, corners[j]);
			ti = -ray.o.z / ray.u.z;
			
			if (ti > 0) {
				timax = Math.max(timax, ti);
				ray.getPoint(ti, pi);
				newPoints.add( xyToLatLong(pi.x, pi.y, camPosLLA) );
			}
		}
		
		// corners 0 and 1 intersect but 2 and 3 extend beyond infinity
		Log.d(TAG, "num points = " + newPoints.size() );
		if ( newPoints.size() == 2 ) {
			Vec3 extremePoints[] = new Vec3[4];
			double xmin = mLatLngBounds.southwest.latitude;
			double ymin = mLatLngBounds.southwest.longitude;
			double xmax = mLatLngBounds.northeast.latitude;
			double ymax = mLatLngBounds.northeast.longitude;
			extremePoints[0] = new Vec3(xmin, ymin, 0);
			extremePoints[1] = new Vec3(xmax, ymin, 0);
			extremePoints[2] = new Vec3(xmax, ymax, 0);
			extremePoints[3] = new Vec3(xmin, ymax, 0);
			
			Line3 line = new Line3();
			Vec3 p0 = new Vec3(camPosLLA.latitude, camPosLLA.longitude, 0);
			Vec3 p1 = new Vec3();
			
			for ( int j=2; j<=3; j++ ) {
				Mat3.mul(ray.u, R, corners[j]);
				ti = -ray.o.z / ray.u.z;
				if (ti < 0) {
					//ray.getPoint(ti, pi);
					//LatLng ll = xyToLatLong(-pi.x, -pi.y, camPosLLA); // take the point symmetric to the origin
					LatLng ll = xyToLatLong(timax*ray.u.x, timax*ray.u.y, camPosLLA);
					p1.set( ll.latitude, ll.longitude, 0 );
					line.makeFromPoints(p0, p1);
					
					
					double tproj, tmax = -Float.MAX_VALUE;
					for( int i=0; i<4; i++ ) {
						tproj = line.project( extremePoints[i] );
						tmax = Math.max(tmax, tproj);
					}
					if ( tmax > 0 ) {
						line.getPoint(pi, tmax);
						newPoints.add( new LatLng(pi.x, pi.y) );
					}
				}
			}			
		}
		
		
		//LatLngBounds bounds = mGoogleMap.getProjection().getVisibleRegion().latLngBounds;
		
		mHandler.post(new Runnable() {
			public void run() {
				mLatLngBounds = mGoogleMap.getProjection().getVisibleRegion().latLngBounds;
				if ( newPoints.isEmpty() ) {
					mPolygon.setVisible( false );
				} else {
					mPolygon.setPoints( newPoints );
				}
			}
		});
	}
	
	
}
