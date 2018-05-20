import java.awt.Dimension;
import java.awt.Graphics;
import java.io.*;
import java.util.*;

import javax.swing.JFrame;

public class RecordData2 extends Graph {

	// Declare global static variables
	public static List<Point> plotPoints;
	public static boolean draw;

	RecordData2(double x1, double x2, double y1, double y2) {
		// Calls graph
		super(x1, x2, y1, y2);
		draw = false;
		plotPoints = new ArrayList<Point>();
		// test();
		//start();
	}

	public void start() {
		JFrame frame = new JFrame("Graphs");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(this);
		frame.setMinimumSize(new Dimension(getDimension().width + 20, getDimension().height + 40));
		frame.setResizable(false);// Prevent resizing
		// frame should adapt to the panel size
		frame.pack();
		frame.setVisible(true);

	}

	public Point[] calculatePoints() {
		// TODO Auto-generated method stub
		return null;
	}

	public double function(double x) {
		return -Math.exp(x);
	}

	public void paintComponent(Graphics g) {
		if (draw) {
			// Draw the points
			if (plotType == POINT) {
//				for (int point = 0; point < points.length; point++) {
//					// g.drawRect((int)points.get(point).x, (int) ((y2 -
//					// points.get(point).y)), 1, 1);
//					g.drawRect((int) points[point].x, (int) ((y2 - points[point].y)), 1, 1);
//
//				}
				// Plot individual points
				for (int point = 0; point < plotPoints.size(); point++) {
					g.drawRect((int) ((plotPoints.get(point).x - x1) / dx), (int) ((y2 - plotPoints.get(point).y) / dy),
							1, 1);
				}
			} else if (plotType == LINE) {
//				for (int point = 0; point < points.length - 1; point++) {
//					g.drawLine(point, (int) ((y2 - points[point].y) / dy), point + 1,
//							(int) ((y2 - points[point + 1].y) / dy));
//				}
				// Plot individual points
				for (int point = 0; point < plotPoints.size() - 1; point++) {
					g.drawLine((int) ((plotPoints.get(point).x - x1) / dx), (int) ((y2 - plotPoints.get(point).y) / dy),
							(int) ((plotPoints.get(point + 1).x - x1) / dx),
							(int) ((y2 - plotPoints.get(point + 1).y) / dy));
				}
			}
		} // if draw

		// Draw the axes
		// Draw y axis
		g.drawLine((int) (-x1 / dx), 0, (int) (-x1 / dx), dimension.height);
		// Draw x axis
		g.drawLine(0, (int) (y2 / dy), dimension.width, (int) (y2 / dy));
	}

	public void test() {
		plotType = LINE;
		for (int increment = 0; increment < points.length; increment++) {
			double x = x1 + dx * increment;
			// points[increment] = new Point(x, function(x));
			points[increment] = new Point(0, 0);
			plotPoints.add(new Point(x, function(x)));
		}

		repaint();
	}

	public static void main(String[] args) {
		double x1 = 0;
		double x2 = 0.25;
		double y1 = -0.25;
		double y2 = 0;

		RecordData2 graph = new RecordData2(x1, x2, y1, y2);

		// #Define and initialize variables and constants
		double Oinit = 0.0805;// 4.61 * Math.PI / 180.0; // Start at 4.61
								// degrees
		double t;// Time

		double m = 0.0252;// mass
		double g = 9.81;// gravitational field
		double r = 0.017334722741;// Radius of object
		double theta;// Ramp angle
		double I = 4.0 / 3 * m * r * r;// Moment of inertia about edge
		double beta = 0.65926745;// 0.43369; Coefficient of restitution (wf/wi)

		double length = 0.279;// Length travelled down ramp

		// Initial and final ramp angles
		int lAngle = 23;
		int hAngle = 33;
		int increment = 1;

		// Define and initialize time variables
		double dt = 0.00001;
		final double PI = 3.1415926535897932384626433832795029;

		// Setup writer to CSV spreadsheet
		try {
			// Creates a new PrintWriter object to write to a CSV file
			PrintWriter output = new PrintWriter(new File("Data.csv"));

			// Prints the date
			output.print("Date,");
			output.println(new Date().toString());
			output.println();
			output.println("Angle,Time,Theta,dTheta");

			// Writes data to the spreadsheet for each ramp angle
			for (int angle = lAngle; angle <= hAngle; angle += increment) {
				// Resets all variables for each new ramp angle
				t = 0;
				double O = Oinit;

				// Lists keep track of angle phi (O) at each time t
				List<Double> xaxis = new ArrayList<Double>();
				List<Double> yaxis = new ArrayList<Double>();
				xaxis.add(t);
				yaxis.add(O);

				// Lists keep track of x and y positions of the center of mass
				List<Double> xValues = new ArrayList<Double>();
				List<Double> yValues = new ArrayList<Double>();
				double x = 0;
				double y = 0;
				xValues.add(x);
				yValues.add(y);

				// Sets the ramp angle and the initial angular velocity
				theta = angle;
				double dO = 0.00000001;

				// Roll until the necessary distance travelled is reached
				while (x <= length * Math.cos(theta / 180.0 * PI)) {
					// Use Euler's method for each roll
					while (O < theta / 180.0 * PI + PI / 4 && x <= length * Math.cos(theta / 180.0 * PI)) {

						// Find the angular velocity
						double dO2 = Math.sqrt(2.0 / I
								* (m * g * r * Math.cos(O) - m * g * r * Math.cos(O + dO * dt) + 0.5 * I * dO * dO));
						// Update the angle using the angular velocity
						O = O + dO * dt;
						t += dt;

						// Update time and phi (O)
						xaxis.add(t);
						yaxis.add(O);

						

						// Update x and y positions of the center of mass
						x += r * (Math.sin(O) - Math.sin(O - dO * dt));// Minus
																		// because
																		// already
																		// did
						y += r * (Math.cos(O) - Math.cos(O - dO * dt));
						xValues.add(x);
						yValues.add(y);
						plotPoints.add(new Point(x, y));
						dO = dO2;
					} // for each time the face hits the board

					// Use Coefficient of restitution
					dO *= beta;
					// Reset initial angle
					O = theta / 180.0 * PI - PI / 4;
				} // For each continuous roll down the entire ramp

				// Print out info
				System.out.println(angle);
				System.out.println("t: " + t);
				System.out.println("x: " + x + "\n");
				output.println(angle + "," + t + "," + O + "," + dO);
			} // For each ramp angle theta
			
			draw = true;
			graph.repaint();
			/*
			 * #Plot, label, and show the graphs plt.plot(xaxis, yaxis, "g-")
			 * plt.title("Angle") plt.xlabel("Time (s)")
			 * plt.ylabel("Angle (Theta)") plt.grid(True) plt.show()
			 */
			System.out.println("Done");
			output.close();
		} // PrintWriter try section
		catch (FileNotFoundException e) {
			// Catch exception
			e.printStackTrace();
		}
	}
}
