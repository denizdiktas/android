package com.example.demo;

import math.Mat3;
import math.Vec3;

import com.google.android.gms.maps.model.LatLng;

public class GeoCoordTrans {

	private final static double a = 6378137.0; // semi-major axis
	private final static double fi = 298.257223563; // reciprocal of flattening
	private final static double f  = 1.0 / fi; // flattening
	private final static double b = 6356752.3142; // semi-minor axis
	private final static double a2 = a * a;
	private final static double b2 = b * b;
	//private final static double e2 = 6.69437999014 / 1000.0;
	//private final static double se2 = 6.73949674228 / 1000.0;
	private final static double e2 = (a2 - b2) / (a2);
	private final static double se2 = (a2 - b2) / (b2);

	
	public static Vec3 toECEF_failed(LatLng gps, double height) {
		double phi = Math.toRadians( gps.latitude );
		double lambda = Math.toRadians( gps.longitude );
		double s = Math.sin(phi);
		double N = a / Math.sqrt(1 - e2 * s * s);

		Vec3 p = new Vec3();
		p.x = (N + height) * Math.cos(phi) * Math.cos(lambda);
		p.y = (N + height) * Math.cos(phi) * Math.sin(lambda);
		p.z = (a2*N/b2 + height) * Math.sin(phi);

		return p;
	}
	
	// http://mathforum.org/library/drmath/view/51832.html
	public static Vec3 lla2ecef( double latitude, double longitude, double height) {
		double lat = Math.toRadians( latitude );
		double lon = Math.toRadians( longitude );
		double cosLat = Math.cos( lat );
		double sinLat = Math.sin( lat );
		
		double C = 1.0 / Math.sqrt( cosLat*cosLat + (1-f)*(1-f)*sinLat*sinLat );
		double S = (1-f)*(1-f) * C;

		Vec3 p = new Vec3();
		p.x = (a*C + height) * cosLat * Math.cos(lon);
		p.y = (a*C + height) * cosLat * Math.sin(lon);
		p.z = (a*S + height) * sinLat;
		
		return p;
	}
	
	// http://mathforum.org/library/drmath/view/51832.html
	public static Vec3 toECEF(LLA lla) {
		return lla2ecef( lla.latitude, lla.longitude, lla.altitude );
	}
	

	public static LLA ECEF2LLA(double x, double y, double z) {
		double p = Math.sqrt(x * x + y * y);
		double theta = Math.atan((z * a) / (p * b));
		
		double lng = Math.atan(y / x);
		double lat = Math.atan((z + se2 * b * Math.pow(Math.sin(theta), 3)) / (p - e2 * a * Math.pow(Math.cos(theta), 3)));
		
		double s = Math.sin(lat);
		double N = a / Math.sqrt(1 - e2 * s * s);
		double h = (p / Math.cos(lat)) - N;  
	
		LLA lla = new LLA();
		lla.latitude = Math.toDegrees(lat);
		lla.longitude = Math.toDegrees(lng);
		lla.altitude = h;
		
		return lla;
	}
	public static LLA ECEF2LLA( Vec3 ecef ) { return GeoCoordTrans.ECEF2LLA(ecef.x, ecef.y, ecef.z); }
	
	
	// TRANSFORM VELOCITIES (VECTORS) ONLY!
	static public class Vector
	{
		static public Vec3 ecef2ltp( Vec3 velocity, LatLng gps )
		{
			double phi = Math.toRadians( gps.latitude );
			double lambda = Math.toRadians( gps.longitude );
			Vec3 v = velocity;
			
			// north
			double north = -v.x * Math.sin(phi) * Math.cos(lambda) - v.y * Math.sin(phi) * Math.sin(lambda) + v.z * Math.cos(phi);
			double east  = -v.x * Math.sin(lambda)                 + v.y * Math.cos(lambda);
			double down  = -v.x * Math.cos(phi) * Math.cos(lambda) - v.y * Math.cos(phi) * Math.sin(lambda) - v.z * Math.sin(phi);
			
			return new Vec3(north, east, down);
		}
		
		static public Vec3 ltp2ecef( Vec3 velocity, LatLng gps )
		{
			double phi = Math.toRadians( gps.latitude );
			double lambda = Math.toRadians( gps.longitude );
			
			// ECEF -> LTP MATRIX
			Mat3 m = new Mat3(	-Math.sin(phi) * Math.cos(lambda), -Math.sin(phi) * Math.sin(lambda),	Math.cos(phi),
												-Math.sin(lambda),					Math.cos(lambda),				0,
								-Math.cos(phi) * Math.cos(lambda), -Math.cos(phi) * Math.sin(lambda),  -Math.sin(phi) 		);
			Mat3 mi = Mat3.inverse(m);
			
			return mi.mul(velocity);
		}
	}
	

}
