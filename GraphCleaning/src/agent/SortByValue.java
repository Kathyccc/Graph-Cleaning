package agent;

import java.util.Comparator;

import core.Pair;

public class SortByValue implements Comparator<Pair<Integer, Double>>
{

	@Override
	public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) 
	{
		return (int) (o1.getValue() - o2.getValue());
	}
}
	
