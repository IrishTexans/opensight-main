package com.iheanyiekechukwu.makewithmoto.opensight;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import android.graphics.Bitmap;
import android_serialport_api.SerialPort;

public class Application extends android.app.Application {

	private SerialPort mSerialPort = null;
	
	private Bitmap mBitmap;

	public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
		if (mSerialPort == null) {
			String path = "/dev/ttyHSL2";
			int baudrate = 9600;

			/* Check parameters */
			if ( (path.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}

			/* Open the serial port */
			mSerialPort = new SerialPort(new File(path), baudrate, 0);
		}
		return mSerialPort;
	}
	
	public void setBitmap(Bitmap b) {
		mBitmap = b;
	}
	
	public Bitmap getBitmap() {
		/*Matrix matrix = new Matrix();
		matrix.postRotate(90);
		
		Bitmap rotated = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix,
				true);
		
		mBitmap.recycle();*/
		return mBitmap;
	}
	

	public void closeSerialPort() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}
}
