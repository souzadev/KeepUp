package com.souzadev.keepmeup;

import android.app.Service;
import android.content.Intent;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

public class Core extends Service {

	//Const
	public static final float DRIFT_X = 0.00024f;
	public static final float DRIFT_Y = 0.00073f;
	public static final float DRIFT_Z = 0.00317f;
	
	//Vars
	private PointF squarePosition;
	private boolean[] checkXyz;
	
	//Sensor
	private SensorManager sensorMan;
	private Sensor sensor;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onCreate() {
		//Init vars
		System.out.println("Starting service");
		/*squarePosition = new PointF();
		checkXyz = new boolean[3];
		//TODO FIX NAME
		checkXyz = intent.getBooleanArrayExtra("xyz");		
		
		//Init Sensor
		sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorMan.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorMan.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_GAME);
        System.out.println("Started");*/
		stopSelf();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();		
//		sensorMan.unregisterListener(sensorListener);
		System.out.println("Service destroy");		
	}
	
	private SensorEventListener sensorListener = new SensorEventListener() {		
		@Override		
		public void onSensorChanged(SensorEvent event) {
			
			//toggleButton = (ToggleButton)findViewById(R.id.main_toggleButton_sensorX);
			//if (toggleButton.isChecked()){
			if (checkXyz[0]){				
				//toggleButton.setText(String.format("%.05f", (event.values[0] - DRIFT_X)));
				//squareView.setY(squareView.getY() + (event.values[0] - DRIFT_X) * 10);
				squarePosition.y = squarePosition.y + (event.values[0] - DRIFT_X) * 10;
			}
						
			//toggleButton = (ToggleButton)findViewById(R.id.main_toggleButton_sensorY);
			//if (toggleButton.isChecked()){
			if (checkXyz[1]){
				//toggleButton.setText(String.format("%.05f", (event.values[1] - DRIFT_Y)));
				//squareView.setX(squareView.getX() + (event.values[1] - DRIFT_Y) * 10);
				squarePosition.x = squarePosition.x + (event.values[1] - DRIFT_Y) * 10;
			}
			
			//toggleButton = (ToggleButton)findViewById(R.id.main_toggleButton_sensorZ);
			//if (toggleButton.isChecked()){
			if (checkXyz[2]){
				//toggleButton.setText(String.format("%.05f", (event.values[2] - DRIFT_Z)));
				squarePosition.y = squarePosition.y + (event.values[2] - DRIFT_Z) * 10;
			}
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub			
		}
	};
}
