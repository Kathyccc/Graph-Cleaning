package main.simulations;

import core.IAgentManager;
import core.IEnvironment;
import core.ISimulation;



public class Simulation implements ISimulation
{
	SimulationFactory _factory;
	IEnvironment _environment;
	IAgentManager _agentManager;
	Evaluator _evaluator;
	String _patternName;
	int EvaluationValue;
	
	public void setEvaluationValue(int value) 
	{
		EvaluationValue = value;
	}
	
	public int getEvaluationValue() 
	{
		return EvaluationValue;
	}
	
	public Simulation(SimulationFactory factory) 
	{
		_factory = factory;
	}
	
	public Simulation(SimulationFactory factory, String patternName) 
	{
		_factory = factory;
		_patternName = patternName;
	}

	
	public void Run(int steps) 
	{		
		for(int i = 0; i < steps ; i++) 
		{
			Step();
//			System.out.println("Step: " + i);
		}
		
	}


	public void Step() 
	{
		_environment.Update();
		
		_agentManager.Move();
		_environment.Update();
		
		_agentManager.Clean();
        _environment.Update();

        _environment.Update();

		EvaluationValue = _evaluator.getEvaluation();
	}

	
	public void Reset() 
	{
		SimulationFactory factory = _factory;

		factory.Make();
		_environment = factory.Environment();
		_agentManager = factory.AgentManager();
        _evaluator = factory.Evaluator();
        
        EvaluationValue = _evaluator.getEvaluation();
	}

}
