package com.example.museum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.example.classes.AccelerometerManager;
import com.example.classes.Colour;
import com.example.classes.EstimoteManager;
import com.example.classes.Globals;
import com.example.classes.MyBeacon;
import com.example.classes.Scene;
import com.example.classes.Objects.*;
import com.example.museum.R;

@SuppressWarnings("rawtypes")
public class SceneMain extends Scene{
	
	public static enum ScreenState{
		NONE,
		MAIN,
		CLUE,
		MISSION,
		MENU;
		
		public static int toInt(ScreenState s){
			switch(s){
			case NONE:
				return 0;
			case MAIN:
				return 1;
			case CLUE:
				return 2;
			case MISSION:
				return 3;
			case MENU:
				return 4;
			}
			return -1;
		}
	}
	
	public static enum MissionType{
		HORIZONTAL,
		VERTICAL;
	}
	
	private MissionType missionType;
	
	
	
	//Menu
	private ButtonObject menuGallery;
	private ImageObject menuFactsLoco,
						menuFactsTank,
						menuFactsFieldGun,
						menuFactsSylvie,
						menuFactsPlanePropellers,
						menuFactsCrawler,
						menuOverlay;
	
	private ImageObject[] menuFactsheets = new ImageObject[6];
	// Main
	private TextObject mainTextStatus,
						mainTextClue,
						mainElapsedTime;
	
	private ImageObject mainImagePressMe,
						mainImageClue,
						mainImageTransportation;
	
	private ButtonObject mainButtonMenu,
						mainToggleClue,
						mainDebug;
	
	private ProgressBarObject mainHealthBar;
	
	// Mission
	private ImageObject missionBackground,
						missionCrosshair,
						crosshairIndicator;
	
	private ButtonObject missionButtonNext,
						menuContinue;
	
	private TextObject missionText,
						missionGivenStatus;
	
	
	private float crosshairOffsetX = 0;
	private float crosshairOffsetY = 0;

	private ScreenState screenState = ScreenState.MAIN;
	
	protected List<AbstractElement> listMainScreen;
	protected List<AbstractElement> listClueScreen;
	protected List<AbstractElement> listMissionScreen;
	protected List<AbstractElement> listMenuScreen;
	
	
	private long startTime;
	
	private Vibrator vibrator;
	
	
	boolean allMissionsCompleted = false;
	
	private String[] missionTitles,
					missionClues,
					missionIntroductions;
	
	private int[] missionImages = { R.drawable.clue_loco,
									R.drawable.clue_tank,
									R.drawable.clue_fieldgun,
									R.drawable.clue_sylvie,
									R.drawable.clue_plane,
									R.drawable.clue_crawler};
	
	
	
	private int[] missionFactsheetImages = new int[6];
	
	private long[] missionStartTimes = new long[6];
	private long[] missionCompletionTimes = new long[6];
	
	private int currentMission = -1;
	private boolean[] missionCompletion = new boolean[6];
	private int currentVibration = 5;
	private long vibrationPatterns[][] =  { {0, 1000, 0},  // 0
											{ 200, 150},  // 20
											{ 500, 150},  // 50
											{ 800, 200},  // 80
											{ 2000, 200} // 100
											};
	
	private boolean playing = false;
	private boolean windowHasFocus = true;
	private Random random = new Random();

	private int health = 100;
	
	private float transportOffsetX;
	private float transportOffsetY;
	
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		int xx = Globals.screenDimensions.x/24;
		int yy = Globals.screenDimensions.y/24;
		float prevX = 0;
		float prevY = 0;
		float cX;
		float cY;
		
		float lerp(float lastVal, float currentVal, float multiplier){
			return (lastVal + ((currentVal - lastVal)) * multiplier);
		}
		
