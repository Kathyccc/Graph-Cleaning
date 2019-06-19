package core;

public class Coordinate{
	
	int X, Y;
		
	public Coordinate(int _x, int _y) {
		X = _x;
		Y = _y;
	}

	//@Override
	public String toString() {
		return this.X + ", " + this.Y;
	}
	
}

