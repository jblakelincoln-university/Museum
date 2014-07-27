package com.example.classes.Objects;

import android.app.Activity;
import android.widget.ProgressBar;

public class ProgressBarObject extends AbstractElement<ProgressBar>{

	public ProgressBarObject(Activity m, int idIn, boolean horizontal)
	{
		if (horizontal)
			obj = new ProgressBar(m, null, android.R.attr.progressBarStyleHorizontal);
		else
			obj = new ProgressBar(m, null, android.R.attr.progressBarStyle);
		obj.setId(idIn);
	}
	
	public void setValue(int v){
		obj.setProgress(v);
	}
	
	public void setSize(int x, int y){
	}
}
