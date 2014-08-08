package com.example.classes.Objects;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public abstract class AbstractElement<T> {
	
	protected T obj;
	protected int customId;
	
	public void setCustomId(int id){
		customId = id;
	}
	
	public int getCustomId(){
		return customId;
	}

	protected RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
    		RelativeLayout.LayoutParams.WRAP_CONTENT, 
    		RelativeLayout.LayoutParams.WRAP_CONTENT);

	public AbstractElement(){
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
	}
	
	public void addView(RelativeLayout r){
		r.addView((View)obj, layoutParams);
	}
	
	public void addView(LinearLayout r){
		r.addView((View)obj, layoutParams);
	}
	
	public RelativeLayout.LayoutParams getLayoutParams() { return layoutParams;}
	
	public void addRule(int verb){
		layoutParams.addRule(verb);
	}
	
	public View getElementView(){
		return (View)obj;
	}
	
	public T getElement(){
		return obj;
	}
	
	public void setVisibility(int v){
		((View)obj).setVisibility(v);
	}
	
	public void addRule(int verb, int anchor){
		layoutParams.addRule(verb, anchor);
	}
	
	public RelativeLayout.LayoutParams getView(){
		return layoutParams;
	}	
	
	public void alignToTop(){
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	}
	
	public void alignToLeft(){
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	}
	
	public void alignToBottom(){
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	}
	
	public void alignToRight(){
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	}
	
	public int getId(){
		return ((View)obj).getId();
	}
	
	public void setWidth(float x){
		layoutParams.width = (int)x;
	}
}
