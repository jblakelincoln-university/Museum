package com.example.classes.Objects;

import android.app.Activity;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class VScrollViewObject extends AbstractElement<ScrollView>{
	LinearLayout scrollLayout;
	
	public VScrollViewObject(Activity m, int idIn)
	{
		obj = new ScrollView(m);
		//type = ScrollView.class;
		obj.setId(idIn);
		//layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		scrollLayout = new LinearLayout(m);
		//scrollLayout.setOrientation(LinearLayout.HORIZONTAL);
		// Add Buttons

		// Add the LinearLayout element to the ScrollView
		obj.addView(scrollLayout);
	}
	
	public LinearLayout getLayout(){
		return scrollLayout;
	}
	
	@Override
	public void setVisibility(int i) {
		obj.setVisibility(i);
		
	}
	
	
}
