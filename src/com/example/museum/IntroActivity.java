package com.example.museum;

import com.example.museum.SceneMain.ScreenState;
import com.lincoln.museum.R;
import com.scenelibrary.classes.AccelerometerManager;
import com.scenelibrary.classes.EstimoteManager;
import com.scenelibrary.classes.Globals;
import com.scenelibrary.classes.LayoutManager;
import com.scenelibrary.classes.MyBeacon;
import com.scenelibrary.classes.SceneActivity;
import com.scenelibrary.classes.Objects.ButtonObject;
import com.scenelibrary.classes.Objects.ImageObject;
import com.scenelibrary.classes.Objects.TextObject;
import com.scenelibrary.classes.Objects.VScrollViewObject;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class IntroActivity extends SceneActivity {
	VScrollViewObject scrollView;
	ButtonObject b;
	ProgressBar pB;

	TextObject textLoading;
	Context context;
	Toast t;
	private LayoutManager layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		Globals.Init(this);
		AccelerometerManager.Init(this);
		EstimoteManager.Init(this);
		t = Toast.makeText(context, "", Toast.LENGTH_LONG);
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
        title.getLayoutParams().setMargins(Globals.screenDimensions.x/30, Globals.screenDimensions.y/20, Globals.screenDimensions.x/30, 0);
        
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
        
        scrollView.getLayoutParams().bottomMargin += Globals.screenDimensions.y/20;
        
        b.getLayoutParams().setMargins(0, 0, 0, Globals.screenDimensions.y/20);
        //setContentView(layout.get(), layout.getParams());  
        
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            b.getElement().setEnabled(false);
            b.setText("Device not supported. Update your phone to 4.3 or higher!");
        }
        
        textLoading = new TextObject("Loading\n\t...", this, Globals.newId());
        textLoading.addView(layout.get());
        textLoading.getElement().setTextSize(Globals.getTextSize()*2.4f);
        textLoading.setVisibility(View.GONE);
        
        layout.setContentView();
        
        b.getElement().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
				
				if (bAdapter == null){
					t = Toast.makeText(context, "Unfortunately your device does not support Bluetooth and cannot play this game.", Toast.LENGTH_LONG);
					t.show();
				}
				else if (!bAdapter.isEnabled()){
					t = Toast.makeText(context, "Your Bluetooth isn't enabled - turn it on to play!", Toast.LENGTH_LONG);
					t.show();
				}
				else{
					t.cancel();
					scrollView.setVisibility(View.GONE);
					b.setVisibility(View.GONE);
					//pB.setVisibility(View.VISIBLE);
					textLoading.setVisibility(View.VISIBLE);
					Intent myIntent = new Intent(IntroActivity.this, GameActivity.class);
					//myIntent.putExtra("key", value); //Optional parameters
					IntroActivity.this.startActivity(myIntent);
				}
			}
		});	
		
		//setContentView(R.layout.activity_intro);
	}
}
