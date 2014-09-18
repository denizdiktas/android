package math;

public class Intersects {
	
	// NEGATIVE return value => NO intersection
	static public double check( Ray3 ray, Plane3 plane )
	{
		double un = Vec3.dot(ray.u, plane.n);
		if ( MathAux.isZero(un) )
			return -1;
		
		// TODO: check performance of steadily creating a vector object
		return Vec3.dot( Vec3.dif(plane.o, ray.o), plane.n) / un; 
	}
}
