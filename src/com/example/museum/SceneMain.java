package com.example.museum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import android.app.Activity;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.classes.Globals;
import com.example.classes.Scene;
import com.example.classes.Objects.*;
import com.example.museum.R;

import com.estimote.sdk.*;

import com.example.museum.*;
import com.example.museum.MainActivity.ScreenState;

public class SceneMain extends Scene{

	//Estimote

	
	public static enum ScreenState{
		MAIN,
		CLUE;
		
		public static int toInt(ScreenState s){
			switch(s){
			case MAIN:
				return 0;
			case CLUE:
				return 1;
			}
			return -1;
		}
	}
	
	//Scene objects
	private TextObject textTitle;
	private TextObject textStatus;
	private TextObject textClue;
	private ImageObject imageClue;
	private ImageObject imageTransportation;
	private ButtonObject buttonGallery;
	
	private ProgressBarObject healthBar;
	private ImageObject buttonFindClue;
	private ProgressBarObject barFinding;
	private ButtonObject buttonDebug;
	
	private ButtonObject buttonViewClueScreen;
	
	private ScreenState screenState = ScreenState.MAIN;
	
	protected List<AbstractElement> listMainScreen;
	protected List<AbstractElement> listClueScreen;
	
	public SceneMain(int idIn, Activity a, boolean visible) {
		super(idIn, a, visible);
		
		
	}
	
	
	
	int p = 0;
	
	private void itemSetup(Activity aIn){
		
		listMainScreen = new ArrayList<AbstractElement>();
		listClueScreen = new ArrayList<AbstractElement>();
		//textTitle = new TextObject("Transport munitions!", aIn, Globals.newId());
        //textTitle.alignToTop();
        //textTitle.getElement().setPaddingRelative(0, Globals.screenDimensions.y/100, 0, Globals.screenDimensions.y/80);
        //textTitle.getElement().setTextSize(50f);
        //textTitle.getElement().setTextSize(TypedValue.COMPLEX_UNIT_SP, 50f);
        
        imageClue = new ImageObject(R.drawable.clue1, aIn, Globals.newId(), false);
        //imageClue.getElement().getLayoutParams().width = Globals.screenDimensions.y/4;
        //imageClue.addRule(RelativeLayout.BELOW, textTitle.getId());
        imageClue.alignToTop();
        imageClue.getElement().setPaddingRelative(0, Globals.screenDimensions.y/100, 0, Globals.screenDimensions.y/100);
        imageClue.setAbsScaleY((int)(Globals.screenDimensions.y/2.5f));
    
        textStatus = new TextObject("Mission status text box with long words everywhere", aIn, Globals.newId());
        textStatus.addRule(RelativeLayout.BELOW, imageClue.getId());
        textStatus.getElement().setWidth(Globals.screenDimensions.x-(Globals.screenDimensions.x/10));
        textStatus.getElement().setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
        textStatus.getElement().setGravity(Gravity.CENTER);
        
        imageTransportation = new ImageObject(R.drawable.ammo_green, aIn, Globals.newId(), false);
        imageTransportation.addRule(RelativeLayout.BELOW, textStatus.getId());
        imageTransportation.addRule(RelativeLayout.ALIGN_END, textStatus.getId());
        imageTransportation.getElement().setPaddingRelative(0,Globals.screenDimensions.y/30, Globals.screenDimensions.x/20, 0);
        imageTransportation.setAbsScaleY(Globals.screenDimensions.y/7);
       
        healthBar = new ProgressBarObject(aIn, Globals.newId(), true);
        healthBar.addRule(RelativeLayout.BELOW, imageTransportation.getId());
        healthBar.addRule(RelativeLayout.ALIGN_START, imageTransportation.getId());
        healthBar.getLayoutParams().setMargins(0, Globals.screenDimensions.y/80, 0, 0);
        healthBar.setWidth(imageTransportation.getWidth());
        healthBar.setValue(50);
        
        textClue = new TextObject("This is the clue text it is clue text that contains a clue of varying length.", aIn, Globals.newId());
        
        textClue.addRule(RelativeLayout.ALIGN_BOTTOM, imageTransportation.getId());
        textClue.getElement().setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
        textClue.setWidth(Globals.screenDimensions.x/2.2f);
        //textClue.getLayoutParams().setMarginStart(Globals.screenDimensions.x/20);
        
        buttonGallery = new ButtonObject("Gallery", aIn, Globals.newId());
        buttonGallery.addRule(RelativeLayout.BELOW, textClue.getId());
        buttonGallery.addRule(RelativeLayout.ALIGN_START, textClue.getId());
        
        buttonFindClue = new ImageObject(R.drawable.find_button, aIn, Globals.newId(), true);
        buttonFindClue.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        buttonFindClue.setAbsScaleY(Globals.screenDimensions.y/5);
        buttonFindClue.getLayoutParams().setMargins(0, 0, 0, Globals.screenDimensions.y/10);
        buttonFindClue.setBackgroundColour(android.R.color.transparent);
        
        buttonDebug = new ButtonObject("Debug", aIn, Globals.newId());
        buttonDebug.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        buttonDebug.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        buttonDebug.getLayoutParams().setMargins(0, 0, (Globals.screenDimensions.x/12), Globals.screenDimensions.y/20);
        
        buttonViewClueScreen = new ButtonObject("View Clue", aIn, Globals.newId());
        
        textClue.addRule(RelativeLayout.ALIGN_RIGHT, buttonFindClue.getId());
	}
	
