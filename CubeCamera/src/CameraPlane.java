public class CameraPlane {

	private Vector n;
	private Vector p;
	private Vector right;
	private double d;
	
	public CameraPlane(Vector cPos, Vector cDir, Vector r, double dist) {
		n = cDir;
		right = r;
		p = cPos.add(cDir.normalize().scale(-dist));
		d = n.dot(p);
	}
	
	public boolean visible(Vector v) {
		//System.out.println(v.dot(n) + " " + d);
		return (v.dot(n) > d);
	}
	
	public Vector intersect(Line l) {
		double t = (d - l.origin().dot(n)) / l.slope().dot(n);
		return l.point(t);
	}
	
	public int[] getCoords(Vector a) {
		int[] output = new int[2];
		
		output[0] = (int)Math.round(right.comp(a));
		output[1] = (int)Math.round(right.cross(n).comp(a));

		
		return output;
		
	}
	
}
