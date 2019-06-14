package agent;

public interface IPathPlanner {
	
	int getNextNode;
	
	boolean CanArrive;
	
	public void Update(TargetPathAgentStatus status);
	
	public void SetExpectation(LitterExistingExpectation expectation);
	
	public void SetSubgoalsNull();
}
