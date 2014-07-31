package com.example.classes;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import com.example.classes.Objects.*;

public abstract class Scene {	
	@SuppressWarnings("rawtypes")
	protected List<AbstractElement> sceneElements = new ArrayList<AbstractElement>();
	protected List<AbstractElement> transitioningElements;
	protected List<Boolean> transitioningBooleans;
	protected int id;
	public int getId(){return id;}
	protected Boolean transitioning = false;
	
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
	 	   @Override
	 	   public void run() {
	 		   if (transitioning){
	 			  for (int i = 0; i < transitioningElements.size(); i++){
	 					if (((View)(transitioningElements.get(i).getElement())).getAlpha() == 0){
	 						transitioningElements.get(i).setVisibility(View.GONE);
	 						transitioningBooleans.set(i, true);
	 					}
	 				}
	 				
	 			    boolean carryOn = true;
	 				for (Boolean b : transitioningBooleans)
	 				{
	 					if (!b)
	 						carryOn = false;
	 				}
	 				
	 				if(carryOn)
	 					transitioning = false;
	 		   }

	 	       handler.postDelayed(this, 100); 
	 	   }
	};
	
	
	public Scene(int idIn, Activity a, boolean visible){
		id = idIn;
		sceneInit(a, visible);
		//transitioningElements = new ArrayList(sceneElements);
		//transitioningElements = sceneElements;
		
		for (AbstractElement e : sceneElements)
			((View)(e.getElement())).setAlpha(0);
		handler.postDelayed(runnable, 100);
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
	
	public abstract void onLoad();
	
	public void transitionOut(List<AbstractElement> l){
		//if (l.isEmpty()){
		//	transitioningElements = sceneElements;
		//}
		//else
		Boolean b = false;
		if (l != null)
			transitioningElements = new ArrayList<AbstractElement>();
		else
			b = true;
		
		transitioningBooleans = new ArrayList<Boolean>();
		
		
		for (AbstractElement e : sceneElements){
			//((View)(e.getElement())).animate().alpha(0);
			
			if (b || !l.contains(e)){
				transitioningElements.add(e);
				((View)(e.getElement())).animate().alpha(0);
			}
			else
			{
				((View)(e.getElement())).animate().alpha(1);
				e.setVisibility(View.VISIBLE);
			}
			
			//((View)(e.getElement())).animate().start();
		}
		
		for (int i = 0; i < transitioningElements.size(); i++)
			transitioningBooleans.add(false);
		
		if (!transitioningElements.isEmpty())
			transitioning = true;
			
	}
	
	public void setVisibility(int v){
		for (int i = 0; i < sceneElements.size(); i++){
			sceneElements.get(i).setVisibility(v);
		}
	}
	
	public abstract void onBackPressed();
}
