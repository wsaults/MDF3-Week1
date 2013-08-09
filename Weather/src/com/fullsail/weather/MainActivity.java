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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {

	Context context = this;
	public static Boolean connected = false;
	Boolean needsWeather;
	Boolean _isCelcius;
	SharedPreferences _preferences;
	SharedPreferences.Editor _editor;
	LinearLayout linearLayout;
	LinearLayout.LayoutParams layoutParams;
	String _city;

	// Weather
	JSONObject _weatherJson;
	String _forecastString;
	TableLayout tableLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		setContentView(R.layout.activity_main);

		// Test Network Connetion
		connected = Connectivity.getConnectionStatus(context);

		Intent intent = getIntent();
		_city = intent.getStringExtra(Intent.EXTRA_TEXT);

		TextView cityLabel = (TextView) findViewById(R.id.city);
		if (_city != null && !_city.isEmpty()) {
			cityLabel.setText(_city);
		} else {
			cityLabel.setText("Dallas");
		}

		// Test Network Connetion
		connected = Connectivity.getConnectionStatus(context);
		// Go get the weather!
		fetchWeather();
		needsWeather = false;

		// Setup a linear layout
		linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		linearLayout.setLayoutParams(layoutParams);

		this.addContentView(linearLayout, layoutParams);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void noConnectionAlert() {
		// Alert the user that there is no internet connection			
		new AlertDialog.Builder(context)
		.setTitle("Warning")
		.setMessage("Cound not connect to the internet")
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {}
		})
		.show();
	}

	/**
	 * Fetch weather based on the city preference.
	 */
	public void fetchWeather() {
		if (!connected) {
			noConnectionAlert();
		} else {
			// Go get the weather!
			Handler dataServieHandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					// Use the respose to populate the weather data table.
					String response = null;
					if (msg.arg1 == RESULT_OK && msg.obj != null) {
						try {
							response = (String) msg.obj;
						} catch (Exception e) {
							Log.e("", e.getMessage().toString());
						}

						// Parse the weather json object.
						Log.i("response", response);

						displayWeatherProvider();
					}
				}
			};

			// Create the messanger and start the service.
			Messenger dataMessenger = new Messenger(dataServieHandler);
			Intent startDataServiceIntent = new Intent(context, DataService.class);
			startDataServiceIntent.putExtra(DataService.MESSENGER_KEY, dataMessenger);
			
			String dataServiceString = "";
			if (_city != null && !_city.isEmpty()) {
				dataServiceString = _city;
			} else {
				dataServiceString = "Dallas";
			}
			
			startDataServiceIntent.putExtra(DataService.CITY_KEY, dataServiceString);
			startService(startDataServiceIntent);

			Log.i("Waiting on servie to end: ", "Waiting...");
		}
	}

	/**
	 * Display weather provider.
	 */
	@SuppressLint("SimpleDateFormat")
	private void displayWeatherProvider() {
		if (tableLayout != null) {
			// Remove any existing rows from the table.
			tableLayout.removeAllViews();
		}

		// Build a table for the rows.
		TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

		tableLayout = new TableLayout(context);
		tableLayout.setLayoutParams(layoutParams);
		tableLayout.setShrinkAllColumns(true);

		LinearLayout tableLL = (LinearLayout) findViewById(R.id.tableLayout);
		tableLL.addView(tableLayout);

		ForecastProvider provider = new ForecastProvider();
		try {
			Cursor cursor = provider.query(ForecastProvider.CONTENT_URI, ForecastProvider.PROJETION, null, null, "ASC");

			if (cursor != null) {
				Log.i("Cursor count", String.valueOf(cursor.getCount()));

				cursor.moveToFirst();
				do {
					//Display the forecast date.
					String dateString = cursor.getString(cursor.getColumnIndex(ForecastProvider.DATE_COLUMN));
					String maxString = cursor.getString(cursor.getColumnIndex(ForecastProvider.MAX_COLUMN));
					String minString = cursor.getString(cursor.getColumnIndex(ForecastProvider.MIN_COLUMN));

					Date date = new Date(Long.parseLong(dateString) * 1000);
					SimpleDateFormat df = new SimpleDateFormat("MM-dd");
					String dateText = df.format(date);

					TableRow tableRow = new TableRow(context);
					tableRow.setLayoutParams(tableParams);

					// Leave as celcius or converte to fahrentheit.
					Boolean isCelcius = false;
//					if (_isCelcius == null) {
//						isCelcius = _preferences.getBoolean("isCelcius", false);
//					} else {
//						isCelcius = _isCelcius;
//					}

					String dateHighLow;
					if (isCelcius) {
						dateHighLow = " Date: " + dateText + " " + "| High: " + maxString + " | Low: " + minString + "\n";
					} else {
						double maxD = Double.parseDouble(maxString);
						DecimalFormat decimalFormatter = new DecimalFormat("0.00");
						maxString = decimalFormatter.format((maxD * 1.8) + 32);

						double minD = Double.parseDouble(minString);
						minString = decimalFormatter.format((minD * 1.8) + 32);
						dateHighLow = " Date: " + dateText + " " + "| High: " + maxString + " | Low: " + minString + "\n";
					}

					TextView text1 = new TextView(context);
					text1.setText(dateHighLow);
					text1.setHeight(50);
					tableRow.addView(text1);
					tableLayout.addView(tableRow);
				} while (cursor.moveToNext());
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
