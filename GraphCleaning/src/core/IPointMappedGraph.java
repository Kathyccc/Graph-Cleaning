package core;

import java.awt.Point;

public interface IPointMappedGraph extends IGraph{

	Point getPoint(int node);
	
	int corNode(Point point);
	
}
