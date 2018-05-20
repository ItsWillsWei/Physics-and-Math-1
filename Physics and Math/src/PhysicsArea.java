//Imports necessary classes
import java.awt.*;
import java.awt.geom.*;
import java.util.List;
import java.util.*;

import javax.swing.*;

/**
 * 
 * @author Will
 *
 */
public class PhysicsArea extends Graph {

	public final int POINT = 0, LINE = 1;
	public List<Point> points2 = new ArrayList<Point>();
	public List<Point> plotPoints = new ArrayList<Point>();

	public List<Double> times = new ArrayList<Double>();
	public List<Double> angles = new ArrayList<Double>();
	public List<Double> angularSpeeds = new ArrayList<Double>();
	public List<Double> xPos = new ArrayList<Double>();
	public List<Double> yPos = new ArrayList<Double>();
	public final double PI = Math.PI;

	public int modelType = 0;

	// Timer variables
	Thread timer;
	Particle shape;

	public boolean draw = false;
	public boolean isFinished = true;;

	// Calculation variables
	public double length;
	public double angle;
	public double dt;

	// Ellipse2D.Double drawing;
	public int plotType = LINE;

	public double a = 2;
	public double b = -2;

	public double A = 1;
	public double B = 1;
	public double C = 0;

	public boolean drawAxes = true;

	/**
	 * Makes a new function with the window sized to the specified size
	 * 
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 */
	PhysicsArea(double x1, double x2, double y1, double y2) {
		super(x1, x2, y1, y2);
	}

	public void updateGraph(double x1, double x2, double y1, double y2) {
		super.setValues(x1, x2, y1, y2);
	}

	public void makeShape(double diameter, int sides, double theta) {
		shape = new Particle(diameter, 0, function(0) + diameter / 2, sides, theta / 180.0 * PI);

		// drawing = new
		// Ellipse2D.Double(0,0,shape.diameter/dx,shape.diameter/dy);
	}

	public void setModel(int modelType) {
		this.modelType = modelType;
	}

