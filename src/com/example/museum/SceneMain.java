package com.example.museum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.example.classes.AccelerometerManager;
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
		MISSION;
		
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
	private ButtonObject buttonDebug;
	
	private ButtonObject buttonToggleClue;
	
	private ScreenState screenState = ScreenState.MAIN;
	
	protected List<AbstractElement> listMainScreen;
	protected List<AbstractElement> listClueScreen;
	protected List<AbstractElement> listMissionScreen;
	
	private TextObject textCurrentElapsedTime;
	private long startTime;
	
	private Vibrator vibrator;
	
	
	
	private String[] missionTitles = {	"Loco", 
										"Tank", 
										"FieldGun", 
										"Sylvie", 
										"PlanePropellers", 
										"Crawler" };
	
	private String[] missionClues = { "Loco Mission Descript",
												"Tank Mission Descript",
												"Field Gun Mission Descript",
												"Sylvie Mission Descript",
												"Plane Propellers Mission Descript",
												"Crawler Mission Descript" };
	
	private String[] missionIntroductions = { "Loco Mission Beginning Dialogue",
												"Tank Mission Beginning Dialogue",
												"Field Gun Mission Beginning Dialogue",
												"Sylvie Mission Beginning Dialogue",
												"Plane Propellers Mission Beginning Dialogue",
												"Crawler Mission Beginning Dialogue" };
	
	private int[] missionImages = { R.drawable.clue_loco,
									R.drawable.clue_tank,
									R.drawable.clue_fieldgun,
									R.drawable.clue_sylvie,
									R.drawable.clue_propellers,
									R.drawable.clue_crawler};
	
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
	
	private int health = 100;
	
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
	 	   @Override
	 	   public void run() {
	           if (playing)
	        	   gameplay();
	 	       handler.postDelayed(this, 100); 
	 	   }
	};
	
	private void gameplay(){
		
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
		
		// Health effects
		if (Math.abs(AccelerometerManager.getX()) > 2 ||
				Math.abs(AccelerometerManager.getY()) > 2.3){
			health -= Math.max(Math.abs(AccelerometerManager.getX()), Math.abs(AccelerometerManager.getY()));
		}
		else if (health < 100)
			health++;
		
		if (health < 0)
			health = 0;
		
		healthBar.setValue(health);
		
        
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
	
	private void missionSetup(){
		playing = false;

		//((AnimationDrawable)Globals.rLayout.getBackground()).
//		setScene(ScreenState.GIVING_MISSION);
		
		//imageMissionGiving.getElement().setAlpha(1f);
		//imageMissionGiving.setVisibility(View.VISIBLE);
		//buttonMissionNext.setVisibility(View.VISIBLE);
		//textMissionText.setVisibility(View.VISIBLE);
		
		if (currentMission >= 0){
			if (missionCompletionTimes[missionCount] == 0){
				missionCompletionTimes[missionCount] = System.currentTimeMillis() - startTime;
			}
			
			missionCompletion[currentMission] = true;
		}
		
		boolean allComplete = true;
		for (boolean x : missionCompletion)
			if (!x){
				allComplete = false;
				break;
			}
		
		if (allComplete){
			buttonMissionNext.setVisibility(View.GONE);
		    listMissionScreen.remove(buttonMissionNext);
			currentMission = missionCompletion.length+1;
			textMissionText.setText("All missions complete!");
			
			return;
		}
		
		do{
			currentMission = random.nextInt(6);
		} while (missionCompletion[currentMission] == true);
		
		missionCount++;
		startTime = System.currentTimeMillis();
		textMissionText.setText(missionIntroductions[currentMission]);
	}

	
	
	
	
	
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
				missionSetup();
				Globals.rLayout.getBackground().setAlpha(120);
		}
		transitionOut(l);
	}
	
	
	public void setClickEvents(){
		
		buttonMissionNext.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Globals.rLayout.getBackground().setAlpha(255);
				setScene(ScreenState.MAIN);
				//textClue.setText(missionClues[currentMission]);
				textStatus.setText("New mission given. Look at your new clue!");
				imageClue.setImage(missionImages[currentMission]);
				imageClue.setAbsScaleY((int)(Globals.screenDimensions.y/2.5f));
				playing = true;
			}
		});
		
		buttonToggleClue.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (screenState == ScreenState.MAIN)
					setScene(ScreenState.CLUE);	
				else
					setScene(ScreenState.MAIN);
			}
		});
		
		buttonGallery.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.SetScreenState(GameActivity.ScreenState.GALLERY);	
			}
		});
		
		buttonDebug.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {;
				activity.SetScreenState(GameActivity.ScreenState.DEBUG);
			}
		});
		
		imageTransportation.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (!missionCompletion[currentMission]){
					MyBeacon d = EstimoteManager.contains(missionTitles[currentMission]);
					if (d != null && d.getDistance() < 2.3f){
						setScene(ScreenState.MISSION);
					}
					else
						textStatus.setText("Hm, not in the right place. Keep looking!");
				}
				//else
				//	missionSetup();
			}
		});		
	}
	
	public void onLoad(){
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
	}
	
	private void itemSetup(Activity aIn){
		
		listMainScreen = new ArrayList<AbstractElement>();
		listClueScreen = new ArrayList<AbstractElement>();
		listMissionScreen = new ArrayList<AbstractElement>();
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
        
        
        
       
        
        
        //textClue.getLayoutParams().setMarginStart(Globals.screenDimensions.x/20);
        
        buttonGallery = new ButtonObject("Gallery", aIn, Globals.newId());
        buttonGallery.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        buttonGallery.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        buttonGallery.getLayoutParams().setMargins((Globals.screenDimensions.x/12), 0, 0, Globals.screenDimensions.y/20);
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
        
        imageTransportation = new ImageObject(R.drawable.ammo_green, aIn, Globals.newId(), true);
        //imageTransportation.addRule(RelativeLayout.BELOW, textStatus.getId());
        imageTransportation.setBackgroundColour(Color.TRANSPARENT);
        imageTransportation.addRule(RelativeLayout.BELOW, buttonToggleClue.getId());
        imageTransportation.getLayoutParams().setMargins(0, Globals.screenDimensions.y/20, 0, 0);
        imageTransportation.setAbsScaleY(Globals.screenDimensions.y/4);
        //imageTransportation.getElement().setPaddingRelative(Globals.screenDimensions.x/20, 0, 0, Globals.screenDimensions.y/30);
        
        healthBar = new ProgressBarObject(aIn, Globals.newId(), true);
        healthBar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
       // healthBar.addRule(RelativeLayout.ALIGN_START, imageTransportation.getId());
        healthBar.getLayoutParams().setMargins(0, 0, 0, Globals.screenDimensions.y/120);
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
        
        textClue = new TextObject("This is the clue text it is clue text that contains a clue of varying length.", aIn, Globals.newId());
        textClue.addRule(RelativeLayout.ALIGN_TOP, buttonToggleClue.getId());
        textClue.getElement().setTextSize(Globals.getTextSize()*1.4f);
        textClue.setWidth(Globals.screenDimensions.x/2.2f);
        textClue.getLayoutParams().setMargins(0, Globals.screenDimensions.y/10, 0, 0);
        textClue.getElement().setGravity(Gravity.CENTER);
        
        
        
        imageMissionGiving = new ImageObject(R.drawable.background, aIn, Globals.newId(), false);
		buttonMissionNext = new ButtonObject(">", aIn, Globals.newId());
		
		textMissionText = new TextObject("Teashdas", aIn, Globals.newId());
		textMissionText.getElement().setTextSize(Globals.getTextSize()*1.8f);
		textMissionText.getLayoutParams().setMargins(Globals.screenDimensions.x/10, 0, Globals.screenDimensions.x/10, 0);
		
		buttonMissionNext.addRule(RelativeLayout.BELOW, textMissionText.getId());
		
		imageMissionGiving.setScale(Globals.screenDimensions.x - (Globals.screenDimensions.x/10), 
        		Globals.screenDimensions.y - (Globals.screenDimensions.y/10));
	}
	
	@Override
	public void sceneInit(Activity aIn, boolean visible) {
		itemSetup(aIn);
        
        //addElementToView(textTitle);
		
		
		
        addElementToView(imageClue);
        addElementToView(textStatus);
        addElementToView(imageTransportation);
        addElementToView(healthBar);
        addElementToView(textClue);
        addElementToView(buttonGallery);
        addElementToView(buttonDebug);
        addElementToView(buttonToggleClue);
        addElementToView(textCurrentElapsedTime);
        
        


        addElementToView(imageMissionGiving);
		addElementToView(buttonMissionNext);
		addElementToView(textMissionText);
        
        listMainScreen.add(imageClue);
        listMainScreen.add(textStatus);
        listMainScreen.add(imageTransportation);
        listMainScreen.add(healthBar);
        listMainScreen.add(buttonGallery);
        listMainScreen.add(buttonDebug);
        listMainScreen.add(buttonToggleClue);
        listMainScreen.add(textCurrentElapsedTime);
        
        listMissionScreen.add(imageMissionGiving);
        listMissionScreen.add(buttonMissionNext);
        listMissionScreen.add(textMissionText);
        
        listClueScreen.add(imageClue);
        listClueScreen.add(textStatus);
        listClueScreen.add(buttonGallery);
        listClueScreen.add(buttonDebug);
        listClueScreen.add(buttonToggleClue);
        listClueScreen.add(textCurrentElapsedTime);
        listClueScreen.add(textClue);
        listClueScreen.add(healthBar);
        
        setClickEvents();
        //estimoteSetup(aIn);
        setScene(ScreenState.MAIN);
        
        super.sceneInit(aIn, visible);
        
        
        onLoad();
		Arrays.fill(missionCompletionTimes, Integer.MAX_VALUE);
		Arrays.fill(missionCompletion, false);
		vibrator = (Vibrator)aIn.getSystemService(Context.VIBRATOR_SERVICE);
		
		
		
		//imageMissionGiving.setVisibility(View.GONE);
		//buttonMissionNext.setVisibility(View.GONE);
		//textMissionText.setVisibility(View.GONE);
		
		handler.postDelayed(runnable, 100);
		setScene(ScreenState.MISSION);
        
        //listMainScreen.remove(imageMissionGiving);
        //listMainScreen.remove(buttonMissionNext);
        //listMainScreen.remove(textMissionText);
	}
}
