import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordData {

	public static void main(String[] args) {

		// #Define and initialize constants
		double Oinit = 5.0 * Math.PI / 180.0; // Start at 5 degrees
		double t;
		double m = 0.0252;
		double g = 9.81;
		double r = 0.017334722741;
		double theta;
		double I = 4.0 / 3 * m * r * r;
		double beta = 0.4245;//0.65926745;//0.43369;

		//double side = r * Math.sqrt(2.0);
		double length = 0.279;
		//double times = length / side;
		
		boolean run = true;

		int lAngle = 23;
		int hAngle = 33;
		// #Define and initialize time variables
		double dt = 0.00001;
		final double PI = 3.1415926535897932384626433832795029;

		// Setup writer to CSV spreadsheet
		try {
			PrintWriter output = new PrintWriter(new File("Data.csv"));

			// Print the date
			output.print("Date,");
			output.println(new Date().toString());
			output.println();
			output.println("Angle,Time,Theta,dTheta");
			for (int angle = lAngle; angle <= hAngle; angle++) {
				t = 0;
				double O = Oinit;
				List<Double> xaxis = new ArrayList<Double>();
				List<Double> yaxis = new ArrayList<Double>();
				xaxis.add(t);
				yaxis.add(O);
				
				List<Double> xValues = new ArrayList<Double>();
				List<Double> yValues = new ArrayList<Double>();
				double x = 0;
				double y = 0;
				xValues.add(x);
				yValues.add(y);
				
				theta = angle;
				
				double Ei = m * g * r * Math.sqrt(2) * Math.sin(theta / 180.0 * PI);
				////double dO = 0.000001;
				// #Calculate each time step for the first roll starting from
				// equilibrium
				while (O < theta / 180.0 * PI + PI / 4) {
					double dO = Math.sqrt(2 * m * g * r * (1 - Math.cos(O)) / (I));
					////dO = Math.sqrt(2.0/I*(m*g*r*Math.cos(O)-m*g*r*Math.cos(O+dO*dt)+0.5*I*dO*dO));
					O = O + dO * dt;
					t += dt;
					xaxis.add(t);
					yaxis.add(O);
					x += r*(Math.sin(O)-Math.sin(O-dO*dt));//Minus because already did
					y += r*(Math.cos(O)-Math.cos(O-dO*dt));
					xValues.add(x);
					yValues.add(y);
					System.out.println(t);
				}
				// #Set initial energy
				double E = beta * m * g * r * (1 - Math.cos(theta / 180.0 * PI + PI / 4));
				System.out.println("a");
				double count = 1;
				////dO *= beta;
				double dO = 0;
				// #Number of spins
				// #while count <= 6:
				//while (run) 
				while(x <= length * Math.cos(theta/180.0*PI)){
					// #Reset initial angle
					O = theta / 180.0 * PI - PI / 4;
					
					System.out.println(count);
					
					// #Use Euler's method
					while (O < theta / 180.0 * PI + PI / 4 && x <= length * Math.cos(theta/180.0*PI)) {
						dO = Math.sqrt(
								2.0 / I * (E + m * g * r * (Math.cos(theta / 180.0 * PI - PI / 4) - Math.cos(O))));
						// dO = math.sqrt(2*(E + Ei -
						// m*g*r*(math.cos(O)-math.sin(PI/4 -
						// theta/180.0*PI)))/(I));
						////dO = Math.sqrt(2.0/I*(m*g*r*Math.cos(O)-m*g*r*Math.cos(O+dO*dt)+0.5*I*dO*dO));
						O = O + dO * dt;
						t += dt;
						xaxis.add(t);
						yaxis.add(O);
						x += r*(Math.sin(O)-Math.sin(O-dO*dt));//Minus because already did
						y += r*(Math.cos(O)-Math.cos(O-dO*dt));
						xValues.add(x);
						yValues.add(y);
						//run = true;//dO < Math.sqrt(g / r * Math.cos(O));
					}
					// #Set Energy
					//E = beta*(E+m*g*r*math.sqrt(2)*math.sin(theta/180.0*PI))
					E = (E + Ei) * beta;
					//count++;
				}
				System.out.println("t: "+t);
				System.out.println("x: " + x);
				output.println(angle+","+t+","+O+","+dO);
			}
			
			/*
			 * #Plot, label, and show the graphs plt.plot(xaxis, yaxis, "g-")
			 * plt.title("Angle") plt.xlabel("Time (s)")
			 * plt.ylabel("Angle (Theta)") plt.grid(True) plt.show()
			 */
			System.out.println("Done"+"\n"+Math.random());
			output.close();
		} // PrintWriter try section
		catch (FileNotFoundException e) {
			// Catch exception
			e.printStackTrace();
		}
	}

}
