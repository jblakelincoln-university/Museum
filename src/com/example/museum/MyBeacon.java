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
	}
	String getName(){return name;}
	double getDistance(){ return distance;}
	
	public void setBeacon(Beacon b){ theBeacon = b;}
	public void setName(String input){name = input;}
	
	void setInitialDistance()
	{
		distance = Math.min(Utils.computeAccuracy(theBeacon), 6.0);
		
	}
	public void updateDistance(){
		lastDistance = distance;
		distance = Math.min(Utils.computeAccuracy(theBeacon), 6.0);
		distance = lerp();
		}
	
	
	double lerp()
	{
		return (lastDistance + ((distance - lastDistance)) * 0.3);
	}
	//double distance = Math.min(Utils.computeAccuracy(beaconList.get(i)), 6.0);
}
