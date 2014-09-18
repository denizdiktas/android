package math;

public class Line3 {
	public Vec3 o, u;
	
	public Line3() {
		o = new Vec3();
		u = new Vec3();
	}

	public void set(Vec3 o, Vec3 u) {
		this.o.set( o );
		this.u.set( u );
	}
	
	public void makeFromPoints( Vec3 p0, Vec3 p1 ) {
		o.set( p0 );
		Vec3.dif(u, p1, p0);
	}
	
	// compute the projection of the given point onto this line and return the projection parameter
	public double project( Vec3 p ) {
		return Vec3.dot(Vec3.dif(p,o), u) / Vec3.dot(u,u); 
	}
	
	// get the point on the line given its parameter
	public void getPoint(Vec3 p, double t) {
		p.x = o.x + t*u.x;
		p.y = o.y + t*u.y;
		p.z = o.z + t*u.z;
	}
}
