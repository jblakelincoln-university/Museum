package com.example.classes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerManager{
	
	private static SensorManager sensorManager;
	private static SensorEventListener sensorEventListener;
	
	private static float accX = 0;
	private static float accY = 0;
	private static float accZ = 0;
	
	public static float getX(){ return accX;}
	public static float getY(){ return accY;}
	public static float getZ(){ return accZ;}
	
	public static void Init(Context c){
		sensorManager = (SensorManager)c.getSystemService(Context.SENSOR_SERVICE);
		
		sensorEventListener = new SensorEventListener(){
			@Override
			public void onSensorChanged(SensorEvent event){
				getAccelerometer(event);
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {}
		};
		
		sensorManager.registerListener(sensorEventListener,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);
	}
	
	private static void getAccelerometer(SensorEvent event) {
	    float[] values = event.values;

	    accX = values[0];
	    accY = values[1];
	    accZ = values[2];
	  }
}
