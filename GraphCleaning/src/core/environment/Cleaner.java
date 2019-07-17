package core.environment;

public class Cleaner {
	int AccumulatedLitter;
	int Litter;
	
	public void setAccumulatedLitter(int litter) {
		this.AccumulatedLitter = litter;
	}
	
	public int getAccumulatedLitter() {
		return this.AccumulatedLitter;
	}
	
	public void setLitter(int litter) {
		this.AccumulatedLitter = litter;
	}
	
	public int getLitter() {
		return this.Litter;
	}
	
	
	public void Clean(Litter litter) {
		Litter = litter.Clean();
		AccumulatedLitter += Litter;
	}
}
