package com.example.classes;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import com.example.classes.Objects.*;

public abstract class Scene {	
	@SuppressWarnings("rawtypes")
	protected List<AbstractElement> sceneElements = new ArrayList<AbstractElement>();
	protected int id;
	
	public Scene(int idIn, Activity a, boolean visible){
		id = idIn;
		sceneInit(a, visible);
	}
	
	protected void sceneInit(Activity a, boolean visible){
		if (visible)
			setVisibility(View.VISIBLE);
		else
			setVisibility(View.GONE);
	}
	
	@SuppressWarnings("rawtypes")
	protected void addElementToView(AbstractElement aIn){
		sceneElements.add(aIn);
		aIn.addView(Globals.rLayout);
	}
	
	public void setVisibility(int v){
		for (int i = 0; i < sceneElements.size(); i++){
			sceneElements.get(i).setVisibility(v);
		}
	}
	
	public abstract void onBackPressed();
}
