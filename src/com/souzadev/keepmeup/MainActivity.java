package com.souzadev.keepmeup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	//Components 
	private View squareView;
	private PointF squarePosition;
	private ToggleButton toggleButton;	
	
	//Sensor
	//**************************************** OVERRIDE ***************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Init vars
		squarePosition = new PointF();		
		initComponents();        
	}
	
	@Override
	protected void onPause() {		
		super.onPause();
	}
	
	//********************************* PRIVATE ******************************
	private void initComponents() {
		squareView = (View)findViewById(R.id.main_view_smallSquare);
	}
	
	private void activate(){
		toggleButton = (ToggleButton)findViewById(R.id.main_toggleButton_activate);
		if (toggleButton.isChecked()){
			squarePosition.x = squareView.getX();
			squarePosition.y = squareView.getY();
			
			boolean[] checkXyz = new boolean[3];
			checkXyz[0] = true;
			checkXyz[1] = true;
			checkXyz[2] = true;
			
			Intent intent = new Intent(this, Core.class);
			intent.putExtra("xyz", checkXyz);
			System.out.println("Sending intent");
			startService(intent);			
		}else{				
			squareView.setX(squarePosition.x);
			squareView.setY(squarePosition.y);			
		}
	}
	
	/*private SensorEventListener sensorListener = new SensorEventListener() {		
		@Override		
		public void onSensorChanged(SensorEvent event) {
			
			
			toggleButton = (ToggleButton)findViewById(R.id.main_toggleButton_sensorX);
			if (toggleButton.isChecked()){
				toggleButton.setText(String.format("%.05f", (event.values[0] - DRIFT_X)));
				squareView.setY(squareView.getY() + (event.values[0] - DRIFT_X) * 10);
			}
						
			toggleButton = (ToggleButton)findViewById(R.id.main_toggleButton_sensorY);
			if (toggleButton.isChecked()){
				toggleButton.setText(String.format("%.05f", (event.values[1] - DRIFT_Y)));
				squareView.setX(squareView.getX() + (event.values[1] - DRIFT_Y) * 10);
			}
			
			toggleButton = (ToggleButton)findViewById(R.id.main_toggleButton_sensorZ);
			if (toggleButton.isChecked()){
				toggleButton.setText(String.format("%.05f", (event.values[2] - DRIFT_Z)));
				squareView.setY(squareView.getY() - (event.values[2] - DRIFT_Z) * 10);
			}
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub			
		}
	};*/
	
	//************************************ PUBLIC *********************************
	
	public void activateButton(View view){
		activate();
	}	
}
