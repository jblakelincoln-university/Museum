package com.example.classes;

import com.example.museum.R;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Typeface;
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
		Fonts.Init(a);
		a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		a.getWindowManager().getDefaultDisplay().getSize(screenDimensions);
        
        Globals.rLayout = new RelativeLayout(a);        
        
        
        Globals.rLayoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT, 
        		RelativeLayout.LayoutParams.MATCH_PARENT);
        
        textObject = new TextView(a);
	}
	
	// Call this function for a new unique ID.
	private static int currentId = 1000;
	public static int newId() { currentId++; return currentId;}
	
	public static class Fonts{
		private static Typeface majorShift;
		public static Typeface MajorShift() {return majorShift;}
		
		private static Typeface sofiaRegular;
		public static Typeface SofiaRegular() {return sofiaRegular;}
		
		
		public static void Init(Activity aIn){
			majorShift = Typeface.createFromAsset(aIn.getAssets(),"fonts/major_shift.ttf");
			sofiaRegular = Typeface.createFromAsset(aIn.getAssets(),"fonts/sofia_regular.otf");
		}
	}
}


