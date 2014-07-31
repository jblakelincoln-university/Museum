package com.example.museum;

import java.util.HashMap;

import com.estimote.sdk.*;

import java.util.ArrayList;
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
import android.view.View;


import com.example.classes.EstimoteManager;
import com.example.classes.Globals;
import com.example.classes.Scene;
import com.example.classes.Objects.AbstractElement;

public class MainActivity extends Activity {
	
	public static EstimoteManager estimoteManager;
	
	public static enum ScreenState{
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

	public static List<Scene> listScenes = new ArrayList<Scene>();
	private SceneMain sceneMain;
	private SceneGallery sceneGallery;
	private SceneDebug sceneDebug;
	public static ScreenState screenState;
	
	private boolean appLoaded = false;
	
	public static void SetScreenState(ScreenState s){
		//listScenes.get(ScreenState.toInt(screenState)).setVisibility(View.GONE);
		List<AbstractElement> l = new ArrayList<AbstractElement>();
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
			c = this;
			// Create all scenes and then add them to a list;
			screenState = ScreenState.MAIN;
			
			sceneDebug = new SceneDebug(0, this, false);
			listScenes.add(sceneDebug);
			sceneMain = new SceneMain(1, this, false);
	        listScenes.add(sceneMain);
	        sceneGallery = new SceneGallery(2, (Activity)c, false);
	 		listScenes.add(sceneGallery); 
	 		
	 		SetScreenState(screenState);
	 		//for(Scene s : listScenes){
	 		//	if (s.getId() == ScreenState.toInt(screenState))
	 		//		s.setVisibility(View.VISIBLE);
	 		//}
	 		
	        Globals.canUpdate = true;
			estimoteManager = new EstimoteManager(this);
			
			setContentView(Globals.rLayout, Globals.rLayoutParams);  
			
			appLoaded = true;
			handler.postDelayed(runnable, 100);
    }
	
	private void update(){
		sceneDebug.update(estimoteManager.getBeaconList());
		//sceneMain.update();
	}
	 @Override
    public void onConfigurationChanged(Configuration newConfig){}
	
	@Override
	public void onBackPressed(){
		if (listScenes != null)
			listScenes.get(ScreenState.toInt(screenState)).onBackPressed();
	}
}
