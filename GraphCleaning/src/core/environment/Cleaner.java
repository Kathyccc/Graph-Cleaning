package core.environment;

public class Cleaner {
	int AccumulatedLitter;
	int Litter;
	
	public void setAccumulatedLitter(int litter) {
		AccumulatedLitter = litter;
	}
	
	public int getAccumulatedLitter() {
		return AccumulatedLitter;
	}
	
	public void setLitter(int litter) {
		AccumulatedLitter = litter;
	}
	
	public int getLitter() {
		return this.Litter;
	}
	
	
	public void Clean(Litter litter) {
		Litter = litter.Clean();
		AccumulatedLitter += Litter;
//		System.out.println("Cleaner...(position, litter):  " + litter.Position + ", " + AccumulatedLitter);
	}
}