	@Override
	protected void sceneInit(Activity aIn, boolean visible) {
		itemSetup(aIn);
        
        //addElementToView(textTitle);
        addElementToView(imageClue);
        listMainScreen.add(imageClue);
        addElementToView(textStatus);
        listMainScreen.add(textStatus);
        addElementToView(imageTransportation);
        listMainScreen.add(imageTransportation);
        addElementToView(healthBar);
        listMainScreen.add(imageTransportation);
        addElementToView(textClue);
        listClueScreen.add(textClue);
        addElementToView(buttonGallery);
        listMainScreen.add(buttonGallery);
        addElementToView(buttonFindClue);
        listMainScreen.add(buttonFindClue);
        
        addElementToView(buttonDebug);
        listMainScreen.add(buttonDebug);
        
        addElementToView(buttonViewClueScreen);
        listMainScreen.add(buttonViewClueScreen);
        
        
        
        setGalleryButtonClickEvent();	
        setFindButtonClickEvent();
        setDebugButtonClickEvent();
        setViewClueScreenClickEvent();
        //estimoteSetup(aIn);
        setScene(ScreenState.MAIN);
        
        super.sceneInit(aIn, visible);
	}
	
	private void setScene(ScreenState sIn){
		screenState = sIn;
		setVisibility(View.GONE);
		
		if (sIn == ScreenState.MAIN){
			for (AbstractElement e : listMainScreen)
				e.setVisibility(View.VISIBLE);
		}
		else if (sIn == ScreenState.CLUE){
			for (AbstractElement e : listClueScreen)
				e.setVisibility(View.VISIBLE);
		}
	}

	
	public void setViewClueScreenClickEvent(){
		buttonViewClueScreen.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setScene(ScreenState.CLUE);	
			}
		});
	}
	
	public void setGalleryButtonClickEvent(){
		buttonGallery.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.SetScreenState(MainActivity.ScreenState.GALLERY);	
			}
		});
	}
	
	public void setDebugButtonClickEvent(){
		buttonDebug.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.SetScreenState(MainActivity.ScreenState.DEBUG);
			}
		});
	}
	
	public void setFindButtonClickEvent(){
		buttonFindClue.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				for (Entry<String, MyBeacon> entry : MainActivity.estimoteManager.getBeaconList().entrySet()) {
					if (entry.getValue().getName() == "Blueberry(6)")
					{
						double lowest = 10000;
						for (int i = 0; i < 500; i++)
						{
							if (entry.getValue().getDistance() < lowest)
								lowest = entry.getValue().getDistance();
						}
						if (lowest < 2.00)
							textStatus.setText("True");
						else
							textStatus.setText("False");
					}
				}
			}
		});		
	}

	@Override
	public void onBackPressed() {
		if (screenState == ScreenState.MAIN)
			System.exit(0);
		else if (screenState == ScreenState.CLUE)
			setScene(ScreenState.MAIN);
	}
}
