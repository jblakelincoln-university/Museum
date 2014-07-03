package com.example.museum;

import com.example.classes.*;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends Activity {

	private TextView myTextView;
	private TextView mySecondTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        myTextView = new TextView(this);
        myTextView.setText("Help");
        
        RelativeLayout linLayout = new RelativeLayout(this);
        

        RelativeLayout.LayoutParams linLayoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT, 
        		RelativeLayout.LayoutParams.MATCH_PARENT);
        
        /*
        
        RelativeLayout.LayoutParams myTextViewLP = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.WRAP_CONTENT, 
        		RelativeLayout.LayoutParams.WRAP_CONTENT);

        myTextViewLP.addRule(RelativeLayout.CENTER_HORIZONTAL);

        
        mySecondTextView = new TextView(this);
        mySecondTextView.setText("Me");
        
        
        
        RelativeLayout.LayoutParams mySecondTextViewLP = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.WRAP_CONTENT, 
        		RelativeLayout.LayoutParams.WRAP_CONTENT);

        myTextView.setId(12);
        
        mySecondTextViewLP.addRule(RelativeLayout.RIGHT_OF, myTextView.getId());

        linLayout.addView(myTextView, myTextViewLP);
        linLayout.addView(mySecondTextView, mySecondTextViewLP);
        */
        
        TextObject myT1 = new TextObject("T1", this, 1);
        TextObject myT2 = new TextObject("T2", this, 1);
        
        linLayout.addView(myT1.getElement(), myT1.getView());
        linLayout.addView(myT2.getElement(), myT2.getView());
        
        setContentView(linLayout, linLayoutParams);
       
    }
}
