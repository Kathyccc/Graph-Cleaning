package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DijkstraAlgorithm 
{
	IGraph _graph;
	Map<Integer, Integer> _distance = new HashMap<>();
	List<Integer> _excludeNodes = new ArrayList<>();
	PotentialCollection Potentials = new PotentialCollection(_distance);
	
	//Map<Integer, Integer> PotentialMap = Potentials._potentials;
	
	
	public DijkstraAlgorithm(IGraph graph) 
	{
		_graph = graph;
	}
	
	
	public PotentialCollection Execute(int destination, int source) 
	{
		if(!_graph.ContainsNode(destination)) throw new IllegalArgumentException("The node is not in the graph: " + destination);
		if(!_graph.ContainsNode(source)) throw new IllegalArgumentException("The node is not in the graph:" + source);
		
		_distance = new HashMap<>();
		
		for(int node : _graph.getNodes()){      // iterate over the nodes in graph or??  does it have anything to do with IEnumerable<T>?
			_distance.put(node,  Integer.MAX_VALUE);
		}
		
		List<Integer> calcs = new ArrayList<>();
		
		calcs.add(destination);
		_distance.put(destination, 0);
		
		int cpot;
		
		while(calcs.size() != 0) 
		{
			int node = calcs.get(0);
			int potential = _distance.get(node);
			
			if(node == source && node!= destination) return this.Potentials;
			
			for(int child : _graph.getParentNodes(node)) 
			{
				cpot = potential + _graph.getWeight(node, child);
				
				if(_distance.get(child) > cpot) 
				{
					if(_distance.get(child) != Integer.MAX_VALUE) calcs.remove(child);
					
					//追加処理
					int index = calcs.size() - 1;
					for(; _distance.get(calcs.get(index)) > cpot; index--);
					calcs.add(index + 1, child);
					
					
					//反映処理
					_distance.put(child, cpot);
				}
			}
			calcs.remove(0);
		}
		return this.Potentials;
	}
	
	
	public PotentialCollection Execute(int destination) 
	{
		return Execute(destination, destination);
	}
}
