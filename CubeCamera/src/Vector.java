
public class Vector {
	private double x;
	private double y;
	private double z;
	
	public Vector(double a, double b, double c) {
		x = a;
		y = b;
		z = c;
	}
	
	public Vector(Vector v1, Vector v2) {
		x = v2.x() - v1.x();
		y = v2.y() - v1.y();
		z = v2.z() - v1.z();
	}
	
	public double x() {
		return x;
	}
	
	public double y() {
		return y;
	}
	
	public double z() {
		return z;
	}
	
	public Vector add(Vector v2) {
		return new Vector(x + v2.x(), y + v2.y(), z + v2.z());
	}
	
	public Vector scale(double c) {
		return new Vector(c * x, c * y, c * z);
	}
	
	public double dot(Vector v2) {
		return (x * v2.x()) + (y * v2.y()) + (z * v2.z());
	}
	
	public Vector cross(Vector v2) {
		return new Vector(y*v2.z() - z*v2.y(), z*v2.x() - x*v2.z(), x*v2.y() - y*v2.x());
	}
	
	public double dist(Vector v2) {
		double dx = v2.x() - x;
		double dy = v2.y() - y;
		double dz = v2.z() - z;
		return Math.sqrt(dx*dx + dy*dy + dz*dz);
	}
	
	public double abs() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public double comp (Vector v2) {
		return dot(v2) / abs();
	}
	
	public Vector normalize() {
		return scale(1/abs());
	}
	
	public String toString() {
		return "<" + x + ", " + y + ", " + z + ">";
	}
	
}
