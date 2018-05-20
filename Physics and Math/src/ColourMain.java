import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

public class ColourMain{

	public ColourMain(){
		JFrame frame = new JFrame("Colourorer");
		frame.setIconImage(Toolkit.getDefaultToolkit()
				.getImage("image.png"));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Set variables
		double x1 = -3;//= -0.00505;
		double x2 = 3;//-0.00500;
		double y1 = -3;//0.01497;
		double y2 = 3;//0.01502;		
		
		//Julia
		//i -0.01,0,0.01,0.02, c=0, d=0.64
		
		ColourLine contentPane = new ColourLine();//x1,x2,y1,y2);
		//Function contentPane = new Function(x1,x2,y1,y2);
		contentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(contentPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setMinimumSize(new Dimension(1500,850));

		// frame should adapt to the panel size
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args){
		new ColourMain();
	}

}
