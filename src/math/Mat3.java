package math;

public class Mat3 {
	double m[][];
	
	public Mat3()
	{
		m = new double[3][3];
	}
	
	public Mat3( double m00, double m01, double m02,
				 double m10, double m11, double m12,
				 double m20, double m21, double m22 )
	{
		m = new double[3][3];
		m[0][0] = m00;   m[0][1] = m01;   m[0][2] = m02;
		m[1][0] = m10;   m[1][1] = m11;   m[1][2] = m12;
		m[2][0] = m20;   m[2][1] = m21;   m[2][2] = m22;
	}
	
	public double det()
	{
		return	m[0][0]*m[1][1]*m[2][2] + m[0][1]*m[1][2]*m[2][0] + m[0][2]*m[1][0]*m[2][1]
			-  (m[2][0]*m[1][1]*m[0][2] + m[2][1]*m[1][2]*m[0][0] + m[2][2]*m[1][0]*m[0][1]);
	}
	
	public Mat3 scale( double factor )
	{
		for ( int i=0; i<3; i++ )
		{
			for ( int j=0; j<3; j++ )
			{
				m[i][j] *= factor;
			}
		}
		
		return this;
	}
	
	public boolean isEqual( Mat3 b )
	{
		for ( int i=0; i<3; i++ )
		{
			for ( int j=0; j<3; j++ )
			{
				if( m[i][j] != b.m[i][j] )
					return false;
			}
		}	
		return true;
	}
	public boolean isEqualEps( Mat3 b )
	{
		for ( int i=0; i<3; i++ )
		{
			for ( int j=0; j<3; j++ )
			{
				if( !MathAux.isEqual(m[i][j], b.m[i][j]) )
					return false;
			}
		}	
		return true;
	}
	public boolean isEqualEps( Mat3 b, double eps )
	{
		for ( int i=0; i<3; i++ )
		{
			for ( int j=0; j<3; j++ )
			{
				if( !MathAux.isEqual(m[i][j], b.m[i][j], eps) )
					return false;
			}
		}	
		return true;
	}
	
	public Vec3 mul( Vec3 v )
	{
		Vec3 r = new Vec3();
		r.x  = m[0][0]*v.x + m[0][1]*v.y + m[0][2]*v.z;
		r.y  = m[1][0]*v.x + m[1][1]*v.y + m[1][2]*v.z;
		r.z  = m[2][0]*v.x + m[2][1]*v.y + m[2][2]*v.z;
		return r;
	}
	
	public Vec3 getRow(int ri)	{ return new Vec3(m[ri][0], m[ri][1], m[ri][2]); }
	public Vec3 getCol(int ci)	{ return new Vec3(m[0][ci], m[1][ci], m[2][ci]); }
	
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for ( int i=0; i<3; i++ )
		{
			for ( int j=0; j<3; j++ )
			{
				sb.append( m[i][j] + ", " );
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}

	
	static public Mat3 createZero() 	{ return new Mat3(0,0,0, 0,0,0, 0,0,0); }
	static public Mat3 createIdentity() { return new Mat3(1,0,0, 0,1,0, 0,0,1); }
	
	static public Mat3 transpose(Mat3 a)
	{
		Mat3 tr = new Mat3();
		for ( int i=0; i<3; i++ )
			for ( int j=0; j<3; j++ )
				tr.m[i][j] = a.m[j][i];
				
		return tr;
	}
	
	static public Mat3 mul( Mat3 a, Mat3 b )
	{
		Mat3 r = new Mat3();
		for ( int i=0; i<3; i++ )
		{
			for ( int j=0; j<3; j++ )
			{
				r.m[i][j] = 0;
				for ( int k=0; k<3; k++ )
				{
					r.m[i][j] += a.m[i][k] * b.m[k][j];
				}
			}
		}	
		return r;
	}
	
	static public Mat3 inverse( Mat3 a )
	{
		Mat3 r = new Mat3();
		r.m[0][0] =  (a.m[1][1]*a.m[2][2] - a.m[2][1]*a.m[2][1]);
		r.m[0][1] = -(a.m[1][0]*a.m[2][2] - a.m[2][0]*a.m[1][2]);
		r.m[0][2] =  (a.m[1][0]*a.m[2][1] - a.m[2][0]*a.m[1][1]);
		
		r.m[1][0] = -(a.m[0][1]*a.m[2][2] - a.m[2][1]*a.m[0][2]);
		r.m[1][1] =  (a.m[0][0]*a.m[2][2] - a.m[2][0]*a.m[0][2]);
		r.m[1][2] = -(a.m[0][0]*a.m[2][1] - a.m[2][0]*a.m[0][1]);
		
		r.m[2][0] =  (a.m[0][1]*a.m[1][2] - a.m[1][1]*a.m[0][2]);
		r.m[2][1] = -(a.m[0][0]*a.m[1][2] - a.m[1][0]*a.m[0][2]);
		r.m[2][2] =  (a.m[0][0]*a.m[1][1] - a.m[1][0]*a.m[0][1]);
		
		r.scale( 1.0 / a.det() );
		return Mat3.transpose(r);
	}
	
	static public Mat3 rotX(double theta) {
		double c = Math.cos(theta);
		double s = Math.sin(theta);
		return new Mat3( 1,	0,  0,
						 0, c, -s,
						 0, s,  c );
	}
	static public Mat3 rotY(double theta) {
		double c = Math.cos(theta);
		double s = Math.sin(theta);
		return new Mat3( c, 0, s,
						 0, 1, 0,
						-s, 0, c );
	}
	static public Mat3 rotZ(double theta) {
		double c = Math.cos(theta);
		double s = Math.sin(theta);
		return new Mat3( c, -s, 0,
						 s,  c, 0, 
						 0,	 0, 1 );
	}
	// rotate around arbitrary UNIT vector
	static public Mat3 rot(double theta, Vec3 u) {
		double c = Math.cos(theta);
		double s = Math.sin(theta);
		return new Mat3( c + u.x*u.x*(1-c),      u.x*u.y*(1-c) - u.z*s,  u.x*u.z*(1-c) + u.y*s,
						 u.y*u.x*(1-c) + u.z*s,      c + u.y*u.y*(1-c),  u.y*u.z*(1-c) - u.x*s,
						 u.z*u.x*(1-c) - u.y*s,  u.z*u.y*(1-c) + u.x*s,      c + u.z*u.z*(1-c) );
	}
	
	static public Mat3 createFromRows( Vec3 row0, Vec3 row1, Vec3 row2 ) {
		return new Mat3( row0.x, row0.y, row0.z, 
						 row1.x, row1.y, row1.z,
						 row2.x, row2.y, row2.z );
	}
	static public Mat3 createFromCols( Vec3 col0, Vec3 col1, Vec3 col2 ) {
		return new Mat3( col0.x, col1.x, col2.x,
						 col0.y, col1.y, col2.y,
						 col0.z, col1.z, col2.z );
	}
	
	// result = left * right
	static public void mul(Vec3 result, Mat3 left, Vec3 right) {
		result.x = left.m[0][0] * right.x + left.m[0][1] * right.y + left.m[0][2] * right.z;
		result.y = left.m[1][0] * right.x + left.m[1][1] * right.y + left.m[1][2] * right.z;
		result.z = left.m[2][0] * right.x + left.m[2][1] * right.y + left.m[2][2] * right.z;
	}
}
