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
	
	Point screenDimensions = new Point(0, 0);
	 
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindowManager().getDefaultDisplay().getSize(screenDimensions);
        linLayout = new RelativeLayout(this);        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        
        RelativeLayout.LayoutParams linLayoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT, 
        		RelativeLayout.LayoutParams.MATCH_PARENT);
        setGameLayout();
        //setGalleryLayout();
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
        setGalleryButtonClickEvent();

	}
	
	private void addToGame(AbstractElement a){
		listGameElements.add(a);
		a.addView(linLayout);
	}
	
	private void setGalleryLayout(){
		ScrollViewObject sObject = new ScrollViewObject(this, 105);
		sObject.getLayoutParams().height=30;
		TextObject t1 = new TextObject("Elo", this, 106);
		t1.addView(sObject.getLayout());
		TextObject t2 = new TextObject("Elo22", this, 107);
		t2.addView(sObject.getLayout());
		TextObject t3 = new TextObject("Elo33", this, 108);
		t3.addView(sObject.getLayout());
		
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
