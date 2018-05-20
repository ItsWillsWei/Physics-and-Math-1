import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Julia extends Graph {
	List<Point> points;

	Julia(double x1, double x2, double y1, double y2) {
		super(x1, x2, y1, y2);
	}

	@Override
	public double function(double x) {
		return -x * x * x;
	}

	public double calculate(double a, double b, double c, double d, int count) {
		if ((a) * (a) + (b) * (b) >= 4)
			return -50000;
		else if (count > 500) {
			//if ((a + c) * (a + c) + (b + d) * (b + d) < 4 && (a + c) * (a + c) + (b + d) * (b + d) > 3.99999999)
				//return 10;
			return 0;
		}
		return 1 + calculate(a * a - b * b + c, 2 * a * b + d, c, d, count + 1);
	}

	@Override
	public Point[] calculatePoints() {
		boolean mandel = true;
		//mandel = false;
		if (mandel)
			calculateMandelbrot();
		else {
			points = new ArrayList<Point>();
			double c = 0.3;//-0.12;//0
			double d = -0.126;//0.75;//0.64
			for (int a = 0; a < dimension.width; a++) {
				for (int b = 0; b < dimension.height; b++) {
					double x = x1 + (x2 - x1) / dimension.width * a;
					double y = y1 + (y2 - y1) / dimension.height * b;
					if (calculate(x, y, c, d, 0) > 0)
						points.add(new Point(x, y));
				}
			}
		}
		return null;
	}

	public void calculateMandelbrot() {
		points = new ArrayList<Point>();
		for (int a = 0; a < dimension.width; a++) {
			for (int b = 0; b < dimension.height; b++) {
				double x = x1 + (x2 - x1) / dimension.width * a;
				double y = y1 + (y2 - y1) / dimension.height * b;
				if (calculate(0, 0, x, y, 0) > 0)
					points.add(new Point(x, y));
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {

		// Draw the points
		for (Point p : points) {
			g.drawRect((int) ((p.x - x1) / (x2 - x1) * dimension.width),
					(int) ((y2 - p.y) / (y2 - y1) * dimension.height), 1, 1);
		}

		// Draw the axes
		// Draw y axis
		g.drawLine((int) (-x1 / dx), 0, (int) (-x1 / dx), dimension.height);
		// Draw x axis
		g.drawLine(0, (int) (y2 / dy), dimension.width, (int) (y2 / dy));
	}
}
