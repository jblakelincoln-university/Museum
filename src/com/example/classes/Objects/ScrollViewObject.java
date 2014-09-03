package com.example.classes.Objects;


import android.app.Activity;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;


public class ScrollViewObject extends AbstractElement<HorizontalScrollView>{
	LinearLayout scrollLayout;
	
	public ScrollViewObject(Activity m, int idIn)
	{
		obj = new HorizontalScrollView(m);
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
	
	@Override
	public void setVisibility(int i) {
		obj.setVisibility(i);
		
	}
	
	
}


