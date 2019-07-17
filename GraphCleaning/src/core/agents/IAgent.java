package core.agents;

import agent.AgentActions;
import core.CommunicationDetails;
import core.LitterSpawnPattern;
import core.ObservedData;

public interface IAgent 
{
	int getRobotID();
	
	AgentActions getAction();
	
	int NextNode();
	
	void Update(ObservedData data);
	
	void Update(ObservedData data, int startPhase);
		
	LitterSpawnPattern getMySpawnPattern();
	
	CommunicationDetails getCommunicateDetails();
	
	void SearchNumberDecrease(int decrease);
	
	int[][] getVisitCountMemory();
}
