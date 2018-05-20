
public class Particle {
	public double diameter;
	public double radius;
	public int sides;
	public double intAngle;
	public Point center;
	public Point vel;
	public Point[] points;
	double theta = 0;

	Particle(double diameter) {
		this.diameter = diameter;
		radius = diameter / 2.0;
		vel = new Point(0, 0);
	}

	Particle(double diameter, double x, double y, int sides, double theta) {
		this(diameter);
		center = new Point(x, y);
		this.sides = sides;
		intAngle = 2 * Math.PI / sides;
		this.theta = theta;
		create();
	}

	/**
	 * Creates the array of points that defines the polygon
	 */
	public void create() {
		points = new Point[sides + 1];
		points[0] = new Point(center.x, center.y - radius);
		points[1] = new Point(center.x, center.y);
		// Add the remaining faces

		for (int point = 2; point < points.length; point++) {
			// Rotate the remaining points about the center to create the shape
			// double x = 0, y = 0;
			double rot = (point - 1) * intAngle;
			double x = (points[0].x - points[1].x) * Math.cos(rot) - (points[0].y - points[1].y) * Math.sin(rot)
					+ points[1].x;
			double y = (points[0].x - points[1].x) * Math.sin(rot) + (points[0].y - points[1].y) * Math.cos(rot)
					+ points[1].y;
			points[point] = new Point(x, y);
		}

		// Rotate the particle about the edge touching the plane
		for (int point = 1; point < points.length; point++) {
			double x = points[point].x;
			double y = points[point].y;
			points[point].x = (x - points[0].x) * Math.cos(theta) - (y - points[0].y) * Math.sin(theta) + points[0].x;
			points[point].y = (x - points[0].x) * Math.sin(theta) + (y - points[0].y) * Math.cos(theta) + points[0].y;
		}

	}

	public void changePoint(int point, double newX, double newY) {
		points[point] = new Point(newX, newY);
	}

	public Point[] getPoints() {
		return points;
	}

	/**
	 * Updates the center of the shape as well as the surrounding points
	 * 
	 * @param x
	 * @param y
	 */
	public void update(double newX, double newY, double theta) {
		center.x = newX;
		center.y = newY;
		this.theta = theta;

		// Same as create() but without creating new Objects
		points[0].x = center.x;
		points[0].y = center.y - radius;
		// = new Point(center.x, center.y - radius);
		points[1].x = center.x;
		points[1].y = center.y;
		// = new Point(center.x, center.y);
		// Add the remaining faces

		for (int point = 2; point < points.length; point++) {
			// Rotate the remaining points about the center to create the shape
			// double x = 0, y = 0;
			double rot = (point - 1) * intAngle;
			double x = (points[0].x - points[1].x) * Math.cos(rot) - (points[0].y - points[1].y) * Math.sin(rot)
					+ points[1].x;
			double y = (points[0].x - points[1].x) * Math.sin(rot) + (points[0].y - points[1].y) * Math.cos(rot)
					+ points[1].y;
			points[point].x = x;
			points[point].y = y;
			// = new Point(x, y);
		}

		// Rotate the particle about the center of the polygon
		for (int point = 0; point < points.length; point++) {
			if (point != 1) {
				double x = points[point].x;
				double y = points[point].y;
				points[point].x = (x - points[1].x) * Math.cos(theta) - (y - points[1].y) * Math.sin(theta)
						+ points[1].x;
				points[point].y = (x - points[1].x) * Math.sin(theta) + (y - points[1].y) * Math.cos(theta)
						+ points[1].y;
			}
		}
	}

	/**
	 * Rotates the particle dTheta clockwise about the center
	 * 
	 * @param dTheta
	 */
	public void updateThetaOld(double dTheta) {
		theta += dTheta;

		// Rotates the center about the edge touching the line
		points[1].x = (points[1].x - points[0].x) * Math.cos(dTheta) - (points[1].y - points[0].y) * Math.sin(dTheta)
				+ points[0].x;
		points[1].y = (points[1].x - points[0].x) * Math.sin(dTheta) + (points[1].y - points[0].y) * Math.cos(dTheta)
				+ points[0].y;

		// Finds dx and dy to determine the angle
		// Used to adjust the shrinking radius problem
		double dx = 0, dy = 0;
		dx = points[1].x - points[0].x;
		dy = points[1].y - points[0].y;

		double angle = 0;
		// Determines the angle from the center to the edge that touches the
		// ground
		if (dx == 0) {
			if (dy > 0) {
				angle = Math.PI / 2.0;
			} else {
				angle = 3.0 * Math.PI / 2.0;
			}
		} else if (dx > 0) {
			if (dy > 0) {
				angle = Math.atan(dy / dx);
			} else {
				angle = 2 * Math.PI + Math.atan(dy / dx);
			}
		} else {
			if (dy > 0) {
				angle = Math.PI + Math.atan(dy / dx);
			} else {
				angle = Math.PI + Math.atan(dy / dx);
			}
		}

		// Makes each point a fixed distance (radius) from the center
		// of the particle
		points[1].x = points[0].x + radius * Math.cos(angle);
		points[1].y = points[0].y + radius * Math.sin(angle);

		// Rotates each of the remaining edges about the center of the
		// particle
		for (int point = 2; point < points.length; point++) {

			double rot = (point - 1) * intAngle;
			points[point].x = (points[0].x - points[1].x) * Math.cos(rot) - (points[0].y - points[1].y) * Math.sin(rot)
					+ points[1].x;
			points[point].y = (points[0].x - points[1].x) * Math.sin(rot) + (points[0].y - points[1].y) * Math.cos(rot)
					+ points[1].y;
		}
	}
}
