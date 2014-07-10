package com.example.classes;

import com.example.museum.*;


import android.R;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.museum.MainActivity;


public class TextObject extends AbstractElement<TextView>{
	TextView textView;
	public TextObject(String s, Activity m, int idIn)
	{
		obj = new TextView(m);
		//type = TextView.class;
		obj.setId(idIn);
		((TextView)obj).setText(s);
		//layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
	}	
}
