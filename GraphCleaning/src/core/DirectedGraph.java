package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectedGraph implements IGraph 
{
	
	List<Integer> _nodes = new ArrayList<>();
	
	Map<Integer, List<Integer>> _parents = new HashMap<>();
	Map<Integer, List<Integer>> _children = new HashMap<>();
	Map<Tuple<Integer, Integer>, Integer> _weight = new HashMap<>(); //what is this??
	
	public DirectedGraph() 
	{
		_nodes = new ArrayList<Integer>();
		_parents = new HashMap<>();
		_children = new HashMap<>();
		_weight = new HashMap<>();
	}
	
	
	
	public boolean IsDirected()
	{
		return true;
	}
	
	
	public List<Integer> getNodes() 
	{
		return _nodes;
		// use iterator?
	}
	
	
	public void AddNode(int node) 
	{
		if(_nodes.contains(node)) {
			throw new IllegalArgumentException("The node is already in the graph.");
		}
		else 
		{
			_nodes.add(node);
			_parents.put(node, new ArrayList<>());
			_children.put(node, new ArrayList<>());
		}
	}
	
	
	public boolean RemoveNode(int node) 
	{
		if(_nodes.contains(node)) {
			List<Integer> parents = new ArrayList<>();
			List<Integer> children = new ArrayList<>();
			parents = _parents.get(node);
			children = _children.get(node);

			
			for(int parent : parents) 
			{
				Tuple<Integer, Integer> set = new Tuple<>(parent, node);
				_children.get(parent).remove(node);
				_weight.remove(set);
			}
			
			for(int child : children) 
			{
				Tuple<Integer, Integer> set = new Tuple<>(node, child);
				_parents.get(child).remove(node);
				_weight.remove(set);
			}
			
			_nodes.remove(node);
			_parents.remove(node);
			_children.remove(node);
			return true;
		}
		else return false;
	}
	
	
	public List<Integer> getParentNodes(int node)
	{
		if(_parents.get(node)!=null) 
		{
			return _parents.get(node);
		}
		else {
			throw new IllegalArgumentException("The node is not in the graph.");
		}
	}
	
	
	public List<Integer> getChildrenNodes(int node){
		if(_children.get(node)!=null)
		{
			return _children.get(node);
		}
		else
		{
			throw new IllegalArgumentException("The node is not in the graph.");
		}
	}
	
	
	public boolean ContainsNode(int node) 
	{
		if(_nodes.contains(node)) return true;
		else return false;
	}
	
	public List<Integer> getNeighbors(int node){
		
		if(ContainsNode(node)==false) 
		{
			throw new IllegalArgumentException("The node is not in the graph.");
		}
		else 
		{
			List<Integer> neighbors = new ArrayList<>();
			List<Integer> parents = new ArrayList<>();
			parents = _parents.get(node);
			List<Integer> children = new ArrayList<>();
			children = _children.get(node);
			
			for(int element : parents)
			{
				neighbors.add(element);
			}
			
			for(int element : children)
			{
				if(!neighbors.contains(element))
				{
					neighbors.add(element);
				}
			}
			return neighbors;
		}
	}
	
	
	public int getNumOfNodes() 
	{
		return _nodes.size();
	}
	
	
	public boolean ContainsEdge(int start, int end) 
	{
		if(_children.get(start).contains(end)) return true;
		else return false;
	}
	
	
	public void setWeight(int start, int end, int weight)
	{
		Tuple<Integer, Integer> set = new Tuple<>(start, end);
		
		if(ContainsEdge(start, end) == false) throw new IllegalArgumentException("The edge is not in the graph.");
		else
		{
			if(_weight.containsKey(set) == false) _weight.put(set, weight);
			else _weight.put(set, weight);
		}
	}
	
	public int getWeight(int start, int end) 
	{
		Tuple<Integer, Integer> set = new Tuple<>(start, end);
		if(_weight.get(set)!=null) return _weight.get(set);
		else throw new IllegalArgumentException("The edge is not in the graph.");
	}
	
	
	public void AddEdge(int start, int end) 
	{
		if(ContainsEdge(start, end) == true) 
		{
			throw new IllegalArgumentException("The edge is already in the graph.");
		}
		else 
		{
			_children.get(start).add(end);
			_parents.get(end).add(start);
			setWeight(start, end , 0);
		}
	}
	
	
	public boolean RemoveEdge(int start, int end)
	{
		Tuple<Integer, Integer> set = new Tuple<>(start, end);
		if(ContainsEdge(start, end) == false)
		{
			throw new IllegalArgumentException("The edge is not in the graph.");
		}
		else 
		{
			_children.get(start).remove(end);
			_parents.get(end).remove(start);
			_weight.remove(set);
			return true;
		}
	}
	
	
	public void AddEdge(int start, int end, int weight) 
	{
		if(ContainsEdge(start, end) == false) 
		{
			_children.get(start).add(end);
			_parents.get(end).add(start);
			setWeight(start, end, weight);
		}
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		for(int node : _nodes) {
			str.append("Node" + Integer.toString(node) + "/n");
			for(int child : _children.get(node)) {
				str.append("-->" + Integer.toString(child) + "(" + getWeight(node, child) + ")");
			}
			for(int parent : _parents.get(node)) {
				str.append("<--" + Integer.toString(parent));
			}
		}
	return str.toString();
	}

}


