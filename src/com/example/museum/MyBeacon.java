package com.example.museum;

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
	
	private double maxRange = 4.0;//in metres
	
	private double recordedDistance;
	void setInitialDistance()
	{
		distance = Math.min(Utils.computeAccuracy(theBeacon), maxRange);
		lastDistance = distance;
		
	}
	public void updateDistance(){
		
		recordedDistance = Math.min(Utils.computeAccuracy(theBeacon), maxRange);
		distance = recordedDistance;
		
		if (recordedDistance != lastDistance)
		{
			lastDistance = distance;
			distance = lerp();
		}
	}
	
	
	double lerp()
	{
		return (lastDistance + ((distance - lastDistance)) * 0.1);
	}
	//double distance = Math.min(Utils.computeAccuracy(beaconList.get(i)), 6.0);
}
