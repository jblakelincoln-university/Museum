package com.example.classes;

import com.example.museum.*;


import android.R;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.museum.MainActivity;


public class ScrollViewObject extends AbstractElement<ScrollView>{
	LinearLayout scrollLayout;
	
	public ScrollViewObject(Activity m, int idIn)
	{
		obj = new ScrollView(m);
		//type = ScrollView.class;
		obj.setId(idIn);
		//layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		scrollLayout = new LinearLayout(m);
		scrollLayout.setOrientation(LinearLayout.HORIZONTAL);
		// Add Buttons

		// Add the LinearLayout element to the ScrollView
		obj.addView(scrollLayout);
	}
	
	public LinearLayout getLayout(){
		return scrollLayout;
	}
	
	public void addElement(Activity m){
		Button button = new Button(m);
		button.setText("Some text");
		scrollLayout.addView(button);
	}
	
	public ScrollView getElement(){
		return obj;
	}

	@Override
	public void setVisibility(int i) {
		obj.setVisibility(i);
		
	}
	
	
}
