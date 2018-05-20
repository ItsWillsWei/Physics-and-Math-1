import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

/**
 * 
 * @author Will
 * @version 06/03/2016
 */
public class Bezier extends JFrame {

	private static Visual display;

	Bezier() {
		super("Bezier");
		display = new Visual();
		setContentPane(display);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}

	public static void main(String[] args) {
		new Bezier().setVisible(true);
	}

	static class Visual extends JPanel {
		private JButton go, delete;
		private JTextField time;
		private JPanel buttons;
		private Board main;

		Visual() {
			setLayout(new BorderLayout());
			makeMain();
			makeButtons();
			setPreferredSize(new Dimension(1024, 768));
		}

		void makeMain() {
			main = new Board();
			this.add(main, BorderLayout.CENTER);
		}

		void makeButtons() {
			buttons = new JPanel();
			buttons.setLayout(new GridLayout(1, 3));
			time = new JTextField("5000");
			buttons.add(time);
			
			go = new JButton("Go");
			go.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Drawing Bezier Curve");
					main.setTime(time.getText());
					main.drawCurve();
				}
			});
			buttons.add(go);

			delete = new JButton("Delete All");
			delete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Delete");
					main.clearPoints();
					repaint(0);
				}
			});
			buttons.add(delete);

			this.add(buttons, BorderLayout.SOUTH);
		}
	}

	static class Board extends JPanel {
		private ArrayList<Point> points, finalCurve;
		private Color[] COLORS = { Color.RED, Color.ORANGE, Color.YELLOW,
				Color.GREEN, Color.BLUE, Color.MAGENTA.darker() };
		private double time;
		private long start;
		private boolean drawCurve;

		Board() {
			time = 5000;
			points = new ArrayList<Point>();
			finalCurve = new ArrayList<Point>();
			drawCurve = false;
			// Adds points within bounds
			this.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					System.out.println("Clicked");
					if (e.getX() >= 0 && e.getX() <= 1000 && e.getY() >= 0
							&& e.getY() <= 700) {
						points.add(new Point(e.getPoint()));
						System.out.println(e.getX() + " " + e.getY());
						repaint(0);
					}
				}
			});
		}
		
		void setTime(String time)
		{
			try{
				this.time = Double.parseDouble(time);
			}catch(Exception e){
				this.time = 5000;
				JOptionPane.showMessageDialog(null, "Invalid time. Time delay set to 5000 ms!", "Warning!", JOptionPane.WARNING_MESSAGE);
			}
		}

		void drawCurve() {
			if(points.size() < 2)
				JOptionPane.showMessageDialog(null, "You must have at least 2 points before drawing!", "Warning!", JOptionPane.WARNING_MESSAGE);
			else
			{
				finalCurve.clear();
				drawCurve = true;
				start = System.currentTimeMillis();
				repaint(0);
			}
		}
		
		void clearPoints(){
			if(!drawCurve)
			{
				points.clear();
				finalCurve.clear();
			}
			else
				JOptionPane.showMessageDialog(null, "You cannot delete points in the middle of drawing!", "Warning!", JOptionPane.WARNING_MESSAGE);
		}

		// Paints the points
		public void paintComponent(Graphics g) {

			super.paintComponent(g);
			g.setColor(Color.BLACK);
			for (int point = 0; point < points.size(); point++) {
				Point curr = points.get(point);
				g.fillOval((int)curr.x - 4, (int)curr.y - 4, 9, 10);
				if(point == 0)
					g.setColor(Color.GRAY);
				else
				{
					Point last = points.get(point - 1);
					g.drawLine((int)last.x, (int)last.y, (int)curr.x, (int)curr.y);
				}
			}

			if (drawCurve) {
				/*
				Point a = points.get(0), b = points.get(1), c = points.get(2), d = points.get(3);
				Point first1 = new Point(a.x, a.y), first2 = new Point(b.x, b.y), first3 = new Point(c.x, c.y),
						second1 = new Point(a.x, a.y), second2 = new Point(b.x, b.y),
						third1 = new Point(a.x, a.y);
						*/
				// long start = System.currentTimeMillis();
				double timeRatio = (System.currentTimeMillis() - start) / time;
				
				
				if (timeRatio > 1)
				{
					drawCurve = false;
				}
				else {
					finalCurve.add(draw(g, points, points.size()-1, timeRatio, 0));
					repaint(0);
				}
				for(Point point: finalCurve)
					g.fillOval((int)point.x, (int)point.y, 5, 5);
				/*//QUADRATIC
				first1.x = (int) (a.x + (b.x - a.x) * timeRatio)-2;
				first1.y = (int) (a.y + (b.y - a.y) * timeRatio)-2;
				g.fillOval(first1.x, first1.y, 5, 5);
				
				first2.x = (int) (b.x + (c.x - b.x) * timeRatio)-2;
				first2.y = (int) (b.y + (c.y - b.y) * timeRatio)-2;
				g.fillOval(first2.x, first2.y, 5, 5);
				
				g.drawLine(first1.x+2, first1.y+2, first2.x+2, first2.y+2);
				
				second1.x = (int) (first1.x + (first2.x-first1.x)*timeRatio);
				second1.y = (int) (first1.y + (first2.y-first1.y) *timeRatio);
				g.fillOval(second1.x , second1.y , 5, 5);
				*/
				/*
				//CUBIC
				first1.x = (int) (a.x + (b.x - a.x) * timeRatio);
				first1.y = (int) (a.y + (b.y - a.y) * timeRatio);
				g.fillOval(first1.x, first1.y, 5, 5);
				
				first2.x = (int) (b.x + (c.x - b.x) * timeRatio);
				first2.y = (int) (b.y + (c.y - b.y) * timeRatio);
				g.fillOval(first2.x, first2.y, 5, 5);
				
				first3.x = (int) (c.x + (d.x - c.x) * timeRatio);
				first3.y = (int) (c.y + (d.y - c.y) * timeRatio);
				g.fillOval(first3.x, first3.y, 5, 5);
				
				g.drawLine(first1.x, first1.y, first2.x, first2.y);
				g.drawLine(first2.x, first2.y, first3.x, first3.y);
				
				second1.x = (int) (first1.x + (first2.x-first1.x)*timeRatio);
				second1.y = (int) (first1.y + (first2.y-first1.y) *timeRatio);
				g.fillOval(second1.x , second1.y , 5, 5);
				
				second2.x = (int) (first2.x + (first3.x-first2.x)*timeRatio);
				second2.y = (int) (first2.y + (first3.y-first2.y) *timeRatio);
				g.fillOval(second2.x , second2.y , 5, 5);
				
				g.drawLine(second1.x, second1.y, second2.x, second2.y);
				
				third1.x = (int)(second1.x + (second2.x-second1.x)*timeRatio);
				third1.y = (int)(second1.y + (second2.y-second1.y)*timeRatio);
				g.fillOval(third1.x, third1.y, 5, 5);
				*/
				//finalCurve.add(third1);
				//g.drawLine(a.x, a.y, first1.x, first1.y);

				/*
				 * try { Thread.sleep(10);
				 * 
				 * } catch (InterruptedException e1) { e1.printStackTrace(); }
				 */

			}
		}
		
		public Point draw(Graphics g, ArrayList<Point> points, int end, double timeRatio, int color)
		{
			g.setColor(COLORS[color++]);
			ArrayList<Point> newPoints = new ArrayList<Point>();
			for(int point = 0; point < end; point++)
			{
				Point first = points.get(point);
				Point middle = points.get(point+1);
				double firstx = first.x + (middle.x-first.x)*timeRatio;
				double firsty = first.y + (middle.y-first.y)*timeRatio;
				newPoints.add(new Point(firstx, firsty));
				g.fillOval((int)firstx, (int)firsty, 5, 5);
			}
			end--;
			for(int point = 0; point < end; point++)
			{
				Point newFirst = newPoints.get(point);
				Point newMiddle = newPoints.get(point+1);
				g.drawLine((int)newFirst.x, (int)newFirst.y, (int)newMiddle.x, (int)newMiddle.y);
			}
			if(end > 0)
				return draw(g, newPoints, end, timeRatio, color%6);
			return newPoints.get(0);
			//End if only last point to draw
		}
	}

}
