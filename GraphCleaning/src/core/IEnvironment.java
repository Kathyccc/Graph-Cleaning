package core;

public interface IEnvironment {
	
	int CreatRobot(RobotSpec spec, int position);
	
	int SetRobotBase(int chargeValue, int position);
	
	void moveRobot(int id, int node);
	
	void changPattern(LitterSpawnPattern pattern);
	
	void ConnectRobotBase(int id);
	
	void DisconnectRobotBase(int id);
	
	void Clean(int id);
	
	void Update();
	
	int GetTime();
	
	IGraph GetSpacialStructure();
	
	RobotData GetRobotData(int id);
	
	RobotDataCollection GetRobotDataCollection();
	
	LitterDataCollection GetLitterDataCollection();
	
	int GetLitterQuantity();
	
	int GetMaxLitterQuantity();
	
	int[] GetEachLitterQuantity();
	
	LitterSpawnPattern GetLitterSpawnPattern();
}