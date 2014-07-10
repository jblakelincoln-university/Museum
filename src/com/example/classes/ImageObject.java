package com.example.classes;

import com.example.museum.*;
import android.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageObject extends AbstractElement<ImageView>{
	private Bitmap bitmap;
	public ImageObject(int im, Activity m, int idIn)
	{
		obj = new ImageView(m);
		obj.setId(idIn);
		obj.setImageResource(im);
		bitmap = BitmapFactory.decodeResource(m.getResources(), im);
		layoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.WRAP_CONTENT, 
        		RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
	}
	
	public ImageObject(Bitmap im, Activity m, int idIn)
	{
		obj = new ImageView(m);
		obj.setId(idIn);
		obj.setImageBitmap(im);
		layoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.WRAP_CONTENT, 
        		RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
	}
	
	public void setImage(int im){
		((ImageView)obj).setImageResource(im);
	}
	
	public void setScaleX(float x){
		obj.setScaleX(x);
	}
	
	public int getWidth(){
		return bitmap.getWidth();
	}
	
	public void setScaleY(float y){
		obj.setScaleY(y);
	}
	
	public void setScale(float x, float y){
		obj.setScaleX(x);
		obj.setScaleY(y);
	}
	
	public void setAbsScaleX(int x){
		float p = bitmap.getWidth();
		float v = bitmap.getHeight();
		//float newH = (x/bitmap.getWidth())*bitmap.getHeight();
		float newH = (x/p)*v;
		Bitmap b = Bitmap.createScaledBitmap(bitmap, x, (int)newH, true);
		obj.setImageBitmap(b);
		bitmap = b;
	}
	
	public void setAbsScaleY(int y){
		float p = bitmap.getWidth();
		float v = bitmap.getHeight();
		//float newH = (x/bitmap.getWidth())*bitmap.getHeight();
		float newH = (y/p)*v;
		Bitmap b = Bitmap.createScaledBitmap(bitmap, (int)newH, y, true);
		obj.setImageBitmap(b);
		bitmap = b;
	}
	
	public void setScale(int width, int height){
		obj.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width, height, false));
	}
}
