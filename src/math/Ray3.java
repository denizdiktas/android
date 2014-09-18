package math;

public class Ray3 {

	public Vec3 o, u;
	
	public Ray3()
	{
		o = new Vec3();
		u = new Vec3();
	}
	
	public void getPoint( double t, Vec3 rp ) 
	{   
		rp.x = o.x + t*u.x;
		rp.y = o.y + t*u.y;
		rp.z = o.z + t*u.z;
	}
}
