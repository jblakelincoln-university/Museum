package com.example.museum;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
	
	public enum ScreenState {
		MAIN,
		GALLERY,
	}
	
	RelativeLayout linLayout;
	TextObject textTitle;
	TextObject textStatus;
	TextObject textClue;
	ImageObject imageClue;
	ImageObject imageTransportation;
	ButtonObject buttonGallery;
	boolean galleryVisible=false;
	
	ScreenState screenState = ScreenState.MAIN;
	
	ProgressBarObject healthBar;
	
	List<AbstractElement> listGameElements = new ArrayList<AbstractElement>();
	List<AbstractElement> listGalleryElements = new ArrayList<AbstractElement>();
	
	ScrollViewObject scrollViewGallery;
	List<AbstractElement> listGalleryScrollView = new ArrayList<AbstractElement>();
	
	/*
	R.drawable[] galleryDrawables = {
			//R.drawable.
	}*/
	
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
        
        linLayout.setBackgroundResource(R.drawable.background);
        
        setGameLayout();
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
        buttonGallery.addRule(RelativeLayout.BELOW, textClue.getId());
        buttonGallery.addRule(RelativeLayout.ALIGN_START, textClue.getId());
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
	
	@Override
	public void onBackPressed(){
		if (screenState == ScreenState.GALLERY)
			screenState = ScreenState.MAIN;
		else
			super.onBackPressed();
		ToggleVisibility();
	}
	
	private void addToGalleryBar(ImageObject i){
		listGalleryScrollView.add(i);
		i.setScale(screenDimensions.x/8, screenDimensions.x/8);
		i.addView(scrollViewGallery.getLayout());
		i.setVisibility(View.GONE);
	}
	
	private void setGalleryLayout(){
		scrollViewGallery = new ScrollViewObject(this, 109);
		scrollViewGallery.addView(linLayout);
		scrollViewGallery.getLayoutParams().width = screenDimensions.x-(screenDimensions.x/20);
		scrollViewGallery.getLayoutParams().setMarginStart(screenDimensions.x/40);
		scrollViewGallery.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		scrollViewGallery.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		int newId=150;
		addToGalleryBar(new ImageObject(R.drawable.ammo_green, this, newId++));
	}
	
	public void ToggleVisibility(){
		for (int i = 0; i < listGameElements.size(); i++){
			if (screenState == ScreenState.MAIN)
				listGameElements.get(i).setVisibility(View.VISIBLE);
			else
				listGameElements.get(i).setVisibility(View.GONE);
		}
		for (int i = 0; i < listGalleryScrollView.size(); i++){
			if (screenState == ScreenState.GALLERY)
				listGalleryScrollView.get(i).setVisibility(View.VISIBLE);
			else
				listGalleryScrollView.get(i).setVisibility(View.GONE);
		}
	}
	
	public void SetScreenState(ScreenState s){
		screenState = s;
		ToggleVisibility();
	}
	
	public void setGalleryButtonClickEvent(){
		buttonGallery.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetScreenState(ScreenState.GALLERY);	
				}
		});
	}
}
