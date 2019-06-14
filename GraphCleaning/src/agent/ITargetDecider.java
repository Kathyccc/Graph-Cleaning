package agent;

public interface ITargetDecider {
	
	int NextTarget;
	
	boolean ChargeRequired;
	
	public void Update(TargetPathAgentStatus status);

	public void SetExpectation(LitterExistingExpectation expectation);

	public void ResetState();
}
