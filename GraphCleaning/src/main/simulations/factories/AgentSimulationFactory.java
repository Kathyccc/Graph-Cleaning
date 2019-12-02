package main.simulations.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import agent.IPathPlanner;
import agent.ITargetDecider;
import agent.TargetPathAgentPDALearning;
import agent.pather.ShortestGreedyPathPlanner;
import agent.pather.SubgoalPathPlanner;
import agent.targetter.GreedyTargetDecider;
import agent.targetter.MyopiaSweepTargetDecider;
import agent.targetter.NonStateLearningTargetDecider;
import agent.targetter.RandomTargetDecider;
import agent.targetter.RepulsionTargetDecider;
import core.Coordinate;
import core.GridGraph;
import core.IAgentManager;
import core.IEnvironment;
import core.LitterSpawnPattern;
import core.LitterSpawnProbability;
import core.NodeProperty;
import core.RobotSpec;
import core.environment.VirtualEnvironment;
import core.utilities.DijkstraAlgorithm;
import core.utilities.LogManager;
import core.utilities.LogWriter;
import core.utilities.PotentialCollection;
import main.simulations.SimulationFactory;
import main.simulations.Evaluator;
import core.agents.AgentManager;

public class AgentSimulationFactory extends SimulationFactory
{
	static int[] _seeds = new int[] { 31, 53, 79, 337, 601, 751, 907, 809, 977, 701,
            1823, 2069, 2939, 3463, 4271, 5059, 5779, 6569, 7499, 8287, 8999, 9337, 9901, 10007, 10459, 10949, 11443, 11933, 12829, 13309,
        13781, 14029, 15013, 15451, 15913, 16433, 17431, 17923, 18371, 18947, 19949, 20399, 20939, 21419, 22381, 22877, 23369, 23873, 24923, 25447,
        25943, 26437, 27509, 27997, 28547, 29017, 30103, 30643, 31151, 31657, 32653, 33179, 33637, 34213, 35251, 35809, 36319, 36821, 37871, 38449,
        38971, 39499, 40591, 41143, 41627, 42139, 43117, 43721, 44257, 44789, 45341, 45943, 46489, 47057, 47581, 48661, 49177, 49711, 50221, 51361,
        51853, 52391, 52967, 53527, 54049, 54581, 55163, 55717, 56239, 57241, 57791, 58321, 59399, 59971, 59998};
	
	VirtualEnvironment _environment;
	AgentManager _agentManager;
    Evaluator _evaluator;
    GridGraph _graph;
    LitterSpawnPattern _spawnPattern;
    Map<Integer, RobotSpec> _robots;
    List<Integer> HighNodeList = new ArrayList<>();
    List<Integer> MiddleNodeList = new ArrayList<>();
    List<Integer> LowNodeList = new ArrayList<>();
    NodeProperty[] nodeProperty;
    int robotCount = 20;
    int scale = 50;
    int basePosition;
    boolean spawnChange = false;
    LogWriter NodesProperty;
    LogWriter NodesConnection;
   
    String patternName = "Block";
    
    final double SpawnLow = 1e-6;
    final double SpawnMiddle = 1e-4;
    final double SpawnHigh = 1e-3;
    
    final double SpawnUniform = 5e-6;

    boolean _isAccumulated = true;

    int _environmentSeed = 0;
    int _patherSeed = 1;
    int _patherNumber = 2;
    int _targetterSeed = 5;
    int _targetterNumber = 6;
    

    public void setLocalNumbers(int eseed, int pseed, int pnumber, int tseed, int tnumber, int robotcount, int _scale, boolean Accumulated, String spawnPattern) 
    {
    	_environmentSeed = eseed;
        _patherSeed = pseed;
        _patherNumber = pnumber;
        _targetterSeed = tseed;
        _targetterNumber = tnumber;
        
        robotCount = robotcount;
        scale = _scale;
        
        _isAccumulated = Accumulated;
        patternName = spawnPattern;
    }
    
    
    public NodeProperty[] getNodesProperty() 
    {
    	return nodeProperty;
    }

	public IEnvironment Environment() 
	{
		return _environment;
	}

	public IAgentManager AgentManager() 
	{
		return _agentManager;
	}

