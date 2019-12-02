/*package agent;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import agent.common.LitterExistingExpectation;
import core.Coordinate;
import core.GridGraph;
import core.LitterSpawnPattern;
import core.LitterSpawnProbability;
import core.ObservedData;
import core.RobotData;
import core.agents.IAgent;
import core.utilities.LogManager;
import core.utilities.LogWriter;


public class TargetPathAgentPDALearning2 implements IAgent
{
	ITargetDecider _targetter;
	IPathPlanner _pather;
	LitterSpawnPattern _mySpawnPattern = new LitterSpawnPattern();
	LitterSpawnPattern _spawnPattern;
	GridGraph _graph;
	List<Integer> _nodes;
	int _baseNode;
	int _target;
	Random forOrderbyRnd;
	boolean _isChargeRequired = false;
	LitterExistingExpectation _expectation = new LitterExistingExpectation (_mySpawnPattern, true);
	int _maxNodeNum;
//	List<Integer> _searchNodes;
//	int _searchNodeNum;
	List<Integer> _excludeNodes = new ArrayList<>();
	Coordinate _myCenterNode = new Coordinate(0,0);
	double _myCenterNodeWeight = 0.0;
	int[] _visitCounter;
	public int[][] _visitCountMemory;
	List<Integer> _visitHistory;
    int startTime = 0;
    int RobotID;
    AgentActions Action;
    int nextNode;
    
    
    // to set and get the robot's ID
    public void setRobotID(int id) { RobotID = id; }
    public int getRobotID() { return RobotID; }
    
    
    // to set and get agent's action 
    public void setAction(AgentActions action) {Action = action; }  
    public AgentActions getAction() { return Action; }
    
    
    // to get the agent's visit records
    public  int[][] getVisitCountMemory()
    {
    	return _visitCountMemory;
    }
    
    // to get the next node 
    public int NextNode() { return _pather.NextNode(); }
    
    
    
    // Initialize and create a new instance
    public TargetPathAgentPDALearning2(int robotID, GridGraph graph, LitterSpawnPattern spawnPattern, List<Integer> excludeNodes)
    {
    	RobotID = robotID;
        forOrderbyRnd = new Random(robotID + 7);
        Action = AgentActions.Wait;
        _excludeNodes = excludeNodes;
        _graph = graph;
        _nodes = new ArrayList<Integer>(_graph.getNodes());
        _visitCounter = new int[_graph.getNumOfNodes()];
                        
        for(int i = 0; i < _graph.getNumOfNodes(); i++) {
        	_visitCounter[i] = 1;
        }
        
        _visitCountMemory = new int[3][_nodes.size()];
        _visitHistory = new ArrayList<>();
        
//        _searchNodeNum = _nodes.size();
        _maxNodeNum = _nodes.size();
        _spawnPattern = spawnPattern;
        InitializeMySpawnPattern();
        
        _expectation = new LitterExistingExpectation (_mySpawnPattern, true);
//        _SearchNodeListup = LogManager.CreateWriter("SearchNodeList-" + RobotID);
    }

    
    public void setExpectation() 
    {
    	_targetter.setExpectation(_expectation);
    	_pather.setExpectation(_expectation);
    }
    
    
    // Initialize the info of event occurrence probability (No initial value )
    public void InitializeMySpawnPattern()
    {
    	_mySpawnPattern = new LitterSpawnPattern();
    	for(int node : _graph.getNodes()) 
    	{
    		_mySpawnPattern.AddSpawnProbability(node, new LitterSpawnProbability(1, 0.00, 1));
    	}
    }
    
    
    // Initialize the info of event occurrence probability (With initial value )
//    public void InitializeMySpawnPattern(LitterSpawnPattern initialPattern) 
//    {
//    	_mySpawnPattern = new LitterSpawnPattern();
//    	for(int node : _graph.getNodes()) 
//    	{
//    		_mySpawnPattern.AddSpawnProbability(node, new LitterSpawnProbability(1, (initialPattern._patterns.get(node).Probability), 1));
//    	}
//    }
    
    
    // set the base node of robot
    public void setBaseNode(int baseNode)
    {
    	_baseNode = baseNode;
    }
    
    // set the target decider
    public void setTargtDecider(ITargetDecider targetter) 
    {
    	_targetter = targetter;
    }
    
    // set the path planner
    public void setPathPlanner(IPathPlanner pather) 
    {
    	_pather = pather;
    }
    
    public LitterSpawnPattern getMySpawnPattern() {return _mySpawnPattern; }
    
    
    // Update the status
    public void Update(ObservedData data) 
    {
    	System.out.println("heybfivcef" + _baseNode);
    	RobotData mydata = data.RobotData._robots.get(RobotID);
    	int position = mydata.Position;

    	int interval = _expectation.getInterval(position, data.Time);
    	

    	int litter = mydata.AccumulatedLitter;
    	    	
    	
    	_visitCounter[position] += 1;
    	_visitHistory.add(position);
    	    	
    	_expectation.Update(data.RobotData, data.Time); // other agents' positions available
 
    	
       if(data.Time == 1000000 || data.Time == 2000000 || data.Time == 2999999)
       {	
        	for(int i=0; i < _nodes.size(); i++) 
        	{
        		if(data.Time == 1000000) _visitCountMemory[0][i] = _visitCounter[i];
        		if(data.Time == 2000000) _visitCountMemory[1][i] = _visitCounter[i] - _visitCountMemory[0][i];
        		if(data.Time == 2999999) _visitCountMemory[2][i] = _visitCounter[i] - _visitCountMemory[0][i] - _visitCountMemory[1][i];
        	}
        }
        
        if(Action == AgentActions.Move) 
        {
        	if(position == _target) 
        	{
        		if(_isChargeRequired || _targetter.IsChargeRequired()) 
        		{
        			Action = AgentActions.Charge;
        			_isChargeRequired = false;	
        		}
        	}
        }
        
        
        else if(Action == AgentActions.Charge) 
        {
        	// when the battery is fully charged
        	if(mydata.BatteryLevel == mydata.Spec.BatteryCapacity) 
        	{
        		Action = AgentActions.Move;
        		_isChargeRequired = false;
        		startTime = data.Time;
        		_pather.setSubgoalsNull();
        	}
        }
        
        else if(Action == AgentActions.Wait) 
        {
        	Action = AgentActions.Move;
        	_target = position;
        }
        
        // Update its own map
        _mySpawnPattern.setLitterSpawnProb(position, litter, interval);
        TargetPathAgentStatus status = new TargetPathAgentStatus(Action, _target, data, _visitCounter, _visitHistory);
        
        _targetter.Update(status);
        
        
        if(_target != _targetter.NextTarget() && !_isChargeRequired) 
        {
        	_target = _targetter.NextTarget();
        	status = new TargetPathAgentStatus(Action, _target, data, _visitCounter, _visitHistory);
        	_pather.Update(status);
        	
        	if(_pather.CanArrive()) 
        	{
        		return;
        	}
        	
        	else 
        	{
        		_target = _baseNode;
        		_isChargeRequired = true;
        	}
        	
        	if(position == _baseNode) 
        	{
        		Action = AgentActions.Charge;
        		return;
        	}
        	
        	status = new TargetPathAgentStatus(Action, _target, data, _visitCounter, _visitHistory);
        }
        
        if(position == _baseNode && _targetter.IsChargeRequired()) 
        {
        	Action = AgentActions.Charge;
        	return;
        }
        
        _pather.Update(status); 
    }
    
  */ 
