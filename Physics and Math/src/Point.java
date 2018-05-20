
public class Point {
	public double x;
	public double y;
	
	Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	Point(java.awt.Point point)
	{
		this.x = point.x;
		this.y = point.y;
	}
}
