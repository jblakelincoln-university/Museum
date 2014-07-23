package com.example.classes;

import android.app.Activity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.classes.Objects.ButtonObject;
import com.example.classes.Objects.ImageObject;
import com.example.classes.Objects.ProgressBarObject;
import com.example.classes.Objects.TextObject;
import com.example.museum.R;

public class SceneMain extends Scene{

	private TextObject textTitle;
	private TextObject textStatus;
	private TextObject textClue;
	private ImageObject imageClue;
	private ImageObject imageTransportation;
	private ButtonObject buttonGallery;
	private ProgressBarObject healthBar;
	
	
	public SceneMain(int idIn, Activity a, boolean visible) {
		super(idIn, a, visible);
	}
	
	@Override
	protected void sceneInit(Activity aIn, boolean visible) {
		textTitle = new TextObject("Transport munitions!", aIn, Globals.newId());
        textTitle.alignToTop();
        textTitle.getElement().setPaddingRelative(0, Globals.screenDimensions.y/100, 0, Globals.screenDimensions.y/80);
        //textTitle.getElement().setTextSize(50f);
        textTitle.getElement().setTextSize(TypedValue.COMPLEX_UNIT_SP, 50f);
        
        imageClue = new ImageObject(R.drawable.clue1, aIn, Globals.newId(), false);
        //imageClue.getElement().getLayoutParams().width = Globals.screenDimensions.y/4;
        imageClue.addRule(RelativeLayout.BELOW, textTitle.getId());
        imageClue.getElement().setPaddingRelative(0, 0, 0, Globals.screenDimensions.y/100);
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
        imageTransportation.setAbsScaleY(Globals.screenDimensions.y/5);
       
        healthBar = new ProgressBarObject(aIn, Globals.newId());
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
        addElementToView(textTitle);
        addElementToView(imageClue);
        addElementToView(textStatus);
        addElementToView(imageTransportation);
        addElementToView(healthBar);
        addElementToView(textClue);
        addElementToView(buttonGallery);
        setGalleryButtonClickEvent();	
        
        super.sceneInit(aIn, visible);
	}
	
	public void setGalleryButtonClickEvent(){
		buttonGallery.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Globals.SetScreenState(Globals.ScreenState.GALLERY);	
			}
		});
	}

	@Override
	public void onBackPressed() {
		System.exit(0);
	}
}
