package core.environment;

import java.util.ArrayList;
import java.util.List;

public class BatteryCharger {
	List<Battery> _batteries;
	int ChargeRate;
	
	public BatteryCharger(int chargeRate) {
		_batteries = new ArrayList<>();
		ChargeRate = chargeRate;
	}
	
	
	public void Charge() {
		for(Battery battery : _batteries) {
			battery.Charge(ChargeRate);
		}
	}
	
	
	public void Connect(Battery battery) {
		_batteries.add(battery);
	}
	
	
	public void Disconnect(Battery battery) {
		_batteries.remove(battery);
	}
}
