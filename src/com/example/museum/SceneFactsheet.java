package com.example.museum;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.classes.Colour;
import com.example.classes.Globals;
import com.example.classes.Scene;
import com.example.classes.Objects.ImageObject;
import com.example.classes.Objects.TextObject;

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
		// TODO Auto-generated constructor stub
	}
	
	ScreenState screenState;
	
	TextObject textBody;
	TextObject textTitle;
	ImageObject imageLeft;
	ImageObject imageRight;
	
	private void itemSetup(Activity aIn){
		textTitle = new TextObject("Title", aIn, Globals.newId());
		textTitle.getElement().setTypeface(Globals.Fonts.SofiaRegular());
		textTitle.getElement().setTextSize(Globals.getTextSize()*2.4f);
		textTitle.alignToTop();
		textTitle.alignToLeft();
		textTitle.getLayoutParams().setMarginStart(Globals.screenDimensions.x/20);
		textTitle.getLayoutParams().setMargins(0, Globals.screenDimensions.y/100, 0, 0);
		
		String s = "";
		
		for (int i = 0; i < 50; i++)
			s += "Hi ";
		textBody = new TextObject(s, aIn, Globals.newId());
		textBody.getElement().setTypeface(Globals.Fonts.SofiaRegular());
		textBody.getElement().setTextSize(Globals.getTextSize()*1.3f);
		
		textBody.alignToLeft();
		textBody.addRule(RelativeLayout.BELOW, textTitle.getId());
		
		textBody.getLayoutParams().setMarginStart(Globals.screenDimensions.x/20);
		textBody.getLayoutParams().setMarginEnd(Globals.screenDimensions.x/20);
		
		textBody.getElement().setLineSpacing(Globals.screenDimensions.y/200, 0.71f);
		
		imageLeft = new ImageObject(null, aIn, Globals.newId(), false);
		imageRight = new ImageObject(null, aIn, Globals.newId(), false);
		
		imageLeft.addRule(RelativeLayout.BELOW, textBody.getId());
		imageRight.addRule(RelativeLayout.BELOW, textBody.getId());
		
		imageLeft.addRule(RelativeLayout.LEFT_OF, imageRight.getId());
		
		imageLeft.getLayoutParams().setMarginStart(Globals.screenDimensions.x/40);
		imageLeft.getLayoutParams().setMarginEnd(Globals.screenDimensions.x/40);
		imageRight.getLayoutParams().setMarginStart(Globals.screenDimensions.x/40);
		imageRight.getLayoutParams().setMarginEnd(Globals.screenDimensions.x/40);
		
		//imageLeft.getElement().setPadding(5, 5, 5, 5);
		
		addElementToView(textTitle);
		addElementToView(textBody);
		
		addElementToView(imageLeft);
		addElementToView(imageRight);
		
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
	
	public void setScene(ScreenState s){
		screenState = s;
		
		textTitle.setText("");
		((View) textTitle.getElementView().getParent()).invalidate();
		switch(screenState){
		case LOCO:
			textTitle.getElement().setText("11111111");
			imageRight.setVisibility(View.VISIBLE);
			//imageLeft.setImage(locoLeft);
			//imageLeft.setImage(locoRight);
			break;
		case TANK:
			textTitle.getElement().setText("22222222");
			//imageRight.setVisibility(View.GONE);
			//imageLeft.setImage(tank);
			break;
		case FIELDGUN:
			textTitle.setText(activity.getString(R.string.factsheet_title_fieldgun).toString());
			imageRight.setVisibility(View.VISIBLE);
			//imageLeft.setImage(fieldgunLeft);
			//imageLeft.setImage(fieldgunRight);
			break;
		case SYLVIE:
			textTitle.setText(activity.getString(R.string.factsheet_title_sylvie).toString());
			imageRight.setVisibility(View.VISIBLE);
			//imageLeft.setImage(sylvieLeft);
			//imageLeft.setImage(sylvieRight);
			break;
		case PLANE:
			textTitle.setText(activity.getString(R.string.factsheet_title_plane).toString());
			imageRight.setVisibility(View.VISIBLE);
			//imageLeft.setImage(planeLeft);
			//imageLeft.setImage(planeRight);
			break;
		case CRAWLER:
			textTitle.setText(activity.getString(R.string.factsheet_title_crawler).toString());
			imageRight.setVisibility(View.VISIBLE);
			//imageLeft.setImage(crawlerLeft);
			//imageLeft.setImage(crawlerRight);
			break;
		}
		
		
		Globals.rLayout.invalidate();
		
		
		
	}
	
	@Override
	public void sceneInit(Activity aIn, boolean visible) {
		itemSetup(aIn);
		
		Globals.rLayout.setBackgroundResource(R.drawable.factsheet_background);
	}

	@Override
	public void onBackPressed() {
		activity.setScreenState(GameActivity.ScreenState.MAIN);
	}

}
