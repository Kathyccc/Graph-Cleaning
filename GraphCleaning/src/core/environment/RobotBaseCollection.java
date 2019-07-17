package core.environment;

import java.util.HashMap;
import java.util.Map;

public class RobotBaseCollection 
{
	Map<Integer, RobotBase> _bases = new HashMap<>();
	
	public void Add(RobotBase base) {
		_bases.put(base.Position, base);
	}
	
	
	public void Remove(RobotBase base) {
		_bases.remove(base.Position);
	}
	
	
	public RobotBase getRobotBase(int position) {
		return _bases.get(position);
	}
	
	
	public Map<Integer, RobotBase> getBasesMap(){
		return _bases;
	}
}
