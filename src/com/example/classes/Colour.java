package com.example.classes;

public class Colour {

	public static int FromRGB(int r, int g, int b, int a){
		return (a << 24) | (r << 16) | (g << 8) | b;
	}
	
	public static int FromRGB(int r, int g, int b){
		return (255 << 24) | (r << 16) | (g << 8) | b;
	}
	
	public static int FromRGB(float r, float g, float b, float a){
		return ((int)(a*255+0.5) << 24) | ((int)(r*255+0.5) << 16) | ((int)(g*255+0.5) << 8) | (int)(b*255+0.5);
	}
	
	public static int FromRGB(float r, float g, float b){
		return (255 << 24) | ((int)(r*255+0.5) << 16) | ((int)(g*255+0.5) << 8) | (int)(b*255+0.5);
	}
	
	static int[] temp = new int[1];
	
	public static int[] ArrFromRGB(int r, int g, int b, int a){
		temp[0] = (a << 24) | (r << 16) | (g << 8) | b;
		return temp;
	}
	
	public static int[] ArrFromRGB(int r, int g, int b){
		temp[0] = (255 << 24) | (r << 16) | (g << 8) | b;
		return temp;
	}
	
	public static final int Transparent = (0 << 24) | (0 << 16) | (0 << 8) | 0;
}
