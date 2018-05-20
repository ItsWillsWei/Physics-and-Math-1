import java.awt.Dimension;
import java.awt.Graphics;
import java.io.*;
import java.util.*;

import javax.swing.JFrame;

public class RecordData3 extends Graph {

	// Declare global static variables
	public static List<Point> plotPoints;
	public static boolean draw;

	RecordData3(double x1, double x2, double y1, double y2) {
		// Calls graph
		super(x1, x2, y1, y2);
		draw = false;
		plotPoints = new ArrayList<Point>();
		//test();
		start();
	}

	public void start() {
		System.out.println("Starting");
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

	
	public static double process(double Oinit, double m, double r, double n, double beta, double length, double angle,
			double dt, int modelType) {
		// Creates a new Particle
		//makeShape(r * 2, (int) n, angle);

		// Resets all variables for each new ramp angle
		double t = 0.0;
		double O = Oinit;
		double g = 9.81;// Gravitational field at Earth's surface

		//this.length = length;
		//this.angle = angle;
		//this.dt = dt;
		// Calculates the moment of inertia about a corner
		double I = m * r * r * (Math.pow(Math.cos(Math.PI / n), 2) / 3.0 + 7.0 / 6.0);

		// Sets a precise starting angle for the cylinder
		if (n > 100) {
			System.out.println("Cylinder:");
			O = angle * Math.PI / 180.0;
		}

		// Lists keep track of angle phi (O) at each time t
		//times = new ArrayList<Double>();
		//angles = new ArrayList<Double>();
		//times.add(t);
		//angles.add(O);

		// Lists keep track of x and y positions of the center of mass
		//xPos = new ArrayList<Double>();
		//yPos = new ArrayList<Double>();
		double x = r * Math.sin(O);
		double y = r * Math.cos(O);
		//xPos.add(x);
		//yPos.add(y);

		// Sets the ramp angle and the initial angular velocity
		double theta = angle;
		double dO = 0.00000001;
		//angularSpeeds = new ArrayList<Double>();
		//angularSpeeds.add(dO);

		boolean walking = true;

		// Set the horizontal distance travelled
		double totalHorizontalDist = length * Math.cos(theta / 180.0 * Math.PI) - r * Math.sin(O);

		// Roll until the necessary distance travelled is reached
		while (x <= totalHorizontalDist && walking) {
			// Use Euler's method for each roll
			while (O < theta / 180.0 * Math.PI + Math.PI / n && x <= totalHorizontalDist && walking) {

				// Find the angular velocity
				// Update the angle using the angular velocity
				O = O + dO * dt;
				t += dt;

				// Update time and phi (O)
				//times.add(t);
				//angles.add(O);

				// Update x and y positions of the center of mass
				x += r * (Math.sin(O) - Math.sin(O - dO * dt));// Minus
																// because
																// already
																// did
				y += r * (Math.cos(O) - Math.cos(O - dO * dt));
				//xPos.add(x);
				//yPos.add(y);
				//plotPoints.add(new Point(x, y));
				dO = Math.sqrt(
						2.0 / I * (m * g * r * Math.cos(O) - m * g * r * Math.cos(O + dO * dt) + 0.5 * I * dO * dO));
				//angularSpeeds.add(dO);

				//Losing contact condition
				if (modelType == 1) {
					walking = dO < Math.sqrt(g * Math.cos(O) / r);
				}
			} // for each time the face hits the board or walking done

			// Use Coefficient of restitution
			if(walking){
				dO *= beta;
			// Reset initial angle
			O = theta / 180.0 * Math.PI - Math.PI / n;
			}
			
			//Use the running model
			if (modelType == 1 && x < totalHorizontalDist && !walking) {
				
				//initial x and y velocity of the center of mass
			    double Vx0 = dO*r*Math.cos(O);
			    double Vy0 = -dO*r*Math.sin(O);
			    double tf = 0; //set the initial flying time to 0
			    //double mu = O;
			    
			    //Define initial H
			    double H = r*Math.cos(O)+r*Math.sin(O)*Math.tan(theta/180.0*Math.PI)-r*Math.sin(O)+r*Math.tan(theta/180.0*Math.PI)*Math.cos(O);
			    double VY = Vy0; //Set initial y-velocity for tracing y-coordinate

			    //Loop until the polygon falls down to the ramp
			    while (H >= 0 && x <= totalHorizontalDist){
			        
			    	//
			    	
			        //Define h, L, K, H
			        double h = r*Math.cos(O)+r*Math.sin(O)*Math.tan(theta/180.0*Math.PI) + Vy0*tf - 1.0/2.0*g*tf*tf + Vx0*tf*Math.tan(theta/180.0*Math.PI);
			
			        double L = r*Math.sin(O + dO*tf);//O+dO*tf);//TODO
			        double K = r*Math.tan(theta/180.0*Math.PI)*Math.cos(O + dO*tf);//TODO
			        H = h-L+K;
			        
			        tf = tf + dt; //update time
			        t += dt;
			        //times.add(t);
			        
			        //mu = mu + dO*dt;
			        //mu += dO*dt;
			        //angles.add(mu);//Continuous
			        //angularSpeeds.add(dO);//Constant Angular Speed
			        //trace the x,y coordinates in the air
			        x = x + Vx0 * dt;
			        y = y + VY*dt;
			        VY = VY - g*dt;
			        
			        //xPos.add(x);
			        //yPos.add(y);
			        //TODO plotPoints.add(new Point(x,y));
			    }//polygon has just touched the ramp
			    
			    //t = t + tf //update total time//TODO
			    
			    //Finds the initial O and dO for the walking model
			    double betar = 0.433692871036404; //edge-on restitution coefficient's square
			    //update energy profile after collision
			    double Er = (0.5*m*Vx0*Vx0 + 0.5*m*Math.pow((Vy0-g*tf),2) + 0.5*(I-m*r*r)*dO*dO)*betar;
			    O = -(Math.PI/2 - (O + dO*tf)); //TODO//starting phyla for gaining contact
			    //Or = Or0 #set initial phyla again for the following loop
			    dO = Math.sqrt(2/I*Er); //set initial angular speed
			    //double tr = 0; //set/reset initial running time
				
				
				
				// Deep going using the running model
				walking = true;
			}
		} // For each conti
		return t;
	}
	
	public static void main(String[] args) {
		double x1 = 0;
		double x2 = 0.25;
		double y1 = -0.25;
		double y2 = 0;

		RecordData3 graph = new RecordData3(x1, x2, y1, y2);

		// #Define and initialize variables and constants
		//double Oinit = 0.0805;// 4.61 * Math.PI / 180.0; // Start at 4.61
		double Oinit = 0.1135177;						// degrees
		double t;// Time

		double m = 0.0233;//0.0252;////square0.0252;//0.0233;//hex:0.0280;//square:0.0252;// mass
		//double g = 9.81;// gravitational field
		double r = 0.011865;//0.01413252572;//0.017334722741;//cylinder//0.011865;//hexagon//0.01413252572;//square//0.017334722741;// Radius of object
		double theta;// Ramp angle
		int n = 4;
		//double I = m*r*r*(Math.pow(Math.cos(Math.PI/n), 2)/3.0+7.0/6.0);//m/6.0 * Math.pow(Math.cos((n-2)*Math.PI/(2.0*n)), 2)*r*r*(1+3*Math.pow(1.0/Math.tan(Math.PI/n), 2)) + m*r*r;// Moment of inertia about edge
		double beta = 1;//0.867107;//0.65926745;//1;//0.867107;//square//0.65926745;//  Coefficient of restitution (wf/wi)

		double length = 0.279;// Length travelled down ramp

		// Initial and final ramp angles
//		int lAngle = 10;
//		int hAngle = 31;
//		int increment = 3;

		// Define and initialize time variables
		double dt = 0.000001;
		final double PI = 3.1415926535897932384626433832795029;
		int modelType = 0;
		
		// Setup writer to CSV spreadsheet
		try {
			// Creates a new PrintWriter object to write to a CSV file
			BufferedReader br = new BufferedReader(new FileReader(new File("In.csv")));
			PrintWriter output = new PrintWriter(new File("Data.csv"));

			// Prints the date
			output.print("Date,");
			output.println(new Date().toString());
			output.println();
			output.println("Angle,Time,Theta,dTheta");

			
			
			// Writes data to the spreadsheet for each ramp angle
			//for (int angle = lAngle; angle <= hAngle; angle += increment) {
			String line = br.readLine();
			while(line != null){
				
				// Resets all variables for each new ramp angle
				t = 0;
				//double O = Oinit;
				double angle = Double.parseDouble(line);
				theta = angle;
//				if(n > 100){
//					System.out.println("Cylinder:");
//					O = angle*PI/180.0;
//				}
				
				double x = 0;
				double y = 0;
				/**
				// Lists keep track of angle phi (O) at each time t
//				List<Double> xaxis = new ArrayList<Double>();
//				List<Double> yaxis = new ArrayList<Double>();
//				xaxis.add(t);
//				yaxis.add(O);

				// Lists keep track of x and y positions of the center of mass
//				List<Double> xValues = new ArrayList<Double>();
//				List<Double> yValues = new ArrayList<Double>();
				
//				xValues.add(x);
//				yValues.add(y);

				// Sets the ramp angle and the initial angular velocity
				theta = angle;
				double dO = 0.00000001;

				// Roll until the necessary distance travelled is reached
				while (x <= length * Math.cos(theta / 180.0 * PI)) {
					// Use Euler's method for each roll
					while (O < theta / 180.0 * PI + PI /n && x <= length * Math.cos(theta / 180.0 * PI)) {

						// Find the angular velocity
						double dO2 = Math.sqrt(2.0 / I
								* (m * g * r * Math.cos(O) - m * g * r * Math.cos(O + dO * dt) + 0.5 * I * dO * dO));
						// Update the angle using the angular velocity
						O = O + dO * dt;
						t += dt;

						// Update time and phi (O)
//						xaxis.add(t);
//						yaxis.add(O);

						

						// Update x and y positions of the center of mass
//						x += r * (Math.sin(O) - Math.sin(O - dO * dt));// Minus
//																		// because
//																		// already
//																		// did
//						y += r * (Math.cos(O) - Math.cos(O - dO * dt));
//						xValues.add(x);
//						yValues.add(y);
//						plotPoints.add(new Point(x, y));
						dO = dO2;
					} // for each time the face hits the board

					// Use Coefficient of restitution
					dO *= beta;
					// Reset initial angle
					O = theta / 180.0 * PI - PI / n;
				} // For each continuous roll down the entire ramp
*/

				t = process(Oinit, m,r, n,beta,length, theta,dt,modelType);
				// Print out info
				System.out.println(angle);
				System.out.println("t: " + t);
				System.out.println("x: " + x + "\n");
				output.println(angle + "," + t);// + "," + O + "," + dO);
				
				
				line = br.readLine();
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
			br.close();
		} // PrintWriter try section
		catch (FileNotFoundException e) {
			// Catch exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