	public Evaluator Evaluator() 
	{
		return _evaluator;
	}

	
	// create the components for simulation
	public void Make() 
	{
		//spatial structure
		CreateSpatialStructure();
		
		nodeProperty = new NodeProperty[_graph.getNumOfNodes()];
		
		for(int i=0; i < _graph.getNumOfNodes(); i++) 
		{
			NodeProperty node = new NodeProperty(0, null, false);
			nodeProperty[i] = node;
		}
		
		// trash generation pattern
		setNodesProperty();
		CreateLitterSpawnPattern();
		
		// environment
		_environment = new VirtualEnvironment(
				_graph, _spawnPattern, _isAccumulated, _seeds[_environmentSeed], HighNodeList, MiddleNodeList, LowNodeList);
		
		_agentManager = new AgentManager(_environment, _graph, _seeds[_environmentSeed + 3]);
		
		// environment settings
		CreateRobotBases();
		CreateRobots(robotCount);
		
		CreateAgent();
		
		_environment.Update();

		CreateEvaluator();
				
		NodesProperty = LogManager.CreateWriter("NodesProperty");
		NodesProperty.WriteLine("id" + "," + "Probability Type" + "," + "Obstacle"+ "," + "Potential" + "," + "X" + "," + "Y");
		
		for(NodeProperty node : nodeProperty) 
		{
			NodesProperty.WriteLine(node.ID + "," + node.ProbabilityType + "," + node.Obstacle + "," + node.Potential + "," + _graph.getCoordinate(node.ID).X + "," + _graph.getCoordinate(node.ID).Y);
		}

	}


