package math;


public class MathAux {
	
	static public double EPS = 0.000001; 

	static public boolean isZero( double x ) 				{ return Math.abs(x) < EPS; }
	static public boolean isZero( double x, double eps )	{ return Math.abs(x) < eps; }
	
	static public boolean isEqual( double x, double y ) 			{ return Math.abs(x-y) < EPS; }
	static public boolean isEqual( double x, double y, double eps )	{ return Math.abs(x-y) < eps; }
}
