package core.environment.controls;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import core.IGraph;
import core.LitterData;
import core.LitterDataCollection;
import core.LitterSpawnPattern;
import core.RobotData;
import core.RobotDataCollection;
import core.environment.Field;
import core.environment.Litter;
import core.environment.LitterCollection;
import core.environment.Robot;

public class ObservationControl {
	
	Field _field;
	
	public ObservationControl(Field field) {
		_field = field;
	}
	
	
	public int getTime() {
		return _field.Time;
	}
	
	
	public IGraph getSpatialStructure() {
		return _field.SpatialStructure;
	}
	
	
	public RobotData getRobotData(int id) {
		Robot robot = _field.Robots.getRobot(id);
		
		return new RobotData(
				robot.ID, 
				robot.getBatteryLevel(),
				robot.Position,
				robot.getAccumulatedLitter(),
				robot.Litter,
				robot.Spec);
	}
	
	
	public RobotDataCollection getRobotDataCollection() {
		
		RobotDataCollection rDataCollection = new RobotDataCollection();
		//Map<Integer, RobotData> robotData = rDataCollection._robots;
		
		Map<Integer, Robot> robots = new HashMap<>();
		robots = _field.Robots._robots;
		
		for(Robot robot : robots.values()) {
			RobotData data = new RobotData(robot.ID, robot.getBatteryLevel(), robot.Position, robot.getAccumulatedLitter(), robot.Litter, robot.Spec);
			rDataCollection.Add(data);
		} 
		
		return new RobotDataCollection(rDataCollection, true);
	}
	
	
	public LitterDataCollection getLitterDataCollection() 
	{
		LitterDataCollection LitterDC = new LitterDataCollection();
		
		LitterCollection litterCollection = _field.getLitter();
		
		for(Litter litter : litterCollection._litter.values()) 
		{			
			//System.out.println("testing..." + litter.Quantity);
			if(litter.Quantity != 0) {
				LitterData data = new LitterData(litter.Position, litter.Type, litter.Quantity);
				LitterDC.AddLitterData(data);
			}
		}
		return LitterDC;
	}
	
	
	public int getLitterQuantity() 
	{
		int quantity = 0;
		
		for(Litter litter : _field.Litter._litter.values()) 
		{
			quantity += litter.Quantity;
//			System.out.println("ObservationControl..." + litter.Quantity);
		}
		
		return quantity;
	}
	
	
	public int getMaxLitterQuantity() 
	{
		int[] tmp = new int[_field.Litter._litter.size()];
		
		for(Map.Entry<Integer, Litter> litterSet : _field.Litter._litter.entrySet()) 
		{
			Litter litter = litterSet.getValue();
			tmp[litter.Position] += litter.Quantity;
		}
		
		return Arrays.stream(tmp).max().getAsInt();
	}
	
	
	public int[] getEachLitterQuantity() 
	{
		int[] tmp = new int[_field.Litter._litter.size()];
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
