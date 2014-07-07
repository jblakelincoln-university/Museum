package com.example.museum;

import com.example.classes.*;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	
	RelativeLayout linLayout;
	TextObject textTitle;
	TextObject textStatus;
	TextObject textClue;
	ImageObject imageClue;
	ImageObject imageTransportation;
	
	ProgressBarObject healthBar;
	
	Point screenDimensions = new Point(0, 0);
	 
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindowManager().getDefaultDisplay().getSize(screenDimensions);
        linLayout = new RelativeLayout(this);
        
        RelativeLayout.LayoutParams linLayoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT, 
        		RelativeLayout.LayoutParams.MATCH_PARENT);
       
        setContentView(linLayout, linLayoutParams);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        textTitle = new TextObject("Transport munitions!", this, linLayout, 100);
        textTitle.alignToTop();
        textTitle.getElement().setPaddingRelative(0, screenDimensions.y/100, 0, screenDimensions.y/80);
        //textTitle.getElement().setTextSize(50f);
        textTitle.getElement().setTextSize(TypedValue.COMPLEX_UNIT_SP, 50f);

        imageClue = new ImageObject(R.drawable.clue1, this, linLayout, 101);
        //imageClue.getElement().getLayoutParams().width = screenDimensions.y/4;
        imageClue.addRule(RelativeLayout.BELOW, textTitle.getId());
        imageClue.getElement().setPaddingRelative(0, 0, 0, screenDimensions.y/100);
        imageClue.setAbsScaleY((int)(screenDimensions.y/2.5f));
        
        
        textStatus = new TextObject("Mission status text box with long words everywhere", this, linLayout, 102);
        textStatus.addRule(RelativeLayout.BELOW, imageClue.getId());
        textStatus.getElement().setWidth(screenDimensions.x-(screenDimensions.x/10));
        textStatus.getElement().setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
        textStatus.getElement().setGravity(Gravity.CENTER);
        
        imageTransportation = new ImageObject(R.drawable.ammo_green, this, linLayout, 103);
        imageTransportation.addRule(RelativeLayout.BELOW, textStatus.getId());
        imageTransportation.addRule(RelativeLayout.ALIGN_END, textStatus.getId());
        imageTransportation.getElement().setPaddingRelative(0,screenDimensions.y/30, screenDimensions.x/20, 0);
        imageTransportation.setAbsScaleY(screenDimensions.y/5);
       
        healthBar = new ProgressBarObject(this, linLayout, 104);
        healthBar.addRule(RelativeLayout.BELOW, imageTransportation.getId());
        healthBar.addRule(RelativeLayout.ALIGN_START, imageTransportation.getId());
        healthBar.getLayoutParams().setMargins(0, screenDimensions.y/80, 0, 0);
        healthBar.setWidth(imageTransportation.getWidth());
        healthBar.setValue(50);
        
        textClue = new TextObject("This is the clue text it is clue text that contains a clue of varying length.", this, linLayout, 104);
        textClue.addRule(RelativeLayout.ALIGN_PARENT_LEFT, textClue.getId());
        textClue.addRule(RelativeLayout.ALIGN_BOTTOM, imageTransportation.getId());
        textClue.getElement().setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
        textClue.setWidth(screenDimensions.x/2.2f);
        textClue.getLayoutParams().setMarginStart(screenDimensions.x/20);
    }
}
