package com.iheanyiekechukwu.makewithmoto.opensight;

import java.lang.ref.SoftReference;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class AnalyzeActivity extends Activity {

	
	private ImageView mEyeImageView;
	private Application mApplication;
	private Bitmap mBitmap;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_analyze);
		// Show the Up button in the action bar.
		setupActionBar();
		
	
		TextView infoTextView = (TextView) findViewById(R.id.infoTextView);
		mEyeImageView = (ImageView) findViewById(R.id.eyeImageView);
		
		Typeface light = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");

		infoTextView.setTypeface(light);
		
		mApplication = (Application) getApplication();
		mBitmap = mApplication.getBitmap();
		int x = mBitmap.getHeight()/4;
		int y = mBitmap.getWidth()/4;
		mBitmap = Bitmap.createScaledBitmap(mBitmap, x, y, false);
		
		mEyeImageView.setImageBitmap(mBitmap);
		
		
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		
		final int height = options.outHeight;
		final int width = options.outWidth;
		
		int sampleSize = 1; 
		
		if (height > reqHeight || width > reqWidth) { 
			
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			
			sampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		
		return sampleSize;
	}
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle("Analysis");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.analyze_activitiy, menu);
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
			//mEyeImageView.clearAnimation();
			//mBitmap.recycle();
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static Bitmap decodeSampledBitmapFromResource(byte[] b,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeByteArray(b, 0, b.length);
	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeByteArray(b, 0, b.length, options);
	}
	
	class BitmapWorkerTask extends AsyncTask<Intent, Void, Bitmap> {
		
		
		private final SoftReference<ImageView> imageViewSoftReference;
		private Intent data;
		
		public BitmapWorkerTask(ImageView imageView) {
			imageViewSoftReference = new SoftReference<ImageView>(imageView);
		}
		
		@Override
		protected Bitmap doInBackground(Intent... params) {
			// TODO Auto-generated method stub
			
			data = params[0];
			byte[] b = data.getByteArrayExtra("image");
					
			return decodeSampledBitmapFromResource(b, 480, 480);
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(imageViewSoftReference != null && result != null) {
				final ImageView imageView = imageViewSoftReference.get();
				if(imageView != null) {
					imageView.setImageBitmap(result);
				}
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		if(mBitmap != null) {
			mBitmap.recycle();
		}
//		mBitmap.recycle();
	}

}
