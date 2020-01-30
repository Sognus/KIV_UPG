package cz.zcu.viteja.upg.other;

import cz.zcu.viteja.upg.NamedPosition;

public class Vector3 {

	public final double x;
	public final double y;
	public final double z;
	
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getDistance(NamedPosition position) {
		double x1 = this.x;
		double y1 = this.y;
		double x2 = position.x;
		double y2 = position.y;

		double sx = x2 - x1;
		double sy = y2 - y1;
		double dv = Math.sqrt(sx*sx + sy*sy);
		return dv;
	}
	
	public double getDistance(Vector3 position) {
		double x1 = this.x;
		double y1 = this.y;
		double x2 = position.x;
		double y2 = position.y;
		
		double sx = x2 - x1;
		double sy = y2 - y1;
		double dv = Math.sqrt(sx*sx + sy*sy);
		return dv;
	
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
}
