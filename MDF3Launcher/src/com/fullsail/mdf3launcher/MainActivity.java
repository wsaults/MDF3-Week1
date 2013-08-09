/*
 * project 	MDF3Launcher
 * 
 * package 	com.fullsail.mdf3launcher
 * 
 * @author 	William Saults
 * 
 * date 	Aug 7, 2013
 */
package com.fullsail.mdf3launcher;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	EditText cityName;
	Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_main);
		
		cityName = (EditText) findViewById(R.id.cityNameEditText);

		// Launch app button
		Button switchViewsButton = (Button) findViewById(R.id.launchApp);
		switchViewsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: send selected city name;
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, cityName.getText().toString());
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
