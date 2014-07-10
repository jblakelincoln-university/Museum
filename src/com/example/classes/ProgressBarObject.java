package com.example.classes;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class ProgressBarObject extends AbstractElement<ProgressBar>{

	public ProgressBarObject(Activity m, int idIn)
	{
		obj = new ProgressBar(m, null, android.R.attr.progressBarStyleHorizontal);
		obj.setId(idIn);
	}
	
	public void setValue(int v){
		obj.setProgress(v);
	}
	
	public void setSize(int x, int y){
	}
}
