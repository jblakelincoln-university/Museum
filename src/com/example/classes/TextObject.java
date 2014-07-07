package com.example.classes;

import com.example.museum.*;


import android.R;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.museum.MainActivity;


public class TextObject extends AbstractElement{
	
	private TextView textView;
	public TextObject(String s, Activity m, RelativeLayout r, int idIn)
	{
		textView = new TextView(m);
		id = idIn;
		textView.setId(id);
		textView.setText(s);
		//layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		r.addView(textView, layoutParams);
	}
	
	public TextView getElement(){
		return textView;
	}
}
