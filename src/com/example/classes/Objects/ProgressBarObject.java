package com.example.classes.Objects;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.widget.ProgressBar;

public class ProgressBarObject extends AbstractElement<ProgressBar>{

	private Mode mode = Mode.MULTIPLY;
	private int colour = Color.BLACK;
	
	public ProgressBarObject(Activity m, int idIn, boolean horizontal)
	{
		if (horizontal)
			obj = new ProgressBar(m, null, android.R.attr.progressBarStyleHorizontal);
		else
			obj = new ProgressBar(m, null, android.R.attr.progressBarStyle);
		obj.setId(idIn);
		
		setAppearance();
	}
	
	public void setValue(int v){
		obj.setProgress(v);
	}
	
	public void setSize(int x, int y){
	}
	
	//http://softwyer.files.wordpress.com/2012/01/porterduffmodes.png
	public void setMode(Mode m){
		mode = m;
		setAppearance();
	}
	
	public void setColour(int colourIn){
		colour = colourIn;
		setAppearance();
	}
	
	private void setAppearance(){
		obj.getProgressDrawable().setColorFilter(colour, mode);
	}
}
