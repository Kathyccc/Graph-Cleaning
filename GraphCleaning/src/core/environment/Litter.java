package core.environment;

public class Litter 
{
	public int Quantity;
	public String Type;
	boolean IsAccumulated;
	public int Position;
	
	
	public Litter(String type, int position, boolean isAccumulated) {
		Type = type;
		Position = position;
		IsAccumulated = isAccumulated;
	}
	
	public int getQuantity() 
	{
		return Quantity;
	}
	
	public void setType(String type) 
	{
		Type = type;
	}
	
	public String getType() 
	{
		return Type;
	}
	
	public void setIsAccumulated(boolean YoN)
	{
		IsAccumulated = YoN;
	}
	
	public boolean getIsAccumulated() 
	{
		return IsAccumulated;
	}
	
	public void setPosition(int position) {
		Position = position;
	}
	
	public int getPosition() {
		return Position;
	}
	
	public void Increase(int quantity) 
	{
		if(IsAccumulated) 
		{
			Quantity =  Quantity + quantity;
		}
		else Quantity = quantity;
	}

	
	//<return> the amount of cleaned trash
	public int Clean(int quantity) 
	{
		int temp = quantity;
		if(this.Quantity < quantity) {
			temp = Quantity;
		}
		Quantity -= temp;
		return temp;
	}
	
	public int Clean() 
	{
		int quantity = Quantity;
		Quantity = 0;
		
//		System.out.println("Litter:  " + quantity);
		return quantity;
	}
}

