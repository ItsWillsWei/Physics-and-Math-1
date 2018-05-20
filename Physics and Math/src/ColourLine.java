import java.awt.*;

import javax.swing.*;

public class ColourLine extends JPanel {
	public ColourLine() {
		super();
		repaint(0);
	}
	
	public double rX1(double x){
		
		//return Math.exp(-1*Math.pow(3*(x-0.25),2));
			return Math.pow(Math.cos(2*Math.PI/3.0*(x-0.25)), 2);
		
//		if (x > 0 && x < 0.5)
//			return -16*(x)*(x-0.5);
//		else if(x > 0.75 && x <=1)
//			return 16*(x-0.75)*(x-0.75);
//		else return 0;
	}
	
	public double gX1(double x){
		
		//return Math.exp(-1*Math.pow(3*(x-0.5),2));
			return Math.pow(Math.cos(Math.PI*(x-0.5)), 2);

		
		//		if (x > 0.25 && x < 0.75)
//			return -16*(x-0.25)*(x-0.75);
//		else if(x>0.75 && x <=1)
//			return 16*(x-0.75)*(x-0.75);
//		else return 0;
	}
	
	public double bX1(double x){
		if(x<0.75)
			return Math.pow(Math.cos(Math.PI*(x-0.75)), 2);
		else
			return Math.pow(Math.cos(2*Math.PI*(x-0.75)), 2);
		//return Math.exp(-1*Math.pow(3*(x-0.75),2));
		
//		if (x > 0.5 && x < 0.75)
//			return -16*(x-0.5)*(x-1);
//		else if (x >= 0.75 && x <= 1)
//			return 1;
//		else return 0;
	}
	
	public double rX(double x){
		if(x<0.5)
			return 0.25*Math.pow(Math.sin(Math.PI*x), 2)+0.5;
		else if(x<0.75)
			return 0.25*Math.pow(Math.sin(2*Math.PI*(x-0.5)), 2)+0.75;
		else return 1;
	}
	
	public double gX(double x){
		if(x<0.5)
			return 0.3*Math.pow(Math.sin(Math.PI*x), 2);
		else if(x<0.625)
			return 0.2*Math.pow(Math.sin(4*Math.PI*(x-0.5)), 2)+0.3;
		else if(x<0.75)
			return 0.5*Math.pow(Math.sin(4*Math.PI*(x-0.625)), 2)+0.5;
		return 1;
	}
	
	public double bX(double x){
		if(x<0.75)
			return 0;
		return Math.pow(Math.sin(2*Math.PI*(x-0.75)), 2);
	}
	
	public double gradient(double x){
		if(x < 0.9)
			return 1;
		else
			return 1;//6000*(1.0/3.0*x*x*x-0.95*x*x+0.9*x)-1700;
	}
	
	/**
	 * Generates a new Color based on a value between 0 and 1
	 * @param x the value between 0 and 1
	 * @return the new color
	 */
	public Color getColor(double x){
		double bright = Math.sqrt(gradient(x));
		return new Color((int)(bright*255*rX(x)),(int)(bright*255*gX(x)),(int)(bright*255*bX(x)));
	}

	public void paintComponent(Graphics g) {
		int limit = 1000;
		//Red lines
		for (int i = 0; i < limit; i++) {
			double j = i/(double)limit;//Fraction from 0 to 1
			g.setColor(new Color((int)(255*rX(j)),(int)(255*j*0),(int)(255*j*0)));
			g.drawLine(i, 20, i, 200);
		}
		
		//Green Lines
		for (int i = 0; i < limit; i++) {
			double j = i/(double)limit;
			g.setColor(new Color((int)(255*j*0),(int)(255*gX(j)),(int)(255*j*0)));
			g.drawLine(i, 220, i, 400);
		}
		
		//Blue Lines
		for (int i = 0; i < limit; i++) {
			double j = i/(double)limit;
			g.setColor(new Color((int)(255*0),(int)(255*j*0),(int)(255*bX(j))));
			g.drawLine(i, 420, i, 600);
		}
		
		//All lines
		for (int i = 0; i < limit; i++) {
			double j = i/(double)limit;
			//g.setColor(new Color((int)(255*rX(j)*j),(int)(255*gX(j)*j),(int)(255*bX(j)*j)));
			g.setColor(getColor(j));
			g.drawLine(i, 620, i, 800);
		}
	}
}
