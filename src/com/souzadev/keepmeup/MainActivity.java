package com.souzadev.keepmeup;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.souzadev.keepmeup.CoreService.LocalBinder;

public class MainActivity extends Activity implements ServiceConnection{
	//Const
	public static final float DRIFT_X = 0.00024f;
	public static final float DRIFT_Y = 0.00073f;
	public static final float DRIFT_Z = 0.00317f;
	
	//Components 
	private View squareView;
	private ToggleButton toggleButton;
	private RelativeLayout layoutBot;
	
	//Connection
	CoreService myService;
	
	//**************************************** OVERRIDE ***************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = new Intent(this, CoreService.class);
		stopService(intent);
		
		initComponents();		
	}
	
	@Override
	protected void onStart() {
		super.onStart();		
		bindToService();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		unbindFromService();
	}
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		LocalBinder locBinder = (LocalBinder)service;
		myService = locBinder.getService();
		myService.setComponents((ToggleButton)findViewById(R.id.main_toggleButton_sensorX), (ToggleButton)findViewById(R.id.main_toggleButton_sensorY), (ToggleButton)findViewById(R.id.main_toggleButton_sensorZ),
								(TextView)findViewById(R.id.main_textView_squarePosition), (TextView)findViewById(R.id.main_textView_virtualPosition), (TextView)findViewById(R.id.main_textView_test),squareView);
		toggleButton = (ToggleButton)findViewById(R.id.main_toggleButton_activate);
		toggleButton.setChecked(true);
		System.out.println("Connected");
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
	}
	
	//********************************* PRIVATE ******************************
	private void initComponents() {
		squareView = (View)findViewById(R.id.main_view_smallSquare);
		layoutBot = (RelativeLayout)findViewById(R.id.main_relativeLayout_bot);
	}
	
	private void activate(){
		toggleButton = (ToggleButton)findViewById(R.id.main_toggleButton_activate);
		if (toggleButton.isChecked()){
			System.out.println("Sending intent");
			Intent intent = new Intent(this, CoreService.class);
			startService(intent);
			bindToService();
		}else{
			unbindFromService();
			Intent intent = new Intent(this, CoreService.class);
			stopService(intent);
			
			squareView.setBackgroundColor(Color.BLACK);
			squareView.setX((layoutBot.getWidth() / 2) - 38);
			squareView.setY((layoutBot.getHeight() / 2) - 38);
			squareView.setRotation(0);
		}
	}
	
	private void bindToService(){
		SeekBar sb = (SeekBar)findViewById(R.id.main_seekBar_sensibility);		
		Intent intent = new Intent(this, CoreService.class);
		intent.putExtra("SENSIBILITY", sb.getProgress());
		try{		
			bindService(intent, this, 0);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void unbindFromService(){
		try{
			unbindService(this);
		}catch(Exception ex){			
		}
	}
	
	private void test(){

	}
	
	//************************************ PUBLIC *********************************
	
	public void activateButton(View view){
		activate();
	}	
	
	public void testButton(View view){
		test();
	}
}
