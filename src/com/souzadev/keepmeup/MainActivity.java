package com.souzadev.keepmeup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	private View squareView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initComponents();		
	}
	
	private void initComponents() {
		squareView = (View)findViewById(R.id.main_view_smallSquare);
	}
}
