package com.example.museum;

import java.util.HashMap;

import com.estimote.sdk.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;


import com.example.classes.Globals;
import com.example.classes.SceneGallery;

public class MainActivity extends Activity {
	
	public static final String TAG = "MainActivity";
	
	private static final String ESTIMOTE_PROXIMITY_UUID = 
			"B9407F30-F5F8-466E-AFF9-25556B57FE6D";
	private static final Region ALL_ESTIMOTE_BEACONS = 
			new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);
	
	private BeaconManager beaconManager;
	private Handler updateHandler;
	
	LinkedHashMap<String,MyBeacon> myBeaconsList = new LinkedHashMap<String, MyBeacon>();
	
	private Map<String, String> beaconNameDictionary;
	
	
	
	
	
	
	
	private class Dddd extends AsyncTask<Activity, Integer, Long>{

		@Override
		protected Long doInBackground(Activity... a) {
			for (Activity act : a)
			{}
			//sceneGallery = new SceneGallery(0, (Activity)act, false);
	        //listScenes.add(sceneGallery);
				
			
			  
			
			long l = 0;
			return l;
		}
		
	}
	
	protected void estimoteSetup(Activity aIn){
		
		beaconManager = new BeaconManager(aIn);
		
		beaconNameDictionary = new HashMap<String,String>();
		beaconNameDictionary.put("FE:E7:C6:3B:BC:DE", "Mint(1)");
		beaconNameDictionary.put("E5:B9:E4:F5:39:98", "Icy(2)");
		beaconNameDictionary.put("EF:A7:AE:AE:4A:3B", "Blueberry(3)");
		beaconNameDictionary.put("C7:5C:63:DF:2C:E4", "Mint(4)");
		beaconNameDictionary.put("CD:2F:A5:DE:92:13", "Icy(5)");
		beaconNameDictionary.put("F8:F6:53:10:8B:B4", "Blueberry(6)");
		
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
		     }
		   });
		
		
		//updateHandler = new Handler();
		//updateHandler.postDelayed(runnable,  0);
	}
	
	/*
	Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            if (Globals.canUpdate){
            	Globals.sceneMain.update();
            }
            
            timerHandler.postDelayed(this, 500);
        }
    };*/

	private Handler handler = new Handler();
	
	private Runnable runnable = new Runnable() { // Runnable is an "update" function - runs all the game stuff.
	 	   @Override
	 	   public void run() {
	 		   if (Globals.canUpdate)
	 			   Globals.sceneMain.update(myBeaconsList);
	 	      handler.postDelayed(this, 100); 
	 	   }
	};
	
	private Context context;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			estimoteSetup(this);
			Globals.Init(this);
			
			context = this;
			setContentView(Globals.rLayout, Globals.rLayoutParams);     
			//new Dddd().execute(this);
			
			handler.postDelayed(runnable, 100);
    }
	
	 @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
		 System.exit(0);
        //setContentView(Globals.rLayout, Globals.rLayoutParams);
        //super.onConfigurationChanged(newConfig);
    }
	
	@Override
	public void onBackPressed(){
		Globals.onBackPressed();
	}
	Timer timer;
	
	/*
	@Override
	public void onResume(){
		timer = new Timer();
		
		timer.schedule(new TimerTask(){
			@Override
			public void run(){
				
			}
		}, 0, 1000);
	}*/
}
