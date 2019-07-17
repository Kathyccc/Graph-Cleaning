package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObservationControl {
	Field _field;
	
	public ObservationControl(Field field) {
		_field = field;
	}
	
	
	public int getTime() {
		return _field.Time;
	}
	
	public IGraph getSpacialStructure() {
		return _field.SpacialStructure;
	}
	
	
	public RobotData getRobotData(int id) {
		Robot robot = _field.Robots.getRobot(id);
		
		return new RobotData(
				robot.ID, 
				robot.BatteryLevel,
				robot.Position,
				robot.AccumulatedLitter,
				robot.Litter,
				robot.Spec);
	}
	
	
	public RobotDataCollection getRobotDataCollection() {
		
		RobotDataCollection rDataCollection = new RobotDataCollection();
		//Map<Integer, RobotData> robotData = rDataCollection._robots;
		
		Map<Integer, Robot> robots = new HashMap<>();
		robots = _field.Robots._robots;
		
		for(Map.Entry<Integer, Robot> robotSet : robots.entrySet()) {
			Robot robot = robotSet.getValue();
			RobotData data = new RobotData(robot.ID, robot.BatteryLevel, robot.Position, robot.AccumulatedLitter, robot.Litter, robot.Spec);
			rDataCollection.Add(data);
		}
		
		return new RobotDataCollection(rDataCollection, true);
	}
	
	
	public LitterDataCollection getLitterDataCollection() 
	{
		LitterDataCollection collection = new LitterDataCollection();
		List<Integer> tmp = new ArrayList<Integer>();
		
		for(Map.Entry<Integer, Litter> litterSet : _field.Litter._litter.entrySet()) 
		{
			Litter litter = litterSet.getValue();
			if(litter.Quantity != 0) {
				LitterData data = new LitterData(litter.Position, litter.Type, litter.Quantity);
				collection.AddLitterData(data);
				tmp.add(litter.Position);
			}
		}
		
		return collection;
	}
	
	
	public int getLitterQuantity() 
	{
		int quantity = 0;
		
		for(Map.Entry<Integer, Litter> litterSet : _field.Litter._litter.entrySet()) 
		{
			Litter litter = litterSet.getValue();
			quantity += litter.Quantity;
		}
		
		return quantity;
	}
	
	
	public int getMaxLitterQuantity() 
	{
		int[] tmp = new int[Integer.MAX_VALUE];
		
		for(Map.Entry<Integer, Litter> litterSet : _field.Litter._litter.entrySet()) 
		{
			Litter litter = litterSet.getValue();
			tmp[litter.Position] += litter.Quantity;
		}
		
		return Arrays.stream(tmp).max().getAsInt();
	}
	
	
	public int[] getEachLitterQuantity() 
	{
		int[] tmp = new int[Integer.MAX_VALUE];
		for(Map.Entry<Integer, Litter> litterSet : _field.Litter._litter.entrySet()) 
		{
			Litter litter = litterSet.getValue();
			tmp[litter.Position] += litter.Quantity;
		}
		
		return tmp;
	}
	
	
	public LitterSpawnPattern getLitterSpawnPattern() 
	{
		return _field.LitterSpawnPattern;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
