package com.example.classes;

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

import com.example.classes.Objects.*;
import com.example.museum.R;

import com.estimote.sdk.*;

import com.example.museum.*;

public class SceneMain extends Scene{

	//Estimote
public static final String TAG = "MainActivity";
	
	private static final String ESTIMOTE_PROXIMITY_UUID = 
			"B9407F30-F5F8-466E-AFF9-25556B57FE6D";
	private static final Region ALL_ESTIMOTE_BEACONS = 
			new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);
	
	private BeaconManager beaconManager;
	private Handler updateHandler;
	
	LinkedHashMap<String,MyBeacon> myBeaconsList = new LinkedHashMap<String, MyBeacon>();
	
	private Map<String, String> beaconNameDictionary;
	
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
	
	public SceneMain(int idIn, Activity a, boolean visible) {
		super(idIn, a, visible);
		
		
	}
	
	protected void estimoteSetup(Activity aIn){
		
		beaconManager = new BeaconManager(aIn);
		
		beaconNameDictionary = new HashMap<String,String>();
		beaconNameDictionary.put("FE:E7:C6:3B:BC:DE", "Mint(1)");
		beaconNameDictionary.put("E5:B9:E4:F5:39:98", "Icy(2)");
		beaconNameDictionary.put("EF:A7:AE:AE:4A:3B", "Blueberry(3)");
		beaconNameDictionary.put("C7:5C:63:DF:2C:E4", "Mint(4)");
		beaconNameDictionary.put("CD:2F:A5:DE:92:13", "Icy(5)");
		beaconNameDictionary.put("F8:F6:53:10:8B:B4", "Blueberry(6)");
		
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
		    @Override 
		    public void onServiceReady() {
		      try {
		        beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
		      } catch (RemoteException e) {
		        Log.e(TAG, "Cannot start ranging", e);
		      }
		    }
		  });
		
		beaconManager.setRangingListener(new BeaconManager.RangingListener() {
		     @Override 
		     public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
		    	 for (int i = 0; i < beacons.size(); i++)
		    	 {
		    		 if (!myBeaconsList.containsKey(beacons.get(i).getMacAddress()))
   				 {
		    			 myBeaconsList.put(beacons.get(i).getMacAddress().toString(), new MyBeacon(beacons.get(i)));
		    			 if (beaconNameDictionary.containsKey(beacons.get(i).getMacAddress()))
		    			 {
		    				 myBeaconsList.get(beacons.get(i).getMacAddress()).setName(beaconNameDictionary.get(beacons.get(i).getMacAddress()));
		    				 //myBeaconsList.get(beacons.get(i)).setInitialDistance(); 
		    			 }
   				 }
		    		 else
		    			 myBeaconsList.get(beacons.get(i).getMacAddress()).setBeacon(beacons.get(i));
		    	 }
		    	 if (beacons.size() == 0)
		    		 p++;
		    	 else
		    		 p = beacons.size();
		     }
		   });
		
		
		//updateHandler = new Handler();
		//updateHandler.postDelayed(runnable,  0);
	}
	
	int p = 0;
	
	private void itemSetup(Activity aIn){
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
        textClue.addRule(RelativeLayout.ALIGN_PARENT_LEFT, textClue.getId());
        textClue.addRule(RelativeLayout.ALIGN_BOTTOM, imageTransportation.getId());
        textClue.getElement().setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
        textClue.setWidth(Globals.screenDimensions.x/2.2f);
        textClue.getLayoutParams().setMarginStart(Globals.screenDimensions.x/20);
        
        buttonGallery = new ButtonObject("Gallery", aIn, Globals.newId());
        buttonGallery.addRule(RelativeLayout.BELOW, textClue.getId());
        buttonGallery.addRule(RelativeLayout.ALIGN_START, textClue.getId());
        
        buttonFindClue = new ImageObject(R.drawable.find_button, aIn, Globals.newId(), true);
        buttonFindClue.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        buttonFindClue.setAbsScaleY(Globals.screenDimensions.y/5);
        buttonFindClue.getLayoutParams().setMargins(0, 0, 0, Globals.screenDimensions.y/10);
        buttonFindClue.setBackgroundColour(android.R.color.transparent);
        
        barFinding = new ProgressBarObject(aIn, Globals.newId(), false);
        barFinding.setValue(50);
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
        addElementToView(barFinding);
        setGalleryButtonClickEvent();	
        setFindButtonClickEvent();
        
        estimoteSetup(aIn);
        
        super.sceneInit(aIn, visible);
	}
	
	public void update(){
		//textClue.setText("aaaa" + p++);
		
		String s = "";
		for (Entry<String, MyBeacon> entry : myBeaconsList.entrySet()) {
			entry.getValue().updateDistance();
			String name = entry.getValue().getName();//entry.getValue().getName();
			String distance = String.format("%.2f", entry.getValue().getDistance());
			s += name + " - " + distance + "\n";
		}
		
		textClue.setText(s);
	}
	
	public void setGalleryButtonClickEvent(){
		buttonGallery.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Globals.SetScreenState(Globals.ScreenState.GALLERY);	
			}
		});
	}
	
	public void setFindButtonClickEvent(){
		buttonFindClue.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for (Entry<String, MyBeacon> entry : myBeaconsList.entrySet()) {
					if (entry.getValue().getName() == "Blueberry(6)")
					{
						double lowest = 10000;
						for (int i = 0; i < 500; i++)
						{
							if (entry.getValue().getDistance() < lowest)
								lowest = entry.getValue().getDistance();
						}
						if (lowest < 0.8)
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
		System.exit(0);
	}
}
