package com.example.classes;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.classes.Objects.AbstractElement;
import com.example.classes.Objects.ImageObject;
import com.example.classes.Objects.ScrollViewObject;
import com.example.museum.R;

public class SceneGallery extends Scene{

	private ScrollViewObject scrollView;
	@SuppressWarnings("rawtypes")
	private List<AbstractElement> listScrollViewElements;
	
	public SceneGallery(int idIn, Activity a, boolean visible) {
		super(idIn, a, visible);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void sceneInit(Activity aIn, boolean visible) {
		listScrollViewElements = new ArrayList<AbstractElement>();
		
		scrollView = new ScrollViewObject(aIn, Globals.newId());
		scrollView.addView(Globals.rLayout);
		scrollView.getLayoutParams().width = Globals.screenDimensions.x-(Globals.screenDimensions.x/20);
		scrollView.getLayoutParams().setMarginStart(Globals.screenDimensions.x/40);
		scrollView.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		scrollView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		addThumbnailToScrollView(new ImageObject(R.drawable.ammo_green, aIn, Globals.newId()));
		
		super.sceneInit(aIn, visible);
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void setVisibility(int v)
	{
		super.setVisibility(v);
		for (AbstractElement x : listScrollViewElements)
			x.setVisibility(v);
	}
	
	private void addThumbnailToScrollView(ImageObject i){
		listScrollViewElements.add(i);
		i.setScale(Globals.screenDimensions.x/8, Globals.screenDimensions.x/8);
		i.addView(scrollView.getLayout());
		i.setVisibility(View.GONE);
	}

	public void onBackPressed() {
		Globals.SetScreenState(Globals.ScreenState.MAIN);		
	}
}