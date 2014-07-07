package com.example.classes;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class ProgressBarObject extends AbstractElement{
	ProgressBar progressBar;
	
	public ProgressBarObject(Activity m, RelativeLayout r, int idIn)
	{
		progressBar = new ProgressBar(m, null, android.R.attr.progressBarStyleHorizontal);
		id = idIn;
		progressBar.setId(id);
		
		r.addView(progressBar, layoutParams);
	}

	public Object getElement() {
		return progressBar;
	}
	
	public void setValue(int v){
		progressBar.setProgress(v);
	}
	
	public void setSize(int x, int y){
	}
}
