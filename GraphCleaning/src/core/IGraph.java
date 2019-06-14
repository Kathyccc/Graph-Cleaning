package core;

public interface IGraph{
	boolean IsDirected();
	
	int[] getNodes();
	
	int numOfNode();
	
	void AddNode(int node);
	
	boolean ReomveNode(int node);
	
	boolean NodeContained(int node);
	
	void AddEdge(int start, int end);
	
	boolean RemoveEdge(int start, int end);
	
	boolean EdgeContained(int start, int end);
	
	int[] getNeighbors(int node);
	
	int[] getParentNodes(int nodes);
	
	int[] getChildNodes(int node);
	
	void AddEdge(int start, int end, int weight);
	
	int getWeight(int start, int end);
	
	void setWeight(int start, int end, int weight);
}
