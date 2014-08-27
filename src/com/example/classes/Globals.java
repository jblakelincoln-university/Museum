package com.example.classes;

import com.example.museum.R;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Globals {
	public static RelativeLayout rLayout;
	public static RelativeLayout.LayoutParams rLayoutParams;
	public static R.drawable rDrawable;
	public static Point screenDimensions = new Point(0,0);
	
	public static boolean canUpdate = false;
	
	public static TextView textObject;
	
	public static float getTextSize() { return textObject.getTextSize();}

	public static void Init(Activity a){
		a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
		a.getWindowManager().getDefaultDisplay().getRealSize(screenDimensions);
        
        Globals.rLayout = new RelativeLayout(a);        
        
        
        Globals.rLayoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT, 
        		RelativeLayout.LayoutParams.MATCH_PARENT);
        
        textObject = new TextView(a);
	}
	
	// Call this function for a new unique ID.
	private static int currentId = 1000;
	public static int newId() { currentId++; return currentId;}
}
