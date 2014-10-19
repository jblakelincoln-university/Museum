package com.example.museum;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

import android.app.Activity;
import android.os.Handler;

import com.scenelibrary.classes.AccelerometerManager;
import com.scenelibrary.classes.EstimoteManager;
import com.scenelibrary.classes.Globals;
import com.scenelibrary.classes.MyBeacon;
import com.scenelibrary.classes.Scene;
import com.scenelibrary.classes.Objects.*;
import com.scenelibrary.classes.LayoutManager;

import com.example.museum.GameActivity;

public class SceneDebug extends Scene{

	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
	 	   @Override
	 	   public void run() {
	 		   update();
	 	       handler.postDelayed(this, 100); 
	 	   }
	};
	
	private TextObject textBeaconList;
	public SceneDebug(int idIn, Activity a, LayoutManager lM, boolean visible) {
		super(idIn, a, lM, visible);
		
		
		
	}
	
	HashMap<String,MyBeacon> beaconList = new HashMap<String,MyBeacon>();
	public void update(){
		//textClue.setText("aaaa" + p++);
		
		textBeaconList.setText("--------------------------------------------------\n");
		String s = "";
		
		//beaconList = EstimoteManager.getBeaconList();
		
		for (Entry<String, MyBeacon> entry : EstimoteManager.getBeaconList().entrySet()) {
			//entry.getValue().updateDistance();
			String name = entry.getValue().getName();//entry.getValue().getName();
			String distance = String.format(Locale.UK, "%.2f", entry.getValue().getDistance());
			s += name + " - " + distance + "\n";
		}
		
		textBeaconList.getElement().append(s);
		
		textBeaconList.getElement().append("\n" + "AccX: " + AccelerometerManager.getX() +
											"\n" + "AccY: " + AccelerometerManager.getY() + 
											"\n" + "AccZ: " + AccelerometerManager.getZ() + 
											"\n\n" + "Text size: " + Globals.getTextSize());
	
		textBeaconList.getElement().append("\n--------------------------------------------------");
	}
	
	public void sceneInit(Activity aIn, LayoutManager lM, boolean visible){
		textBeaconList = new TextObject("", aIn, Globals.newId());
		textBeaconList.getElement().setTextSize(Globals.getTextSize()*1.8f);
		addElementToView(textBeaconList);
		
		super.sceneInit(aIn, visible);
		
		handler.postDelayed(runnable, 100);
	}
	@Override
	public void onBackPressed() {
		((GameActivity)activity).SetScreenState(GameActivity.ScreenState.MAIN);
	}

}
