package com.iheanyiekechukwu.makewithmoto.opensight;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ScanActivity extends Activity implements OnClickListener {

	
	private Camera mCamera;
	private List<Size> mSupportedPreviewSizes;
	private CameraPreview mPreview;
	private ImageButton captureButton;
	private ImageButton cancelButton;
	
	private Application mApplication;
	private ImageButton okButton;
	
	private Bitmap mBitmap;
	
	private byte[] mImageData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_scan);
		// Show the Up button in the action bar.
		//setupActionBar();
		
		mCamera = getCameraInstance();
		//mPreview = new CameraPreview(this, mCamera);
		mPreview = (CameraPreview) findViewById(R.id.previewSurfaceView);
		mPreview.setupCameraPreview(this, mCamera);
		
		mApplication = (Application) getApplication();
		
		
		
		//mCamera.autoFocus(mFocusCallback);
		
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		
		setupImageButtons();

		//preview.addView(mPreview);
	}
	AutoFocusCallback mFocusCallback = new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean arg0, Camera arg1) {
			// TODO Auto-generated method stub
			captureButton.setEnabled(true);
		}
		
	};
	private void setupImageButtons() {
		captureButton = (ImageButton) findViewById(R.id.captureButton);
		captureButton.setOnClickListener(this);
		
		cancelButton = (ImageButton) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(this);
		
		okButton = (ImageButton) findViewById(R.id.confirmButton);
		okButton.setOnClickListener(this);
		

	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return c;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scan, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void takePicture() {
		mCamera.takePicture(null, null, mPictureCallback);

		changeCaptureVisibility(false);
	}
	
	private class ImageCaptureTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			mCamera.takePicture(null, null, mPictureCallback);
			
	        try {
	            Thread.sleep(3000); // 3 second preview
	        } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			mCamera.startPreview();
		}
		
		
	}
	
	Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			
			mImageData = data;
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			
			mBitmap = bitmap;
			mApplication.setBitmap(bitmap);
			
			//bitmap.recycle();
		}
	};
	
	public void customImageDialog(Bitmap b) {
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final ImageView imgView = new ImageView(this);
		
		alert.setTitle("Image Confirmation");
		
		alert.setView(imgView);
		
		alert.setPositiveButton("Analyze", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				/* From here, we will be able to create a new activity, which will be
				 * able to show the diagnostics information of the application. 
				 * This activity also should be passed the relevant bitmap image. */
				
				
			}
		});
		
		alert.setNegativeButton("Retake", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				mPreview.setupCameraPreview(getBaseContext(), mCamera);
			}
		});
		
		AlertDialog showAlert = alert.create();
		
		showAlert.show();
		
	}
	
	private void changeCaptureVisibility(boolean qVisible) {
		
		if(qVisible) {
			captureButton.setVisibility(View.VISIBLE);
			okButton.setVisibility(View.GONE);
			cancelButton.setVisibility(View.GONE);
		} else {
			captureButton.setVisibility(View.GONE);
			okButton.setVisibility(View.VISIBLE);
			cancelButton.setVisibility(View.VISIBLE);
		}
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		switch(v.getId()) {
		case R.id.captureButton:
			//captureButton.setEnabled(false);
			//mCamera.autoFocus(mFocusCallback);

			//mCamera.takePicture(null, rawCallback, null);
			//mCamera.takePicture(shutter, raw, jpeg)
			
			takePicture();
			break;
			
		case R.id.confirmButton:
			
			/* In this case, we will be passing the new data to the next intent,
			 * which will later serve as the basis for our tests.
			 */
			
			mApplication.setBitmap(mBitmap);
			
			Intent analyzeIntent = new Intent(ScanActivity.this, AnalyzeActivity.class);
			startActivity(analyzeIntent);
			
			//mBitmap.recycle();
			
			
			break;
			
		case R.id.cancelButton:
			
			/* We want to start the preview over again */
			
			//mPreview.setupCameraPreview(this, mCamera);
			
			mBitmap.recycle();
			mCamera.startPreview();
			changeCaptureVisibility(true);
			break;
		
		default:
			break;
		}
		

	}
	

}
