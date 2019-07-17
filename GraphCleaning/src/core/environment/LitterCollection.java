package core.environment;

import java.util.HashMap;
import java.util.Map;

public class LitterCollection 
{
	public Map<Integer, Litter> _litter;
	
	public LitterCollection() {
		_litter = new HashMap<>();
	}
	

	public void Add(Litter litter) {
		_litter.put(litter.Position, litter);
	}
	
	public boolean Remove(Litter litter) {
		_litter.remove(litter.Position);
		if(_litter.containsValue(litter) == false) return true;
		else return false;
	}
	
	public Litter getLitter(int position) {
		return _litter.get(position);
	}	
	
	
	public Map<Integer, Litter> getLitterCollection(){
		return _litter;
	}
}
