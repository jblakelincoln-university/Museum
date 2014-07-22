package com.example.museum;

import android.app.Activity;
import android.os.Bundle;

import com.example.classes.Globals;

public class MainActivity extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		Globals.Init(this);

        setContentView(Globals.rLayout, Globals.rLayoutParams);
    }
	
	@Override
	public void onBackPressed(){
		Globals.onBackPressed();
	}
}
