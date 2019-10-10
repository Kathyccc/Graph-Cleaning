package agent;

import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import agent.common.LitterExistingExpectation;
import core.CommunicationDetails;
import core.Coordinate;
import core.GridGraph;
import core.LitterSpawnPattern;
import core.LitterSpawnProbability;
import core.ObservedData;
import core.Pair;
import core.RobotData;
import core.Tuple;
import core.agents.IAgent;
import core.utilities.LogManager;
import core.utilities.LogWriter;


public class TargetPathAgentPDALearning implements IAgent
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
	List<Integer> _searchNodes;
	int _searchNodeNum;
	List<Integer> _excludeNodes = new ArrayList<>();
	Coordinate _myCenterNode = new Coordinate(0,0);
	double _myCenterNodeWeight = 0.0;
	CommunicationDetails _communicationDetail;
	LogWriter _SearchNodeListup;
	int[] _visitCounter;
	public int[][] _visitCountMemory;
	List<Integer> _visitHistory;
	int vHSize = 36867;
	int startPhase = 0;
    int canMoveMaxCount = 900;
    int myCycle; 
    double[] cycleValues = new double[] { 0,0,0}; 
    int cycleLearnTerm = 100000;
    Tuple<Integer, Double> ccAndAve = new Tuple<Integer, Double>(0, 0.0);//cycle count and average collected event (per 1 tick)
    double cycleLearnRatio = 0.1;
    double cycleLearnEpsilon = 0.05;
    int cycleIndex;
    boolean isCycleLearning = false;
    int collectedCycleLitter = 0;
    LogWriter _cycleValues;
    int startTime = 0;
    int currentCycle = 0;
    int maxCycle = 2700;
    int cycleCount;
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
    public TargetPathAgentPDALearning(int robotID, GridGraph graph, LitterSpawnPattern spawnPattern, List<Integer> excludeNodes)
    {
    	RobotID = robotID;
        forOrderbyRnd = new Random(robotID + 7);
        Action = AgentActions.Wait;
        _excludeNodes = excludeNodes;
        _graph = graph;
        _nodes = new ArrayList<Integer>(_graph.getNodes());
        _visitCounter = new int[_graph.getNumOfNodes()];

        for(int node : excludeNodes) { 
        	_nodes.remove(new Integer(node)); 
        }
        
        _searchNodes = new ArrayList<>(_nodes);
        
        for(int i = 0; i < _graph.getNumOfNodes(); i++) {
        	_visitCounter[i] = 1;
        }
        
        _visitCountMemory = new int[3][_nodes.size()];
        _visitHistory = new ArrayList<>();
        
        _searchNodeNum = _nodes.size();
        _maxNodeNum = _nodes.size();
        _spawnPattern = spawnPattern;
        InitializeMySpawnPattern();
        
        _expectation = new LitterExistingExpectation (_mySpawnPattern, true);
        _communicationDetail = new CommunicationDetails(_myCenterNode, _myCenterNodeWeight, _mySpawnPattern, _searchNodes);
        LogManager.setLogDirectory("/Search Node List");
        _SearchNodeListup = LogManager.CreateWriter("SearchNodeList-" + RobotID);
        LogManager.setLogDirectory("/Cycle Values");
        _cycleValues = LogManager.CreateWriter("CycleValues-" + RobotID);
        _cycleValues.WriteLine("" + "," + "c300" + "," + "c900" + "," + "c2700");
        cycleIndex = forOrderbyRnd.nextInt(3);
        myCycle = (cycleIndex == 0) ? 300 : (cycleIndex == 1) ? 900 : 2700;
        cycleCount = maxCycle / myCycle;   
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
    public void InitializeMySpawnPattern(LitterSpawnPattern initialPattern) 
    {
    	_mySpawnPattern = new LitterSpawnPattern();
    	for(int node : _graph.getNodes()) 
    	{
    		_mySpawnPattern.AddSpawnProbability(node, new LitterSpawnProbability(1, (initialPattern._patterns.get(node).Probability), 1));
    	}
    }
    
    
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

    	RobotData mydata = data.RobotData.getRobotData(RobotID);
    	int position = mydata.Position;

    	int interval = _expectation.getInterval(position, data.Time);

    	int litter = mydata.Litter;
    	collectedCycleLitter += litter;

    	
    	_visitCounter[position]++;
    	_visitHistory.add(position);
    	
    	if(_visitHistory.size() > vHSize) { _visitHistory.remove(0);}
    	
    	_expectation.Update(data.RobotData, data.Time); // other agents' positions available
        //_expectation.Update(position, data.Time);  //other agents' positions unavailable
    	
    	//System.out.println("print the time: " + data.Time);
    	if(data.Time % 10000 == 0) 
    	{ 
    		_cycleValues.WriteLine(data.Time + "," + cycleValues[0] + "," + cycleValues[1] + "," + cycleValues[2]);
    	}
    	
        if(data.Time == 1000000 || data.Time == 2000000 || data.Time == 2999999)
        {
        	for(int node : _searchNodes) 
        	{
        		_SearchNodeListup.WriteLine(node + "," + _mySpawnPattern._patterns.get(node).Probability);
        	}
        	
        	_SearchNodeListup.WriteLine("");
        	
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
        			CalculateSearchNode();
        			CalculateCenterNode();
        			ccAndAve = new Tuple<Integer, Double>(ccAndAve.a + 1, ccAndAve.b + ((double)collectedCycleLitter / (double)(data.Time - startTime)));
        			currentCycle = data.Time - startTime;
                    collectedCycleLitter = 0;
        			
                    if (isCycleLearning == true)
                    {
                        if (ccAndAve.a == cycleCount)
                        {
                            CycleLearning();
                        }
                    }
                    
                    else 
                    {
                    	if(data.Time > cycleLearnTerm) 
                    	{
                    		setInitialValueToCycle();
                    		isCycleLearning = true;
                            cycleIndex = forOrderbyRnd.nextInt(3);
                            myCycle = (cycleIndex == 0) ? 300 : (cycleIndex == 1) ? 900 : 2700;
                            cycleCount = maxCycle / myCycle;
                    	}
                    	else
                    	{
                    		cycleIndex = forOrderbyRnd.nextInt(3);
                            myCycle = (cycleIndex == 0) ? 300 : (cycleIndex == 1) ? 900 : 2700;
                    	}
                    }	
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
        TargetPathAgentStatus status = new TargetPathAgentStatus(Action, _target, data, _searchNodes, _visitCounter, _visitHistory, myCycle);
        
        _targetter.Update(status);
        
        if(_target != _targetter.NextTarget() && !_isChargeRequired) 
        {
        	_target = _targetter.NextTarget();
        	status = new TargetPathAgentStatus(Action, _target, data, myCycle);
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
        	
        	status = new TargetPathAgentStatus(Action, _target, data, myCycle);
        }
        
        if(position == _baseNode && _targetter.IsChargeRequired()) 
        {
        	Action = AgentActions.Charge;
        	return;
        }
        
        _pather.Update(status); 
    }
    
    public void CalculateCenterNode() 
    {
    	double sumPDA = 0.0;
    	for(int node : _searchNodes) 
    	{
    		sumPDA += _mySpawnPattern._patterns.get(node).Probability;
    	}
    	_communicationDetail._myExperienceWeight = sumPDA;
    	
    	double sumX = 0.0;
    	double sumY = 0.0;
    	
    	for(int node : _searchNodes) 
    	{
    		double n = (sumPDA==0) ? 0.0 : (_mySpawnPattern._patterns.get(node).Probability/sumPDA);
    		sumX += n * _graph.getCoordinate(node).X;
    		sumY += n * _graph.getCoordinate(node).Y;
    	}
    	
    	Coordinate centerCandidate = new Coordinate((int)Math.round(sumX), (int)Math.round(sumY));
    	Coordinate centerNode = _graph.getClosestNode(centerCandidate);
    	_myCenterNode.X = centerNode.X;
    	_myCenterNode.Y = centerNode.Y;
    	
    	_communicationDetail._myCenterNode = _myCenterNode;
    }
    
   
    
    public void CalculateSearchNode() 
    {
    	_searchNodes.clear();
    	List<Pair<Integer, Double>> list = new ArrayList<Pair<Integer, Double>>();
    	
    	for(int node : _nodes) 
    	{
    		list.add(new Pair<Integer, Double>(node, _mySpawnPattern._patterns.get(node).Probability));
    	}
    	
//		List<Pair<Integer, Double>> sort_list;
//		
//		Collections.sort(list, new Comparator<Pair<Integer, Double>>() {
//		    @Override
//		    public int compare(final Pair<Integer, Double> o1, final Pair<Integer, Double> o2) {
//		        return (int)(o1.getValue() - o2.getValue());
//		    }
//		});
    	
		final Comparator<Pair<Integer, Double>> sortByValue = reverseOrder(comparing(Pair::getValue));
		Collections.sort(list, sortByValue);

		int count = 0;
		
		for(Pair<Integer, Double> p : list) 
		{
			_searchNodes.add(p.getKey());
			count++;
			if(count == _searchNodeNum) break;
		}
		 
		 

    }
    

//	public class SortByValue implements Comparator<Pair<Integer, Double>>
//    {
//    	@Override
//    	public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) 
//    	{
//    		return (int) (o1.getValue() - o2.getValue());
//    	}
//    }
    
    public void SearchNumberDecrease(int decrease) 
    {
    	_searchNodeNum -= decrease;
    	if(_searchNodeNum > _maxNodeNum) _searchNodeNum = _maxNodeNum;
    }
    
    public CommunicationDetails getCommunicateDetails() 
    {
    	return _communicationDetail;
    }

    
    private void setInitialValueToCycle() 
    {
    	double averageCollectedEvent = ccAndAve.b / ccAndAve.a;
    	for(int k = 0; k < cycleValues.length; k++) 
    	{
    		cycleValues[k] = averageCollectedEvent;
    	}
    	
    	ccAndAve = new Tuple<Integer, Double>(0, 0.0);
    }
    
    private void CycleLearning(){
    	double averageCollectedEvent = ccAndAve.b / ccAndAve.a;
    	cycleValues[cycleIndex] = (1 - cycleLearnRatio) * cycleValues[cycleIndex] + cycleLearnRatio * averageCollectedEvent;
    	
    	if(forOrderbyRnd.nextDouble() < cycleLearnEpsilon) 
    	{
    		cycleIndex = forOrderbyRnd.nextInt(3);
    	}
    	else
    	{
    		cycleIndex = -1;
    		double maxValue = -1.0;
    		for(int i=0; i<cycleValues.length;i++) 
    		{
    			if(cycleValues[i] >= maxValue) 
    			{
    				if(cycleValues[i] == maxValue && forOrderbyRnd.nextDouble() < 0.5) continue;
    				else 
    				{
    					cycleIndex = i;
    					maxValue = cycleValues[i];
    				}
    			}
    		}
    	}
    	
    	myCycle = (cycleIndex == 0) ? 300 : (cycleIndex == 1) ? 900 :2700;
    	cycleCount = maxCycle / myCycle;
    	ccAndAve = new Tuple<Integer, Double>(0, 0.0);
    }

}
