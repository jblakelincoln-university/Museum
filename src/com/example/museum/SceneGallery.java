package com.example.museum;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.scenelibrary.classes.Globals;
import com.scenelibrary.classes.Scene;
import com.scenelibrary.classes.Objects.ImageObject;
import com.scenelibrary.classes.Objects.ScrollViewObject;
import com.scenelibrary.classes.Objects.TextObject;
import com.example.museum.R;

import com.scenelibrary.classes.LayoutManager;

public class SceneGallery extends Scene{

	private ScrollViewObject scrollView;
	private List<ImageObject> listScrollViewElements;
	
	private ImageObject imageLarge;
	private TextObject textDescription;
	private List<Integer> listImages;
	private List<String> listDescriptions;
	
	private int uniqueImageId = 0;
	public SceneGallery(int idIn, Activity a, boolean visible) {
		super(idIn, a, visible);
	}

	
	public void onLoad(){
		super.onLoad();
		this.setVisibility(RelativeLayout.VISIBLE);
	}
	
	
	@Override
	public void sceneInit(Activity aIn, boolean visible) {
		listScrollViewElements = new ArrayList<ImageObject>();
		listImages = new ArrayList<Integer>();
		listDescriptions = new ArrayList<String>();
		
		scrollView = new ScrollViewObject(aIn, Globals.newId());
		scrollView.addView(layoutManager.get());
		scrollView.getLayoutParams().width = Globals.screenDimensions.x-(Globals.screenDimensions.x/20);
		scrollView.getLayoutParams().setMarginStart(Globals.screenDimensions.x/40);
		scrollView.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		scrollView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		sceneElements.add(scrollView);
		setImages(aIn);
		
		imageLarge = new ImageObject(R.drawable.empty, aIn, Globals.newId(), false);
		//imageLarge.setAbsScaleX(200);
		imageLarge.addView(layoutManager.get());
		//imageLarge.addRule(RelativeLayout.ALIGN_BOTTOM, scrollView.getId());
		imageLarge.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		
		textDescription = new TextObject("", aIn, Globals.newId());
		textDescription.addRule(RelativeLayout.ABOVE, scrollView.getId());
		textDescription.getElement().setTextSize(Globals.getTextSize()*1.1f);
		textDescription.getElement().setTypeface(Globals.Fonts.ExoRegular());
		textDescription.getLayoutParams().setMargins(Globals.screenDimensions.x/12, 0, Globals.screenDimensions.x/12, Globals.screenDimensions.y/20);
		textDescription.getElement().setGravity(Gravity.CENTER);
		
		
		sceneElements.add(imageLarge);
		addElementToView(textDescription);
		
		//for (AbstractElement a : listScrollViewElements)
		//	sceneElements.add(a);
		
		//if (uniqueImageId > 0)
			//listScrollViewElements.get(0).getElement().performClick();
		super.sceneInit(aIn, visible);
		
	}
	
	private void setImages(Activity a){
		for (int i = 0; i < 1; i++)
		{
			addThumbnailToScrollView(new ImageObject(R.drawable.gallery_one, a, Globals.newId(), true), "Ruston Loco (place and date unknown)");
			addThumbnailToScrollView(new ImageObject(R.drawable.gallery_two, a, Globals.newId(), true), "Ruston Loco at Waltham Abbey Gun Power Works");
			addThumbnailToScrollView(new ImageObject(R.drawable.gallery_three, a, Globals.newId(), true), "1000th plane made by Ruston Proctor (1918)");
			addThumbnailToScrollView(new ImageObject(R.drawable.gallery_four, a, Globals.newId(), true), "Ruston Proctor aircraft production (1910)");
			addThumbnailToScrollView(new ImageObject(R.drawable.gallery_five, a, Globals.newId(), true), "Field gun outside Monks Abbey");
			addThumbnailToScrollView(new ImageObject(R.drawable.gallery_six, a, Globals.newId(), true), "Field gun on train");
			addThumbnailToScrollView(new ImageObject(R.drawable.gallery_seven, a, Globals.newId(), true), "Ruston Loco (place and date unknwon)");
			addThumbnailToScrollView(new ImageObject(R.drawable.gallery_eight, a, Globals.newId(), true), "Sylvie threshing at Church Farm Museum");
			addThumbnailToScrollView(new ImageObject(R.drawable.gallery_nine, a, Globals.newId(), true), "Crawler tractor towing plane (credit: Peter Green)");
			addThumbnailToScrollView(new ImageObject(R.drawable.gallery_ten, a, Globals.newId(), true), "Crawler tractor towing plane (credit: Ray Hooley)");
			addThumbnailToScrollView(new ImageObject(R.drawable.gallery_eleven, a, Globals.newId(), true), "Strutter plane in  factory (credit: Ray Hooley)");
			addThumbnailToScrollView(new ImageObject(R.drawable.gallery_twelve, a, Globals.newId(), true), "Strutter plane assembly (credit: Ray Hooley)");
		}
	}
	
	@Override
	public void setVisibility(int v)
	{
		super.setVisibility(v);
		for (ImageObject x : listScrollViewElements)
			x.setVisibility(v);
	}
	
	private void addThumbnailToScrollView(ImageObject i, String s){
		listScrollViewElements.add(i);
		listImages.add(i.getDrawable());
		i.setScale(Globals.screenDimensions.x/8, Globals.screenDimensions.x/8);
		i.addView(scrollView.getLayout());
		i.setVisibility(View.GONE);
		i.setUniqueId(uniqueImageId++);
		listDescriptions.add(s);
		setThumbnailClickEvent(i);
	}
	
	public void setThumbnailClickEvent(final ImageObject i){
		i.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				imageLarge.setImage(listImages.get(i.getUniqueId()));
				textDescription.setText(listDescriptions.get(i.getUniqueId()));
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
		((GameActivity)activity).SetScreenState(GameActivity.ScreenState.MAIN);		
	}
}