package main.simulations;

import core.IAgentManager;
import core.IEnvironment;
//import core.LitterSpawnPattern;

public abstract class SimulationFactory 
{
	public abstract IEnvironment Environment() ;
	
	public abstract IAgentManager AgentManager();
	
	public abstract Evaluator Evaluator();
	
	public abstract void Make();

	//public abstract LitterSpawnPattern CreateLitterSpawnPattern(String patternName);
}
