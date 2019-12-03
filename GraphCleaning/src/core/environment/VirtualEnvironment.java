package core.environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import core.IEnvironment;
import core.IGraph;
import core.LitterDataCollection;
import core.LitterSpawnPattern;
import core.RobotData;
import core.RobotDataCollection;
import core.RobotSpec;
import core.environment.controls.BatteryChargeControl;
import core.environment.controls.CleaningControl;
import core.environment.controls.FieldSettingControl;
import core.environment.controls.LitterSpawnControl;
import core.environment.controls.ObservationControl;
import core.environment.controls.RobotMoveControl;
import core.utilities.LogManager;
import core.utilities.LogWriter;

public class VirtualEnvironment implements IEnvironment
{
	Field _field;
	
	int _state = 0;
	
	FieldSettingControl _fieldSettingControl;
	BatteryChargeControl _batteryChargeControl;
	CleaningControl _cleaningControl;
	LitterSpawnControl _litterSpawnControl;
	RobotMoveControl _robotMoveControl;
	ObservationControl _observer;
	Random _rand;
//	LogWriter _AverageRemainingLitterLogger;
	List<Integer> _high;
	List<Integer> _middle;
	List<Integer> _low;
//	List<Integer>[][] _eachNodeQuantity = new ArrayList[10201][2];
//	int[][] _eachNodeLitterAmount = new int[10201][2];
	
	int Initializing;
	int LitterSpawning = 1;
	int RobotMoving = 2;
	int RobotCreating = 3;
	int TimeUpdating = 4;
	
	public int getState() 
	{
		return _state;
	}
	
	public VirtualEnvironment(IGraph spatialStructure, LitterSpawnPattern spawnPattern, boolean isLitterAccumulated, int seed, List<Integer> high, List<Integer> middle, List<Integer> low)
	{
		_rand = new Random(seed);
		_field = new Field(spatialStructure, spawnPattern);
//		System.out.println("virtualEnvironment     " + _field.Litter._litter.isEmpty());
		_fieldSettingControl = new FieldSettingControl(_field);
		_batteryChargeControl = new BatteryChargeControl(_field);
		_cleaningControl = new CleaningControl(_field, _rand.nextInt());
		_litterSpawnControl = new LitterSpawnControl(_field, _rand.nextInt());
		_robotMoveControl = new RobotMoveControl(_field);
		_observer = new ObservationControl(_field);
		_high = high;
		_middle = middle;
		_low = low;
		
		
//		_AverageRemainingLitterLogger = LogManager.CreateWriter("AverageRemainingLitter");
//		_AverageRemainingLitterLogger.WriteLine("" + "," + "AverageRL*T" + "," + "AverageRTime" + "," + "WorstRL*T-Node" + "," + "WorstRT-Node");
		
		for(int node : spatialStructure.getNodes()) 
		{
			_fieldSettingControl.CreateLitter("non", node, isLitterAccumulated);
		}
		
		
//		for(int i = 0; i<10201; i++) 
//		{
//			_eachNodeQuantity[i][0] = new ArrayList<>();
//			_eachNodeQuantity[i][1] = new ArrayList<>();
//		}
	}
	
	
	public int CreateRobot(RobotSpec spec, int position) 
	{
		return _fieldSettingControl.CreateRobot(spec, position);
	}


	public int SetRobotBase(int chargeValue, int position) 
	{
		return _fieldSettingControl.CreateRobotBase(chargeValue, position);
	}


	public void moveRobot(int id, int node) 
	{
		_robotMoveControl.Move(id, node);
	}

	
	public void ConnectRobotBase(int id) 
	{
		_batteryChargeControl.Connect(id);
	}

	public void DisconnectRobotBase(int id) 
	{
		_batteryChargeControl.Disconnect(id);
	}

	public void Clean(int id) 
	{
		_cleaningControl.Clean(id);
	}

