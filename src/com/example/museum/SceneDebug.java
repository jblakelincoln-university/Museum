package com.example.museum;

import java.util.HashMap;
import java.util.Map.Entry;

import android.app.Activity;

import com.example.classes.Globals;
import com.example.classes.Scene;
import com.example.classes.Objects.*;

public class SceneDebug extends Scene{

	private TextObject textBeaconList;
	public SceneDebug(int idIn, Activity a, boolean visible) {
		super(idIn, a, visible);
		
	}
	
	public void update(HashMap<String, MyBeacon> beaconList){
		//textClue.setText("aaaa" + p++);
		
		String s = "";
		for (Entry<String, MyBeacon> entry : beaconList.entrySet()) {
			//entry.getValue().updateDistance();
			String name = entry.getValue().getName();//entry.getValue().getName();
			String distance = String.format("%.2f", entry.getValue().getDistance());
			s += name + " - " + distance + "\n";
		}
		
		textBeaconList.setText(s);
	}
	
	protected void sceneInit(Activity aIn, boolean visible){
		textBeaconList = new TextObject("", aIn, Globals.newId());
		
		addElementToView(textBeaconList);
		
		super.sceneInit(aIn, visible);
	}
	@Override
	public void onBackPressed() {
		MainActivity.SetScreenState(MainActivity.ScreenState.MAIN);
	}

}
