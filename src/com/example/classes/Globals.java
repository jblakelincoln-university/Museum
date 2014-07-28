package com.example.classes;

import java.util.ArrayList;
import java.util.List;

import com.example.museum.R;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.AsyncTask;
import android.view.View;
import android.widget.RelativeLayout;

public class Globals {
	public static RelativeLayout rLayout;
	public static RelativeLayout.LayoutParams rLayoutParams;
	public static R.drawable rDrawable;
	public static Point screenDimensions = new Point(0,0);
	
	public static boolean canUpdate = false;

	public static void Init(Activity a){
		a.getWindowManager().getDefaultDisplay().getSize(screenDimensions);
        
        Globals.rLayout = new RelativeLayout(a);        
        a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        Globals.rLayoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT, 
        		RelativeLayout.LayoutParams.MATCH_PARENT);
        
        Globals.rLayout.setBackgroundResource(R.drawable.background);
	}
	
	// Call this function for a new unique ID.
	private static int currentId = 1000;
	public static int newId() { currentId++; return currentId;}
}
