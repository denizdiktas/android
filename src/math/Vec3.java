package math;

public class Vec3 {

	public double x,y,z;

	public static final Vec3 Ex = new Vec3(1,0,0);
	public static final Vec3 Ey = new Vec3(0,1,0);
	public static final Vec3 Ez = new Vec3(0,0,1);
	
	public Vec3() {}
	public Vec3( Vec3 b ) { this.set(b); }
	public Vec3( double x, double y, double z ) { this.set(x,y,z); }
	
	
	public void zero() { x = y = z = 0; }
	public void set( double x, double y, double z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void set( Vec3 source )
	{
		this.x = source.x;
		this.y = source.y;
		this.z = source.z;
	}
	
	public double length()
	{
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public Vec3 scale( double factor )
	{
		x *= factor;
		y *= factor;
		z *= factor;
		return this;
	}
	
	public Vec3 invScale( double factor )
	{
		x /= factor;
		y /= factor;
		z /= factor;
		return this;
	}

	public Vec3 normalize()
	{
		double len = this.length();
		x /= len;
		y /= len;
		z /= len;
		return this;
	}
	
	public void reverse() {
		x = -x;
		y = -y;
		z = -z;
	}
	
	public Vec3 add( Vec3 b ) 
	{ 
		x += b.x;
		y += b.y;
		z += b.z;
		return this;
	}
	public Vec3 sub( Vec3 b ) 
	{ 
		x -= b.x;
		y -= b.y;
		z -= b.z;
		return this;
	}
	
	public boolean isEqual( Vec3 b )
	{
		return (x == b.x) && (y == b.y) && (z == b.z);
	}
	public boolean isEqualEps( Vec3 b )
	{
		return MathAux.isEqual(x, b.x) && MathAux.isEqual(y, b.y) && MathAux.isEqual(z, b.z); 
	}
	public boolean isEqualEps( Vec3 b, double eps )
	{
		return MathAux.isEqual(x, b.x, eps) && MathAux.isEqual(y, b.y, eps) && MathAux.isEqual(z, b.z, eps); 
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append( Double.toString(x) );
		sb.append( ", " );
		sb.append( Double.toString(y) );
		sb.append( ", " );
		sb.append( Double.toString(z) );
		
		return sb.toString();
	}
	
	static public void cross(Vec3 crossProd, Vec3 u, Vec3 v) {
		crossProd.x = u.y*v.z - u.z*v.y;
		crossProd.y = u.z*v.x - u.x*v.z;
		crossProd.z = u.x*v.y - u.y*v.x;
	}
	static public Vec3 cross( Vec3 u, Vec3 v ) { 
		Vec3 cp = new Vec3();
		cross(cp, u, v);
		return cp;
	}
	
	static public void sum( Vec3 res, Vec3 a, Vec3 b ) { 
		res.x = a.x + b.x;
		res.y = a.y + b.y;
		res.z = a.z + b.z;
	}
	static public void dif( Vec3 res, Vec3 a, Vec3 b ) { 
		res.x = a.x - b.x;
		res.y = a.y - b.y;
		res.z = a.z - b.z;
	}
	
	static public Vec3 sum( Vec3 a, Vec3 b )	{ return new Vec3(a.x+b.x, a.y+b.y, a.z+b.z); }
	static public Vec3 dif( Vec3 a, Vec3 b )	{ return new Vec3(a.x-b.x, a.y-b.y, a.z-b.z); }
	static public Vec3 reverse( Vec3 b )		{ return new Vec3(-b.x, -b.y, -b.z); }
	static public Vec3 scale( Vec3 a, double factor )	{  return new Vec3(factor*a.x, factor*a.y, factor*a.z); }
	
	static public double dot( Vec3 a, Vec3 b )	{ return (a.x*b.x + a.y*b.y + a.z*b.z); }

	// linear interoplation between two vectors: res = a*(1-t) + b*t 
	static public void lerp( Vec3 res, Vec3 a, Vec3 b, double t ) {  
		res.x = (1-t)*a.x + t*b.x;
		res.y = (1-t)*a.y + t*b.y;
		res.z = (1-t)*a.z + t*b.z;
	}
}















