package com.example.classes;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import com.example.museum.*;


import android.R;
import android.app.Activity;
import android.os.IInterface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.museum.MainActivity;


public class ButtonObject extends AbstractElement<Button>{
	
	public ButtonObject(String s, Activity m, int idIn)
	{
		obj = new Button(m);
		obj.setId(idIn);
		obj.setText(s);
		//layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
	}
	
	public void setText(String s){
		obj.setText(s);
	}
}
