package agent.common;

import java.util.ArrayList;
import java.util.List;

public class ChangeFinder 
{
	double _pdfMulVal = -0.5;
	int _arDimension;
	int _smoothingWindow;
	double _forgetParameter;
	List<Double> pastData;
    double _estimatedMyu;
    double _estimatedSigma;
    double[] pdfBase;
    double[] yuleWalkerAns;
    List<Double> scores;
    ChangeFinder _secondChangeFinder;
    
    
    public ChangeFinder(int arDimension, int smoothingWindow, double forgetParameter, boolean hasSecond)
    {
    	_arDimension = arDimension;
    	_smoothingWindow = smoothingWindow;
    	_forgetParameter = forgetParameter;
    	pastData = new ArrayList<Double>();
    	pdfBase = new double[arDimension + 1];
    	yuleWalkerAns = new double[arDimension + 1];
    	scores = new ArrayList<>();
    	
    	if(hasSecond == true) {
    		_secondChangeFinder = new ChangeFinder(arDimension, smoothingWindow, forgetParameter, false);
    	}
    }
    
    
    public double calculateScore(double input) 
    {
    	pastData.add(0, input);
    	int datalength = pastData.size();
    	
    	_estimatedMyu = (1 - _forgetParameter) * _estimatedMyu + _forgetParameter * input;
    	
    	int pdfBaseNum = Math.min(datalength, _arDimension + 1);
    	
    	for(int i=0; i < pdfBaseNum; i++) 
    	{
    		pdfBase[i] = (1 - _forgetParameter) * pdfBase[i] + _forgetParameter*(input - _estimatedMyu)* Math.pow(pastData.get(i)- _estimatedMyu, 1);
    	}
    	
    	
    	for(int yuleIndex = 1; yuleIndex < pdfBaseNum; yuleIndex++) 
    	{
    		double nowPdfBase = pdfBase[yuleIndex];
    		
    		for(int i=1; i<yuleIndex; i++) 
    		{
    			nowPdfBase = nowPdfBase - (pdfBase[yuleIndex - i] * yuleWalkerAns[i]);
    		}
    		
    		double yuleResult = nowPdfBase / pdfBase[0];
    		yuleWalkerAns[yuleIndex] = yuleResult;
    	} 
    	
    	double estimatedValue = _estimatedMyu;
    	
    	for(int i=1; i < pdfBaseNum; i++) 
    	{
    		double dataResult = pastData.get(i);
    		double dist = yuleWalkerAns[i] * (dataResult - _estimatedMyu);
    		estimatedValue = estimatedValue + dist;
    	}
    	
    	if(pastData.size() > _arDimension) pastData.remove(pastData.size()-1);
    	
    	_estimatedSigma = (1 - _forgetParameter) * _estimatedSigma + _forgetParameter * Math.pow((input-estimatedValue), 2);
    	
    	double firstScore = calcFirstScore(estimatedValue, input, _estimatedSigma, _smoothingWindow);
    	 
    	scores.add(firstScore);
    	if(scores.size() > _smoothingWindow) scores.remove(0);
    	
    	double movingAverage = smoothing(scores, _smoothingWindow);
    	
    	if(_secondChangeFinder == null) return movingAverage;
    	
    	double secondScore = _secondChangeFinder.calculateScore(movingAverage);
    	
    	return secondScore;
    }
    
    
    public static double smoothing(List<Double> scores, int smoothingWindow) 
    {
    	if(scores == null || scores.size() == 0) return 0.0;
    	
    	double scoreSum = 0.0;
    	
    	for(double score : scores) 
    	{
    		scoreSum = scoreSum + score;
    	}
    	
    	return scoreSum / smoothingWindow;
    }
    
    
    public double calcFirstScore(double estimatedValue, double realValue, double estimatedSigma, int smoothingWindow) 
    {
    	if(estimatedSigma == 0) return 0.0;
    	
    	else 
    	{
    		double logNumerator = Math.exp(((_pdfMulVal) * Math.pow(realValue - estimatedValue, 2)) / estimatedSigma); 
    		double logDenominator = Math.sqrt(2 * Math.PI) * Math.sqrt(Math.abs(estimatedSigma));
    		double logValue = logNumerator / logDenominator;
    		
    		return -Math.log(logValue);
    	}
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
