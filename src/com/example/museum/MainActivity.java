package com.example.museum;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.example.classes.Globals;
import com.example.classes.SceneGallery;

public class MainActivity extends Activity {
	
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
	 			   Globals.sceneMain.update();
	 	      handler.postDelayed(this, 100); 
	 	   }
	};
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			Globals.Init(this);
			
			
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
