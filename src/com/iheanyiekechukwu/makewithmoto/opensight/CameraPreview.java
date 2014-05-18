package com.iheanyiekechukwu.makewithmoto.opensight;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private Context mContext;
	
	public boolean isPreviewing;
	
	static final String TAG = "CameraPreview";
	
	
	public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    // TODO Auto-generated constructor stub
	}

	public CameraPreview(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    // TODO Auto-generated constructor stub
	}
	public CameraPreview(Context context) {
		super(context);
	}
	
	@SuppressWarnings("deprecation") 
	public CameraPreview(Context context, Camera camera) {
		super(context);
		
		mCamera = camera;
		mContext = context;
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	public void setupCameraPreview(Context context, Camera camera) {
		
		mCamera = camera;
		mContext = context;
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}
	}
	
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		
		if(mCamera != null) {
			mCamera.stopPreview();
			isPreviewing = false;
		}
		
		mCamera.release();
		
		mCamera = null;
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		

		
		if(mHolder.getSurface() == null) {
			return;
		}
		
		try {
			mCamera.stopPreview();
		} catch(Exception e) {
			
		}


	        Parameters parameters = mCamera.getParameters();
	        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
	        mCamera.setParameters(parameters);

	        if(display.getRotation() == Surface.ROTATION_0)
	        {
	            parameters.setPreviewSize(height, width);                           
	            mCamera.setDisplayOrientation(90);
	        }

	        if(display.getRotation() == Surface.ROTATION_90)
	        {
	            parameters.setPreviewSize(width, height);                           
	        }

	        if(display.getRotation() == Surface.ROTATION_180)
	        {
	            parameters.setPreviewSize(height, width);               
	        }

	        if(display.getRotation() == Surface.ROTATION_270)
	        {
	            parameters.setPreviewSize(width, height);
	            mCamera.setDisplayOrientation(180);
	        }
		
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
			isPreviewing = true;
		} catch(Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}

}
