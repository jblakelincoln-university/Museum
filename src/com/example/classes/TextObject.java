package com.example.classes;
import android.app.Activity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.museum.MainActivity;


public class TextObject extends AbstractElement{
	
	private TextView textView;
	public TextObject(String s, Activity m, int id)
	{
		textView = new TextView(m);
		textView.setId(id);
		textView.setText(s);
		
		layoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.WRAP_CONTENT, 
        		RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		
	}
	
	public TextView getElement(){
		return textView;
	}
}
