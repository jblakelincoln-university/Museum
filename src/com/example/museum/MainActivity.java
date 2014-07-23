package com.example.museum;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

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
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			Globals.Init(this);
			setContentView(Globals.rLayout, Globals.rLayoutParams);     
			//new Dddd().execute(this);
			
			
			
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
}