		public void calculateCrosshair(ImageObject a, float offsetX, float offsetY){
				cX = xx * AccelerometerManager.getX();
			   
				cX = -cX;
				
				cX += offsetX + a.getElement().getX();
			   
				cY = 0;
			  
				if (missionType == MissionType.HORIZONTAL)
					cY = yy * AccelerometerManager.getY();
				else{
					cY = yy * AccelerometerManager.getZ();
					cY = -cY;
				}
				cY += offsetY + a.getElement().getY();
			   
				cX = lerp(prevX, cX, 0.3f);
				cY = lerp(prevY, cY, 0.3f);
				//imageCrosshairTarget.getElement().setPadding((int)w, (int)(crosshairOffset+q), 0, 0);
				crosshairIndicator.getElement().setPadding((int)(cX), (int)(cY), 0, 0);
			  
				prevX = cX;
				prevY = cY;
		}
		
		@Override
		public void run() {
	 		   
			if (screenState == ScreenState.MISSION){
				calculateCrosshair(missionCrosshair, crosshairOffsetX, crosshairOffsetY);
				if ((cX > missionCrosshair.getElement().getX() && cX < missionCrosshair.getElement().getX() + missionCrosshair.getWidth()) && 
					(cY > missionCrosshair.getElement().getY() && cY < missionCrosshair.getElement().getY() + missionCrosshair.getHeight()))
					missionCrosshair.getElement().setColorFilter(Colour.FromRGB(20, 255, 50));
				else
 				  missionCrosshair.getElement().setColorFilter(Colour.FromRGB(255, 0, 0), PorterDuff.Mode.MULTIPLY);
	 		   }
	           if (playing){
	        	   gameplay();
	        	   if (screenState == ScreenState.MAIN)
	        		   calculateCrosshair(mainImageTransportation, transportOffsetX, transportOffsetY);
	           }
	 	       handler.postDelayed(this, 100); 
	 	   }
	};
	
	private void gameplay(){
		vibrator.cancel();
		//Vibration patterns
		if (windowHasFocus){
			if (health <= 0 && currentVibration != 0){
				currentVibration = 0;
				vibrator.vibrate(vibrationPatterns[0], -1);
			}
			if (health > 0 && health <= 20 && currentVibration != 20){
				currentVibration = 1;
				vibrator.vibrate(vibrationPatterns[1], 0);
			}
			else if (health > 20 && health <= 50 && currentVibration != 50){
				currentVibration = 50;
				vibrator.vibrate(vibrationPatterns[2], 0);
			}
			else if (health > 50 && health <= 80 && currentVibration != 80){
				currentVibration = 80;
				vibrator.vibrate(vibrationPatterns[3], 0);
			}
			else if (health > 80 && health <= 100 && currentVibration != 100){
				currentVibration = 100;
				vibrator.vibrate(vibrationPatterns[4], 0);
			}
		}
		
		float y = 0;
		
		if (missionType == MissionType.VERTICAL)
			y = AccelerometerManager.getZ();
		else
			y = AccelerometerManager.getY();
		// Health effects
		if (Math.abs(AccelerometerManager.getX()) > 2 ||
				Math.abs(y) > 2.3){
			health -= Math.max(Math.abs(AccelerometerManager.getX()), Math.abs(y));
		}
		else if (health < 100)
			health++;
		
		if (health < 0){
			health = 0;
			missionGivenStatus.setText("You failed! Try again!");
			if (screenState == ScreenState.MAIN || screenState == ScreenState.CLUE)
				setScene(ScreenState.MISSION);
			return;
		}
		
		mainHealthBar.setValue(health);
		
		float rotVal= 0;
		
		if (missionType == MissionType.VERTICAL)
			rotVal = -(AccelerometerManager.getX()*4.5f);
		else if (missionType == MissionType.HORIZONTAL)
			rotVal = -(AccelerometerManager.getY()*4.5f);
		mainImageTransportation.rotate(rotVal);

		rotVal = Math.abs(rotVal);
		mainImageTransportation.getElement().setColorFilter(Colour.FromRGB((int)(102+(rotVal*2.5f)), (int)(123-(rotVal*2.5f)), (int)(42-(rotVal))), PorterDuff.Mode.MULTIPLY);
		
		long millis = System.currentTimeMillis() - startTime;
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds     = seconds % 60;
        mainElapsedTime.setText(String.format("Current elapsed \ntime: %d:%02d", minutes, seconds));
	}

	public SceneMain(int idIn, Activity a, boolean visible) {
		super(idIn, a, visible);
	}

	private void missionSetup(){
		playing = false;
		vibrator.cancel();
		
		if (currentMission >= 0){
			missionCompletionTimes[currentMission] = System.currentTimeMillis();
			menuFactsheets[currentMission].setImage(missionFactsheetImages[currentMission]);
			menuFactsheets[currentMission].setAbsScaleY(Globals.screenDimensions.y/6);
			missionGivenStatus.setText(missionTitles[currentMission] + " mission complete!\nFactsheet unlocked in menu!");
			missionCompletion[currentMission] = true;
		}
		
		boolean allComplete = true;
		for (boolean x : missionCompletion)
			if (!x){
				allComplete = false;
				break;
			}
		
		if (allComplete){
			allMissionsCompleted = true;

			currentMission = missionCompletion.length+1;
			missionText.setText("All missions complete!\n\n");			
			menuContinue.setText("All missions complete!");			
			for (int i = 0; i < 6; i++){
				long millis = missionCompletionTimes[i] - missionStartTimes[i];
		        int seconds = (int) (millis / 1000);
		        int minutes = seconds / 60;
		        seconds     = seconds % 60;
				missionText.getElement().append(missionTitles[i] + String.format(": %d:%02d\n", minutes, seconds));
			}
			missionText.getElement().append("\nClick the button to go back to menu and view your factsheets!");
			missionCrosshair.setVisibility(View.GONE);
			crosshairIndicator.setVisibility(View.GONE);
			return;
		}
		
		do{
			currentMission = random.nextInt(6);
		} while (missionCompletion[currentMission] == true);
		
		if (currentMission == 0 || currentMission == 3 || currentMission == 5){
			missionType = MissionType.VERTICAL;
			if (missionTitles[currentMission] == "Sylvie")
				mainImageTransportation.setImage(R.drawable.transportation_sylvie);
			else
				mainImageTransportation.setImage(R.drawable.transportation_crawler_green);
		}
		else{
			missionType = MissionType.HORIZONTAL;
			mainImageTransportation.setImage(R.drawable.transportation_horizontal_green);
		}
		
		mainImageTransportation.setAbsScaleY(Globals.screenDimensions.y/4);
		mainTextClue.setText(missionClues[currentMission]);
		crosshairIndicator.setVisibility(View.VISIBLE);
		startTime = System.currentTimeMillis();
		missionStartTimes[currentMission] = startTime;
		missionText.setText(missionIntroductions[currentMission] + "\n\n");
	}

	private void setScene(ScreenState sIn){
		screenState = sIn;
		List<AbstractElement> l = listMainScreen;

		if (sIn == ScreenState.MAIN){
			//l = listMainScreen;
		}
		else if (sIn == ScreenState.CLUE){
			l = listClueScreen;
		}
		else if (sIn == ScreenState.MISSION){
			l = listMissionScreen;
			activity.getLayout().get().getBackground().setAlpha(120);
		}
		
		transitionOut(l);
		
		if (sIn == ScreenState.MENU){
			transitionOut(listMenuScreen);
		}
		
		if (sIn == ScreenState.MISSION && allMissionsCompleted){
			missionCrosshair.setVisibility(View.GONE);
			crosshairIndicator.setVisibility(View.GONE);
			missionButtonNext.setVisibility(View.VISIBLE);
		}
	}

	public void setClickEvents(){
		
		missionButtonNext.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!allMissionsCompleted){
					mainImageTransportation.getElement().setEnabled(true);
					activity.getLayout().get().getBackground().setAlpha(255);
					setScene(ScreenState.MAIN);
					
					if (health != 0){
						mainTextStatus.setText("New mission given. Look at your new clue!");
						mainImageClue.setImage(missionImages[currentMission]);
						mainImageClue.setAbsScaleY((int)(Globals.screenDimensions.y/2.5f));
					}
					playing = true;
					health = 100;
				}
				else
					setScene(ScreenState.MENU);
			}
		});
		
		mainToggleClue.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (menuOverlay.getElement().getVisibility() == View.VISIBLE)
					return;
				
				if (screenState == ScreenState.MAIN)
					setScene(ScreenState.CLUE);	
				else
					setScene(ScreenState.MAIN);
			}
		});
		
		menuGallery.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.getLayout().get().getBackground().setAlpha(255);
				activity.SetScreenState(GameActivity.ScreenState.GALLERY);	
			}
		});
		
		mainButtonMenu.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setScene(ScreenState.MENU);
			}
		});
		
		mainDebug.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {;
				if (menuOverlay.getElement().getVisibility() == View.VISIBLE)
					return;
			
				activity.SetScreenState(GameActivity.ScreenState.DEBUG);
			}
		});
		
		mainImageTransportation.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (menuOverlay.getElement().getVisibility() == View.VISIBLE)
					return;
				
				if (!missionCompletion[currentMission]){
					if (EstimoteManager.getBeaconList().size() == 0){
						mainTextStatus.setText("Your Bluetooth may not be turned on, or your device is not supported.");
					}
					MyBeacon d = EstimoteManager.contains(missionTitles[currentMission].toString());
					if (d != null && d.getDistance() < 2.3f){
						mainImageTransportation.getElement().setEnabled(false);
						missionSetup();	
						setScene(ScreenState.MISSION);	
					}
					else
						mainTextStatus.setText("Hm, not in the right place. Keep looking!");
				}
			}
		});		
		
		menuContinue.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (allMissionsCompleted){
					activity.setScreenState(GameActivity.ScreenState.MAIN);
					setScene(ScreenState.MISSION);
					return;
				}

				setScene(ScreenState.MAIN);
			}
		});		
		
		menuFactsLoco.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (missionCompletionTimes[0] == 0)
					sheetLockedDialog("Loco");
				else{
				activity.SetScreenState(GameActivity.ScreenState.FACTSHEET);
				activity.getFactsheet().setScene(SceneFactsheet.ScreenState.LOCO);
				}
			}
		});		
		
		menuFactsTank.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (missionCompletionTimes[1] == 0)
					sheetLockedDialog("Tank");
				else{
				activity.SetScreenState(GameActivity.ScreenState.FACTSHEET);
				activity.getFactsheet().setScene(SceneFactsheet.ScreenState.TANK);
				}
			}
		});	
		
		menuFactsFieldGun.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (missionCompletionTimes[2] == 0)
					sheetLockedDialog("Field Gun");
				else{
				activity.SetScreenState(GameActivity.ScreenState.FACTSHEET);
				activity.getFactsheet().setScene(SceneFactsheet.ScreenState.FIELDGUN);
				}
			}
		});	
		
		menuFactsSylvie.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (missionCompletionTimes[3] == 0)
					sheetLockedDialog("Sylvie");
				else{
				activity.SetScreenState(GameActivity.ScreenState.FACTSHEET);
				activity.getFactsheet().setScene(SceneFactsheet.ScreenState.SYLVIE);
				}
			}
		});	
		
		menuFactsPlanePropellers.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (missionCompletionTimes[4] == 0)
					sheetLockedDialog("Plane Propellers");
				else{
					activity.SetScreenState(GameActivity.ScreenState.FACTSHEET);
					activity.getFactsheet().setScene(SceneFactsheet.ScreenState.PLANE);
				}
			}
		});	
		
		menuFactsCrawler.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (missionCompletionTimes[5] == 0)
					sheetLockedDialog("Crawler");
				else{
					activity.SetScreenState(GameActivity.ScreenState.FACTSHEET);
					activity.getFactsheet().setScene(SceneFactsheet.ScreenState.CRAWLER);
				}
			}
		});	
	}
	
	public void sheetLockedDialog(String s){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage("Complete the " + s + " mission to unlock this factsheet!");
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK!",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
	}
	
	public void onLoad(){
		activity.getLayout().get().setBackgroundResource(R.drawable.background_newtoo);
		setScene(screenState);
		if (startTime == 0)
			startTime = System.currentTimeMillis();
	}

	public void toggleVibration(boolean on){
		currentVibration = -1;
		windowHasFocus = on;
		if (!on)
			vibrator.cancel();
	}

	@Override
	public void onBackPressed() {
		if (screenState == ScreenState.MAIN)
			System.exit(0);
		else if (screenState == ScreenState.CLUE)
			setScene(ScreenState.MAIN);
		else if (screenState == ScreenState.MENU){
			if (!allMissionsCompleted){
				if (health > 0)
					setScene(ScreenState.MAIN);
				else
					setScene(ScreenState.MISSION);
			}	
		}
	}
	
	private void itemSetup(Activity aIn){
		
		listMainScreen = new ArrayList<AbstractElement>();
		listClueScreen = new ArrayList<AbstractElement>();
		listMissionScreen = new ArrayList<AbstractElement>();
		listMenuScreen = new ArrayList<AbstractElement>();
		
		missionTitles = aIn.getResources().getStringArray(R.array.mission_titles_array);
		missionClues = aIn.getResources().getStringArray(R.array.mission_clues_array);
		missionIntroductions = aIn.getResources().getStringArray(R.array.mission_introductions_array);

        
        mainImageClue = new ImageObject(R.drawable.clue1, aIn, Globals.newId(), false);
        mainImageClue.alignToTop();
        mainImageClue.getElement().setPaddingRelative(0, Globals.screenDimensions.y/100, 0, Globals.screenDimensions.y/100);
        mainImageClue.setAbsScaleY((int)(Globals.screenDimensions.y/2.5f));
    
        mainTextStatus = new TextObject("Mission status text box with long words everywhere", aIn, Globals.newId());
        mainTextStatus.addRule(RelativeLayout.BELOW, mainImageClue.getId());
        mainTextStatus.getElement().setWidth(Globals.screenDimensions.x-(Globals.screenDimensions.x/10));
        mainTextStatus.getElement().setTextSize(Globals.getTextSize());
        mainTextStatus.getElement().setGravity(Gravity.CENTER);
        mainTextStatus.getElement().setTypeface(Globals.Fonts.ExoRegular());

        mainButtonMenu = new ButtonObject("Menu", aIn, Globals.newId());
        mainButtonMenu.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mainButtonMenu.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mainButtonMenu.getLayoutParams().setMargins((Globals.screenDimensions.x/60), Globals.screenDimensions.y/40, 0, Globals.screenDimensions.y/20);
        mainButtonMenu.getElement().setTextColor(Colour.FromRGB(10, 0, 10));
        mainButtonMenu.getElement().setBackgroundColor(Colour.Transparent);
        mainButtonMenu.getElement().setTypeface(Globals.Fonts.MajorShift());
        mainButtonMenu.getElement().setTextSize(Globals.getTextSize()*2.2f);
        
        mainDebug = new ButtonObject("Debug", aIn, Globals.newId());
        mainDebug.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mainDebug.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mainDebug.getLayoutParams().setMargins(0, 0, (Globals.screenDimensions.x/12), Globals.screenDimensions.y/20);
        
        mainToggleClue = new ButtonObject("Toggle Clue", aIn, Globals.newId());
        mainToggleClue.addRule(RelativeLayout.ALIGN_TOP, mainTextStatus.getId());
        mainToggleClue.getLayoutParams().setMargins(0, Globals.screenDimensions.y/14, 0, 0);
        mainToggleClue.getElement().setTextSize(Globals.getTextSize());
        mainToggleClue.setWidth(Globals.screenDimensions.x/4);
        
        mainImageTransportation = new ImageObject(R.drawable.transportation_crawler_green, aIn, Globals.newId(), true);
        mainImageTransportation.setBackgroundColour(Color.TRANSPARENT);
        mainImageTransportation.addRule(RelativeLayout.BELOW, mainToggleClue.getId());
        mainImageTransportation.getLayoutParams().setMargins(0, Globals.screenDimensions.y/20, 0, 0);
        mainImageTransportation.setAbsScaleY(Globals.screenDimensions.y/4);
        
        mainImagePressMe = new ImageObject(R.drawable.press_me, aIn, Globals.newId(), false);
        mainImagePressMe.setAbsScaleX(Globals.screenDimensions.x/4);
        mainImagePressMe.addRule(RelativeLayout.RIGHT_OF, mainImageTransportation.getId());
        mainImagePressMe.addRule(RelativeLayout.ALIGN_TOP, mainImageTransportation.getId());

        mainHealthBar = new ProgressBarObject(aIn, Globals.newId(), true);
        mainHealthBar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mainHealthBar.getLayoutParams().setMargins(0, 0, 0, Globals.screenDimensions.y/40);
        mainHealthBar.setWidth(mainImageTransportation.getWidth());
        mainHealthBar.setValue(50);

        mainElapsedTime = new TextObject("Current elapsed time: 0:00", aIn, Globals.newId());
        mainElapsedTime.addRule(RelativeLayout.ALIGN_TOP, mainToggleClue.getId());
        mainElapsedTime.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mainElapsedTime.getLayoutParams().setMargins(Globals.screenDimensions.x/12, 0, 0, 0);
        mainElapsedTime.getElement().setGravity(Gravity.CENTER);
        mainElapsedTime.getElement().setTypeface(Globals.Fonts.ExoRegular());
        mainElapsedTime.getElement().setTextSize(Globals.getTextSize()*0.8f);
        
        mainTextClue = new TextObject("This is the clue text it is clue text that contains a clue of varying length.", aIn, Globals.newId());
        mainTextClue.addRule(RelativeLayout.ALIGN_TOP, mainToggleClue.getId());
        mainTextClue.getElement().setTextSize(Globals.getTextSize()*1.4f);
        mainTextClue.setWidth(Globals.screenDimensions.x/2.2f);
        mainTextClue.getLayoutParams().setMargins(0, Globals.screenDimensions.y/10, 0, 0);
        mainTextClue.getElement().setGravity(Gravity.CENTER);
        mainTextClue.getElement().setTypeface(Globals.Fonts.ExoRegular());
        
		missionGivenStatus = new TextObject("", aIn, Globals.newId());
		missionGivenStatus.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		missionGivenStatus.getLayoutParams().setMargins(0, Globals.screenDimensions.y/20, 0, Globals.screenDimensions.y/20);
		missionGivenStatus.getElement().setTextSize(Globals.getTextSize()*1.3f);
		missionGivenStatus.getElement().setTypeface(Globals.Fonts.ChunkFive());
		missionGivenStatus.setTextColour(Colour.FromRGB(255, 20, 20));
		
		missionText = new TextObject("Teashdas", aIn, Globals.newId());
		missionText.getElement().setTextSize(Globals.getTextSize()*1.4f);
		missionText.addRule(RelativeLayout.BELOW, missionGivenStatus.getId());
		missionText.getLayoutParams().setMargins(Globals.screenDimensions.x/10, 0, Globals.screenDimensions.x/10, 0);
		missionText.getElement().setTypeface(Globals.Fonts.ExoRegular());
		
		missionBackground = new ImageObject(R.drawable.background, aIn, Globals.newId(), false);
		missionBackground.setScale(Globals.screenDimensions.x - (Globals.screenDimensions.x/10), 
        		Globals.screenDimensions.y - (Globals.screenDimensions.y/10));
		
		missionButtonNext = new ButtonObject("Continue", aIn, Globals.newId());
		missionButtonNext.getLayoutParams().setMargins(0, 0, 0, Globals.screenDimensions.y/15);
		missionButtonNext.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		missionCrosshair = new ImageObject(R.drawable.crosshair_red, aIn, Globals.newId(), false);
		missionCrosshair.setAbsScaleX(Globals.screenDimensions.x/6);
		missionCrosshair.addRule(RelativeLayout.ABOVE, missionButtonNext.getId());
		
		crosshairIndicator = new ImageObject(R.drawable.crosshair_red, aIn, Globals.newId(), false);
		crosshairIndicator.alignToTop();
		crosshairIndicator.alignToLeft();
		crosshairIndicator.setAbsScaleX(Globals.screenDimensions.x/26);
		
		menuGallery = new ButtonObject("Gallery", aIn, Globals.newId());
		menuFactsLoco = new ImageObject(R.drawable.factsheet_loco_locked, aIn, Globals.newId(), true);
		menuFactsTank = new ImageObject(R.drawable.factsheet_tank_locked, aIn, Globals.newId(), true);
		menuFactsFieldGun = new ImageObject(R.drawable.factsheet_fieldgun_locked, aIn, Globals.newId(), true);
		menuFactsSylvie = new ImageObject(R.drawable.factsheet_sylvie_locked, aIn, Globals.newId(), true);
		menuFactsPlanePropellers = new ImageObject(R.drawable.factsheet_plane_locked, aIn, Globals.newId(), true);
		menuFactsCrawler = new ImageObject(R.drawable.factsheet_crawler_locked, aIn, Globals.newId(), true);
		
		menuFactsLoco.setAbsScaleY(Globals.screenDimensions.y/6);
		menuFactsTank.setAbsScaleY(Globals.screenDimensions.y/6);
		menuFactsFieldGun.setAbsScaleY(Globals.screenDimensions.y/6);
		menuFactsSylvie.setAbsScaleY(Globals.screenDimensions.y/6);
		menuFactsPlanePropellers.setAbsScaleY(Globals.screenDimensions.y/6);
		menuFactsCrawler.setAbsScaleY(Globals.screenDimensions.y/6);
		
		menuFactsheets[0] = menuFactsLoco;
		menuFactsheets[1] = menuFactsTank;
		menuFactsheets[2] = menuFactsFieldGun;
		menuFactsheets[3] = menuFactsSylvie;
		menuFactsheets[4] = menuFactsPlanePropellers;
		menuFactsheets[5] = menuFactsCrawler;
		
		missionFactsheetImages[0] = R.drawable.factsheet_loco_unlocked;
		missionFactsheetImages[1] = R.drawable.factsheet_tank_unlocked;
		missionFactsheetImages[2] = R.drawable.factsheet_fieldgun_unlocked;
		missionFactsheetImages[3] = R.drawable.factsheet_sylvie_unlocked;
		missionFactsheetImages[4] = R.drawable.factsheet_plane_unlocked;
		missionFactsheetImages[5] = R.drawable.factsheet_crawler_unlocked;
	}
	
	@Override
	public void sceneInit(Activity aIn, boolean visible) {
		itemSetup(aIn);
		Bitmap b = Bitmap.createBitmap(Colour.ArrFromRGB(0,0,0,180), 1, 1, Bitmap.Config.ARGB_8888);
		menuOverlay = new ImageObject(b, aIn, Globals.newId(), false);
		menuOverlay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
															RelativeLayout.LayoutParams.MATCH_PARENT));
		menuOverlay.getElement().setScaleType(ScaleType.FIT_XY);
		menuOverlay.getElement().setClickable(false);
		menuOverlay.setVisibility(View.GONE);
		
		int marginWidth = (int)(Globals.screenDimensions.x/2 - (menuFactsSylvie.getWidth()*1.3f));
		
		menuFactsFieldGun.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		menuFactsFieldGun.getLayoutParams().setMargins(marginWidth, 0, 0, 0);
		
		menuFactsSylvie.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		menuFactsSylvie.getLayoutParams().setMargins(0, 0, marginWidth, 0);
		
		menuFactsLoco.getLayoutParams().setMargins(marginWidth, 0, 0, 0);
		menuFactsLoco.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		menuFactsLoco.addRule(RelativeLayout.ABOVE, menuFactsFieldGun.getId());
		
		menuFactsTank.getLayoutParams().setMargins(0, 0, marginWidth, 0);
		menuFactsTank.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		menuFactsTank.addRule(RelativeLayout.ABOVE, menuFactsSylvie.getId());
		
		menuFactsPlanePropellers.getLayoutParams().setMargins(marginWidth, 0, 0, 0);
		menuFactsPlanePropellers.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		menuFactsPlanePropellers.addRule(RelativeLayout.BELOW, menuFactsFieldGun.getId());
		
		menuFactsCrawler.getLayoutParams().setMargins(0, 0, marginWidth, 0);
		menuFactsCrawler.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		menuFactsCrawler.addRule(RelativeLayout.BELOW, menuFactsSylvie.getId());
		
		menuContinue = new ButtonObject("CONTINUE\nPLAYING", aIn, Globals.newId());
		menuContinue.getElement().setTypeface(Globals.Fonts.MajorShift());
		menuContinue.getLayoutParams().setMargins(0, Globals.screenDimensions.y/36, 0, 0);
		menuContinue.getElement().setTextSize(Globals.getTextSize()*2.8f);
		menuContinue.getElement().setBackgroundColor(android.graphics.Color.TRANSPARENT);
		menuContinue.addRule(RelativeLayout.BELOW, menuFactsPlanePropellers.getId());
		
		menuGallery = new ButtonObject("VIEW\nGALLERY", aIn, Globals.newId());
		menuGallery.getElement().setTypeface(Globals.Fonts.MajorShift());
		menuGallery.getLayoutParams().setMargins(0, 0, 0, Globals.screenDimensions.y/36);
		menuGallery.getElement().setTextSize(Globals.getTextSize()*2.8f);
		menuGallery.getElement().setBackgroundColor(android.graphics.Color.TRANSPARENT);
		menuGallery.addRule(RelativeLayout.ABOVE, menuFactsLoco.getId());

        listMenuScreen.add(menuOverlay);
        listMenuScreen.add(menuGallery);

        for (int i = 0; i < 6; i++)
        	listMenuScreen.add(menuFactsheets[i]);
        
		listMenuScreen.add(menuContinue);

        listMainScreen.add(mainImageClue);
        listMainScreen.add(mainTextStatus);
        listMainScreen.add(mainImageTransportation);
        listMainScreen.add(mainImagePressMe);
        listMainScreen.add(mainHealthBar);
        listMainScreen.add(mainButtonMenu);
        listMainScreen.add(mainDebug);
        listMainScreen.add(mainToggleClue);
        listMainScreen.add(mainElapsedTime);

        listMissionScreen.add(missionBackground);
        listMissionScreen.add(missionButtonNext);
        listMissionScreen.add(missionText);
        listMissionScreen.add(missionCrosshair);
        listMissionScreen.add(crosshairIndicator);
        listMissionScreen.add(missionGivenStatus);

        listClueScreen.add(mainImageClue);
        listClueScreen.add(mainTextStatus);
        listClueScreen.add(mainButtonMenu);
        listClueScreen.add(mainDebug);
        listClueScreen.add(mainToggleClue);
        listClueScreen.add(mainElapsedTime);
        listClueScreen.add(mainTextClue);
        listClueScreen.add(mainHealthBar);
        
        for (AbstractElement a : listMainScreen)
        	addElementToView(a);

        for (AbstractElement a : listClueScreen)
        	addElementToView(a);
        
        for (AbstractElement a : listMenuScreen)
        	addElementToView(a);
        
        for (AbstractElement a : listMissionScreen)
        	addElementToView(a);
        
        listMainScreen.add(crosshairIndicator);
        addElementToView(crosshairIndicator);
        
        setClickEvents();
        setScene(ScreenState.MAIN);
        
        super.sceneInit(aIn, visible);
        
        
		Arrays.fill(missionCompletionTimes, 0);
		Arrays.fill(missionCompletion, false);
		vibrator = (Vibrator)aIn.getSystemService(Context.VIBRATOR_SERVICE);
		
		screenState = ScreenState.MISSION;
		missionSetup();

		
		crosshairOffsetX = missionCrosshair.getHeight()/2;
		crosshairOffsetX -= crosshairIndicator.getHeight()/2;
		
		crosshairOffsetY = missionCrosshair.getWidth()/2;
		crosshairOffsetY -= crosshairIndicator.getWidth()/2;
		
		transportOffsetX = mainImageTransportation.getHeight()/2;
		transportOffsetX -= crosshairIndicator.getHeight()/2;
		
		transportOffsetY = mainImageTransportation.getWidth()/2;

		handler.postDelayed(runnable, 100);
	}

}
