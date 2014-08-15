package com.example.museum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.classes.AccelerometerManager;
import com.example.classes.Globals;
import com.example.classes.Scene;
import com.example.classes.Objects.*;
import com.example.museum.R;

@SuppressWarnings("rawtypes")
public class SceneMain extends Scene{
	
	public static enum ScreenState{
		NONE,
		MAIN,
		CLUE;
		
		public static int toInt(ScreenState s){
			switch(s){
			case NONE:
				return 0;
			case MAIN:
				return 1;
			case CLUE:
				return 2;
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
	private ButtonObject buttonDebug;
	
	private ButtonObject buttonToggleClue;
	
	private ScreenState screenState = ScreenState.MAIN;
	
	protected List<AbstractElement> listMainScreen;
	protected List<AbstractElement> listClueScreen;
	
	private TextObject textCurrentElapsedTime;
	private long startTime;
	
	private Vibrator vibrator;
	
	private Handler handler = new Handler();
	
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
	
	private int[] missionCompletionTimes = new int[6];
	
	private int currentMission = -1;
	
	private boolean[] missionCompletion = new boolean[6];
	
	private int currentVibration = 5;
	
	private long vibrationPatterns[][] =  { {0, 1000, 0},  // 0
											{ 200, 150},  // 20
											{ 500, 150},  // 50
											{ 800, 200},  // 80
											{ 2000, 200} // 100
											};
	
	private boolean playing = true;
	
	private Random random = new Random();
	
	private int health = 100;
	
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
		
		Arrays.fill(missionCompletionTimes, Integer.MAX_VALUE);
		Arrays.fill(missionCompletion, false);
		vibrator = (Vibrator)a.getSystemService(Context.VIBRATOR_SERVICE);
		
		missionSetup();
		handler.postDelayed(runnable, 100);
	}
	
	private void missionSetup(){
		if (currentMission >= 0)
			missionCompletion[currentMission] = true;
		
		boolean allComplete = true;
		for (boolean x : missionCompletion)
			if (!x){
				allComplete = false;
				break;
			}
		
		if (allComplete){
			textStatus.setText("All missions complete!");
			return;
		}
		
		do{
			currentMission = random.nextInt(6);
		} while (missionCompletion[currentMission] == true);
		
		textClue.setText(missionClues[currentMission]);
		
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
        textStatus.getElement().setTextSize(Globals.getTextSize());
        textStatus.getElement().setGravity(Gravity.CENTER);
        
        imageTransportation = new ImageObject(R.drawable.ammo_green, aIn, Globals.newId(), false);
        //imageTransportation.addRule(RelativeLayout.BELOW, textStatus.getId());
        imageTransportation.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        imageTransportation.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        imageTransportation.getElement().setPaddingRelative(Globals.screenDimensions.x/20, 0, 0, Globals.screenDimensions.y/30);
        
       
        healthBar = new ProgressBarObject(aIn, Globals.newId(), true);
        healthBar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
       // healthBar.addRule(RelativeLayout.ALIGN_START, imageTransportation.getId());
        healthBar.getLayoutParams().setMargins(0, 0, 0, Globals.screenDimensions.y/120);
        healthBar.setWidth(imageTransportation.getWidth());
        healthBar.setValue(50);
        healthBar.setMode(Mode.LIGHTEN);
        
        //textClue.getLayoutParams().setMarginStart(Globals.screenDimensions.x/20);
        
        buttonGallery = new ButtonObject("Gallery", aIn, Globals.newId());
        buttonGallery.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        buttonGallery.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        buttonGallery.getLayoutParams().setMargins((Globals.screenDimensions.x/12), 0, 0, Globals.screenDimensions.y/20);
       // buttonGallery.addRule(RelativeLayout.ALIGN_START, textClue.getId());
        
        buttonFindClue = new ImageObject(R.drawable.find_button, aIn, Globals.newId(), true);
        buttonFindClue.setAbsScaleX((int)(Globals.screenDimensions.x/2.2));
        buttonFindClue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        buttonFindClue.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        buttonFindClue.getElement().setPaddingRelative(0, 0, Globals.screenDimensions.x/20, Globals.screenDimensions.y/30);
        buttonFindClue.setBackgroundColour(android.R.color.transparent);
        imageTransportation.setAbsScaleX((int)(buttonFindClue.getWidth()/1.1f));
        
        buttonDebug = new ButtonObject("Debug", aIn, Globals.newId());
        buttonDebug.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        buttonDebug.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        buttonDebug.getLayoutParams().setMargins(0, 0, (Globals.screenDimensions.x/12), Globals.screenDimensions.y/20);
        
        buttonToggleClue = new ButtonObject("Toggle Clue", aIn, Globals.newId());
        buttonToggleClue.addRule(RelativeLayout.ALIGN_TOP, textStatus.getId());
        buttonToggleClue.getLayoutParams().setMargins(0, Globals.screenDimensions.y/14, 0, 0);
        buttonToggleClue.getElement().setTextSize(Globals.getTextSize());
        buttonToggleClue.setWidth(Globals.screenDimensions.x/4);
        
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
	}
	
	@Override
	protected void sceneInit(Activity aIn, boolean visible) {
		itemSetup(aIn);
        
        //addElementToView(textTitle);
        addElementToView(imageClue);
        addElementToView(textStatus);
        addElementToView(imageTransportation);
        addElementToView(healthBar);
        addElementToView(textClue);
        addElementToView(buttonGallery);
        addElementToView(buttonFindClue);
        addElementToView(buttonDebug);
        addElementToView(buttonToggleClue);
        addElementToView(textCurrentElapsedTime);
        
        listMainScreen.add(imageClue);
        listMainScreen.add(textStatus);
        listMainScreen.add(imageTransportation);
        listMainScreen.add(healthBar);
        listMainScreen.add(buttonGallery);
        listMainScreen.add(buttonFindClue);
        listMainScreen.add(buttonDebug);
        listMainScreen.add(buttonToggleClue);
        listMainScreen.add(textCurrentElapsedTime);
        
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
        
        
	}
	
	private void setScene(ScreenState sIn){
		screenState = sIn;
		//setVisibility(View.GONE);
		List<AbstractElement> l = listMainScreen;

		if (sIn == ScreenState.MAIN){
			//l = listMainScreen;
		}
		else if (sIn == ScreenState.CLUE){
			l = listClueScreen;
		}
		transitionOut(l);
	}
	
	
	public void setClickEvents(){
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
		
		buttonFindClue.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (screenState == ScreenState.MAIN)
					setScene(ScreenState.CLUE);
				else
					setScene(ScreenState.MAIN);
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
}
