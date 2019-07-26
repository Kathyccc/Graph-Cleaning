package core;

import java.util.ArrayList;
import java.util.List;

public class LitterDataCollection {
	
	public List<LitterData> _litter;
	
	public LitterDataCollection() {
		_litter = new ArrayList<>();
	}
	
	public void AddLitterData(LitterData litter) {
		_litter.add(litter);
	}
}
