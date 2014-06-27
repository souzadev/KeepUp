package com.souzadev.keepmeup;

import android.app.Activity;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	//Components 
	private View squareView;
	private PointF squarePosition;
	private ToggleButton toggleButton;	
	
	//Sensor
	private SensorManager sensorMan;
	private Sensor sensor;
	//**************************************** OVERRIDE ***************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initComponents();
		
		//Init Sensor
		sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorMan.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        
	}
	
	//********************************* PRIVATE ******************************
	private void initComponents() {
		squareView = (View)findViewById(R.id.main_view_smallSquare);
		squarePosition.x = squareView.getX();
		squarePosition.y = squareView.getY();
	}
	
	private void activate(){
		toggleButton = (ToggleButton)findViewById(R.id.main_toggleButton_activate);
		if (toggleButton.isChecked()){
			sensorMan.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_GAME);			
		}else{						
			sensorMan.unregisterListener(sensorListener);
			squareView.setX(squarePosition.x);
			squareView.setX(squarePosition.y);
		}
	}
	
	private SensorEventListener sensorListener = new SensorEventListener() {		
		@Override
		public void onSensorChanged(SensorEvent event) {
			
			
			toggleButton = (ToggleButton)findViewById(R.id.main_toggleButton_sensorX);
			if (toggleButton.isChecked()){
				toggleButton.setText(String.format("%.02f", event.values[0]));
				squareView.setY(squareView.getY() + event.values[0] * 10);
			}
			
			
			toggleButton = (ToggleButton)findViewById(R.id.main_toggleButton_sensorY);
			if (toggleButton.isChecked()){
				toggleButton.setText(String.format("%.02f", event.values[1]));
				squareView.setX(squareView.getX() + event.values[1] * 10);
			}
			
			toggleButton = (ToggleButton)findViewById(R.id.main_toggleButton_sensorZ);
			if (toggleButton.isChecked()){
				toggleButton.setText(String.format("%.02f", event.values[2]));
				squareView.setY(squareView.getY() - event.values[2] * 10);
			}
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub			
		}
	};
	
	//************************************ PUBLIC *********************************
	
	public void activateButton(View view){
		activate();
	}	
}
