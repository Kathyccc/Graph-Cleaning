package core.agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import agent.AgentActions;
import core.GridGraph;
import core.IAgentManager;
import core.IEnvironment;
import core.ObservedData;
import core.Pair;
import core.RobotDataCollection;
import core.utilities.LogManager;
import core.utilities.LogWriter;
import core.utilities.PotentialCollection;

public class AgentManager implements IAgentManager
{
	IEnvironment _environment;
	List<Pair<IAgent, Integer>> _agents;
	Map<Integer, AgentActions> _agentActions;
	GridGraph _graph;
	Map<String, LogWriter> _logWriters = new HashMap<>();
	List<Integer> _excludedNodes = new ArrayList<>();
	PotentialCollection[] _agentPotential = new PotentialCollection[20];
	String PatternName;
	List<Integer> agentNumList = new ArrayList<>();

	
	public AgentManager(IEnvironment environment, GridGraph graph, int seed) 
	{
		_environment = environment;
		_agentActions = new HashMap<>();
		_agents = new ArrayList<Pair<IAgent, Integer>>();

		InitializeLogWriter();
	}
	
	
	public void AddAgent(IAgent agent) 
	{
		_agents.add(new Pair<IAgent, Integer>(agent, agent.getRobotID()));
		_agentActions.put(agent.getRobotID(), AgentActions.Wait);
	}
	
	
	public void Move() 
	{
		RobotDataCollection robots = _environment.GetRobotDataCollection();
		int time = _environment.GetTime();
		
		ObservedData data = new ObservedData(time, robots);
		
		String log = Integer.toString(data.Time) + ",";
		
		for(AgentActions agentAction : _agentActions.values()) 
		{
			log += agentAction.name() + ",";
//			System.out.println("agent action:  " + agentAction.name());
		}
				
		_logWriters.get("AgentActions").WriteLine(log);
				
		for(Pair<IAgent, Integer> p : _agents) 
		{
			IAgent agent = p.getKey();
			int id = p.getValue();
									
			agent.Update(data);
			
			if(agent.getAction() == AgentActions.Move) 
			{
				if(_agentActions.get(id) == AgentActions.Charge) 
				{
					_environment.DisconnectRobotBase(id);
				}
				_environment.moveRobot(id, agent.NextNode());
			}
			
			else if(agent.getAction() == AgentActions.Charge) 
			{
				if(_agentActions.get(id) != AgentActions.Charge) 
				{
					_environment.ConnectRobotBase(id);
				}
			}

			_agentActions.put(id,agent.getAction());
		}
	}
	
	
	public void Clean() 
	{
		for(Pair<IAgent, Integer> robot : _agents) 
		{
			_environment.Clean(robot.getKey().getRobotID());
		}
	}
	
	
	private void AddLogWriterMap(String fileName) 
	{
		_logWriters.put(fileName, LogManager.CreateWriter(fileName));
	}
	
	
	private void InitializeLogWriter() 
	{
        AddLogWriterMap("AgentActions");
        _logWriters.get("AgentActions").WriteLine("" + "," + "0" + "," + "1" + "," + "2" + "," + "3" + "," + "4" + "," + "5" + "," + "6" + "," + "7" + "," + "8" + "," + "9" + "," + "10"
            + "," + "11" + "," + "12" + "," + "13" + "," + "14" + "," + "15" + "," + "16" + "," + "17" + "," + "18" + "," + "19");
	}
}
