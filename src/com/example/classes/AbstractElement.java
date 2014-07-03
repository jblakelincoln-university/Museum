package com.example.classes;
import android.widget.RelativeLayout;


public abstract class AbstractElement {
	protected RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
    		RelativeLayout.LayoutParams.WRAP_CONTENT, 
    		RelativeLayout.LayoutParams.WRAP_CONTENT);
	
	public AbstractElement(){
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	}
	
	public RelativeLayout.LayoutParams getLayoutParams() { return layoutParams;}
	
	public void addRule(int verb){
		layoutParams.addRule(verb);
	}
	
	public abstract Object getElement();
	
	public void addRule(int verb, int anchor){
		layoutParams.addRule(verb, anchor);
	}
	
	public RelativeLayout.LayoutParams getView(){
		return layoutParams;
	}
	
}
