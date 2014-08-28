package com.example.museum;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

import android.app.Activity;
import android.os.Handler;

import com.example.classes.AccelerometerManager;
import com.example.classes.EstimoteManager;
import com.example.classes.Globals;
import com.example.classes.MyBeacon;
import com.example.classes.Scene;
import com.example.classes.Objects.*;

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
	public SceneDebug(int idIn, Activity a, boolean visible) {
		super(idIn, a, visible);
		
		
		
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
	
	public void sceneInit(Activity aIn, boolean visible){
		textBeaconList = new TextObject("", aIn, Globals.newId());
		textBeaconList.getElement().setTextSize(Globals.getTextSize()*1.8f);
		addElementToView(textBeaconList);
		
		super.sceneInit(aIn, visible);
		
		handler.postDelayed(runnable, 100);
	}
	@Override
	public void onBackPressed() {
		activity.SetScreenState(GameActivity.ScreenState.MAIN);
	}

}
