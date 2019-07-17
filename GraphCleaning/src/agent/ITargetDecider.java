package agent;

public interface ITargetDecider {
	
	int NextTarget();
	
	boolean IsChargeRequired();
	
	public void Update(TargetPathAgentStatus status);

	public void setExpectation(LitterExistingExpectation expectation);

	public void ResetState();
}
  