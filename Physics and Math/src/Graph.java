
//Imports necessary classes
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

/**
 * 
 * @author Will
 *
 */
public class Graph extends JPanel {
	public Point center;
	public Dimension dimension = new Dimension(1000, 1000);
	public Point[] points;
	protected double x1, x2, y1, y2, dx, dy;

	public final int POINT = 0, LINE = 1;
	public int plotType = LINE;
	public List<Point> dataPoints = new ArrayList<Point>();

	Graph(double x1, double x2, double y1, double y2) {
		super();

		points = new Point[dimension.width];
		setValues(x1, x2, y1, y2);
		//calculatePoints();
	}

	/**
	 * Sets the size of the graph
	 * 
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 */
	public void setValues(double x1, double x2, double y1, double y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		dy = (y2 - y1) / dimension.height;
		dx = (x2 - x1) / dimension.width;
		//repaint();
	}

	public Point[] calculatePoints() {
		return null;
	}

	public double function(double x) {
		return -1;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void paintComponent(Graphics g) {
		// CLEARS
		g.setColor(Color.GRAY.brighter());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		if (plotType == POINT) {
			// Plot individual points
			for (int point = 0; point < dataPoints.size(); point++) {
				g.drawRect((int) ((dataPoints.get(point).x - x1) / dx), (int) ((y2 - dataPoints.get(point).y) / dy), 1,
						1);
			}
		} else if (plotType == LINE) {
			// Plot individual points
			for (int point = 0; point < dataPoints.size() - 1; point++) {
				g.drawLine((int) ((dataPoints.get(point).x - x1) / dx), (int) ((y2 - dataPoints.get(point).y) / dy),
						(int) ((dataPoints.get(point + 1).x - x1) / dx),
						(int) ((y2 - dataPoints.get(point + 1).y) / dy));
			}
		}

		// Draw the axes
		// Draw y axis
		g.drawLine((int) (-x1 / dx), 0, (int) (-x1 / dx), dimension.height);
		// Draw x axis
		g.drawLine(0, (int) (y2 / dy), dimension.width, (int) (y2 / dy));

		
		 /* // Draw the points if (plotType == POINT) for (int point = 0; point <
		 * points.length; point++) { g.drawRect((int)points[point].x, (int) ((y2
		 * - points[point].y)), 1, 1); } else if (plotType == LINE) for (int
		 * point = 0; point < points.length - 1; point++) { g.drawLine(point,
		 * (int) ((y2 - points[point].y) / dy), point + 1, (int) ((y2 -
		 * points[point + 1].y) / dy)); }
		 * 
		 * //Draw the axes //Draw y axis g.drawLine((int)(-x1/dx), 0,
		 * (int)(-x1/dx), dimension.height); //Draw x axis g.drawLine(0,
		 * (int)(y2/dy), dimension.width, (int)(y2/dy));
		 */
	}
}
