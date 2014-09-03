package com.example.museum;

import com.example.classes.Colour;
import com.example.classes.Globals;
import com.example.classes.Globals.Fonts;
import com.example.classes.Objects.TextObject;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IntroActivity extends Activity {

	
	public RelativeLayout rLayout;
	public RelativeLayout.LayoutParams rLayoutParams;
	public R.drawable rDrawable;
	public Point screenDimensions = new Point(0,0);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.getWindowManager().getDefaultDisplay().getSize(screenDimensions);
		
		rLayout = new RelativeLayout(this);        
        
        
        rLayoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT, 
        		RelativeLayout.LayoutParams.MATCH_PARENT);
        
        TextObject title = new TextObject("Museum of Lincolnshire Life\nAgricultural and Industrial Gallery Game", this, Globals.newId());
        title.setTextColour(Colour.FromRGB(255, 0, 0));
        title.getElement().setGravity(Gravity.CENTER);
        title.alignToTop();
        title.addView(rLayout);
        
        TextObject p1 = new TextObject("When you begin the game, you will be presented with a mission related to an object in the museum.", this, Globals.newId());
        
        
        setContentView(rLayout, rLayoutParams);  
        
		
		//setContentView(R.layout.activity_intro);
	}
}
