package com.example.classes.Objects;

import android.app.Activity;
import android.widget.Button;

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
