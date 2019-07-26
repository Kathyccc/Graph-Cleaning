package core.agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
	int[] _potentialCenterNode = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	int[] _centerNodes = new int[20];
	int[] _preLitter = new int[20];
	double[] _experienceWeight = new double[20];
	int[] searchNodeNum = new int[20];
	Random _rand;
	
	List<Integer> agentNumList = new ArrayList<>();

//	List<Integer> _stopAgents = new ArrayList<>(Arrays.asList(Integer.MAX_VALUE)); 
//	double[][] _agentImpManager = new double[10201][20];
//	int allNodesImpCounter = 0;
//	int similarNodeNum = 10;
//	double similarNodeRatio = 10.0;
//	String PatternName;
	
//    LogWriter[] _NodesImportanceManager = new LogWriter[30];
//    double[][] probabilityAve = new double[20][7];
//    int[][] visitCounter = new int[20][7];
//    //int[][] DvisitCounter;
//    int[][] visitCounterRoom = new int[20][6];
//    int k = 100;
//    double regionAve = 81.0;
//    double[] _tmpEntropyMemory = new double[20];
    
    
//    public void setPatternName(String name) 
//    {
//    	PatternName = name;
//    }
//    
//    public String getPatternName() 
//    {
//    	return PatternName;
//    }
	
	public AgentManager(IEnvironment environment, GridGraph graph, int seed) 
	{
		_environment = environment;
		_agentActions = new HashMap<>();
		_agents = new ArrayList<Pair<IAgent, Integer>>();
		_graph = graph;
		_rand = new Random(seed);		
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
		
		_logWriters.get("AgentActions").MapValueWriteLine(_agentActions, Integer.toString(data.getTime()) + ",");
		
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
	
	
	public void setExcludedNodes(List<Integer> exclude) 
	{
		_excludedNodes = exclude;
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
		for(int i = 0; i < _agents.size(); i++) 
		{
			agentNumList.add(i);
		}
		
		AddLogWriterMap("ProbabilityCheck");
		AddLogWriterMap("ProbabilityCheck2");
		AddLogWriterMap("ProbabilityCheck3");
		AddLogWriterMap("ProbabilityCheck4");

		AddLogWriterMap("CenterNodeChanges");
        _logWriters.get("CenterNodeChanges").WriteLine("" + "," + "0" + "," + "1" + "," + "2" + "," + "3" + "," + "4" + "," + "5" + "," + "6" + "," + "7" + "," + "8" + "," + "9" + "," + "10"
            + "," + "11" + "," + "12" + "," + "13" + "," + "14" + "," + "15" + "," + "16" + "," + "17" + "," + "18" + "," + "19");

        AddLogWriterMap("AgentActions");
        _logWriters.get("AgentActions").WriteLine("" + "," + "0" + "," + "1" + "," + "2" + "," + "3" + "," + "4" + "," + "5" + "," + "6" + "," + "7" + "," + "8" + "," + "9" + "," + "10"
            + "," + "11" + "," + "12" + "," + "13" + "," + "14" + "," + "15" + "," + "16" + "," + "17" + "," + "18" + "," + "19");
        
//        AddLogWriterMap("SearchNodesChecker");
//        _logWriters.get("SearchNodesChecker").ListWriteLine(agentNumList, "" + ",");
//        
//        AddLogWriterMap("AccumulatedLitter");
//        _logWriters.get("AccumulatedLitter").ListWriteLine(agentNumList, "" + ",");
//        
//        AddLogWriterMap("ExperienceWeight");
//        _logWriters.get("ExperienceWeight").ListWriteLine(agentNumList, "" + ",");
	}
}
