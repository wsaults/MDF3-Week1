/*
 * project 	Weather
 * 
 * package 	com.fullsail.weather
 * 
 * @author 	William Saults
 * 
 * date 	Aug 7, 2013
 */
package com.fullsail.weather;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		String city = intent.getStringExtra(Intent.EXTRA_TEXT);
		
		TextView cityLabel = (TextView) findViewById(R.id.city);
		cityLabel.setText(city);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
