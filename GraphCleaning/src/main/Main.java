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
		
		int counter = 0;
		int scale = 50;
		
		
		for (int robots = 20; robots <= 20; robots += 5)
		{
			for (int t = 0; t < 4; t++)
			{
				for (int p = 0; p < 1; p++)
				{
					for (int s = 0; s < 1; s++) // s -> random seed
					{
						System.out.println(counter);
//						SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//						Date time = new Date();
						//System.out.println(formatter.format(time));
						LogManager.setLogDirectory("/Block/" + t + "/" + robots + "/" + "[" + s + "]");
//						+ formatter.format(time)
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
			for (int t = 0; t < 4; t++)
			{
				for (int p = 0; p < 1; p++)
				{
					for (int s = 0; s < 1; s++)
					{
						System.out.println(counter);
//						SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//						Date time = new Date();
						LogManager.setLogDirectory("/Uniform/" + t + "/" + robots + "/" + "[" + s + "]");
						AgentSimulationFactory factory = new AgentSimulationFactory();
						factory.setLocalNumbers(s, s + 1, p, s + 2, t, robots, scale, isAccumulated, "Uniform");
						Simulation simulation = new Simulation(factory, "Uniform");
						simulation.Reset();
						System.out.println("running2");
						simulation.Run(100000);
						counter++;
					}
				}
			}
		}
		

		counter = 0;
		for (int robots = 20; robots <= 20; robots += 5)
		{
			for (int t = 0; t < 4; t++)
			{
				for (int p = 0; p < 1; p++)
				{
					for (int s = 0; s < 1; s++)
					{
						System.out.println(counter);	
//						SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//						Date time = new Date();
						LogManager.setLogDirectory("/Round/" + t + "/" + robots + "/" + "[" + s + "]");
						AgentSimulationFactory factory = new AgentSimulationFactory();
						factory.setLocalNumbers(s, s + 1, p, s + 2, t, robots, scale, isAccumulated, "Round");
						Simulation simulation = new Simulation(factory, "Round");
						simulation.Reset();
						System.out.println("running3");
						simulation.Run(100000);
						counter++;
					}
				}
			}
		}

		counter = 0;
		for (int robots = 20; robots <= 20; robots += 5)
		{
			for (int t = 0; t < 4; t++)
			{
				for (int p = 0; p < 1; p++)
				{
					for (int s = 0; s < 1; s++)
					{
						System.out.println(counter);
//						SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//						Date time = new Date();
						LogManager.setLogDirectory("/Complex/" + t + "/" + robots + "/" + "[" + s + "]");
						AgentSimulationFactory factory = new AgentSimulationFactory();
						factory.setLocalNumbers(s, s + 1, p, s + 2, t, robots, scale, isAccumulated, "Complex");
						Simulation simulation = new Simulation(factory, "Complex");
						simulation.Reset();
						System.out.println("running4");
						simulation.Run(100000);
						counter++;
					}
				}
			}
			System.out.println("Complex Area done.");
		}
	}
}
