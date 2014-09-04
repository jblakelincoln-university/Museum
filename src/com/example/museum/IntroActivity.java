package com.example.museum;

import com.example.classes.AccelerometerManager;
import com.example.classes.Colour;
import com.example.classes.EstimoteManager;
import com.example.classes.Globals;
import com.example.classes.Globals.Fonts;
import com.example.classes.LayoutManager;
import com.example.classes.Objects.ButtonObject;
import com.example.classes.Objects.ImageObject;
import com.example.classes.Objects.ProgressBarObject;
import com.example.classes.Objects.TextObject;
import com.example.classes.Objects.VScrollViewObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IntroActivity extends Activity {
	VScrollViewObject scrollView;
	ButtonObject b;
	ProgressBar pB;

	private LayoutManager layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Globals.Init(this);
		AccelerometerManager.Init(this);
		EstimoteManager.Init(this);
		
		layout = new LayoutManager(this);
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//this.getWindowManager().getDefaultDisplay().getSize(screenDimensions);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        layout.get().setBackgroundResource(R.drawable.background_newtoox);
        
        pB = new ProgressBar(this);
		layout.get().addView(pB, layout.getParams());
		pB.setVisibility(View.GONE);
        TextObject title = new TextObject("Museum of Lincolnshire Life\nAgricultural and Industrial Gallery Game", this, Globals.newId());
        
        float textSize = title.getElement().getTextSize();
        title.getElement().setTextSize(textSize*1.5f);
        title.getElement().setGravity(Gravity.CENTER);
        title.alignToTop();
        title.addView(layout.get());
        title.getElement().setTypeface(Globals.Fonts.ChunkFive());
        
        scrollView = new VScrollViewObject(this, Globals.newId());
        scrollView.addRule(RelativeLayout.BELOW, title.getId());
        
        scrollView.addView(layout.get());
        
        scrollView.getLayout().setGravity(Gravity.CENTER);
        
        scrollView.getLayoutParams().setMargins(Globals.screenDimensions.x/10, 0, Globals.screenDimensions.x/10, 0);
        
        TextObject p1 = new TextObject("When you begin the game, you will be presented with a mission related to an object in the museum.", this, Globals.newId());
        ImageObject i1 = new ImageObject(R.drawable.info_one, this, Globals.newId(), false);
        TextObject p2 = new TextObject("During the game you will be given an image and text clue to locate the object.", this, Globals.newId());
        ImageObject i2 = new ImageObject(R.drawable.info_two, this, Globals.newId(), false);
        TextObject p3 = new TextObject("When you think you have found the object, press the image (with the arrow pointing at it) to complete the mission!\nMake sure your Bluetooth is turned ON!", this, Globals.newId());
        
        b = new ButtonObject("Begin!", this, Globals.newId());
        
        p1.getElement().setTextSize(textSize*1.1f);
        p2.getElement().setTextSize(textSize*1.1f);
        p3.getElement().setTextSize(textSize*1.1f);
        p1.getElement().setTypeface(Globals.Fonts.ExoRegular());
        p2.getElement().setTypeface(Globals.Fonts.ExoRegular());
        p3.getElement().setTypeface(Globals.Fonts.ExoRegular());
        
        p1.addView(scrollView.getLayout());
        
        i1.addView(scrollView.getLayout());
        i1.addRule(RelativeLayout.BELOW, p1.getId());
        i1.getLayoutParams().setMargins(((Globals.screenDimensions.x/2)-(i1.getWidth()/2)), 0, 0, 0);
        p2.addView(scrollView.getLayout());
        p2.addRule(RelativeLayout.BELOW, i1.getId());
        i2.addView(scrollView.getLayout());
        i2.addRule(RelativeLayout.BELOW, p2.getId());
        p3.addView(scrollView.getLayout());
        p3.addRule(RelativeLayout.BELOW, i2.getId());
        b.addView(scrollView.getLayout());
        b.addRule(RelativeLayout.BELOW, p3.getId());
        //setContentView(layout.get(), layout.getParams());  
        
        layout.setContentView();
        b.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scrollView.setVisibility(View.GONE);
				b.setVisibility(View.GONE);
				pB.setVisibility(View.VISIBLE);
				Intent myIntent = new Intent(IntroActivity.this, GameActivity.class);
				//myIntent.putExtra("key", value); //Optional parameters
				IntroActivity.this.startActivity(myIntent);
			}
		});	
		
		//setContentView(R.layout.activity_intro);
	}
}
