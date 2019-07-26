package main;

import java.time.LocalTime;

import core.utilities.LogManager;
import main.simulations.Simulation;
import main.simulations.factories.AgentSimulationFactory;

public class Main 
{
	public static void main(String[] args) 
	{
		Simulate();
	}
	
	public static void Simulate() 
	{
		boolean isAccumulated = true;
		String logdir = "LogB";
		
		int counter = 0;
		int scale = 50;
		
		
		for (int robots = 20; robots <= 20; robots += 5)
		{
			for (int t = 0; t < 7; t++)
			{
				if (t != 6 && t != 7)
					continue;
				for (int p = 2; p < 3; p++)
				{
					for (int s = 0; s < 1; s++)
					{
						System.out.println(counter);
						LocalTime time = LocalTime.now();
						LogManager.LogDirectory = logdir + "Block" + t + "\\" + robots + "\\" + "[" + s + "]" + time;
						AgentSimulationFactory factory = new AgentSimulationFactory();
						factory.setLocalNumbers(s, s+1, p, s+2, t, robots, scale, isAccumulated, "Block");
						Simulation simulation = new Simulation(factory, "Block");
						simulation.Reset();
						simulation.Run(100000);
						counter++;
					}
				}
			}
		}

		counter = 0;
		for (int robots = 20; robots <= 20; robots += 5)
		{
			for (int t = 0; t < 7; t++)
			{
				if (t != 5 && t != 7)
					continue;
				for (int p = 2; p < 3; p++)
				{
					for (int s = 0; s < 30; s++)
					{
						System.out.println(counter);
						LocalTime time = LocalTime.now();
						LogManager.LogDirectory = logdir + "Uniform" + t + "\\" + robots + "\\" + "[" + s + "]" + time;
						AgentSimulationFactory factory = new AgentSimulationFactory();
						factory.setLocalNumbers(s, s + 1, p, s + 2, t, robots, scale, isAccumulated, "Uniform");
						Simulation simulation = new Simulation(factory, "Uniform");
						simulation.Reset();
						simulation.Run(3010000);
						counter++;
					}
				}
			}
		}
		

		counter = 0;
		for (int robots = 20; robots <= 20; robots += 5)
		{
			for (int t = 0; t < 7; t++)
			{
				if (t != 5 && t != 7)
					continue;
				for (int p = 2; p < 3; p++)
				{
					for (int s = 0; s < 10; s++)
					{
						System.out.println(counter);	
						LocalTime time = LocalTime.now();
						LogManager.LogDirectory = logdir + "Round" + t + "\\" + robots + "\\" + "[" + s + "]" + time;
						AgentSimulationFactory factory = new AgentSimulationFactory();
						factory.setLocalNumbers(s, s + 1, p, s + 2, t, robots, scale, isAccumulated, "Round");
						Simulation simulation = new Simulation(factory, "Round");
						simulation.Reset();
						simulation.Run(3010000);
						counter++;
					}
				}
			}
		}

		counter = 0;
		for (int robots = 20; robots <= 20; robots += 5)
		{
			for (int t = 0; t < 10; t++)
			{
				if (t != 6)
					continue;
				for (int p = 2; p < 3; p++)
				{
					for (int s = 0; s < 5; s++)
					{
						System.out.println(counter);
						LocalTime time = LocalTime.now();
						LogManager.LogDirectory = logdir + "Complex" + t + "\\" + robots + "\\" + "[" + s + "]" + time;
						AgentSimulationFactory factory = new AgentSimulationFactory();
						factory.setLocalNumbers(s, s + 1, p, s + 2, t, robots, scale, isAccumulated, "Complex");
						Simulation simulation = new Simulation(factory, "Complex");
						simulation.Reset();
						simulation.Run(3010000);
						counter++;
					}
				}
			}
		}	
	}
}
