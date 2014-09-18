package math;

public class Transform {

	static public void rotatePoint( Vec3 pout, Vec3 pin, double theta, Line3 axis ) {
		Vec3 uz = new Vec3( axis.u );
		uz.normalize();
		
		Vec3 diff = Vec3.dif(pin, axis.o);
		Vec3 uy = Vec3.cross(axis.u, diff);
		uy.normalize();
		Vec3 ux = Vec3.cross(uy, uz);
		
		Vec3 lp = new Vec3();
		lp.z = Vec3.dot(uz, diff);
		double rxy = Vec3.dot(ux, diff);
		lp.x = rxy * Math.cos( theta );
		lp.y = rxy * Math.sin( theta );
	
		ux.scale( lp.x );
		uy.scale( lp.y );
		uz.scale( lp.z );
		
		pout.set( axis.o );
		pout.add( ux );
		pout.add( uy );
		pout.add( uz );
	}
}
