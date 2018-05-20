/**
 * This Java method uses Euler integration to model the movement of a polygon.
 * It returns a rolling time for a polygon with n sides using either the
 * walking or the running model.
 * @param Oinit the initial angle phi of the polygon
 * @param m the mass of the polygon
 * @param r the distance from a corner to the center of the polygon
 * @param n the total number of sides of the polygon, n >= 3
 * @param e the coefficient of restitution
 * @param length the distance to be traveled along the hypotenuse of the ramp
 * @param angle the inclination angle of the ramp in degrees
 * @param dt the time increment for the Euler integration
 * @return the success boolean
 */
/*
public boolean process(double Oinit, double m, double r, double n, double e, double length, double angle,
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

	// Sets a precise starting angle for the cylinder approximate with large number of sides
	if (n >= 100) {
		System.out.println("Cylinder:");
		O = angle * Math.PI / 180.0;
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

	// Set the horizontal distance traveled
	double totalHorizontalDist = length * Math.cos(theta / 180.0 * Math.PI) - r * Math.sin(O);

	// Roll until the necessary distance traveled is reached
	while (x <= totalHorizontalDist && walking) {
		// Use Euler's method for each roll
		while (O < theta / 180.0 * Math.PI + Math.PI / n && x <= totalHorizontalDist && walking) {

			// Find the angular velocity
			// Update the angle using the angular velocity
			O = O + dO * dt;
			t += dt;

			// Update time and phi (O)
			times.add(t);
			angles.add(O);
			
			// Update x and y positions of the center of mass
			x += r * (Math.sin(O) - Math.sin(O - dO * dt));// Minus because already updated
			y += r * (Math.cos(O) - Math.cos(O - dO * dt));
			xPos.add(x);
			yPos.add(y);
			plotPoints.add(new Point(x, y));
			dO = Math.sqrt(
					2.0 / I * (m * g * r * Math.cos(O) - m * g * r * Math.cos(O + dO * dt) + 0.5 * I * dO * dO));
			angularSpeeds.add(dO);
			
			// Losing contact condition
			if (modelType == 1) {
				walking = dO < Math.sqrt(g * Math.cos(O) / r);
			}
		} // for each time the face hits the board or walking done

		// Use Coefficient of restitution for collision
		if (walking) {
			dO *= e;
			// Reset initial angle
			O = theta / 180.0 * Math.PI - Math.PI / n;
		}

		// Use the running model
		if (modelType == 1 && x < totalHorizontalDist && !walking) {

			// Sets the initial x and y velocity of the center of mass
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

			double VY = Vy0; // Set initial y-velocity for tracing
								// y-coordinate

			// Loop until the polygon falls down to the ramp
			while (!stop && x <= totalHorizontalDist) {
				//For each corner
				for (int corner = 0; corner < n-1 && !stop; corner++) {
					
					// Define h = height of center above ramp
					double h = r * Math.cos(O) + r * Math.sin(O) * Math.tan(theta / 180.0 * Math.PI) + Vy0 * tf
							- 1.0 / 2.0 * g * tf * tf + Vx0 * tf * Math.tan(theta / 180.0 * Math.PI);
					
					mus[corner] += dO*dt;
					// Define L = Distance from horizontal through center to corner
					double L = r * Math.sin(mus[corner]);
					// Define K = Distance from horizontal through bottom of h to ramp, below corner
					double K = r * Math.tan(theta / 180.0 * Math.PI) * Math.cos(mus[corner]);
					Hs[corner] = h - L + K;
					
					//Collision detection
					if(Hs[corner] <= 0){
						if(corner != n ){
						lastCorner = corner;
						stop = true;
						}
					}
				}
				
				//Update times
				tf = tf + dt;
				t += dt;
				times.add(t);

				angles.add(O + dO*tf);// Continuous
				angularSpeeds.add(dO);// Constant Angular Speed
				// trace the x,y coordinates in the air
				x = x + Vx0 * dt;
				y = y + VY * dt;
				VY = VY - g * dt;

				xPos.add(x);
				yPos.add(y);
				plotPoints.add(new Point(x, y));
			} // polygon has just touched the ramp

			// Finds the initial O and dO for the walking model
			double beta = e*e;// edge-on restitution for energy: coefficient's square
			
			// update energy profile after collision
			double Er = (0.5 * m * Vx0 * Vx0 + 0.5 * m * Math.pow((Vy0 - g * tf), 2)
					+ 0.5 * (I - m * r * r) * dO * dO) * beta;
			
			//Converts the mu of the colliding corner to phi
			if(stop == false)
				lastCorner = 0;
			O = mus[lastCorner] - Math.PI/2;//starting phi for
											// gaining contact
			// set initial phi again for the following loop
			dO = Math.sqrt(2 / I * Er); // set initial angular speed

			// Keep going using the running model
			walking = true;
		}
	} //While the prism hasn't reached the total distance yet
	
	System.out.println("time: " + t);
	return true;
}
*/