	public void Update() 
	{
		switch(_state) 
		{
		case 0:
			_state = 1;
			break;
		
		case 1:
			_litterSpawnControl.Update();
			_state = 2;
			break;
			
		case 2:
			_batteryChargeControl.Update();
			_robotMoveControl.Update();
			_state = 3;
			break;
			
		case 3:
			_state = 4;
			List<Integer> _taskLog = _cleaningControl.Update();
//			MemorizedLitter(_taskLog);
//			if(_observer.getTime() == 1000000 || _observer.getTime() == 2000000 || _observer.getTime() == 2999999) 
//			{
//				OutputAverageLitter();
//			}
			_state = 4;
			break;
		
		case 4:
			_fieldSettingControl.Update();
			_state = 1;
			break;
		}
	}

	
//	public void MemorizedLitter(List<Integer> task) 
//	{
//		LitterCollection litterColl = _field.Litter;
//		for(Litter litter : litterColl._litter.values()) 
//		{
//			List<Integer> _task = task;
//			int position = litter.getPosition();
//			int quantity = litter.getQuantity();
//			
//			if(_task.contains(new Integer(position))) 
//			{
//				_eachNodeQuantity[position][0].add(_eachNodeLitterAmount[position][0]);
//				_eachNodeQuantity[position][1].add(_eachNodeLitterAmount[position][1]);
//				_eachNodeLitterAmount[position][0] = 0;
//				_eachNodeLitterAmount[position][1] = 0;
//			}
//			else 
//			{
//				_eachNodeLitterAmount[position][0] += quantity;
//				if(quantity > 0) _eachNodeLitterAmount[position][1]++;
//			}
//		}
//	}
	
	
//	public void OutputAverageLitter() 
//	{
//		for(int i=0; i<10201; i++) 
//		{
//			for(int j=0; j<2; j++) 
//			{
//				if(_eachNodeQuantity[i][j].size() == 0) 
//				{
//					_eachNodeQuantity[i][j].add((int) Short.MIN_VALUE);
//				}
//			}
//			
//			double HighRegionAveRT = 0.0;
//			double MiddleRegionAveRT = 0.0;
//			double LowRegionAveRT = 0.0;
//			
//			double HighRegionAveTime = 0.0;
//			double MiddleRegionAveTime = 0.0;
//			double LowRegionAveTime = 0.0;
//			
//			int[] _nonLitterCount = new int[3];
//			
//			int _worstValueRT = 0;
//			int _worstRTNode = -1;
//			int _worstTNode = -1;
//			int _worstValueT = 0;
//			
//			double sumRT = 0;
//			double sumT = 0;
//			
//			double sum_R = 0.0;
//			double sum_T = 0.0;
//			
//			for(int k=0; k<10201; k++) 
//			{
//				for(Integer quantity: _eachNodeQuantity[k][0]) 
//				{
//					sum_R += quantity;
//				}
//				double RT = (sum_R/_eachNodeQuantity[k][0].size());
//				
//				for(Integer quantity: _eachNodeQuantity[k][1]) 
//				{
//					sum_T += quantity;
//				}
//				
//				double T = (sum_T/_eachNodeQuantity[k][1].size());
//				
//				if(RT >= 0) 
//				{
//					sumRT += RT;
//					sumT += T;
//				}
//				
//				if(_worstValueRT < Collections.max(_eachNodeQuantity[k][0])) 
//				{
//					_worstValueRT = Collections.max(_eachNodeQuantity[k][0]);
//					_worstRTNode = k;
//				}
//				
//				if(_worstValueT < Collections.max(_eachNodeQuantity[k][1])) 
//				{
//					_worstValueT = Collections.max(_eachNodeQuantity[k][1]);
//					_worstTNode = k;
//				}
//				
//				
//				if (_high.contains(k))
//                {
//                    if (RT < 0)
//                        _nonLitterCount[0]++;
//                    else
//                    {
//                    	HighRegionAveRT += RT;
//                        HighRegionAveTime += T;
//                    }
//                }
//                else if(_middle.contains(i))
//                {
//                    if (RT < 0)
//                        _nonLitterCount[1]++;
//                    else
//                    {
//                        MiddleRegionAveRT += RT;
//                        MiddleRegionAveTime += T;
//                    }
//                }
//
//                else if(_low.contains(i))
//                {
//                    if (RT < 0)
//                        _nonLitterCount[2]++;
//                    else
//                    {
//                        LowRegionAveRT += RT;
//                        LowRegionAveTime += T;
//                    }
//                }
//			}
//			
//			int hightmp = _high.size() - _nonLitterCount[0];
//			int midtmp = _middle.size() - _nonLitterCount[1];
//			int lowtmp = _low.size() - _nonLitterCount[2];
//			
//			int countedNode = (10201 - _nonLitterCount[0] - _nonLitterCount[1] - _nonLitterCount[2]);
//			double aveRT = sumRT/ countedNode;
//			double aveT = sumT / countedNode;
			
//			_AverageRemainingLitterLogger.WriteLine("WholeAvgerage" + "," + aveRT + "," + aveT);
//            _AverageRemainingLitterLogger.WriteLine("HighNodesAvgerage" + "," + HighRegionAveRT/hightmp + "," + HighRegionAveTime/hightmp);
//            _AverageRemainingLitterLogger.WriteLine("MiddleNodesAvgerage" + "," + MiddleRegionAveRT / midtmp + "," + MiddleRegionAveTime / midtmp);
//            _AverageRemainingLitterLogger.WriteLine("LowNodesAvgerage" + "," + LowRegionAveRT / lowtmp + "," + LowRegionAveTime / lowtmp);
//            _AverageRemainingLitterLogger.WriteLine("WorstValue" + "," + _worstValueRT + "," + _worstValueT + "," + _worstRTNode +","+ _worstTNode);
//            
//            
//            for (int i1 = 0; i1 < 10201; i1++)
//            {
//                _eachNodeQuantity[i1][0].clear();
//                _eachNodeQuantity[i1][1].clear();
//            }
//		}
//	}
	
	
	public int GetTime() 
	{
		return _observer.getTime();
	}

	public IGraph GetSpatialStructure() 
	{
		return _observer.getSpatialStructure();
	}

	public RobotData GetRobotData(int id) 
	{
		return _observer.getRobotData(id);
	}

	public RobotDataCollection GetRobotDataCollection() 
	{
		return _observer.getRobotDataCollection();
	}

	public LitterDataCollection GetLitterDataCollection() 
	{
		return _observer.getLitterDataCollection();
	}

	
	public int GetLitterQuantity() 
	{
		return _observer.getLitterQuantity();
	}

	public int GetMaxLitterQuantity() 
	{
		return _observer.getMaxLitterQuantity();
	}

	public int[] GetEachLitterQuantity() 
	{
		return _observer.getEachLitterQuantity();
	}

	public LitterSpawnPattern GetLitterSpawnPattern() 
	{
		return _observer.getLitterSpawnPattern();
	}
}

