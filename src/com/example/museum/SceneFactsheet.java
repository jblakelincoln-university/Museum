package com.example.museum;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.classes.Globals;
import com.example.classes.Scene;
import com.example.classes.Objects.ImageObject;
import com.example.classes.Objects.TextObject;
import com.example.classes.Objects.VScrollViewObject;

public class SceneFactsheet extends Scene {

	public enum ScreenState{
		LOCO,
		TANK,
		FIELDGUN,
		SYLVIE,
		PLANE,
		CRAWLER;
	}
	public SceneFactsheet(int idIn, Activity a, boolean visible) {
		super(idIn, a, visible);
	}
	
	ScreenState screenState;
	
	VScrollViewObject sV;
	TextObject textBody;
	TextObject textTitle;
	ImageObject imageLeft;
	ImageObject imageRight;
	
	String[] titles;
	String[] bodies;
	
	private void itemSetup(Activity aIn){
		textTitle = new TextObject("Title", aIn, Globals.newId());
		textTitle.getElement().setTypeface(Globals.Fonts.ChunkFive());
		textTitle.getElement().setTextSize(Globals.getTextSize()*2.4f);
		textTitle.alignToTop();
		textTitle.alignToLeft();
		textTitle.getLayoutParams().setMarginStart(Globals.screenDimensions.x/20);
		textTitle.getLayoutParams().setMargins(0, Globals.screenDimensions.y/100, 0, 0);
		
		locoLeft = R.drawable.factsheet_loco_left;
		locoRight = R.drawable.factsheet_loco_right;
		tank = R.drawable.factsheet_tank;
		fieldgunLeft = R.drawable.factsheet_fieldgun_left;
		fieldgunRight = R.drawable.factsheet_fieldgun_right;
		sylvieLeft = R.drawable.factsheet_sylvie_left;
		sylvieRight = R.drawable.factsheet_sylvie_right;
		planeLeft = R.drawable.factsheet_plane_left;
		planeRight = R.drawable.factsheet_plane_right;
		crawlerLeft = R.drawable.factsheet_crawler_left;
		crawlerRight = R.drawable.factsheet_crawler_right;
		background = R.drawable.factsheet_background;
		
		titles = aIn.getResources().getStringArray(R.array.factsheet_titles_array);
		bodies = aIn.getResources().getStringArray(R.array.factsheet_body_array);
		
		String s = "";
		
		//for (int i = 0; i < 5001; i++)
		//	s += "Hi ";
		textBody = new TextObject(s, aIn, Globals.newId());
		textBody.getElement().setTypeface(Globals.Fonts.ExoRegular());
		textBody.getElement().setTextSize(Globals.getTextSize()*1.5f);
		textBody.getElement().setLineSpacing(Globals.screenDimensions.y/200, 0.71f);
		
		
		imageLeft = new ImageObject(locoLeft, aIn, Globals.newId(), false);
		imageRight = new ImageObject(locoRight, aIn, Globals.newId(), false);
		
		
		
		//imageLeft.addRule(RelativeLayout.LEFT_OF, imageRight.getId());
		
		sV = new VScrollViewObject(aIn, Globals.newId());
		
		addElementToView(textTitle);
		
		sV.getLayoutParams().height = (int)(Globals.screenDimensions.y/2.4f);
		
		sV.alignToLeft();
		sV.addRule(RelativeLayout.BELOW, textTitle.getId());
		
		//sV.getLayoutParams().setMarginStart(Globals.screenDimensions.x/20);
		//sV.getLayoutParams().setMarginEnd(Globals.screenDimensions.x/20);
		
		imageLeft.addRule(RelativeLayout.BELOW, sV.getId());
		imageRight.addRule(RelativeLayout.BELOW, sV.getId());
		
		//imageLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		imageRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		
		imageRight.setVisibility(View.GONE);
		
		sV.getLayoutParams().setMargins(Globals.screenDimensions.x/20, 0, Globals.screenDimensions.x/20, Globals.screenDimensions.y/20);
		
		
		imageLeft.getLayoutParams().setMargins(Globals.screenDimensions.x/40, Globals.screenDimensions.y/40, Globals.screenDimensions.x/40, Globals.screenDimensions.y/40);
		imageRight.getLayoutParams().setMargins(Globals.screenDimensions.x/40, Globals.screenDimensions.y/40, Globals.screenDimensions.x/40, Globals.screenDimensions.y/40);
		
		//imageLeft.getElement().setPadding(5, 5, 5, 5);
		
		addElementToView(sV);
		
		textBody.addView(sV.getLayout());
		//addElementToView(textBody);
		
		addElementToView(imageLeft);
		//addElementToView(imageRight);
		
		
	}
	
	int locoLeft;
	int locoRight;
	int tank;
	int fieldgunLeft;
	int fieldgunRight;
	int sylvieLeft;
	int sylvieRight;
	int planeLeft;
	int planeRight;
	int crawlerLeft;
	int crawlerRight;
	int background;
	
	public void setScene(ScreenState s){
		
		
		screenState = s;
		
		textTitle.setText("");
		textBody.setText("");
		sV.getElement().scrollTo(0, 0);
		switch(screenState){
		case LOCO:
			textTitle.getElement().setText(titles[0]);
			textBody.getElement().setText(bodies[0]);
			//imageRight.setVisibility(View.VISIBLE);
			imageLeft.setImage(locoLeft);
			//imageRight.setImage(locoRight);
			break;
		case TANK:
			textTitle.getElement().setText(titles[1]);
			textBody.getElement().setText(bodies[1]);
			//imageRight.setVisibility(View.GONE);
			imageLeft.setImage(tank);
			break;
		case FIELDGUN:
			textTitle.setText(titles[2]);
			textBody.getElement().setText(bodies[2]);
			//imageRight.setVisibility(View.VISIBLE);
			imageLeft.setImage(fieldgunLeft);
			//imageRight.setImage(fieldgunRight);
			break;
		case SYLVIE:
			textTitle.setText(titles[3]);
			textBody.getElement().setText(bodies[3]);
			//imageRight.setVisibility(View.VISIBLE);
			imageLeft.setImage(sylvieLeft);
			//imageRight.setImage(sylvieRight);
			break;
		case PLANE:
			textTitle.setText(titles[4]);
			textBody.getElement().setText(bodies[4]);
			//imageRight.setVisibility(View.VISIBLE);
			imageLeft.setImage(planeLeft);
			//imageRight.setImage(planeRight);
			break;
		case CRAWLER:
			textTitle.setText(titles[5]);
			textBody.getElement().setText(bodies[5]);
			//imageRight.setVisibility(View.VISIBLE);
			imageLeft.setImage(crawlerLeft);
			//imageRight.setImage(crawlerRight);
			break;
		}	
		
		imageLeft.setAbsScaleX((int)(Globals.screenDimensions.x));
		//imageRight.setAbsScaleX((int)(Globals.screenDimensions.x/2.2f));
		
		//imageLeft.addRule(RelativeLayout.ALIGN_BASELINE, imageRight.getId());
		
		activity.getLayout().get().setBackgroundResource(background);
		activity.getLayout().get().getBackground().setAlpha(255);
	}
	
	@Override
	public void sceneInit(Activity aIn, boolean visible) {
		itemSetup(aIn);
		
		super.sceneInit(aIn, false);
		activity.getLayout().get().setBackgroundResource(R.drawable.factsheet_background);
	}

	@Override
	public void onBackPressed() {
		activity.setScreenState(GameActivity.ScreenState.MAIN);
	}

}
