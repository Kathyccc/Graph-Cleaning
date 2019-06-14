package core;

public interface ISimulation {
	void Run(int steps);
	
	void Step();
	
	void Reset();
}
