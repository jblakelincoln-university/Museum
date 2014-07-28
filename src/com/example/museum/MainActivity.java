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


import com.example.classes.EstimoteManager;
import com.example.classes.Globals;
import com.example.classes.SceneGallery;

public class MainActivity extends Activity {
	
	EstimoteManager estimoteManager;
/*
	
	*/
	private Handler handler = new Handler();
	
	private Runnable runnable = new Runnable() { // Runnable is an "update" function - runs all the game stuff.
	 	   @Override
	 	   public void run() {
	 		   if (Globals.canUpdate)
	 			   Globals.sceneMain.update(estimoteManager.myBeaconsList);
	 	      handler.postDelayed(this, 100); 
	 	   }
	};
	
	private Context context;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			//estimoteSetup(this);
			Globals.Init(this);
			
			context = this;
			
			estimoteManager = new EstimoteManager(this);
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
