
public class Line {

	private Vector p;
	private Vector r;
	
	public Line(Vector v1, Vector v2) {
		p = new Vector(v1.x(), v1.y(), v1.z());
		r = new Vector(v2.x() - v1.x(), v2.y() - v1.y(), v2.z() - v1.z());
	}
	
	public Vector origin() {
		return p;
	}
	
	public Vector slope() {
		return r;
	}
	
	public Vector point(double t) {
		return p.add(r.scale(t));
	}
	
}
