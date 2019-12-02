package core;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridGraph implements IPointMappedGraph{
	
	//fields of GridGraph
	DirectedGraph _graph = new DirectedGraph();
	
	public Map<Integer, Coordinate> _coordinate = new HashMap<>();
	
	Coordinate _scale;
	
	public Coordinate MaxCoordinate;
	
	public Coordinate MinCoordinate;
	
	
	public GridGraph() {
		_graph = new DirectedGraph();
		_coordinate = new HashMap<>();
	}
	
	
	public int GetNode(Coordinate coordinate) {
		int i = coordinate.X - MinCoordinate.X;
		int j = coordinate.Y - MinCoordinate.Y;
		
		return (_scale.X+1) * i + j;
	}
	
	public int GetNode(int x, int y) {
		return GetNode(new Coordinate(x,y));
	}
	
	public GridGraph(Coordinate max, Coordinate min) {
		MaxCoordinate = max;
		MinCoordinate = min;
		
		_scale = new Coordinate(max.X - min.X, max.Y - min.Y);

		
		int n = 0;
		
		//generate nodes in the graph
		for(int i = min.X; i <= max.X; i++) {
			for(int j = min.Y; j <= max.Y; j++) {
				_graph.AddNode(n);
				_coordinate.put(n, new Coordinate(i,j));
				n++;
			}
		}
		
		//set the edges
		for(int i = min.X; i <= max.X; i++) {
			for(int j = min.Y; j <= max.Y; j++) 
			{
				if(i != max.X) AddEdge(GetNode(i,j), GetNode(i+1, j));
				if(i != min.X) AddEdge(GetNode(i,j), GetNode(i-1, j));
				if(j != max.Y) AddEdge(GetNode(i,j), GetNode(i, j+1));
				if(j != min.Y) AddEdge(GetNode(i,j), GetNode(i, j-1));
			}
		}
	}
	
	
	public Coordinate getCoordinate(int node) {
		return _coordinate.get(node);
	}
	
	//implement IPointMappedGraph
	
	public Point getPoint(int node) {
		Coordinate coo = _coordinate.get(node);
		return new Point(coo.X,coo.Y);
	}
	
	
	public int corNode(Point point) {
		int i = (int)point.x - MinCoordinate.X;
		int j = (int)point.y - MinCoordinate.Y;
		
		return (_scale.X + 1) * i + j;
 	}
	
	//implement IGraph
	
	public boolean IsDirected() {
		return _graph.IsDirected();
	}
	

	
	public List<Integer> getNodes(){
		return _graph.getNodes();
	}
	

	
	public int getNumOfNodes() {
		return _graph.getNumOfNodes();
	}
	
	
	public void AddNode(int node) {
		throw new IllegalStateException("This object does not support this method.");
	}
	
	
	public boolean  RemoveNode(int node) {
		throw new IllegalStateException("This object does not support this method");
	}
	
	
	public boolean ContainsNode(int node) {
		return _graph.ContainsNode(node);
	}
	
	
	public void AddEdge(int start, int end) {
		Coordinate start_coordinate = getCoordinate(start);
		Coordinate end_coordinate = getCoordinate(end);
		
		int x = start_coordinate.X - end_coordinate.X;
		int y = start_coordinate.Y - end_coordinate.Y;
		
		if((x*x + y*y) != 1) throw new IllegalArgumentException("This edge is invalid.");
		
		_graph.AddEdge(start, end, 1);
	}
	
	
	public boolean RemoveEdge(int start, int end) {
		return _graph.RemoveEdge(start, end);
	}
	
	
	public boolean ContainsEdge(int start, int end) {
		return _graph.ContainsEdge(start, end);
	}
	
	
	public List<Integer> getNeighbors(int node){
		return _graph.getNeighbors(node);
	}
	
	
	public List<Integer> getParentNodes(int node){
		return _graph.getParentNodes(node);
	}
	
	
	public List<Integer> getChildrenNodes(int node){
		return _graph.getChildrenNodes(node);
	}
	
	
	public void AddEdge(int start, int end, int weight) {
		throw new IllegalStateException("This object does not support this method.");
	}
	
	
	public int getWeight(int start, int end) {
		if(ContainsEdge(start, end)) return 1;
		else throw new IllegalArgumentException("The edge is not in the graph.");
	}
	
	
	public void setWeight(int start, int end, int weight) {
		throw new IllegalStateException("This object does not support this method.");
	}


	//このオブジェクトの文字列表現を返します
	
	public String toString(){
		return _graph.toString();
	}
	
	
	public Coordinate getClosestNode(Coordinate candidate) {
		double  closestNodeDistance = Integer.MAX_VALUE;
		int closestNode = -1;
		
		for(Map.Entry<Integer, Coordinate> node: _coordinate.entrySet()) {
			double distance = Hypot(node.getValue(), candidate);
			if(distance < closestNodeDistance) {
				closestNode = node.getKey();
				closestNodeDistance = distance;
			}
		}
		return _coordinate.get(closestNode);
	}
	
	
	public double Hypot(Coordinate A, Coordinate B) {
		
		double hypot = Math.sqrt(Math.pow((A.X - B.X), 2.0) + Math.pow((A.Y - B.Y), 2.0));
	
		return hypot;
	}

}
 