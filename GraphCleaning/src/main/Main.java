package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import core.utilities.LogManager;
import main.simulations.Simulation;
import main.simulations.factories.AgentSimulationFactory;

public class Main 
{
	public static void main(String[] args) 
	{
		Simulate();
		System.out.println("Done");
	}
	
	public static void Simulate() 
	{
		boolean isAccumulated = true;
		
		int scale = 50;
		
		
		for (int robots = 100; robots <= 100; robots += 5)
		{
			for (int t = 1; t < 2; t++)
			{
				for (int p = 1; p < 2; p++)
				{
					for (int s = 0; s < 1; s++) // s -> random seed
					{
						SimpleDateFormat formatter = new SimpleDateFormat("dd:MM:yyyy HH-mm-ss");
						Date time = new Date();
						System.out.println(formatter.format(time));
//						LogManager.setLogDirectory("/Block/" + t + "/" + formatter.format(time) + "(n= " + robots + ")" );
//						+ formatter.format(time)
						LogManager.setLogDirectory("test");
						AgentSimulationFactory factory = new AgentSimulationFactory();
						factory.setLocalNumbers(s, s+1, p, s+2, t, robots, scale, isAccumulated, "Block");
						Simulation simulation = new Simulation(factory, "Block");
						simulation.Reset();
						simulation.Run(500000);
					}
				}
			}
		}

		
		for (int robots = 100; robots <= 100; robots += 5)
		{
			for (int t = 0; t < 5; t++)
			{
				for (int p = 1; p < 2; p++)
				{
					for (int s = 0; s < 1; s++)
					{
						SimpleDateFormat formatter = new SimpleDateFormat("dd:MM:yyyy HH-mm-ss");
						Date time = new Date();
						LogManager.setLogDirectory("/Uniform/" + t + "/" + formatter.format(time)+ "(n= " + robots + ")" );
						AgentSimulationFactory factory = new AgentSimulationFactory();
						factory.setLocalNumbers(s, s + 1, p, s + 2, t, robots, scale, isAccumulated, "Uniform");
						Simulation simulation = new Simulation(factory, "Uniform");
						simulation.Reset();
						simulation.Run(500000);
					}
				}
			}
		}
		

		for (int robots = 100; robots <= 100; robots += 5)
		{
			for (int t = 0; t < 5; t++)
			{
				for (int p = 1; p < 2; p++)
				{
					for (int s = 0; s < 1; s++)
					{
						SimpleDateFormat formatter = new SimpleDateFormat("dd:MM:yyyy HH-mm-ss");
						Date time = new Date();
						LogManager.setLogDirectory("/Round/" + t + "/" + formatter.format(time)+ "(n= " + robots + ")" );
						AgentSimulationFactory factory = new AgentSimulationFactory();
						factory.setLocalNumbers(s, s + 1, p, s + 2, t, robots, scale, isAccumulated, "Round");
						Simulation simulation = new Simulation(factory, "Round");
						simulation.Reset();
						simulation.Run(500000);
					}
				}
			}
		}

		for (int robots = 100; robots <= 100; robots += 5)
		{
			for (int t = 0; t < 5; t++)
			{
				for (int p = 1; p < 2; p++)
				{
					for (int s = 0; s < 1; s++)
					{
						SimpleDateFormat formatter = new SimpleDateFormat("dd:MM:yyyy HH-mm-ss");
						Date time = new Date();
						LogManager.setLogDirectory("/Complex/" + t + "/" + formatter.format(time)+ "(n= " + robots + ")" );
						AgentSimulationFactory factory = new AgentSimulationFactory();
						factory.setLocalNumbers(s, s + 1, p, s + 2, t, robots, scale, isAccumulated, "Complex");
						Simulation simulation = new Simulation(factory, "Complex");
						simulation.Reset();
						simulation.Run(500000);
					}
				}
			}
		}
	}
}
