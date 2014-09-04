package com.example.classes;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Typeface;
import android.widget.TextView;

public class Globals {
	
	public static Point screenDimensions = new Point(0,0);
	public static TextView textObject;
	
	
	public static float getTextSize() { return textObject.getTextSize();}

	public static void Init(Activity a){
		Fonts.Init(a);
		a.getWindowManager().getDefaultDisplay().getSize(screenDimensions);
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
		
		private static Typeface chunkFive;
		public static Typeface ChunkFive() {return chunkFive;}
		
		private static Typeface robotoThin;
		public static Typeface RobotoThin() {return robotoThin;}
		
		private static Typeface exoRegular;
		public static Typeface ExoRegular() {return exoRegular;}
		
		public static void Init(Activity aIn){
			majorShift = Typeface.createFromAsset(aIn.getAssets(),"fonts/major_shift.ttf");
			sofiaRegular = Typeface.createFromAsset(aIn.getAssets(),"fonts/sofia_regular.otf");
			chunkFive = Typeface.createFromAsset(aIn.getAssets(),"fonts/chunk_five.otf");
			robotoThin = Typeface.createFromAsset(aIn.getAssets(),"fonts/roboto_thin.ttf");
			exoRegular = Typeface.createFromAsset(aIn.getAssets(),"fonts/exo_regular.otf");
		}
	}
}


