package core;

import java.util.List;

public interface IGraph
{
	boolean IsDirected();
	
	List<Integer> Nodes();
	
	int numOfNodes();
	
	void AddNode(int node);
	
	boolean RemoveNode(int node);
	
	boolean NodeContained(int node);
	
	void AddEdge(int start, int end);
	
	boolean RemoveEdge(int start, int end);
	
	boolean EdgeContained(int start, int end);
	
	List<Integer> getNeighbors(int node);
	
	List<Integer> getParentNodes(int nodes);
	
	List<Integer> getChildrenNodes(int node);
	
	void AddEdge(int start, int end, int weight);
	
	int getWeight(int start, int end);
	
	void setWeight(int start, int end, int weight);
}
