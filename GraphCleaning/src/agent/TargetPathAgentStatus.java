package agent;

import java.util.ArrayList;
import java.util.List;
import agent.common.LitterExistingExpectation;
import core.ObservedData;


public class TargetPathAgentStatus
{
	ITargetDecider _targetter;
	IPathPlanner _pather;
	int _baseNode;
	int _target;
	boolean _isChargeRequired;
	LitterExistingExpectation _expectation;
	public AgentActions Action;
	public int TargetNode;
	public ObservedData ObservedData;
//	public List<Integer> SearchNodes = new ArrayList<>();
	int[] VisitCounter;
	List<Integer> VisitHistory;
	

	
	public TargetPathAgentStatus(AgentActions action, int target, ObservedData data, int[] visitCounter, List<Integer> visitHistory) 
	{
		Action = action;
		TargetNode = target;
		ObservedData = data;
		VisitCounter = visitCounter;
		VisitHistory = visitHistory;
		
//		SearchNodes = searchNodes;
	}
	
	
	public void setAction(AgentActions action) {
		Action = action;
	}	
	
	public AgentActions getAction() {
		return Action;
	}

	public void setTargetNode(int target) {
		TargetNode = target;
	}
	
	public int getTargetNode() {
		return TargetNode;
	}
	
	public void setObservedData(ObservedData data) {
		ObservedData = data;
	}
	
	public ObservedData getObservedData() {
		return ObservedData;
	}
	
//	public void setSearchNodes(List<Integer> nodes) {
//		SearchNodes = nodes;
//	}
//	
//	public List<Integer> getSearchNodes(){
//		return SearchNodes;
//	}
	
	public void setVisitCounter(int[] counter) {
		VisitCounter = counter;
	}
	
	public int[] getVisitCounter() {
		return VisitCounter;
	}
	
	public void setVisitHistory(List<Integer> history) {
		VisitHistory = history;
	}
	
	public List<Integer> getVisitHistory(){
		return VisitHistory;
	}

	
//	public TargetPathAgentStatus(AgentActions action, int target, ObservedData data) 
//	{
//		Action = action;
//		TargetNode = target;
//		ObservedData = data;
//	}
	
	
//	public TargetPathAgentStatus(AgentActions action, int target, ObservedData data, int mycycle) 
//	{
//		Action = action;
//		TargetNode = target;
//		ObservedData = data;
//		MyCycle = mycycle;
//	}
	
}
