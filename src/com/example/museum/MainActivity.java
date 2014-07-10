package com.example.museum;

import java.util.ArrayList;
import java.util.List;

import com.example.classes.*;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class MainActivity extends Activity {
	
	RelativeLayout linLayout;
	TextObject textTitle;
	TextObject textStatus;
	TextObject textClue;
	ImageObject imageClue;
	ImageObject imageTransportation;
	ButtonObject buttonGallery;
	boolean galleryVisible=false;
	
	ProgressBarObject healthBar;
	
	List<AbstractElement> listGameElements = new ArrayList<AbstractElement>();
	List<AbstractElement> listGalleryElements = new ArrayList<AbstractElement>();
	
	ScrollViewObject scrollViewGallery;
	List<ImageObject> listGalleryScrollView = new ArrayList<ImageObject>();
	
	Point screenDimensions = new Point(0, 0);
	 
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindowManager().getDefaultDisplay().getSize(screenDimensions);
        linLayout = new RelativeLayout(this);        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        RelativeLayout.LayoutParams linLayoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT, 
        		RelativeLayout.LayoutParams.MATCH_PARENT);
        //setGameLayout();
        setGalleryLayout();
        
        //buttonGallery.getElement().callOnClick();
        
        setContentView(linLayout, linLayoutParams);
    }
	
	private void setGameLayout(){
        textTitle = new TextObject("Transport munitions!", this, 100);
        textTitle.alignToTop();
        textTitle.getElement().setPaddingRelative(0, screenDimensions.y/100, 0, screenDimensions.y/80);
        //textTitle.getElement().setTextSize(50f);
        textTitle.getElement().setTextSize(TypedValue.COMPLEX_UNIT_SP, 50f);
        

        imageClue = new ImageObject(R.drawable.clue1, this, 101);
        //imageClue.getElement().getLayoutParams().width = screenDimensions.y/4;
        imageClue.addRule(RelativeLayout.BELOW, textTitle.getId());
        imageClue.getElement().setPaddingRelative(0, 0, 0, screenDimensions.y/100);
        imageClue.setAbsScaleY((int)(screenDimensions.y/2.5f));
    
        textStatus = new TextObject("Mission status text box with long words everywhere", this, 102);
        textStatus.addRule(RelativeLayout.BELOW, imageClue.getId());
        textStatus.getElement().setWidth(screenDimensions.x-(screenDimensions.x/10));
        textStatus.getElement().setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
        textStatus.getElement().setGravity(Gravity.CENTER);
        
        imageTransportation = new ImageObject(R.drawable.ammo_green, this, 103);
        imageTransportation.addRule(RelativeLayout.BELOW, textStatus.getId());
        imageTransportation.addRule(RelativeLayout.ALIGN_END, textStatus.getId());
        imageTransportation.getElement().setPaddingRelative(0,screenDimensions.y/30, screenDimensions.x/20, 0);
        imageTransportation.setAbsScaleY(screenDimensions.y/5);
       
        healthBar = new ProgressBarObject(this, 104);
        healthBar.addRule(RelativeLayout.BELOW, imageTransportation.getId());
        healthBar.addRule(RelativeLayout.ALIGN_START, imageTransportation.getId());
        healthBar.getLayoutParams().setMargins(0, screenDimensions.y/80, 0, 0);
        healthBar.setWidth(imageTransportation.getWidth());
        healthBar.setValue(50);
        
        textClue = new TextObject("This is the clue text it is clue text that contains a clue of varying length.", this, 104);
        textClue.addRule(RelativeLayout.ALIGN_PARENT_LEFT, textClue.getId());
        textClue.addRule(RelativeLayout.ALIGN_BOTTOM, imageTransportation.getId());
        textClue.getElement().setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
        textClue.setWidth(screenDimensions.x/2.2f);
        textClue.getLayoutParams().setMarginStart(screenDimensions.x/20);
        
        buttonGallery = new ButtonObject("Gallery", this, 105);
        
        addToGame(textTitle);
        addToGame(imageClue);
        addToGame(textStatus);
        addToGame(imageTransportation);
        addToGame(healthBar);
        addToGame(textClue);
        addToGame(buttonGallery);
        setGalleryButtonClickEvent();
	}
	
	private void addToGame(AbstractElement a){
		listGameElements.add(a);
		a.addView(linLayout);
	}
	
	private void addToGalleryBar(ImageObject i){
		//listGalleryScrollView.add(i);
		i.setScale(screenDimensions.x/8, screenDimensions.x/8);
		i.addView(scrollViewGallery.getLayout());
	}
	
	private void setGalleryLayout(){
		
		//sObject.getLayoutParams().width=300;
		//sObject.getLayoutParams().height=300;
		//
		
		scrollViewGallery = new ScrollViewObject(this, 109);
		scrollViewGallery.addView(linLayout);
		/*
		scrollViewGallery.getLayoutParams().setMargins(0, 0, 0, 0);
		ImageObject t1 = new ImageObject(R.drawable.clue1, this, 110);
		t1.setScale(300, 300);
		t1.addView(scrollViewGallery.getLayout());
		
		ImageObject t2 = new ImageObject(R.drawable.clue1, this, 111);
		t2.setScale(300, 400);
		t2.addView(scrollViewGallery.getLayout());
		ImageObject t3 = new ImageObject(R.drawable.clue1, this, 112);
		t3.setScale(300, 300);
		t3.addView(scrollViewGallery.getLayout());
		
		
		t1.addRule(RelativeLayout.ABOVE, t2.getId());
		*/
		
		int newId=200;
		addToGalleryBar(new ImageObject(R.drawable.clue1, this, newId++));
		addToGalleryBar(new ImageObject(R.drawable.clue1, this, newId++));
		addToGalleryBar(new ImageObject(R.drawable.clue1, this, newId++));
		
		/*
		TextObject t1 = new TextObject("11111", this, 110);
		t1.addView(sObject.getLayout());
		TextObject t2 = new TextObject("22222", this, 111);
		t2.addView(sObject.getLayout());
		TextObject t3 = new TextObject("33333", this, 112);
		t3.addView(sObject.getLayout());
		*/
		
		
		//sObject.addElement(this);
		//sObject.addElement(t2.getElement());
		//sObject.addElement(t3.getElement());
	}
	
	public void setGalleryButtonClickEvent(){
		buttonGallery.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				galleryVisible = !galleryVisible;
				
				for (int i = 0; i < listGameElements.size(); i++){
					if (galleryVisible)
						listGameElements.get(i).setVisibility(View.GONE);
					else
						listGameElements.get(i).setVisibility(View.VISIBLE);
				}

			}
		});
	}
}
