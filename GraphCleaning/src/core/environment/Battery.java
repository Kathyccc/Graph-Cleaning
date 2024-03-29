package core.environment;

public class Battery 
{
	int Level;
	int Capacity;
	
	
	public Battery(int capacity) {
		Capacity = capacity;
		setLevel(Capacity);
	}
	
	
	public int getLevel() {
		return this.Level;
	}
	
	
	public void setLevel(int value) {
		if(value > Capacity) Level = Capacity;
		else if(value < 0) Level = 0;
		else Level = value;
	}
	
	
	public int Charge(int value) {
		Level += value;
		return Level;
	}
	
	
	public int Discharge(int value) {
		Level -= value;
		return Level;
	}
}
