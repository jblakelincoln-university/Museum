package com.example.classes;

import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.os.RemoteException;
import android.util.Log;

import com.estimote.sdk.*;

public class EstimoteManager {
public static final String TAG = "MainActivity";
	
	private static final String ESTIMOTE_PROXIMITY_UUID = 
			"B9407F30-F5F8-466E-AFF9-25556B57FE6D";
	private static final Region ALL_ESTIMOTE_BEACONS = 
			new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);
	
	private static BeaconManager beaconManager;
	
	private static LinkedHashMap<String,MyBeacon> myBeaconsList = new LinkedHashMap<String, MyBeacon>();
	public static LinkedHashMap<String,MyBeacon> getBeaconList(){return myBeaconsList;}
	private static Map<String, String> beaconNameDictionary;
	
	
	
	public static void Init(Activity aIn)
	{
		fillDictionary(aIn);
		estimoteSetup();
	}
	
	// Use this class to attach names to MAC addresses.
	protected static void fillDictionary(Activity aIn){
		beaconManager = new BeaconManager(aIn);
		beaconNameDictionary = new HashMap<String,String>();
		
		beaconNameDictionary.put("FE:E7:C6:3B:BC:DE", "Loco");
		beaconNameDictionary.put("E5:B9:E4:F5:39:98", "Tank");
		beaconNameDictionary.put("EF:A7:AE:AE:4A:3B", "Field Gun");
		beaconNameDictionary.put("C7:5C:63:DF:2C:E4", "Sylvie");
		beaconNameDictionary.put("CD:2F:A5:DE:92:13", "Plane");
		beaconNameDictionary.put("F8:F6:53:10:8B:B4", "Crawler");
	}
	
	public static MyBeacon contains(String s){
		for (Entry<String, MyBeacon> b : myBeaconsList.entrySet())
			if (b.getValue().getName().compareTo(s) == 0)
				return b.getValue();
		return null;
	}
	
	protected static void estimoteSetup(){
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
		    @Override 
		    public void onServiceReady() {
		      try {
		        beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
		      } catch (RemoteException e) {
		        Log.e(TAG, "Cannot start ranging", e);
		      }
		    }
		  });
		
		beaconManager.setRangingListener(new BeaconManager.RangingListener() {
		     @Override 
		     public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
		    	 // If detected beacon is not listed, add it to the list.
		    	 
		    	 for (int i = 0; i < beacons.size(); i++)
		    	 {
		    		 if (!myBeaconsList.containsKey(beacons.get(i).getMacAddress()))
		    		 {
		    			 myBeaconsList.put(beacons.get(i).getMacAddress().toString(), new MyBeacon(beacons.get(i)));
		    			 if (beaconNameDictionary.containsKey(beacons.get(i).getMacAddress()))
		    			 {
		    				 myBeaconsList.get(beacons.get(i).getMacAddress()).setName(beaconNameDictionary.get(beacons.get(i).getMacAddress()));
		    				 //myBeaconsList.get(beacons.get(i)).setInitialDistance(); 
		    			 }
		    		 }
		    		 else
		    			 myBeaconsList.get(beacons.get(i).getMacAddress()).setBeacon(beacons.get(i));
		    	 }
		    	 
		    	 // Update each beacon on the list.
		    	 for (Entry<String, MyBeacon> entry : myBeaconsList.entrySet())
		 			entry.getValue().updateDistance();
		     }
		   });
		
		
		//updateHandler = new Handler();
		//updateHandler.postDelayed(runnable,  0);
	}
	
	public MyBeacon getBeacon(String name){
		if (myBeaconsList.containsKey(name))
			return myBeaconsList.get(name);
		else
			return null;
	}
}
