package core;

public class Coordinate{
	
	public int X;
	public int Y;
		
	public Coordinate(int _x, int _y) {
		X = _x;
		Y = _y;
	}

	//@Override
	public String toString() {
		return this.X + ", " + this.Y;
	}
	
}

