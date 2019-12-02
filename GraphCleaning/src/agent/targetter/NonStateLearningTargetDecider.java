package agent.targetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import agent.AgentActions;
import agent.ITargetDecider;
import agent.TargetPathAgentStatus;
import agent.common.LitterExistingExpectation;
import core.IGraph;
import core.LitterSpawnPattern;
import core.RobotData;
import core.utilities.LogManager;
import core.utilities.LogWriter;

public class NonStateLearningTargetDecider implements ITargetDecider
{
	int _robotID;
	
	List<Integer> _nodes;
	LitterExistingExpectation  _expectation;
	double _alpha;
	double _epsilon;
	Random _rand;
	
	List<ITargetDecider> _deciders;
	Map<ITargetDecider, Double> _Q;
	Map<ITargetDecider, Integer> _deciderNumber;
	ITargetDecider _selectedDecider = null;
	ITargetDecider _preSelectedDecider = null;
	
	LogWriter _qLogger;
	
	double _collectedLitter = 0.0;
	int _preLitter = 0;
	int _travels = 0;
	int _time;
	
	
	public NonStateLearningTargetDecider(int id, IGraph map, LitterSpawnPattern pattern, boolean isAccumulated, int seed) 
	{
		_robotID = id;
		_nodes = new ArrayList<>(map.getNodes());
		_alpha = 0.1;
		_epsilon = 0.05;
		_rand = new Random(seed);
		
		_deciders = new ArrayList<>();
		_Q = new HashMap<>();
		_deciderNumber = new HashMap<>();
		
		_qLogger = LogManager.CreateWriter("QValue" + _robotID);
	}
	
	
	public void setExpectation(LitterExistingExpectation expectation) 
	{
		_expectation = expectation;
		for(ITargetDecider strategy : _deciders) 
		{
			strategy.setExpectation(expectation);
		}
	} 
	
	
	public void setParameters(double alpha, double epsilon) 
	{
		_alpha = alpha;
		_epsilon = epsilon;
	}
	
	
	public void AddTargetDecider(ITargetDecider decider) 
	{
		_deciders.add(decider);
		_Q.put(decider, 1.0);
		_deciderNumber.put(decider,  _deciders.size());
	}
	
	
	public int NextTarget() 
	{
		return _selectedDecider.NextTarget();
	}
	
	
	public boolean IsChargeRequired() 
	{
		return _selectedDecider.IsChargeRequired();
	}
	
	public void Update(TargetPathAgentStatus status) 
	{
		Update(status, false);
	}
	
	
	public void Update(TargetPathAgentStatus status, boolean isSkip) 
	{
		_time = status.ObservedData.Time;
		RobotData mydata = status.ObservedData.RobotData.getRobotData(_robotID);
		
		if(_time % 3600 == 0 && _selectedDecider != null)
			_qLogger.WriteLine(_time + "," + _deciderNumber.get(_selectedDecider) + "," + _Q.get(_selectedDecider));
		
		if (status.Action != AgentActions.Move)
            return;
		
		_travels++;
		
		if(mydata.AccumulatedLitter > 0)
        {
            _collectedLitter++;
        }
		
		//目標更新
        if (mydata.Position == status.TargetNode)
        {
            if (_selectedDecider == null)
            {
                //ランダム選択
                _selectedDecider = _deciders.get(_rand.nextInt(_deciders.size()));
                _preSelectedDecider = _selectedDecider;
            }
            else
            {
                //実ごみ回収
                double reward = _collectedLitter / _travels;  //(double)(mydata.AccumulatedLitter - _preLitters) / _travels;

                _collectedLitter = 0;
                //_preLitters = mydata.AccumulatedLitter;
                
                
                UpdateQ(reward);
                selectDecider();
                if (_selectedDecider != _preSelectedDecider)
                {
                    _preSelectedDecider.ResetState();
                    _preSelectedDecider = _selectedDecider;
                }
                _selectedDecider.Update(status);
            }
            _travels = 0;
        }
	}
	
	
	public void UpdateQ(double reward) 
	{
		double q = (1.0 - _alpha) * _Q.get(_selectedDecider) + _alpha * reward;
		_Q.put(_selectedDecider, q);
	}
	
	public void selectDecider() 
	{
		if(_rand.nextDouble() < _epsilon) 
		{
			_selectedDecider = _deciders.get(_rand.nextInt(_deciders.size()));
		}
		else 
		{
			double max = - (Double.MAX_VALUE);
			ITargetDecider maxDecider = null;
			for(ITargetDecider decider : _deciders) 
			{
				if(_Q.get(decider) > max) 
				{
					max = _Q.get(decider);
					maxDecider = decider;
				}
			}
			_selectedDecider = maxDecider;
		}
	}

	public void ResetState() {}
}
