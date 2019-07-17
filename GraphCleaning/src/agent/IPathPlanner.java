package agent;

import agent.common.LitterExistingExpectation;

public interface IPathPlanner {
	
	int NextNode();
	
	boolean CanArrive();
	
	public void Update(TargetPathAgentStatus status);
	
	public void setExpectation(LitterExistingExpectation expectation);
	
	public void setSubgoalsNull();
}
