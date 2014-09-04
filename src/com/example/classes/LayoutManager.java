package com.example.classes;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class LayoutManager {
	private RelativeLayout rLayout;
	private RelativeLayout.LayoutParams rLayoutParams;
	
	public RelativeLayout get() {return rLayout;}
	public RelativeLayout.LayoutParams getParams() {return rLayoutParams;}
	private Context c;
	public LayoutManager(Activity a){
		rLayout = new RelativeLayout(a);        
        c = a;
        
        rLayoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT, 
        		RelativeLayout.LayoutParams.MATCH_PARENT);
        
        //a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//a.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//a.setContentView(rLayout, rLayoutParams);
	}
	
	public Activity getActivity(){
		return ((Activity) c);
	}
	public void setContentView(){
		((Activity) c).setContentView(rLayout, rLayoutParams);
	}
	public void addView(View v){
		rLayout.addView(v, rLayoutParams);
	}
	
	public void setBackgroundResource(int r){
		rLayout.setBackgroundResource(r);
	}
}
