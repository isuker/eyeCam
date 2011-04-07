package ch.hsr.eyecam;

import java.util.List;

import ch.hsr.eyecam.colormodel.ColorTransform;
import ch.hsr.eyecam.view.ColorView;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * This class represents the core of the eyeCam application. It is responsible for
 * the initialization of the Camera and the View and managing all aspects of the
 * life cycle of the application itself.
 * 
 * @author Dominik Spengler
 * @see <a href="http://developer.android.com/reference/
 * 			android/app/Activity.html">
 * 			android.app.Activity</a>
 */
public class EyeCamActivity extends Activity {
	private Camera mCamera;
	private ColorView mColorView;
	private boolean mCamIsPreviewing;
	private byte[] mCallBackBuffer;
	private final DisplayMetrics mMetrics = new DisplayMetrics();
	private final static String LOG_TAG = "ch.hsr.eyecam.EyeCamActivity";
	public final static int CAMERA_START_PREVIEW = 0;
	public final static int CAMERA_STOP_PREVIEW = 1;
	
	private PowerManager.WakeLock mWakeLock;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
			case CAMERA_START_PREVIEW:
				startCameraPreview();
				break;
			case CAMERA_STOP_PREVIEW:
				stopCameraPreview();
				break;
			}
		}

	};
	
	private OnClickListener mOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (mCamIsPreviewing) stopCameraPreview();
			else startCameraPreview();
		}
	};
	
	/** Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mColorView = (ColorView) findViewById(R.id.cameraSurface);
		mColorView.setActivityHandler(mHandler);
		mColorView.setOnClickListener(mOnClick);
		getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
		
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "eyeCam");
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		initCamera();
		mWakeLock.acquire();
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		releaseCamera();
		mWakeLock.release();
	}

	/**
	 * By overwriting this hook, the activity blocks search requests.
	 * 
	 * @see <a href="http://developer.android.com/reference/
	 * 			android/app/Activity.html#onSearchRequested()">
     * 			android.app.Activity#onSearchRequested()</a>
	 */
	@Override
	public boolean onSearchRequested(){
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0,1,0,"None");
		menu.add(0,2,0,"Simulate");
		menu.add(0,3,0,"Intensify");
		menu.add(0,4,0,"Partial False Colors");
		menu.add(0,5,0,"Partial Black");
		menu.add(0,6,0,"Partial Intensify");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()){
		case 1:
			ColorTransform.setEffect(ColorTransform.COLOR_EFFECT_NONE);
			break;
		case 2:
			ColorTransform.setEffect(ColorTransform.COLOR_EFFECT_SIMULATE);
			break;
		case 3:
			ColorTransform.setEffect(ColorTransform.COLOR_EFFECT_INTENSIFY_DIFFERENCE);
			break;
		case 4:
			ColorTransform.setPartialEffect(ColorTransform.COLOR_EFFECT_FALSE_COLORS);
			break;
		case 5:
			ColorTransform.setPartialEffect(ColorTransform.COLOR_EFFECT_BLACK);
			break;
		case 6:
			ColorTransform.setPartialEffect(ColorTransform.COLOR_EFFECT_INTENSIFY_DIFFERENCE);
			break;
		}
		return true;
	}
	
	private void startCameraPreview() {
		mCamera.addCallbackBuffer(mCallBackBuffer);
		mCamera.setPreviewCallbackWithBuffer((PreviewCallback) mColorView);
		mCamera.startPreview();
		mCamIsPreviewing = true;
	}
	
	private void stopCameraPreview() {
		mCamera.setPreviewCallbackWithBuffer(null);
		mCamera.stopPreview();
		mCamIsPreviewing = false;
	}
	
	private boolean isNotNull(Object anyObject) {
		return anyObject != null;
	}
	
	private boolean isNull(Object anyObject){
		return !isNotNull(anyObject);
	}
	
	private void initCamera() {
		mCamera= Camera.open();
		Camera.Parameters parameters = mCamera.getParameters();	
		
		Size optSize = getOptimalSize(parameters.getSupportedPreviewSizes());
		for (Size s : parameters.getSupportedPreviewSizes()){
			Log.d(LOG_TAG, "Supported - H:" + s.height + "W:" + s.width);
		}
		parameters.setPreviewSize(optSize.width, optSize.height);
		Log.d(LOG_TAG, "Chosen - H:" +optSize.height + "W:" +optSize.width);
		Log.d(LOG_TAG, "Screen - H:" +mMetrics.heightPixels + "W:" +mMetrics.widthPixels);
		
		mCallBackBuffer = new byte[optSize.width*optSize.height*2];
		mCamera.setParameters(parameters);
	}

	private Size getOptimalSize(List<Size> sizeList){
		if(isNull(sizeList)) return null;
		
		double targetRatio = (double) mMetrics.widthPixels / mMetrics.heightPixels;
		int targetHeight = mMetrics.heightPixels;
		double diffRatio = Double.MAX_VALUE;
		Size optSize = null;
		
		for(Size size : sizeList){
			double tmpDiffRatio = (double) size.width / size.height;
			if(Math.abs(targetRatio-tmpDiffRatio)< diffRatio){
				optSize = size;
				diffRatio = Math.abs(targetRatio-tmpDiffRatio) +
							Math.abs(size.height-targetHeight);
				if (diffRatio == 0) return optSize;
			}
		}
		return optSize;
	}
	
	private void releaseCamera(){
		if(isNull(mCamera)) return;
		
		stopCameraPreview();
		mCamera.release();
		mCamera = null;
	}
}