	public void CreateEvaluator() 
	{
		_evaluator = new Evaluator(_environment, HighNodeList, MiddleNodeList, LowNodeList);
	}

	
	public void CreateSpatialStructure() 
	{
		Coordinate max = new Coordinate(scale, scale);
		Coordinate min = new Coordinate(-scale, -scale);
		
		_graph = new GridGraph(max, min);
	}
	
	
	public void setNodesProperty() 
	{
		for(int node : _graph.getNodes()) 
		{
			Coordinate coor = _graph.getCoordinate(node);
	
			if(patternName == "Block") 
			{
				if ((35 < coor.X && coor.X < 45 && 35 < coor.Y && coor.Y < 45) ||
                        (20 < coor.X && coor.X < 30 && -30 < coor.Y && coor.Y < -20) ||
                        (-50 < coor.X && coor.X < -40 && -35 < coor.Y && coor.Y < -25) ||
                        (-35 < coor.X && coor.X < -25 && -50 < coor.Y && coor.Y < -40)) 
				{
					nodeProperty[node] = new NodeProperty(node, "Middle", false);
				}
				else if((coor.X > -15) && (-5 > coor.X) && 5 < coor.Y && coor.Y < 15) 
				{
					nodeProperty[node] = new NodeProperty(node, "High", false);
				}
				else 
				{
					nodeProperty[node] = new NodeProperty(node, "Low", false);
				}
			} 
			
			
			else if (patternName == "Round")
            {
                if (coor.X < -40 || 40 < coor.X || coor.Y < -40 || 40 < coor.Y)
                {
                	nodeProperty[node] = new NodeProperty(node, "Middle", false);
                }
                else
                {
                	nodeProperty[node] = new NodeProperty(node, "Low", false);
                }
            }
			
            else if (patternName == "Complex")
            {
                if (coor.X < -43 || 43 < coor.X || coor.Y < -43 || 43 < coor.Y)
                {
                	nodeProperty[node] = new NodeProperty(node, "Middle", false);
                }
                else if (20 < coor.X && coor.X < 30 && -30 < coor.Y && coor.Y < -20)
                {
                	nodeProperty[node] = new NodeProperty(node, "Middle", false);
                }
                else if (-30 < coor.X && coor.X < -20 && 20 < coor.Y && coor.Y < 30)
                {
                	nodeProperty[node] = new NodeProperty(node, "High", false);
                }
                else
                {
                	nodeProperty[node] = new NodeProperty(node, "Low", false);
                }
            }
			
            else if (patternName == "Uniform")
            {
            	nodeProperty[node] = new NodeProperty(node, "Uniform", false);
            }
		}
		
		DijkstraAlgorithm tmpDijk = new DijkstraAlgorithm(_graph);
        PotentialCollection potentialmMap = tmpDijk.Execute(0);
        
        for(int _node : _graph.getNodes())
        {
        	nodeProperty[_node].Potential = potentialmMap._potentials.get(_node);
        }	
	}
	
	
	public void CreateLitterSpawnPattern() 
	{
		Random rand = new Random(_seeds[5]);
		
		_spawnPattern = new LitterSpawnPattern();
		
		for(int node : _graph.getNodes()) 
		{
			double prob;
			
			NodeProperty property = nodeProperty[node];
			
			if(property.ProbabilityType == "High") 
			{
				prob = SpawnHigh + (rand.nextDouble() - 0.5) * SpawnHigh * 0.01;
				HighNodeList.add(node);
			}
			
			else if(property.ProbabilityType == "Middle") 
			{
				prob = SpawnMiddle + (rand.nextDouble() - 0.5) * SpawnMiddle * 0.01;
				MiddleNodeList.add(node);
			}
			
			else if(property.ProbabilityType == "Low") 
			{
				prob = SpawnLow + (rand.nextDouble() - 0.5) * SpawnLow * 0.01;
			LowNodeList.add(node);
			}
			
			else if(property.ProbabilityType == "Uniform") 
			{
				prob = SpawnUniform * rand.nextDouble();
			}
			
			else 
			{
				prob = 0.0;
			}
			
			_spawnPattern.AddSpawnProbability(node, new LitterSpawnProbability(1, prob, 1));
			
		}
	}

	
	public void CreateRobotBases() 
	{		
		basePosition = _graph.GetNode(0, 0);
		
		_environment.SetRobotBase(1, basePosition);
		_spawnPattern._patterns.get(basePosition).Probability = 0.0;
	}
	
	
	public void CreateRobots(int num) 
	{
		_robots = new HashMap<>();
		RobotSpec spec = new RobotSpec(2700,3);
		
		for(int i =0; i<num; i++) 
		{
			int robot = _environment.CreateRobot(spec, basePosition);
			_robots.put(robot, spec);
		}
	}
	
	
	public void CreateAgent() 
	{
		Random path_rand = new Random(_seeds[_patherSeed]);
		Random target_rand = new Random(_seeds[_targetterSeed]); 
		
		for(Map.Entry<Integer, RobotSpec> robot : _robots.entrySet()) 
		{			
			TargetPathAgentPDALearning agent = new TargetPathAgentPDALearning(robot.getKey(), _graph, _spawnPattern);
			agent.setBaseNode(basePosition);

			ITargetDecider targetter = null;
            IPathPlanner pather;
			
            // path planning
            switch (_patherNumber)
            {
                case 0:
                    pather = new ShortestGreedyPathPlanner(
                        robot.getKey(), _graph, _spawnPattern, basePosition, _isAccumulated, path_rand.nextInt());
                    break;
                default:
                    pather = new SubgoalPathPlanner(robot.getKey(), _graph, _spawnPattern, basePosition, _isAccumulated, path_rand.nextInt());
                    break;
            }
			
			// target decision
            switch (_targetterNumber)
            {
                case 0:
                    targetter = new RandomTargetDecider(
                        robot.getKey(), _graph, target_rand.nextInt());
                    break;
                case 1:
                    targetter = new GreedyTargetDecider(robot.getKey(), _graph, _spawnPattern, _isAccumulated, target_rand.nextInt());
                    break;
                case 2:
                    targetter = new RepulsionTargetDecider(robot.getKey(), _graph, target_rand.nextInt());
                    break;
                case 3:
                    targetter = new MyopiaSweepTargetDecider(robot.getKey(), _graph, _spawnPattern, _isAccumulated, target_rand.nextInt());
                    break;
                case 4:
                	NonStateLearningTargetDecider decider = new NonStateLearningTargetDecider(robot.getKey(), _graph, _spawnPattern, _isAccumulated, target_rand.nextInt());
                	decider.AddTargetDecider(
                			new RandomTargetDecider(robot.getKey(), _graph, target_rand.nextInt()));
                	decider.AddTargetDecider(
                			new GreedyTargetDecider(robot.getKey(), _graph, _spawnPattern, _isAccumulated, target_rand.nextInt()));;
                	decider.AddTargetDecider(
                			new RepulsionTargetDecider(robot.getKey(), _graph, target_rand.nextInt()));
                	decider.AddTargetDecider(
                			new MyopiaSweepTargetDecider(robot.getKey(), _graph, _spawnPattern, _isAccumulated, target_rand.nextInt()));
            }
			
			agent.setPathPlanner(pather);
			agent.setTargetDecider(targetter);
			agent.setExpectation();
			_agentManager.AddAgent(agent);
		}
	}	
}

