package main.simulations;

import java.util.Date;
import java.time.Instant;

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
		long startTime = Instant.now().toEpochMilli();
		
		for(int i = 0; i < steps ; i++) 
		{
			Step();
			if(i % 10000 == 0) 
			{
				long endTime = Instant.now().toEpochMilli();
				long time_elapsed = endTime - startTime;
				System.out.println(i + "It takes " + time_elapsed);
				
				Date time = new Date(0);
				long t = 0;

				for(int j = 0; j < (steps-i)/10000; j++)
				{
					t += time_elapsed;
				}
				System.out.println("This trial will finish at" + time + t + "ms");
			}
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