	/**
	 * Uses Euler integration to model the movement of the polygon
	 * 
	 * @param Oinit
	 *            the initial angle phi of the polygon
	 * @param m
	 *            the mass of the polygon
	 * @param r
	 *            the distance from a corner to the center of the polygon
	 * @param n
	 *            the total number of sides of the polygon
	 * @param beta
	 *            the coefficient of restitution for an edge collision
	 * @param length
	 *            the distance to be travelled along the hypotenuse of the ramp
	 * @param angle
	 *            the inclination angle of the ramp in degrees
	 * @param dt
	 *            the time increment for the Euler integration
	 * @return
	 */
	public boolean process(double Oinit, double m, double r, double n, double beta, double length, double angle,
			double dt) {
		// Creates a new Particle
		makeShape(r * 2, (int) n, angle);

		// Resets all variables for each new ramp angle
		double t = 0.0;
		double O = Oinit;
		double g = 9.81;// Gravitational field at Earth's surface

		this.length = length;
		this.angle = angle;
		this.dt = dt;
		// Calculates the moment of inertia about a corner
		double I = m * r * r * (Math.pow(Math.cos(Math.PI / n), 2) / 3.0 + 7.0 / 6.0);

		// Sets a precise starting angle for the cylinder
		if (n >= 100) {
			System.out.println("Cylinder:");
			O = angle * PI / 180.0;
		}

		// Lists keep track of angle phi (O) at each time t
		times = new ArrayList<Double>();
		angles = new ArrayList<Double>();
		times.add(t);
		angles.add(O);

		// Lists keep track of x and y positions of the center of mass
		xPos = new ArrayList<Double>();
		yPos = new ArrayList<Double>();
		double x = r * Math.sin(O);
		double y = r * Math.cos(O);
		xPos.add(x);
		yPos.add(y);

		// Sets the ramp angle and the initial angular velocity
		double theta = angle;
		double dO = 0.00000001;
		angularSpeeds = new ArrayList<Double>();
		angularSpeeds.add(dO);

		boolean walking = true;

		// Set the horizontal distance travelled
		double totalHorizontalDist = length * Math.cos(theta / 180.0 * PI) - r * Math.sin(O);

		// Roll until the necessary distance travelled is reached
		while (x <= totalHorizontalDist && walking) {
			// Use Euler's method for each roll
			while (O < theta / 180.0 * PI + PI / n && x <= totalHorizontalDist && walking) {

				// Find the angular velocity
				// Update the angle using the angular velocity
				O = O + dO * dt;
				t += dt;

				// Update time and phi (O)
				times.add(t);
				angles.add(O);
				if((int)(t/dt) % 100000 == 0)
					System.out.println(t);
				// Update x and y positions of the center of mass
				x += r * (Math.sin(O) - Math.sin(O - dO * dt));// Minus
																// because
																// already
																// did
				y += r * (Math.cos(O) - Math.cos(O - dO * dt));
				xPos.add(x);
				yPos.add(y);
				plotPoints.add(new Point(x, y));
				dO = Math.sqrt(
						2.0 / I * (m * g * r * Math.cos(O) - m * g * r * Math.cos(O + dO * dt) + 0.5 * I * dO * dO));
				angularSpeeds.add(dO);
				//angularSpeeds.add(1.0);
				
				
				// Losing contact condition
				if (modelType == 1) {
					walking = dO < Math.sqrt(g * Math.cos(O) / r);
				}
			} // for each time the face hits the board or walking done

			// Use Coefficient of restitution
			if (walking) {
				dO *= beta;
				// Reset initial angle
				O = theta / 180.0 * PI - PI / n;
			}

			// Use the running model
			if (modelType == 1 && x < totalHorizontalDist && !walking) {

				// initial x and y velocity of the center of mass
				double Vx0 = dO * r * Math.cos(O);
				double Vy0 = -dO * r * Math.sin(O);
				double tf = 0; // set the initial flying time to 0

				double[] mus = new double[(int) n];
				double[] musI = new double[(int) n];
				double[] Hs = new double[(int) n];

				double mu0 = O + Math.PI / 2 - 2 * Math.PI / n;
				// Sets initial mu values and initial H values for each corner
				for (int corner = 0; corner < n-1; corner++) {
					musI[corner] = mu0 - corner * 2 * Math.PI / n;
					mus[corner] = mu0 - corner * 2 * Math.PI / n;
				}

				boolean stop = false;
				int lastCorner = -1;

				// Define initial H
				double VY = Vy0; // Set initial y-velocity for tracing
									// y-coordinate

				// Loop until the polygon falls down to the ramp
				while (!stop && x <= totalHorizontalDist) {
					//For each corner
					for (int corner = 0; corner < n && !stop; corner++) {
						
						
						
						// Define h, L, K, H
						double h = r * Math.cos(O) + r * Math.sin(O) * Math.tan(theta / 180.0 * Math.PI) + Vy0 * tf
								- 1.0 / 2.0 * g * tf * tf + Vx0 * tf * Math.tan(theta / 180.0 * Math.PI);// Equation
						
						mus[corner] += dO*dt;// x

						double L = r * Math.sin(mus[corner]);// O+dO*tf);//TODO
						double K = r * Math.tan(theta / 180.0 * Math.PI) * Math.cos(mus[corner]);// TODO
						Hs[corner] = h - L + K;
						
						
						if(Hs[corner] <= 0){
							if(corner != n ){//|| Hs[0] > 0.001){
							lastCorner = corner;
							//System.out.println(lastCorner);
							stop = true;
							}
						}
					}
					
					tf = tf + dt; // update time
					t += dt;
					times.add(t);

					angles.add(O + dO*tf);//mus[0]-Math.PI/2);// Continuous
					angularSpeeds.add(dO);// Constant Angular Speed
					//angularSpeeds.add(Hs[1]);
					// trace the x,y coordinates in the air
					x = x + Vx0 * dt;
					y = y + VY * dt;
					VY = VY - g * dt;

					xPos.add(x);
					yPos.add(y);
					plotPoints.add(new Point(x, y));
				} // polygon has just touched the ramp

				// t = t + tf //update total time//TODO

				// Finds the initial O and dO for the walking model
				double betar = beta*beta;//0.433692871036404; // edge-on restitution
													// coefficient's square
				// update energy profile after collision
				double Er = (0.5 * m * Vx0 * Vx0 + 0.5 * m * Math.pow((Vy0 - g * tf), 2)
						+ 0.5 * (I - m * r * r) * dO * dO) * betar;
				
				if(stop == false)
					lastCorner = 0;
				O = mus[lastCorner] - Math.PI/2;//O = -(PI / 2 - (O + dO * tf)); // TODO//starting phyla for
												// gaining contact
				// Or = Or0 #set initial phyla again for the following loop
				dO = Math.sqrt(2 / I * Er); // set initial angular speed
				// double tr = 0; //set/reset initial running time

				// Deep going using the running model
				walking = true;
			}
		} // For each conti
		System.out.println("time" + t);
		return true;
	}

