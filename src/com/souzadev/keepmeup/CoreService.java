package com.souzadev.keepmeup;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class CoreService extends Service implements SensorEventListener {	
	//Const
	public static final float DRIFT_X = 0.00025f;
	public static final float DRIFT_Y = 0.00050f;
	public static final float DRIFT_Z = 0.00317f;
	private final IBinder myBinder = new LocalBinder();
	
	//Vars
	private boolean bound;
	private boolean inBounds;
	private float[] position = new float[3];
	private int sensibility;
	
	//Sensor
	private SensorManager sensorMan;
	private Sensor sensor;
	
	//Components
	private ToggleButton[] toggleButton = new ToggleButton[3];
	private TextView[] textView = new TextView[3];
	private View squareView;

	//Vibration
	private Vibrator vibrator;
	
	public class LocalBinder extends Binder{
		CoreService getService(){
			return CoreService.this;
		}
	}
	
	
	@Override
	public void onCreate() {
		//Init vars
		System.out.println("Creating service");
		bound = false;
		inBounds = true;
		
		//Init Sensor
		sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorMan.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        
        //Vibrator
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        
        //Foreground Notification
        Intent notIntent = new Intent(this, MainActivity.class)
        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, notIntent, 0);
        
        Notification.Builder nBuilder = new Notification.Builder(this)
        .setSmallIcon(android.R.drawable.ic_dialog_alert)
        .setContentTitle("Wake up!")
        .setContentText("Wake the fuck up!")
        .setContentIntent(pIntent);
                		
        startForeground(1, nBuilder.build());
        
        System.out.println("created");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("started");		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("service bound");
		sensibility = intent.getIntExtra("SENSIBILITY", 10);
		bound = true;
		return myBinder;
	}
	
	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
		System.out.println("service rebinded");
		sensibility = intent.getIntExtra("SENSIBILITY", 10);
		bound = true;		
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		System.out.println("service unbound");
		bound = false;
		return true;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();		
		stopSensor();
		stopForeground(true);
		vibrator.cancel();
		System.out.println("Service destroyed");
	}
	
	//PRIVATE
	private void outOfBounds(){
		squareView.setBackgroundColor(Color.RED);
		long pattern[] = {0,500,500};
		vibrator.vibrate(pattern, 0);
	}
	
	private void inBounds(){
		squareView.setBackgroundColor(Color.BLACK);
		vibrator.cancel();
	}
	
	public void setComponents(ToggleButton toggleButton0, ToggleButton toggleButton1, ToggleButton toggleButton2,
							  TextView textView0, TextView textView1, TextView textView2, View squareView){
		this.toggleButton[0] = toggleButton0;
		this.toggleButton[1] = toggleButton1;
		this.toggleButton[2] = toggleButton2;
		this.textView[0] = textView0;
		this.textView[1] = textView1;
		this.textView[2] = textView2;
		this.squareView = squareView;
		
		startSensor();
	}
	
	public void startSensor(){
		sensorMan.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
	}
	
	public void stopSensor(){
		sensorMan.unregisterListener(this);
	}
	
	public Boolean getBinding(){
		return bound;
	}
	
	//Listeners
	public void onSensorChanged(SensorEvent event) {
		if (toggleButton[0].isChecked()){
			position[0] = (position[0] + (event.values[0] - DRIFT_X) * sensibility);
			if(bound){				
				toggleButton[0].setText(String.format("%.05f", (event.values[0] - DRIFT_X)));
				textView[0].setText(String.valueOf(position[0]));
				squareView.setY(squareView.getY() + (event.values[0] - DRIFT_Y) * sensibility);
			}
		}
					
		if (toggleButton[1].isChecked()){
			position[1] = (position[1] + (event.values[1] - DRIFT_Y) * sensibility);
			if(bound){
				toggleButton[1].setText(String.format("%.05f", (event.values[1] - DRIFT_Y)));
				textView[1].setText(String.valueOf(position[1]));
				squareView.setX(squareView.getX() + (event.values[1] - DRIFT_X) * sensibility);
			}
		}
		
		if (toggleButton[2].isChecked()){
			position[2] = (position[2] + (event.values[2] - DRIFT_Z) * sensibility);
			if(bound){
				toggleButton[2].setText(String.format("%.05f", (event.values[2] - DRIFT_Z)));
				textView[2].setText(String.valueOf(position[2]));
				squareView.setRotation(squareView.getRotation() - (event.values[2] - DRIFT_Z) * sensibility);
			}
		}
		
		if((position[0] > 40) || (position[1] > 40) || (position[2] > 40) || (position[0] < -40) || (position[1] < -40) || (position[2] < -40)){
			if(inBounds){
				inBounds = false;
				outOfBounds();
			}
		}else{
			if(!inBounds){
				inBounds = true;
				inBounds();
			}
		}
		
		
						
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {			
	}
}
