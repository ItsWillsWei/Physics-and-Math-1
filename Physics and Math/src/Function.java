import java.awt.*;

import javax.swing.*;

/**
 * 
 * @author Will
 *
 */
public class Function extends Graph {

	public final int POINT = 0, LINE = 1;
	public int plotType = LINE;
	public Point[][] thicc = new Point[2][1000];
	/**
	 * Makes a new function with the window sized to the specified size
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 */
	Function(double x1, double x2, double y1, double y2) {
		super(x1,x2,y1,y2);
		calculatePoints();
	}
	
	public void plotLine(){
		plotType = LINE;
	}
	
	public void plotPoint(){
		plotType = POINT;
	}

	public Point[] calculatePoints() {
		for (int increment = 0; increment < points.length; increment++) {
			double x = x1 + dx * increment;
			points[increment] = new Point(x, function(x));
			double r = 0.02;
			double m = -1/(4*Math.cos(2*x));
//			thicc[0][increment] = new Point(x-0.01, function(x)-0.01);
//			thicc[1][increment] = new Point(x+0.01, function(x)+0.01);
			
//			thicc[0][increment] = new Point(x-r/Math.sqrt(1+m),function2(x,x-r/Math.sqrt(1+m)));
//			thicc[1][increment] = new Point(x+r/Math.sqrt(1+m),function2(x,x+r/Math.sqrt(1+m)));
//			drawLine(x-r/Math.sqrt(1+m),function2(x,x-r/Math.sqrt(1+m)),
//					x+r/Math.sqrt(1+m),function2(x,x+r/Math.sqrt(1+m)));
		}
		return points;
	}
	
	public double function(double x) {
		return 2*Math.sin(2*x);
	}
	
	public double function2(double xc, double x){
		return -1/(4*Math.cos(2*xc))*x + xc/(4*Math.cos(2*xc))+function(xc);
	}

	public Dimension getDimension() {
		return dimension;
	}
	
	public void drawLine(Graphics g, double xa, double ya, double xb, double yb){
		g.drawLine((int)((xa-x1)/dx), (int)((y2-ya)/dy), (int)((xb-x1)/dx), (int)((y2-yb)/dy));
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1000, 1000);
		g.setColor(Color.BLACK);
		
		
		g.setColor(new Color(112,48,160));
		
		// Draw the points
		if (plotType == POINT)
			for (int point = 0; point < points.length; point++) {
				g.drawRect(point, (int) ((y2 - points[point].y) / dy), 1, 1);
			}
		else if (plotType == LINE)
			for (int point = 0; point < points.length - 1; point++) {
				g.drawLine(point, (int) ((y2 - points[point].y) / dy), point + 1,
						(int) ((y2 - points[point + 1].y) / dy));
				
				boolean thicker = true;
				if(thicker){
				//Make thicker line
					//double x = x1+point/1000;
					//drawLine(g,thicc[0][point].x,thicc[0][point].y,thicc[1][point].x,thicc[1][point].y);
//				drawLine(g,point, (int) ((y2 - points[point].y) / dy), point,
//						(int) ((y2 - points[point + 1].y) / dy));
					
					
					int lim = 6;
					for(int i = 1; i<=lim && point > lim && point < 1000-lim ; i++){
						g.drawLine(point, i+(int) ((y2 - points[point].y) / dy), point + i,
								i+(int) ((y2 - points[point + i].y) / dy));
						g.drawLine(point, -i+(int) ((y2 - points[point].y) / dy), point + i,
								-i+(int) ((y2 - points[point + i].y) / dy));
						g.drawLine(i+point, (int) ((y2 - points[point].y) / dy), i+point + i,
								(int) ((y2 - points[point + i].y) / dy));
						g.drawLine(-i+point, (int) ((y2 - points[point].y) / dy), -i+point + i,
								(int) ((y2 - points[point + i].y) / dy));
					}
					
					
//				g.drawLine(point, 1+(int) ((y2 - points[point].y) / dy), point + 1,
//						1+(int) ((y2 - points[point + 1].y) / dy));
//				g.drawLine(point, -1+(int) ((y2 - points[point].y) / dy), point + 1,
//						-1+(int) ((y2 - points[point + 1].y) / dy));
//				g.drawLine(point, 2+(int) ((y2 - points[point].y) / dy), point + 1,
//						2+(int) ((y2 - points[point + 1].y) / dy));
//				g.drawLine(point, -2+(int) ((y2 - points[point].y) / dy), point + 1,
//						-2+(int) ((y2 - points[point + 1].y) / dy));
//				
//				//Horizontal
//				g.drawLine(1+point, (int) ((y2 - points[point].y) / dy), 1+point + 1,
//						(int) ((y2 - points[point + 1].y) / dy));
//				g.drawLine(-1+point, (int) ((y2 - points[point].y) / dy), -1+point + 1,
//						(int) ((y2 - points[point + 1].y) / dy));
//				g.drawLine(2+point, (int) ((y2 - points[point].y) / dy), 2+point + 1,
//						(int) ((y2 - points[point + 1].y) / dy));
//				g.drawLine(-2+point, (int) ((y2 - points[point].y) / dy), -2+point + 1,
//						(int) ((y2 - points[point + 1].y) / dy));
				}
			}

		boolean drawAxes = false;
		if(drawAxes){
		//Draw the axes
		//Draw y axis
		g.drawLine((int)(-x1/dx), 0, (int)(-x1/dx), dimension.height);
		//Draw x axis
		g.drawLine(0, (int)(y2/dy), dimension.width, (int)(y2/dy));
		}
	}
}