	/**
	 * Adds some random point
	 */
	public void bounce() {
		boolean doIt = false;
		if (doIt) {
			points2.add(new Point(a, b));
			// Reflects over the line Ax+By+C=0
			double x = (a * B * B - a * A * A - 2 * b * A * B - 2 * A * C) / (A * A + B * B);
			double y = (b * A * A - b * B * B - 2 * a * A * B - 2 * B * C) / (A * A + B * B);
			points2.add(new Point(x, y));
		}
	}

	public void startTime(double slowFactor) {
		timer = new Thread(new TimerThread(dt, slowFactor));
		draw = true;
		timer.start();
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void stopAnimating() {
		isFinished = true;
	}

	public void plotLine() {
		plotType = LINE;
	}

	public void plotPoint() {
		plotType = POINT;
	}

	public Point[] calculatePoints() {
		for (int increment = 0; increment < points.length; increment++) {
			double x = x1 + dx * increment;
			points[increment] = new Point(x, function(x));
		}
		return points;
	}

	public double function(double x) {
		return -A / B * x - C / B;// Math.exp(x);
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void graphics() {
		repaint(0);
	}

	public void paintComponent(Graphics g) {
		// CLEARS
		g.setColor(Color.GRAY.brighter());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);

		if (draw) {
			// Draw the ramp
			//for (int point = 0; point < points.length - 1; point++) {
				// g.drawLine(point, (int) ((y2 - points[point].y) / dy), point
				// + 1,
				// (int) ((y2 - points[point + 1].y) / dy));
				g.drawLine((int) ((0.0 - x1) / dx), (int) ((y2 - 0.0) / dy),
						(int) ((length * Math.cos(angle * PI / 180.0) - x1) / dx),
						(int) ((y2 - (-length * Math.sin(angle * PI / 180.0))) / dy));
			//}

			// Draw the points
			// Draws the particle
			Point[] points = shape.getPoints();
			for (int point = 2; point < points.length; point++) {
				g.drawLine((int) ((points[point].x - x1) / dx), (int) ((y2 - points[point].y) / dy),
						(int) ((points[(point + 1) % points.length].x - x1) / dx),
						(int) ((y2 - points[(point + 1) % points.length].y) / dy));
			}
			// Connects the first point to the origin
			g.drawLine((int) ((points[2].x - x1) / dx), (int) ((y2 - points[2].y) / dy),
					(int) ((points[0].x - x1) / dx), (int) ((y2 - points[0].y) / dy));
			// Draws the radius
			g.setColor(Color.RED);
			g.drawLine((int) ((points[1].x - x1) / dx), (int) ((y2 - points[1].y) / dy),
					(int) ((points[0].x - x1) / dx), (int) ((y2 - points[0].y) / dy));
			g.setColor(Color.BLACK);

		} // if draw

		// Draw the axes
		// Draw y axis
		g.drawLine((int) (-x1 / dx), 0, (int) (-x1 / dx), dimension.height);
		// Draw x axis
		g.drawLine(0, (int) (y2 / dy), dimension.width, (int) (y2 / dy));
	}

	public class TimerThread implements Runnable {
		public long start;
		public long last;
		public double dt;
		public double slow;

		public TimerThread(double dt, double slowFactor) {
			this.dt = dt;
			this.slow = slowFactor;
		}

		public void run() {
			start = System.currentTimeMillis();
			last = start;
			isFinished = false;
			int element = 0;
			while (element < times.size() && !isFinished) {
				try {
					Thread.sleep(20);
					long time = System.currentTimeMillis();
					double dt = (time - last) / 1000.0;
					last = time;

					// Update position and angle (angle is counterclockwise)
					shape.update(xPos.get(element), yPos.get(element), -angles.get(element));

					graphics();

					// Advance by this many elements/frames
					element += (int)(dt/(this.dt)/slow);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			stopAnimating();
		}

	}
}
