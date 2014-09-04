package com.example.classes;

import java.util.ArrayList;

import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.classes.Objects.*;
import com.example.museum.GameActivity;
@SuppressWarnings("rawtypes")
public abstract class Scene {	
	
	protected List<AbstractElement> sceneElements = new ArrayList<AbstractElement>();
	protected List<AbstractElement> transitioningElements;
	protected List<Boolean> transitioningBooleans;
	protected int id;
	public int getId(){return id;}
	protected Boolean transitioning = false;
	
	protected GameActivity activity;
	
	private Handler handler = new Handler();
	
	protected enum Transition{
		FADE;
	}
	
	public boolean initialised = false;
	protected Transition transitionType = Transition.FADE;
	
	private Runnable runnable = new Runnable() {
	 	   @Override
	 	   public void run() {
	 		   if (transitioning){
	 			   if (transitioningElements != null){
		 			  for (int i = 0; i < transitioningElements.size(); i++){
		 					if (((View)(transitioningElements.get(i).getElement())).getAlpha() == 0){
		 						transitioningElements.get(i).setVisibility(View.GONE);
		 						transitioningBooleans.set(i, true);
		 					}
		 				}
	 			   }
	 				
	 			    boolean carryOn = true;
	 			    
	 			    if (transitioningBooleans != null){
		 				for (Boolean b : transitioningBooleans){
		 					if (!b)
		 						carryOn = false;
		 				}
	 			    }
	 				if(carryOn)
	 					transitioning = false;
	 		   }

	 	       handler.postDelayed(this, 100); 
	 	   }
	};
	
	
	public Scene(int idIn, Activity a, boolean visible){
		id = idIn;
		activity = (GameActivity) a;
		//sceneInit(a, visible);
		//transitioningElements = new ArrayList(sceneElements);
		//transitioningElements = sceneElements;
		
		for (AbstractElement<View> e : sceneElements)
			(e.getElement()).setAlpha(0);
		handler.postDelayed(runnable, 100);
	}
	
	public void sceneInit(Activity a, boolean visible){
		if (visible)
			setVisibility(View.VISIBLE);
		else
			setVisibility(View.GONE);
		
		initialised = true;
	}
	
	protected void addElementToView(AbstractElement aIn){
		if (!sceneElements.contains(aIn)){
			sceneElements.add(aIn);
			aIn.addView(activity.getLayout().get());
		}
	}
	
	public void onLoad(){
		transitionOut(sceneElements);
	}
	
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
		
		transitioningElements = new ArrayList<AbstractElement>();
		transitioningBooleans = new ArrayList<Boolean>();
		
		
		for (AbstractElement<View> e : sceneElements){
			//((View)(e.getElement())).animate().alpha(0);
			
			if (b || !l.contains(e)){
				transitioningElements.add(e);
				if (e.getElementView().getVisibility() == RelativeLayout.VISIBLE){
					e.getElementView().animate().setStartDelay(0);
					((View)(e.getElement())).animate().alpha(0);
					
				}
			}
			else
			{
				e.setVisibility(View.VISIBLE);
				((View)(e.getElement())).animate().alpha(1);
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
