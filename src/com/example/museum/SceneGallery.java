package com.example.museum;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.classes.Globals;
import com.example.classes.Scene;
import com.example.classes.Objects.ImageObject;
import com.example.classes.Objects.ScrollViewObject;
import com.example.museum.R;

public class SceneGallery extends Scene{

	private ScrollViewObject scrollView;
	private List<ImageObject> listScrollViewElements;
	
	private ImageObject imageLarge;
	private List<Integer> listImages;
	
	private int uniqueImageId = 0;
	public SceneGallery(int idIn, Activity a, boolean visible) {
		super(idIn, a, visible);
	}

	
	public void onLoad(){
		super.onLoad();
		this.setVisibility(RelativeLayout.VISIBLE);
	}
	
	
	@Override
	protected void sceneInit(Activity aIn, boolean visible) {
		listScrollViewElements = new ArrayList<ImageObject>();
		listImages = new ArrayList<Integer>();
		
		scrollView = new ScrollViewObject(aIn, Globals.newId());
		scrollView.addView(Globals.rLayout);
		scrollView.getLayoutParams().width = Globals.screenDimensions.x-(Globals.screenDimensions.x/20);
		scrollView.getLayoutParams().setMarginStart(Globals.screenDimensions.x/40);
		scrollView.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		scrollView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		sceneElements.add(scrollView);
		setImages(aIn);
		
		imageLarge = new ImageObject(R.drawable.empty, aIn, Globals.newId(), false);
		//imageLarge.setAbsScaleX(200);
		imageLarge.addView(Globals.rLayout);
		//imageLarge.addRule(RelativeLayout.ALIGN_BOTTOM, scrollView.getId());
		imageLarge.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		
		sceneElements.add(imageLarge);
		
		//for (AbstractElement a : listScrollViewElements)
		//	sceneElements.add(a);
		
		//if (uniqueImageId > 0)
			//listScrollViewElements.get(0).getElement().performClick();
		super.sceneInit(aIn, visible);
		
	}
	
	private void setImages(Activity a){
		for (int i = 0; i < 1; i++)
		{
			addThumbnailToScrollView(new ImageObject(R.drawable.background, a, Globals.newId(), true));
			addThumbnailToScrollView(new ImageObject(R.drawable.ammo_green, a, Globals.newId(), true));
		}
	}
	
	@Override
	public void setVisibility(int v)
	{
		super.setVisibility(v);
		for (ImageObject x : listScrollViewElements)
			x.setVisibility(v);
	}
	
	private void addThumbnailToScrollView(ImageObject i){
		listScrollViewElements.add(i);
		listImages.add(i.getDrawable());
		i.setScale(Globals.screenDimensions.x/8, Globals.screenDimensions.x/8);
		i.addView(scrollView.getLayout());
		i.setVisibility(View.GONE);
		i.setUniqueId(uniqueImageId++);
		setThumbnailClickEvent(i);
	}
	
	public void setThumbnailClickEvent(final ImageObject i){
		i.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				imageLarge.setImage(listImages.get(i.getUniqueId()));
				if (imageLarge.getWidth() > Globals.screenDimensions.x-(Globals.screenDimensions.x/10))
					imageLarge.setAbsScaleX(Globals.screenDimensions.x - (Globals.screenDimensions.x/10));
				
				if (imageLarge.getWidth() > Globals.screenDimensions.y-(Globals.screenDimensions.y/6))
					imageLarge.setAbsScaleX(Globals.screenDimensions.y - (Globals.screenDimensions.y/6));
					//imageLarge.setAbsScaleX(Globals.screenDimensions.x);

				imageLarge.getLayoutParams().setMargins(0, (Globals.screenDimensions.y-imageLarge.getHeight())/4, 0, Globals.screenDimensions.y/8);
				//System.exit(0);
			}
		});
	}

	public void onBackPressed() {
		//for (AbstractElement a : listScrollViewElements)
		//	a.setVisibility(RelativeLayout.GONE);
		//transitionOut(null);
		activity.SetScreenState(GameActivity.ScreenState.MAIN);		
	}
}