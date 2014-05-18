package com.iheanyiekechukwu.makewithmoto.opensight;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	public TextView text;
	
	private static final String MAKR_ENABLE = "/sys/class/makr/makr/5v_enable";
	private static final String TAG = "MainActivity";
	
	
	private TextView mTextView;
	private Button scanButton;
	private Button chartButton;
	private Bitmap mImageBitmap;
	private ImageView mImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
		setContentView(R.layout.activity_main);
		
		initializeMAKR();
		
		
	    
		mTextView = (TextView) findViewById(R.id.explainTextView);
		
		
		Typeface light = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
		
		mTextView.setTypeface(light);
		scanButton = (Button) findViewById(R.id.scanButton);
		scanButton.setOnClickListener(this);
		
		chartButton = (Button) findViewById(R.id.chartButton);
		chartButton.setOnClickListener(this);
		
		scanButton.setTypeface(light);
		chartButton.setTypeface(light);
		mImageView = (ImageView) findViewById(R.id.eyeImageView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()) {
		
		case R.id.scanButton:
			
			Intent scanIntent = new Intent(MainActivity.this, ScanActivity.class);
			startActivity(scanIntent);
			
			break;
			
		default:
			
			break;
		}
	}
	

	/*
	 * Turn on or off the device
	 */
	public void enable(boolean value) {
		BufferedWriter writer = null;
		try {
			FileOutputStream fos = new FileOutputStream(MAKR_ENABLE);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			writer = new BufferedWriter(osw);
			if (value)
				writer.write("on\n");
			else
				writer.write("off\n");
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public void initializeMAKR() {

		// enable the device
		if (!isEnabled()) {
			enable(true);
		}
	}
	

	public boolean isEnabled() {
		boolean status = false;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(MAKR_ENABLE));
			String rv = br.readLine();
			if (rv.matches("on")) {
				return true;
			}
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		return status;
	}

	protected void onDataReceived(byte[] buffer, int size) {
		// TODO Auto-generated method stub
		
	}
	
	

}
