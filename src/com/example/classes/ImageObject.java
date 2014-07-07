package com.example.classes;

import com.example.museum.*;
import android.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageObject extends AbstractElement{
	private ImageView imageView;
	private Bitmap bitmap;
	public ImageObject(int im, Activity m, RelativeLayout r, int idIn)
	{
		imageView = new ImageView(m);
		id = idIn;
		imageView.setId(id);
		imageView.setImageResource(im);
		bitmap = BitmapFactory.decodeResource(m.getResources(), im);
		layoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.WRAP_CONTENT, 
        		RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		r.addView(imageView, layoutParams);
	}
	
	public ImageObject(Bitmap im, Activity m, RelativeLayout r, int idIn)
	{
		imageView = new ImageView(m);
		id = idIn;
		imageView.setId(id);
		imageView.setImageBitmap(im);
		layoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.WRAP_CONTENT, 
        		RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		r.addView(imageView, layoutParams);
	}
	
	public void setImage(int im){
		imageView.setImageResource(im);
	}
	
	public void setScaleX(float x){
		imageView.setScaleX(x);
	}
	
	public int getWidth(){
		return bitmap.getWidth();
	}
	
	public void setScaleY(float y){
		imageView.setScaleY(y);
	}
	
	public void setScale(float x, float y){
		imageView.setScaleX(x);
		imageView.setScaleY(y);
	}
	
	public void setAbsScaleX(int x){
		float p = bitmap.getWidth();
		float v = bitmap.getHeight();
		//float newH = (x/bitmap.getWidth())*bitmap.getHeight();
		float newH = (x/p)*v;
		Bitmap b = Bitmap.createScaledBitmap(bitmap, x, (int)newH, true);
		imageView.setImageBitmap(b);
		bitmap = b;
	}
	
	public void setAbsScaleY(int y){
		float p = bitmap.getWidth();
		float v = bitmap.getHeight();
		//float newH = (x/bitmap.getWidth())*bitmap.getHeight();
		float newH = (y/p)*v;
		Bitmap b = Bitmap.createScaledBitmap(bitmap, (int)newH, y, true);
		imageView.setImageBitmap(b);
		bitmap = b;
	}
	
	public void setScale(int width, int height){
		imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width, height, false));
	}
	
	public ImageView getElement(){
		return imageView;
	}
	
}
