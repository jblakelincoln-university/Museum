package com.example.classes;

import java.util.ArrayList;
import java.util.List;

import com.example.museum.R;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.view.View;
import android.widget.RelativeLayout;

// Sorry.
// But I think this is the best course of action!
public class Globals {
	
	// Application independent.
	public static RelativeLayout rLayout;
	public static RelativeLayout.LayoutParams rLayoutParams;
	public static R.drawable rDrawable;
	public static Point screenDimensions = new Point(0,0);
	public static void Init(Activity a){
		a.getWindowManager().getDefaultDisplay().getSize(screenDimensions);
        
        Globals.rLayout = new RelativeLayout(a);        
        a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        Globals.rLayoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT, 
        		RelativeLayout.LayoutParams.MATCH_PARENT);
        
        Globals.rLayout.setBackgroundResource(R.drawable.background);
        
        // Application specific.
        sceneMain = new SceneMain(0, a, true);
        sceneGallery = new SceneGallery(0, a, false);
        listScenes.add(sceneMain);
        listScenes.add(sceneGallery);
	}
	
	// Call this function for a new unique ID.
	private static int currentId = 1000;
	public static int newId() { currentId++; return currentId;}
	
	// All following is application specific.
	public static enum ScreenState{
		MAIN,
		GALLERY;
		
		public static int toInt(ScreenState s){
			switch(s){
			case MAIN:
				return 0;
			case GALLERY:
				return 1;
			}
			return -1;
		}
	}
	
	// Calls function from current scene.
	public static void onBackPressed(){
		if (listScenes != null)
			listScenes.get(ScreenState.toInt(screenState)).onBackPressed();
	}
	
	// Makes the current scene not visible and makes next scene visible.
	public static void SetScreenState(ScreenState s){
		listScenes.get(ScreenState.toInt(screenState)).setVisibility(View.GONE);
		screenState = s;
		listScenes.get(ScreenState.toInt(screenState)).setVisibility(View.VISIBLE);
	}
	
	public static List<Scene> listScenes = new ArrayList<Scene>();
	public static Scene sceneMain;
	public static Scene sceneGallery;
	public static ScreenState screenState = ScreenState.MAIN;
	
	
	
	
		
}
