
//Import necessary packages
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class PhysicsFrame extends JPanel {
	// Declare and initialize Global variables
	public final Dimension DIMENSION = new Dimension(1000, 1000);
	public final Dimension FIELD = new Dimension(100, 20);
	public final Dimension STRETCH = new Dimension(120,20);
	public final String[][] PARAMETERS = {{"-0.1","1","-2","2"},{"-0.1","1.0","-10","100"},{"-0.05","0.25","-0.25","0.05"}};
	public PhysicsArea area;
	public boolean finishedProcessing = false, animating = false;
	public List<JPanel> panels = new ArrayList<JPanel>();
	public List<Graph> graphs = new ArrayList<Graph>();
	public JPanel main;
	public int modellingType = 0;

	public PhysicsFrame() {
		// Create a new JPanel that holds all tabs
		super(new GridLayout(1, 0));
		// this.setBackground(new Color(200,255,255));
		// setPreferredSize(SIZE);

		// Sets up the animation panel
		setUp();

		// Sets up the other graph panels
		createPanels();

		// Adds all panels as tabs
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Animation", main);

		for (int panel = 0; panel < panels.size(); panel++) {
			tabs.addTab(panels.get(panel).getName(), null, panels.get(panel));
		}

		customizeTabs(tabs);
		add(tabs);
		this.setVisible(true);
	}

	public void customizeTabs(JTabbedPane tabs) {
		tabs.setSelectedIndex(0);
	}

	public void createPanels() {
		JPanel thetaTime = new JPanel();
		thetaTime.setName("Angle vs Time");

		JPanel omegaTime = new JPanel();
		omegaTime.setName("Angular Speed vs Time");

		JPanel xy = new JPanel();
		xy.setName("Position Graph");

		// linear velocity vs time
		// x vs time

		panels.add(thetaTime);
		panels.add(omegaTime);
		panels.add(xy);

		// Add settings common to all graphs
		for (int panel = 0; panel < panels.size(); panel++) {
			JPanel current = panels.get(panel);
			current.setLayout(new GridBagLayout());

			GridBagConstraints constraints = new GridBagConstraints();

			// Adds the graph
			Graph graph = new Graph(Double.parseDouble(PARAMETERS[panel][0]), Double.parseDouble(PARAMETERS[panel][1]), Double.parseDouble(PARAMETERS[panel][2]), Double.parseDouble(PARAMETERS[panel][3]));

			// Adds graph
			graph.setPreferredSize(DIMENSION);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.PAGE_START;
			// int temp = constraints.anchor;
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.gridheight = 7;
			current.add(graph, constraints);
			graphs.add(graph);

			// Adds some labels
			JLabel label = new JLabel("Parameters: ");
			label.setPreferredSize(STRETCH);
			constraints.fill = GridBagConstraints.NONE;
			// constraints.anchor = temp;
			constraints.gridx = 1;
			constraints.gridy = 0;
			constraints.gridheight = 1;
			current.add(label, constraints);

			JLabel label2 = new JLabel(" ");
			label2.setPreferredSize(STRETCH);
			constraints.fill = GridBagConstraints.NONE;
			constraints.gridx = 2;
			constraints.gridy = 0;
			constraints.gridheight = 1;
			current.add(label2, constraints);

			// Adds Labels and TextFields for parameters
			JLabel x1Label = new JLabel("x1: ");
			JTextField x1Text = new JTextField(PARAMETERS[panel][0]);
			JLabel x2Label = new JLabel("x2: ");
			JTextField x2Text = new JTextField(PARAMETERS[panel][1]);
			JLabel y1Label = new JLabel("y1: ");
			JTextField y1Text = new JTextField(PARAMETERS[panel][2]);
			JLabel y2Label = new JLabel("y2: ");
			JTextField y2Text = new JTextField(PARAMETERS[panel][3]);

			// Add graph size labels
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 1;
			constraints.gridy = 1;
			constraints.gridheight = 1;
			current.add(x1Label, constraints);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 2;
			constraints.gridy = 1;
			constraints.gridheight = 1;
			current.add(x1Text, constraints);

			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 1;
			constraints.gridy = 2;
			constraints.gridheight = 1;
			current.add(x2Label, constraints);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 2;
			constraints.gridy = 2;
			constraints.gridheight = 1;
			current.add(x2Text, constraints);

			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 1;
			constraints.gridy = 3;
			constraints.gridheight = 1;
			current.add(y1Label, constraints);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 2;
			constraints.gridy = 3;
			constraints.gridheight = 1;
			current.add(y1Text, constraints);

			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 1;
			constraints.gridy = 4;
			constraints.gridheight = 1;
			current.add(y2Label, constraints);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 2;
			constraints.gridy = 4;
			constraints.gridheight = 1;
			current.add(y2Text, constraints);

			// Resize
			JButton resize = new JButton("Resize!");
			resize.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					// Set the graph window size
					graph.setValues(Double.parseDouble(x1Text.getText()), //
							Double.parseDouble(x2Text.getText()), //
							Double.parseDouble(y1Text.getText()), //
							Double.parseDouble(y2Text.getText()));//
					graph.repaint();
				}

			});
			
			graph.repaint();

			// Adds the button
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 1;
			constraints.gridy = 5;
			constraints.gridwidth = 2;
			current.add(resize, constraints);
			
			JLabel space = new JLabel(" ");
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 1;
			constraints.gridy = 6;
			constraints.gridwidth = 2;
			current.add(space, constraints);

		}

	}

	/**
	 * Sets up the frame with buttons on the right and the display on the left
	 * middle
	 */
	public void setUp() {
		main = new JPanel();
		main.setLayout(new GridBagLayout());
		// Creates a Graph area to display the animation
		area = new PhysicsArea(-0.1, 0.3, -0.3, 0.1);
		// JButton a = new JButton("A");

		// Adds Labels and TextFields for parameters
		JLabel x1Label = new JLabel("x1: ");
		JTextField x1Text = new JTextField("-0.1");
		JLabel x2Label = new JLabel("x2: ");
		JTextField x2Text = new JTextField("0.3");
		JLabel y1Label = new JLabel("y1: ");
		JTextField y1Text = new JTextField("-0.3");
		JLabel y2Label = new JLabel("y2: ");
		JTextField y2Text = new JTextField("0.1");

		JLabel OinitLabel = new JLabel("Initial \u03D5 (rad): ");
		JTextField OinitText = new JTextField("0.085");
		JLabel massLabel = new JLabel("Mass (kg): ");
		JTextField massText = new JTextField("0.0252");
		JLabel radiusLabel = new JLabel("Radius (m): ");
		JTextField radiusText = new JTextField("0.01733");
		JLabel sidesLabel = new JLabel("Number of sides: ");
		JTextField sidesText = new JTextField("4");
		JLabel betaLabel = new JLabel("\u03B2: ");
		JTextField betaText = new JTextField("0.433");
		JLabel rampLabel = new JLabel("Ramp length (m): ");
		JTextField rampText = new JTextField("0.279");
		JLabel rampAngleLabel = new JLabel("Ramp Angle \u03B8 (deg): ");
		JTextField rampAngleText = new JTextField("33");
		JLabel dtLabel = new JLabel("dt: ");
		JTextField dtText = new JTextField("0.000001");

		JRadioButton walking = new JRadioButton("Walking");
		walking.setSelected(true);
		walking.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				modellingType = 0;
			}
		});

		JRadioButton running = new JRadioButton("Running");
		running.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				modellingType = 1;
			}
		});

		ButtonGroup modelType = new ButtonGroup();
		modelType.add(walking);
		modelType.add(running);

		JLabel animSpeedLabel = new JLabel("Animation Speed:");
		
		JTextField speedText = new JTextField("10");
		speedText.setHorizontalAlignment(JTextField.RIGHT);
		
		JLabel speedLabel = new JLabel("x slower");
		
		JButton process = new JButton("Process");
		JButton animate = new JButton("Animate!");
		animate.setEnabled(false);

		process.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// Stop the animation if it is already started
				if (!area.isFinished()) {
					area.stopAnimating();
					animate.setText("Animate");

					// Allow 10 milliseconds for the animation to stop
					try {
						Thread.sleep(10);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				// Set the graph window size
				area.updateGraph(Double.parseDouble(x1Text.getText()), //
						Double.parseDouble(x2Text.getText()), //
						Double.parseDouble(y1Text.getText()), //
						Double.parseDouble(y2Text.getText()));//

				// Display processing info while the information is processing

				process.setText("Processing");
				process.setEnabled(false);
				area.setModel(modellingType);
				area.process(// Process information and parse Strings
						Double.parseDouble(OinitText.getText()), // Initial
																	// phi
						Double.parseDouble(massText.getText()), // mass
						Double.parseDouble(radiusText.getText()), // radius
						Double.parseDouble(sidesText.getText()), // number
																	// of
																	// sides
						Double.parseDouble(betaText.getText()), // coefficient
																// of
																// restitution
						Double.parseDouble(rampText.getText()), // ramp
																// length
						Double.parseDouble(rampAngleText.getText()), // ramp
																		// angle
																		// in
																		// degrees
						Double.parseDouble(dtText.getText()));// time
																// increment
				// Set up other graphs
				graphPanels();

				// Allow for re-processing
				process.setText("Re-process");
				process.setEnabled(true);
				// Enable animation
				animate.setEnabled(true);

			}
		});

		animate.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// Stop the animation
				if (!area.isFinished()) {
					area.stopAnimating();
					animate.setText("Animate");
				} else {
					// Animate
					animate.setText("Start/Stop");
					// animate.setEnabled(false);
					process.setText("Animating!!!");
					process.setEnabled(false);
					// area.requestFocusInWindow();
					area.startTime(Double.parseDouble(speedText.getText()));
					// area.requestFocusInWindow();
					// animating = false;

					/// animate.setText("Animate!");
					// animate.setEnabled(true);
					process.setText("Re-process");
					process.setEnabled(true);
					// finishedProcessing = false;
				}

			}
		});

		// c.setEnabled(false);
		JLabel space1 = new JLabel("Parameters:");
		JLabel space2 = new JLabel(" ");
		JLabel space3 = new JLabel(" ");

		GridBagConstraints constraints = new GridBagConstraints();

		// Adds the graph
		area.setPreferredSize(DIMENSION);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 19;
		main.add(area, constraints);

		space1.setPreferredSize(STRETCH);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		main.add(space1, constraints);
		space2.setPreferredSize(STRETCH);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		main.add(space2, constraints);
		
		
		// Add graph size labels		

		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridheight = 1;
		main.add(x1Label, constraints);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridheight = 1;
		main.add(x1Text, constraints);
		
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridheight = 1;
		main.add(x2Label, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridheight = 1;
		main.add(x2Text, constraints);

		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridheight = 1;
		main.add(y1Label, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 2;
		constraints.gridy = 3;
		constraints.gridheight = 1;
		main.add(y1Text, constraints);

		
		// Adds Oinit label
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridheight = 1;
		main.add(y2Label, constraints);
		
		// Adds Oinit field
		//OinitText.setPreferredSize(FIELD);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 2;
		constraints.gridy = 4;
		constraints.gridheight = 1;
		main.add(y2Text, constraints);

		
		// Adds mass label
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridheight = 1;
		main.add(OinitLabel, constraints);
		
		// Adds mass field
		///massText.setPreferredSize(FIELD);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 2;
		constraints.gridy = 5;
		constraints.gridheight = 1;
		main.add(OinitText, constraints);

		
		// Adds radius label
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 6;
		constraints.gridheight = 1;
		main.add(massLabel, constraints);
		
		// Adds radius field
		//radiusText.setPreferredSize(FIELD);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 2;
		constraints.gridy = 6;
		constraints.gridheight = 1;
		main.add(massText, constraints);

		
		// Adds sides label
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 7;
		constraints.gridheight = 1;
		main.add(radiusLabel, constraints);
		
		// Adds sides field
		//sidesText.setPreferredSize(FIELD);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 2;
		constraints.gridy = 7;
		constraints.gridheight = 1;
		main.add(radiusText, constraints);

		
		// Adds beta label
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 8;
		constraints.gridheight = 1;
		main.add(sidesLabel, constraints);
		
		// Adds beta field
		//betaText.setPreferredSize(FIELD);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 2;
		constraints.gridy = 8;
		constraints.gridheight = 1;
		main.add(sidesText, constraints);

		
		
		// Adds ramp label
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 9;
		constraints.gridheight = 1;
		main.add(betaLabel, constraints);
		
		// Adds ramp field
		rampText.setPreferredSize(STRETCH);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 2;
		constraints.gridy = 9;
		constraints.gridheight = 1;
		main.add(betaText, constraints);

		
		// Adds ramp angle label
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 10;
		constraints.gridheight = 1;
		main.add(rampLabel, constraints);
		
		// Adds ramp angle field
		//rampAngleText.setPreferredSize(FIELD);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 2;
		constraints.gridy = 10;
		constraints.gridheight = 1;
		main.add(rampText, constraints);

		
		// Adds dt label
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 11;
		constraints.gridheight = 1;
		main.add(rampAngleLabel, constraints);
		
		// Adds dt field
		//dtText.setPreferredSize(FIELD);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 2;
		constraints.gridy = 11;
		constraints.gridheight = 1;
		main.add(rampAngleText, constraints);

		//dt label
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 12;
		constraints.gridheight = 1;
		main.add(dtLabel, constraints);
		
		//dt text
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 2;
		constraints.gridy = 12;
		constraints.gridheight = 1;
		main.add(dtText, constraints);

		//Adds the radiobuttons for model type
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 13;
		constraints.gridwidth = 1;
		main.add(walking, constraints);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 2;
		constraints.gridy = 13;
		constraints.gridwidth = 1;
		main.add(running, constraints);
		
		// Adds start/stop button
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 14;
		constraints.gridwidth = 2;
		main.add(process, constraints);

		// Adds animate button
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 15;
		constraints.gridwidth = 2;
		main.add(animate, constraints);

		//Animation speed label
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 16;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		main.add(animSpeedLabel, constraints);
		
		//Speed text
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 17;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		main.add(speedText, constraints);
		
		//Speed label
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 2;
		constraints.gridy = 17;
		constraints.gridheight = 1;
		main.add(speedLabel, constraints);

		//Space
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 18;
		main.add(space3, constraints);

	}

	public void graphPanels() {
		for (int graph = 0; graph < graphs.size(); graph++) {
			graphs.get(graph).dataPoints = new ArrayList<Point>();
		}

		for (int element = 0; element < area.times.size(); element++) {
			graphs.get(0).dataPoints.add(new Point(area.times.get(element), area.angles.get(element)));
			graphs.get(1).dataPoints.add(new Point(area.times.get(element), area.angularSpeeds.get(element)));
			graphs.get(2).dataPoints.add(new Point(area.xPos.get(element), area.yPos.get(element)));
		}
		
		for (int graph = 0; graph < graphs.size(); graph++) {
			graphs.get(graph).repaint();
		}
	}

	public Dimension getDimension() {
		return DIMENSION;
	}

}
