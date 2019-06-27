package core;

public class Litter 
{
	int Quantity;
	String Type;
	boolean IsAccumulated;
	int Position;
	
	public void setQuantity(int quantity) 
	{
		if(quantity < 0) Quantity = 0;
		else Quantity = quantity;
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
		if(IsAccumulated) Quantity += quantity;
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
		
		return quantity;
	}
	
	
	
	
	
	
	
	
	
}

