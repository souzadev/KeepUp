package com.souzadev.keepmeup;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	//Components
	private View squareView;
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
        sensorMan.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_GAME);
	}
	
	//********************************* PRIVATE ******************************
	private void initComponents() {
		squareView = (View)findViewById(R.id.main_view_smallSquare);		
	}
	
	private SensorEventListener sensorListener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			toggleButton = (ToggleButton)findViewById(R.id.main_toggleButton_sensorX);
			toggleButton.setText(String.format("%.02f", event.values[0]));
			
			toggleButton = (ToggleButton)findViewById(R.id.main_toggleButton_sensorY);
			toggleButton.setText(String.format("%.02f", event.values[1]));
			
			toggleButton = (ToggleButton)findViewById(R.id.main_toggleButton_sensorZ);
			toggleButton.setText(String.format("%.02f", event.values[2]));
			
			squareView.setX(squareView.getX() + event.values[1] * 10);
			squareView.setY(squareView.getY() + event.values[0] * 10);
			squareView.setY(squareView.getY() - event.values[2] * 10);
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
}
