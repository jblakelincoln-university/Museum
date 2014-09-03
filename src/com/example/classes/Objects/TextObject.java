package com.example.classes.Objects;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;



public class TextObject extends AbstractElement<TextView>{
	TextView textView;
	public TextObject(String s, Activity m, int idIn)
	{
		obj = new TextView(m);
		//type = TextView.class;
		obj.setId(idIn);
		((TextView)obj).setText(s);
		
		obj.setTextColor(m.getResources().getColor(android.R.color.black));
		//layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
	}
	
	public void setTextColour(int c){
		obj.setTextColor(c);
	}
	
	public void setTimedText(String s){
		obj.setAlpha(1.0f);
		obj.setText(s);
		obj.animate().alpha(0.0f).setStartDelay(4000);
	}
	
	public void setText(String s){
		obj.setText(s);
	}
}
