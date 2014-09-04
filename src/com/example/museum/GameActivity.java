package com.example.museum;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;

import com.example.classes.LayoutManager;
import com.example.classes.Scene;

public class GameActivity extends Activity {
	
	//public static EstimoteManager estimoteManager;
	
	public enum ScreenState{
		DEBUG,
		MAIN,
		GALLERY,
		FACTSHEET;
		
		public static int toInt(ScreenState s){
			switch(s){
			case DEBUG:
				return 0;
			case MAIN:
				return 1;
			case GALLERY:
				return 2;
			case FACTSHEET:
				return 3;
			}
			return -1;
		}
	}

	public void setScreenState(ScreenState s){
		SetScreenState(s);
	}

	public List<Scene> listScenes = new ArrayList<Scene>();
	private SceneMain sceneMain;
	private SceneGallery sceneGallery;
	private SceneDebug sceneDebug;
	private SceneFactsheet sceneFactsheet;
	public SceneFactsheet getFactsheet() {return sceneFactsheet;}
	public ScreenState screenState;
	
	private LayoutManager layout;
	public LayoutManager getLayout() {return layout;}
	
	public void SetScreenState(ScreenState s){
		//listScenes.get(ScreenState.toInt(screenState)).setVisibility(View.GONE);
		listScenes.get(ScreenState.toInt(screenState)).transitionOut(null);
		screenState = s;
		if (!listScenes.get(ScreenState.toInt(screenState)).initialised){
			listScenes.get(ScreenState.toInt(screenState)).sceneInit(layout.getActivity(), true);
		}
		
		//listScenes.get(ScreenState.toInt(screenState)).setVisibility(View.VISIBLE);
		listScenes.get(ScreenState.toInt(screenState)).onLoad();
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			layout = new LayoutManager(this);
			// Create all scenes and then add them to a list;
			screenState = ScreenState.MAIN;
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			sceneDebug = new SceneDebug(0, this, false);
			listScenes.add(sceneDebug);
			sceneMain = new SceneMain(1, this, true);
	        listScenes.add(sceneMain);
	        sceneGallery = new SceneGallery(2, this, false);
	 		listScenes.add(sceneGallery); 
	 		sceneFactsheet = new SceneFactsheet(3, this, false);
	 		listScenes.add(sceneFactsheet);
	 		
	 		for (Scene s : listScenes)
	 			s.sceneInit(this, false);

	        layout.setBackgroundResource(R.drawable.background_newtoo);

	        layout.setContentView();  
	        SetScreenState(screenState);
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
