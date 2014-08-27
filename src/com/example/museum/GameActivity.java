package com.example.museum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.example.classes.AccelerometerManager;
import com.example.classes.EstimoteManager;
import com.example.classes.Globals;
import com.example.classes.Scene;
import com.example.classes.Objects.ImageObject;

public class GameActivity extends Activity {
	
	//public static EstimoteManager estimoteManager;
	
	public enum ScreenState{
		DEBUG,
		MAIN,
		GALLERY;
		
		public static int toInt(ScreenState s){
			switch(s){
			case DEBUG:
				return 0;
			case MAIN:
				return 1;
			case GALLERY:
				return 2;
			}
			return -1;
		}
	}

	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
	 	   @Override
	 	   public void run() {
	 		   if (appLoaded)
	 			   update();
	 	       handler.postDelayed(this, 100); 
	 	   }
	};
	
	public void setScreenState(ScreenState s){
		SetScreenState(s);
	}

	public List<Scene> listScenes = new ArrayList<Scene>();
	private SceneMain sceneMain;
	private SceneGallery sceneGallery;
	private SceneDebug sceneDebug;
	public ScreenState screenState;
	
	private boolean appLoaded = false;
	
	public void SetScreenState(ScreenState s){
		//listScenes.get(ScreenState.toInt(screenState)).setVisibility(View.GONE);
		listScenes.get(ScreenState.toInt(screenState)).transitionOut(null);
		screenState = s;
		//listScenes.get(ScreenState.toInt(screenState)).setVisibility(View.VISIBLE);
		listScenes.get(ScreenState.toInt(screenState)).onLoad();
	}
	private Context c;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			Globals.Init(this);
			AccelerometerManager.Init(this);
			EstimoteManager.Init(this);
			c = this;
			// Create all scenes and then add them to a list;
			screenState = ScreenState.MAIN;
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			sceneDebug = new SceneDebug(0, this, false);
			listScenes.add(sceneDebug);
			sceneMain = new SceneMain(1, this, true);
	        listScenes.add(sceneMain);
	        sceneGallery = new SceneGallery(2, (Activity)c, false);
	 		listScenes.add(sceneGallery); 
	 		
	 		//
	 		
	 		//SetScreenState(screenState);
	 		
	 	    
	 		//for(Scene s : listScenes){
	 		//	if (s.getId() == ScreenState.toInt(screenState))
	 		//		s.setVisibility(View.VISIBLE);
	 		//}
	 		
	        Globals.canUpdate = true;
	        //Globals.rLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
	        Globals.rLayout.setBackgroundResource(R.drawable.background_newtoo);
	        
			//estimoteManager = new EstimoteManager(this);
			
			setContentView(Globals.rLayout, Globals.rLayoutParams);  
			
			ImageObject i = new ImageObject(R.drawable.background_new, this, Globals.newId(), false);
			//i.setAbsScaleY(Globals.screenDimensions.x);
			i.setScale(Globals.screenDimensions.x, Globals.screenDimensions.y-20);
			//i.addView(Globals.rLayout);
			handler.postDelayed(runnable, 100);
			
			//Globals.rLayout.getBackground().setAlpha(50);
    }
	
	private void update(){
		//sceneDebug.update(estimoteManager.getBeaconList());
		//sceneMain.update();
	}
	 @Override
    public void onConfigurationChanged(Configuration newConfig){}
	
	@Override
	public void onResume(){
		super.onResume();
		if (sceneMain != null)
			sceneMain.toggleVibration(true);
	}  
	
	@Override
	public void onPause(){
		super.onPause();
		if (sceneMain != null)
			sceneMain.toggleVibration(false);
	} 
	
	@Override
	public void onBackPressed(){
		if (listScenes != null)
			listScenes.get(ScreenState.toInt(screenState)).onBackPressed();
	}
}