//    public void CalculateSearchNode() 
//    {
//    	_searchNodes.clear();
//    	List<Pair<Integer, Double>> list = new ArrayList<Pair<Integer, Double>>();
//    	
//    	for(int node : _nodes) 
//    	{
//    		list.add(new Pair<Integer, Double>(node, _mySpawnPattern._patterns.get(node).Probability));
//    	}
//
//    	
//		final Comparator<Pair<Integer, Double>> sortByValue = reverseOrder(comparing(Pair::getValue));
//		Collections.sort(list, sortByValue);
//
//		int count = 0;
//		
//		for(Pair<Integer, Double> p : list) 
//		{
//			_searchNodes.add(p.getKey());
//			count++;
//			if(count == _searchNodeNum) break;
//		}
//    }
//    
//    
//    public void SearchNumberDecrease(int decrease) 
//    {
//    	_searchNodeNum -= decrease;
//    	if(_searchNodeNum > _maxNodeNum) _searchNodeNum = _maxNodeNum;
//    }

    
//    private void setInitialValueToCycle() 
//    {
//    	double averageCollectedEvent = ccAndAve.b / ccAndAve.a;
//    	for(int k = 0; k < cycleValues.length; k++) 
//    	{
//    		cycleValues[k] = averageCollectedEvent;
//    	}
//    	
//    	ccAndAve = new Tuple<Integer, Double>(0, 0.0);
//    }
    
//    private void CycleLearning(){
//    	double averageCollectedEvent = ccAndAve.b / ccAndAve.a;
//    	cycleValues[cycleIndex] = (1 - cycleLearnRatio) * cycleValues[cycleIndex] + cycleLearnRatio * averageCollectedEvent;
//    	
//    	if(forOrderbyRnd.nextDouble() < cycleLearnEpsilon) 
//    	{
//    		cycleIndex = forOrderbyRnd.nextInt(3);
//    	}
//    	else
//    	{
//    		cycleIndex = -1;
//    		double maxValue = -1.0;
//    		for(int i=0; i<cycleValues.length;i++) 
//    		{
//    			if(cycleValues[i] >= maxValue) 
//    			{
//    				if(cycleValues[i] == maxValue && forOrderbyRnd.nextDouble() < 0.5) continue;
//    				else 
//    				{
//    					cycleIndex = i;
//    					maxValue = cycleValues[i];
//    				}
//    			}
//    		}
//    	}
//    	
//    	myCycle = (cycleIndex == 0) ? 300 : (cycleIndex == 1) ? 900 :2700;
//    	cycleCount = maxCycle / myCycle;
//    	ccAndAve = new Tuple<Integer, Double>(0, 0.0);
//    }

//}
