package com.fullsail.mdf3launcher;

import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Switch views button
		Button switchViewsButton = (Button) findViewById(R.id.launchApp);
		switchViewsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: send selected city name;
				Intent intent = new Intent("android.intent.action.MAIN");
			    intent.setComponent(ComponentName.unflattenFromString("com.fullsail.weather"));
			    intent.addCategory("android.intent.category.LAUNCHER");
			    startActivity(intent);
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
