package com.example.classes;

import java.util.ArrayList;
import java.util.List;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;

public class MyBeacon {
	private Beacon theBeacon;
	private String name = "Nameless";
	private double lastDistance = 1000;
	private double distance = 1000;
	
	public MyBeacon(Beacon b)
	{
		theBeacon = b;
		name = "Initialised";
		setInitialDistance();
	}
	public String getName(){return name;}
	public double getDistance(){ return distance;}
	
	public void setBeacon(Beacon b){ theBeacon = b;}
	public void setName(String input){name = input;}
	
	public Beacon getBeacon(){return theBeacon;}
	
	private double maxRange = 10;//in metres
	
	private double recordedDistance;
	
	private List<Double> distanceList = new ArrayList<Double>(); 
	void setInitialDistance()
	{
		distance = Math.min(Utils.computeAccuracy(theBeacon), maxRange);
		lastDistance = distance;
		
		for (int i = 0; i < 15; i++){
			distanceList.add(distance);
		}
		
	}
	public void updateDistance(){
		
		recordedDistance = Math.min(Utils.computeAccuracy(theBeacon), maxRange);
		distance = recordedDistance;
		
		
		if (recordedDistance != lastDistance)
		{
			lastDistance = distance;
			
			distanceList.remove(0);
			distanceList.add(distance);
			double sum = 0;
			
			for (int i = 0; i < distanceList.size(); i++){
				sum += distanceList.get(i);
			}
			
			distance = sum/distanceList.size();
			
			
			//distance = lerp();
		}
		
	}
	
	
	double lerp()
	{
		return (lastDistance + ((distance - lastDistance)) * 0.01);
	}
	//double distance = Math.min(Utils.computeAccuracy(beaconList.get(i)), 6.0);
}
