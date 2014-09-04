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
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TableLayout;

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
	
	//Scene objects
	private TextObject textTitle;
	private TextObject textStatus;
	private TextObject textClue;
	private ImageObject imageClue;
	private ImageObject imageTransportation;
	private ButtonObject buttonMenu;
	
	private ProgressBarObject healthBar;
	private ButtonObject buttonDebug;
	
	private ButtonObject buttonToggleClue;
	
	private ScreenState screenState = ScreenState.MAIN;
	
	protected List<AbstractElement> listMainScreen;
	protected List<AbstractElement> listClueScreen;
	protected List<AbstractElement> listMissionScreen;
	protected List<AbstractElement> listMenuScreen;
	
	private TextObject textCurrentElapsedTime;
	private long startTime;
	
	private Vibrator vibrator;
	
	
	
	private String[] missionTitles;
	
	private String[] missionClues;
	
	private String[] missionIntroductions;
	
	private int[] missionImages = { R.drawable.clue_loco,
									R.drawable.clue_tank,
									R.drawable.clue_fieldgun,
									R.drawable.clue_sylvie,
									R.drawable.clue_plane,
									R.drawable.clue_crawler};
	
	private ImageObject[] missionFactsheets = new ImageObject[6];
	
	private int[] missionFactsheetImages = new int[6];
	
	private long[] missionStartTimes = new long[6];
	private long[] missionCompletionTimes = new long[6];
	
	private int missionCount = -1;
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
	
	private Random random = new Random();
	
	private MissionType missionType;
	
	private int health = 100;
	
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		int xx = Globals.screenDimensions.x/24;
		int yy = Globals.screenDimensions.y/24;
		float prevX = 0;
		float prevY = 0;
		
		float cX;
		float cY;
		
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
			  imageCrosshairTarget.getElement().setPadding((int)(cX), (int)(cY), 0, 0);
			  
			  prevX = cX;
 			  prevY = cY;
		}
	 	   @Override
	 	   public void run() {
	 		   
	 		   if (screenState == ScreenState.MISSION){
	 			   
	 			  calculateCrosshair(imageCrosshair, crosshairOffsetX, crosshairOffsetY);
	 			  if ((cX > imageCrosshair.getElement().getX() && cX < imageCrosshair.getElement().getX() + imageCrosshair.getWidth()) && 
	 					 (cY > imageCrosshair.getElement().getY() && cY < imageCrosshair.getElement().getY() + imageCrosshair.getHeight()))
	 				  imageCrosshair.getElement().setColorFilter(Colour.FromRGB(20, 255, 50));
	 			  else
	 				  imageCrosshair.getElement().setColorFilter(Colour.FromRGB(255, 0, 0), PorterDuff.Mode.MULTIPLY);
	 			  
	 			  
	 			 // imageCrosshairTarget.addView(activity.getLayout().get());
	 		   }
	           if (playing){
	        	   gameplay();
	        	   if (screenState == ScreenState.MAIN)
	        		   calculateCrosshair(imageTransportation, transportOffsetX, transportOffsetY);
	           }
	 	       handler.postDelayed(this, 100); 
	 	   }
	};
	
	float lerp(float lastVal, float currentVal, float multiplier)
	{
		return (lastVal + ((currentVal - lastVal)) * multiplier);
	}
	
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
		
		healthBar.setValue(health);
		
		float rotVal= 0;
		
		if (missionType == MissionType.VERTICAL)
			rotVal = -(AccelerometerManager.getX()*4.5f);
		else if (missionType == MissionType.HORIZONTAL)
			rotVal = -(AccelerometerManager.getY()*4.5f);
		imageTransportation.rotate(rotVal);
		//imageTransportationRed.rotate(rotVal);
		
		//102 123 42
		
		rotVal = Math.abs(rotVal);
		imageTransportation.getElement().setColorFilter(Colour.FromRGB((int)(102+(rotVal*2.5f)), (int)(123-(rotVal*2.5f)), (int)(42-(rotVal))), PorterDuff.Mode.MULTIPLY);
		
		//imageTransportation.getElement().setAlpha((int)(255-(Math.abs(rotVal*5))));
        
		long millis = System.currentTimeMillis() - startTime;
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds     = seconds % 60;
        textCurrentElapsedTime.setText(String.format("Current elapsed \ntime: %d:%02d", minutes, seconds));
	}

	public SceneMain(int idIn, Activity a, boolean visible) {
		super(idIn, a, visible);
		
		
	}
	
	ImageObject imageMissionGiving;
	ButtonObject buttonMissionNext;
	TextObject textMissionText;
	ImageObject imageCrosshair;
	ImageObject imageCrosshairTarget;
	
	private void missionSetup(){
		playing = false;

		//((AnimationDrawable)activity.getLayout().get().getBackground()).
//		setScene(ScreenState.GIVING_MISSION);
		
		//imageMissionGiving.getElement().setAlpha(1f);
		//imageMissionGiving.setVisibility(View.VISIBLE);
		//buttonMissionNext.setVisibility(View.VISIBLE);
		//textMissionText.setVisibility(View.VISIBLE);
		vibrator.cancel();
		
		if (currentMission >= 0){
			missionCompletionTimes[currentMission] = System.currentTimeMillis();
			missionFactsheets[currentMission].setImage(missionFactsheetImages[currentMission]);
			missionFactsheets[currentMission].setAbsScaleY(Globals.screenDimensions.y/6);
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
			
			//buttonMissionNext.setVisibility(View.GONE);
		    //listMissionScreen.remove(buttonMissionNext);
			currentMission = missionCompletion.length+1;
			textMissionText.setText("All missions complete!\n\n");
			
			menuContinue.setText("All missions complete!");
			
		
			
			for (int i = 0; i < 6; i++){
				long millis = missionCompletionTimes[i] - missionStartTimes[i];
		        int seconds = (int) (millis / 1000);
		        int minutes = seconds / 60;
		        seconds     = seconds % 60;
				textMissionText.getElement().append(missionTitles[i] + String.format(": %d:%02d\n", minutes, seconds));
			}
			
			textMissionText.getElement().append("\nClick the button to go back to menu and view your factsheets!");
			//setScene(ScreenState.MENU);
			
			imageCrosshair.setVisibility(View.GONE);
			imageCrosshairTarget.setVisibility(View.GONE);
			
			return;
		}
		
		do{
			currentMission = random.nextInt(6);
		} while (missionCompletion[currentMission] == true);
		
		if (currentMission == 0 || currentMission == 3 || currentMission == 5){
			missionType = MissionType.VERTICAL;
			if (missionTitles[currentMission] == "Sylvie")
				imageTransportation.setImage(R.drawable.transportation_sylvie);
			else
				imageTransportation.setImage(R.drawable.transportation_crawler_green);
		}
		else{
			missionType = MissionType.HORIZONTAL;
			imageTransportation.setImage(R.drawable.transportation_horizontal_green);
		}
		
		imageTransportation.setAbsScaleY(Globals.screenDimensions.y/4);
		textClue.setText(missionClues[currentMission]);
		imageCrosshairTarget.setVisibility(View.VISIBLE);
		missionCount++;
		startTime = System.currentTimeMillis();
		missionStartTimes[currentMission] = startTime;
		textMissionText.setText(missionIntroductions[currentMission] + "\n\n");
	}

	
	
	boolean allMissionsCompleted = false;
	
	
	private void setScene(ScreenState sIn){
		screenState = sIn;
		//setVisibility(View.GONE);
		List<AbstractElement> l = listMainScreen;

		if (sIn == ScreenState.MAIN){
			//listl = listMainScreen;
		}
		else if (sIn == ScreenState.CLUE){
			l = listClueScreen;
		}
		else if (sIn == ScreenState.MISSION){
			l = listMissionScreen;
				//textStatus.setText("Mission complete! You did it!");
				//missionCompletion[currentMission] = true;
			//if (health != 0){
				//missionSetup();
				//if (allMissionsCompleted){
					//setScene(ScreenState.MENU);
				//	return;
				//}
			//}
				activity.getLayout().get().getBackground().setAlpha(120);
		}
		transitionOut(l);
		
		if (sIn == ScreenState.MENU){
			transitionOut(listMenuScreen);
		}
		
		if (sIn == ScreenState.MISSION && allMissionsCompleted){
			imageCrosshair.setVisibility(View.GONE);
			imageCrosshairTarget.setVisibility(View.GONE);
			buttonMissionNext.setVisibility(View.VISIBLE);
		}
	}
	
	
	public void setClickEvents(){
		
		buttonMissionNext.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!allMissionsCompleted){
					imageTransportation.getElement().setEnabled(true);
					activity.getLayout().get().getBackground().setAlpha(255);
					setScene(ScreenState.MAIN);
					
					if (health != 0){
						//textClue.setText(missionClues[currentMission]);
						textStatus.setText("New mission given. Look at your new clue!");
						imageClue.setImage(missionImages[currentMission]);
						imageClue.setAbsScaleY((int)(Globals.screenDimensions.y/2.5f));
						
					}
					
					playing = true;
					health = 100;
				}
				else
					setScene(ScreenState.MENU);
			}
		});
		
		buttonToggleClue.getElement().setOnClickListener(new View.OnClickListener() {
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
				//if (screenState == ScreenState.MAIN)
				//{
				
				
					//menuOverlay.setVisibility(View.VISIBLE);
					//menuOverlay.getElement().animate().alpha(1.0f);
				//}
				activity.getLayout().get().getBackground().setAlpha(255);
				activity.SetScreenState(GameActivity.ScreenState.GALLERY);	
				
				
			}
		});
		
		buttonMenu.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//if (screenState == ScreenState.MAIN)
				//{
				setScene(ScreenState.MENU);
				
				//for (AbstractElement a: listMenuScreen){
				//	a.setVisibility(View.VISIBLE);
				//	a.getElementView().animate().alpha(1.0f);
				//}
				//	screenState = ScreenState.MENU;
				//}
				//activity.SetScreenState(GameActivity.ScreenState.GALLERY);	
			}
		});
		
		buttonDebug.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {;
			
			
				if (menuOverlay.getElement().getVisibility() == View.VISIBLE)
					return;
			
				activity.SetScreenState(GameActivity.ScreenState.DEBUG);
			}
		});
		
		imageTransportation.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (menuOverlay.getElement().getVisibility() == View.VISIBLE)
					return;
				
				if (!missionCompletion[currentMission]){
					if (EstimoteManager.getBeaconList().size() == 0){
						textStatus.setText("Your Bluetooth may not be turned on, or your device is not supported.");
					}
					MyBeacon d = EstimoteManager.contains(missionTitles[currentMission].toString());
					if (d != null && d.getDistance() < 2.3f){
						imageTransportation.getElement().setEnabled(false);
						missionSetup();	
						setScene(ScreenState.MISSION);	
					}
					else
						textStatus.setText("Hm, not in the right place. Keep looking!");
				}
				//else
				//	missionSetup();
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
				//sheetLockedDialog("Loco");
				//else
				//	missionSetup();
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
				//sheetLockedDialog("Loco");
				//else
				//	missionSetup();
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
				//sheetLockedDialog("Tanks");
				//else
				//	missionSetup();
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
				//sheetLockedDialog("Field Gun");
				//else
				//	missionSetup();
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
				//sheetLockedDialog("Sylvie");
				//else
				//	missionSetup();
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
				//sheetLockedDialog("Plane");
				//else
				//	missionSetup();
			}
		});	
		
		menuFactsCrawler.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//sheetLockedDialog("Crawler");
				if (missionCompletionTimes[5] == 0)
					sheetLockedDialog("Crawler");
				else{
				activity.SetScreenState(GameActivity.ScreenState.FACTSHEET);
				activity.getFactsheet().setScene(SceneFactsheet.ScreenState.CRAWLER);
				}
				//else
				//	missionSetup();
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
	private boolean windowHasFocus = true;
	
	public void toggleVibration(boolean on){
		currentVibration = -1;
		
		windowHasFocus = on;
		if (!on)
			vibrator.cancel();
	}
	protected void update(){}

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
	
	
	ButtonObject menuGallery;
	//ButtonObject menuFactsLoco;
	//ButtonObject menuFactsTank;
	//ButtonObject menuFactsFieldGun;
	//ButtonObject menuFactsPlanePropellers;
	//ButtonObject menuFactsCrawler;
	ImageObject menuFactsLoco;
	ImageObject menuFactsTank;
	ImageObject menuFactsFieldGun;
	ImageObject menuFactsSylvie;
	ImageObject menuFactsPlanePropellers;
	ImageObject menuFactsCrawler;
	
	ImageObject imageTransportationRed;
	ImageObject imagePressMe;
	
	TextObject missionGivenStatus;
	private void itemSetup(Activity aIn){
		
		listMainScreen = new ArrayList<AbstractElement>();
		listClueScreen = new ArrayList<AbstractElement>();
		listMissionScreen = new ArrayList<AbstractElement>();
		listMenuScreen = new ArrayList<AbstractElement>();
		
		missionTitles = aIn.getResources().getStringArray(R.array.mission_titles_array);
		missionClues = aIn.getResources().getStringArray(R.array.mission_clues_array);
		missionIntroductions = aIn.getResources().getStringArray(R.array.mission_introductions_array);
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
        textStatus.getElement().setTextSize(Globals.getTextSize());
        textStatus.getElement().setGravity(Gravity.CENTER);
        textStatus.getElement().setTypeface(Globals.Fonts.ExoRegular());
        
        
       
        
        
        //textClue.getLayoutParams().setMarginStart(Globals.screenDimensions.x/20);
        
        buttonMenu = new ButtonObject("Menu", aIn, Globals.newId());
        buttonMenu.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        buttonMenu.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        buttonMenu.getLayoutParams().setMargins((Globals.screenDimensions.x/60), Globals.screenDimensions.y/40, 0, Globals.screenDimensions.y/20);
        buttonMenu.getElement().setTextColor(Colour.FromRGB(10, 0, 10));
        buttonMenu.getElement().setBackgroundColor(Colour.Transparent);
        buttonMenu.getElement().setTypeface(Globals.Fonts.MajorShift());
        buttonMenu.getElement().setTextSize(Globals.getTextSize()*2.2f);
       // buttonGallery.addRule(RelativeLayout.ALIGN_START, textClue.getId());
        
        buttonDebug = new ButtonObject("Debug", aIn, Globals.newId());
        buttonDebug.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        buttonDebug.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        buttonDebug.getLayoutParams().setMargins(0, 0, (Globals.screenDimensions.x/12), Globals.screenDimensions.y/20);
        
        buttonToggleClue = new ButtonObject("Toggle Clue", aIn, Globals.newId());
        buttonToggleClue.addRule(RelativeLayout.ALIGN_TOP, textStatus.getId());
        buttonToggleClue.getLayoutParams().setMargins(0, Globals.screenDimensions.y/14, 0, 0);
        buttonToggleClue.getElement().setTextSize(Globals.getTextSize());
        buttonToggleClue.setWidth(Globals.screenDimensions.x/4);
        
        imageTransportation = new ImageObject(R.drawable.transportation_crawler_green, aIn, Globals.newId(), true);
        //imageTransportation.addRule(RelativeLayout.BELOW, textStatus.getId());
        imageTransportation.setBackgroundColour(Color.TRANSPARENT);
        imageTransportation.addRule(RelativeLayout.BELOW, buttonToggleClue.getId());
        imageTransportation.getLayoutParams().setMargins(0, Globals.screenDimensions.y/20, 0, 0);
        imageTransportation.setAbsScaleY(Globals.screenDimensions.y/4);
        
        imageTransportationRed = new ImageObject(R.drawable.transportation_crawler_red, aIn, Globals.newId(), false);
        //imageTransportation.addRule(RelativeLayout.BELOW, textStatus.getId());
        //imageTransportationRed.setBackgroundColour(Color.TRANSPARENT);
        //imageTransportationRed.addRule(RelativeLayout.BELOW, buttonToggleClue.getId());
        //imageTransportationRed.getLayoutParams().setMargins(0, Globals.screenDimensions.y/20, 0, 0);
        imageTransportationRed.getLayoutParams().setMargins(0, Globals.screenDimensions.y/20, 0, 0);
        imageTransportationRed.setAbsScaleY(Globals.screenDimensions.y/4);
        
        imagePressMe = new ImageObject(R.drawable.press_me, aIn, Globals.newId(), false);
        imagePressMe.setAbsScaleX(Globals.screenDimensions.x/4);
        imagePressMe.addRule(RelativeLayout.RIGHT_OF, imageTransportation.getId());

        imagePressMe.addRule(RelativeLayout.ALIGN_TOP, imageTransportation.getId());
        
        
        imageTransportationRed.addRule(RelativeLayout.ALIGN_BOTTOM, imageTransportation.getId());
        //imageTransportation.getElement().setPaddingRelative(Globals.screenDimensions.x/20, 0, 0, Globals.screenDimensions.y/30);
        
        healthBar = new ProgressBarObject(aIn, Globals.newId(), true);
        healthBar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
       // healthBar.addRule(RelativeLayout.ALIGN_START, imageTransportation.getId());
        healthBar.getLayoutParams().setMargins(0, 0, 0, Globals.screenDimensions.y/40);
        healthBar.setWidth(imageTransportation.getWidth());
        healthBar.setValue(50);
        //healthBar.setMode(Mode.LIGHTEN);
       // healthBar.getElement().setProgressDrawable(aIn.getResources().getDrawable(R.drawable.find_button));
       // healthBar.getElement().setScaleY(3);
        
        
        textCurrentElapsedTime = new TextObject("Current elapsed time: 0:00", aIn, Globals.newId());
        textCurrentElapsedTime.addRule(RelativeLayout.ALIGN_TOP, buttonToggleClue.getId());
        textCurrentElapsedTime.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        textCurrentElapsedTime.getLayoutParams().setMargins(Globals.screenDimensions.x/12, 0, 0, 0);
        textCurrentElapsedTime.getElement().setGravity(Gravity.CENTER);
        textCurrentElapsedTime.getElement().setTypeface(Globals.Fonts.ExoRegular());
        textCurrentElapsedTime.getElement().setTextSize(Globals.getTextSize()*0.8f);
        
        textClue = new TextObject("This is the clue text it is clue text that contains a clue of varying length.", aIn, Globals.newId());
        textClue.addRule(RelativeLayout.ALIGN_TOP, buttonToggleClue.getId());
        textClue.getElement().setTextSize(Globals.getTextSize()*1.4f);
        textClue.setWidth(Globals.screenDimensions.x/2.2f);
        textClue.getLayoutParams().setMargins(0, Globals.screenDimensions.y/10, 0, 0);
        textClue.getElement().setGravity(Gravity.CENTER);
        textClue.getElement().setTypeface(Globals.Fonts.ExoRegular());
        
        
        imageMissionGiving = new ImageObject(R.drawable.background, aIn, Globals.newId(), false);
		buttonMissionNext = new ButtonObject("Continue", aIn, Globals.newId());
		
		missionGivenStatus = new TextObject("", aIn, Globals.newId());
		missionGivenStatus.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		missionGivenStatus.getLayoutParams().setMargins(0, Globals.screenDimensions.y/20, 0, Globals.screenDimensions.y/20);
		missionGivenStatus.getElement().setTextSize(Globals.getTextSize()*1.3f);
		missionGivenStatus.getElement().setTypeface(Globals.Fonts.ChunkFive());
		missionGivenStatus.setTextColour(Colour.FromRGB(255, 20, 20));
		
		textMissionText = new TextObject("Teashdas", aIn, Globals.newId());
		textMissionText.getElement().setTextSize(Globals.getTextSize()*1.4f);
		textMissionText.addRule(RelativeLayout.BELOW, missionGivenStatus.getId());
		textMissionText.getLayoutParams().setMargins(Globals.screenDimensions.x/10, 0, Globals.screenDimensions.x/10, 0);
		textMissionText.getElement().setTypeface(Globals.Fonts.ExoRegular());
		
		
		imageMissionGiving.setScale(Globals.screenDimensions.x - (Globals.screenDimensions.x/10), 
        		Globals.screenDimensions.y - (Globals.screenDimensions.y/10));
		
		imageCrosshair = new ImageObject(R.drawable.crosshair_red, aIn, Globals.newId(), false);
		
		
		imageCrosshair.setAbsScaleX(Globals.screenDimensions.x/6);
		
		buttonMissionNext.getLayoutParams().setMargins(0, 0, 0, Globals.screenDimensions.y/15);
		buttonMissionNext.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		imageCrosshair.addRule(RelativeLayout.ABOVE, buttonMissionNext.getId());
		
		imageCrosshairTarget = new ImageObject(R.drawable.crosshair_red, aIn, Globals.newId(), false);
		
		//imageCrosshairTarget.addRule(RelativeLayout.ALIGN_TOP, imageCrosshair.getId());
		imageCrosshairTarget.alignToTop();
		imageCrosshairTarget.alignToLeft();
		imageCrosshairTarget.setAbsScaleX(Globals.screenDimensions.x/26);
		
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
		
		
		
		missionFactsheets[0] = menuFactsLoco;
		missionFactsheets[1] = menuFactsTank;
		missionFactsheets[2] = menuFactsFieldGun;
		missionFactsheets[3] = menuFactsSylvie;
		missionFactsheets[4] = menuFactsPlanePropellers;
		missionFactsheets[5] = menuFactsCrawler;
		
		missionFactsheetImages[0] = R.drawable.factsheet_loco_unlocked;
		missionFactsheetImages[1] = R.drawable.factsheet_tank_unlocked;
		missionFactsheetImages[2] = R.drawable.factsheet_fieldgun_unlocked;
		missionFactsheetImages[3] = R.drawable.factsheet_sylvie_unlocked;
		missionFactsheetImages[4] = R.drawable.factsheet_plane_unlocked;
		missionFactsheetImages[5] = R.drawable.factsheet_crawler_unlocked;
		
		
		
		//menuFactsLoco.addRule(RelativeLayout.LEFT_OF, menuFactsTank.getId());
		
		//menuFactsPlanePropellers.addRule(RelativeLayout.LEFT_OF, menuFactsCrawler.getId());
		
		
		//menuFactsTank.addRule(RelativeLayout.RIGHT_OF, menuFactsLoco.getId());
	}
	TextObject textInvisible;
	
	
	float crosshairOffsetX = 0;
	float crosshairOffsetY = 0;
	ImageObject menuOverlay;
	
	ButtonObject menuContinue;
	@Override
	public void sceneInit(Activity aIn, boolean visible) {
		itemSetup(aIn);
        
        //addElementToView(textTitle);

		Bitmap b = Bitmap.createBitmap(Colour.ArrFromRGB(0,0,0,180), 1, 1, Bitmap.Config.ARGB_8888);
		menuOverlay = new ImageObject(b, aIn, Globals.newId(), false);
		menuOverlay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
															RelativeLayout.LayoutParams.FILL_PARENT));
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
        
        /*
		listMenuScreen.add(menuFactsLoco);
		listMenuScreen.add(menuFactsTank);
		listMenuScreen.add(menuFactsFieldGun);
		listMenuScreen.add(menuFactsSylvie);
		listMenuScreen.add(menuFactsPlanePropellers);
		listMenuScreen.add(menuFactsCrawler);*/
        
        for (int i = 0; i < 6; i++)
        	listMenuScreen.add(missionFactsheets[i]);
		listMenuScreen.add(menuContinue);

        listMainScreen.add(imageClue);
        listMainScreen.add(textStatus);
        
       // listMainScreen.add(imageTransportationRed);
        listMainScreen.add(imageTransportation);
        listMainScreen.add(imagePressMe);
        
        listMainScreen.add(healthBar);
        listMainScreen.add(buttonMenu);
        listMainScreen.add(buttonDebug);
        listMainScreen.add(buttonToggleClue);
        listMainScreen.add(textCurrentElapsedTime);
        
        
        
        
        listMissionScreen.add(imageMissionGiving);
        listMissionScreen.add(buttonMissionNext);
        listMissionScreen.add(textMissionText);
        listMissionScreen.add(imageCrosshair);
        listMissionScreen.add(imageCrosshairTarget);
        listMissionScreen.add(missionGivenStatus);
        
        
        
        listClueScreen.add(imageClue);
        listClueScreen.add(textStatus);
        listClueScreen.add(buttonMenu);
        listClueScreen.add(buttonDebug);
        listClueScreen.add(buttonToggleClue);
        listClueScreen.add(textCurrentElapsedTime);
        listClueScreen.add(textClue);
        listClueScreen.add(healthBar);
        
        for (AbstractElement a : listMainScreen)
        	addElementToView(a);

        for (AbstractElement a : listClueScreen)
        	addElementToView(a);
        
        for (AbstractElement a : listMenuScreen)
        	addElementToView(a);
        
        for (AbstractElement a : listMissionScreen)
        	addElementToView(a);
        
        listMainScreen.add(imageCrosshairTarget);
        addElementToView(imageCrosshairTarget);
        
        setClickEvents();
        //estimoteSetup(aIn);
        setScene(ScreenState.MAIN);
        
        super.sceneInit(aIn, visible);
        
        
		Arrays.fill(missionCompletionTimes, 0);
		Arrays.fill(missionCompletion, false);
		vibrator = (Vibrator)aIn.getSystemService(Context.VIBRATOR_SERVICE);
		
		screenState = ScreenState.MISSION;
        //onLoad();
		
		
		missionSetup();
		
		//imageMissionGiving.setVisibility(View.GONE);
		//buttonMissionNext.setVisibility(View.GONE);
		//textMissionText.setVisibility(View.GONE);
		
		crosshairOffsetX = imageCrosshair.getHeight()/2;
		crosshairOffsetX -= imageCrosshairTarget.getHeight()/2;
		
		crosshairOffsetY = imageCrosshair.getWidth()/2;
		crosshairOffsetY -= imageCrosshairTarget.getWidth()/2;
		
		transportOffsetX = imageTransportation.getHeight()/2;
		transportOffsetX -= imageCrosshairTarget.getHeight()/2;
		
		transportOffsetY = imageTransportation.getWidth()/2;
		transportOffsetY -= imageCrosshairTarget.getWidth()/2;
		//crosshairOffset += imageCrosshair.getElement().getY();
		//imageCrosshairTarget.getLayoutParams().setMargins(0, (int)crosshairOffset, 0, 0);
		handler.postDelayed(runnable, 100);
		//setScene(ScreenState.MISSION);
        //listMainScreen.remove(imageMissionGiving);
        //listMainScreen.remove(buttonMissionNext);
        //listMainScreen.remove(textMissionText);
		
		
		
	}
	
	float transportOffsetX;
	float transportOffsetY;
}
