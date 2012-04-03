package ch.hsr.eyecam.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageButton;
import ch.hsr.eyecam.Debug;
import ch.hsr.eyecam.R;

/**
 * A class extending android.widget.ImageButton. The idea behind this class
 * is to merge the benefit of the android.widget.ToggleButton and the 
 * android.widget.ImageButton. Or in other words, we add a State to the 
 * android.widget.ImageButton and each State has it own picture. 
 * 
 * The default State is false.
 * 
 * @author Patrice Mueller
 * @see ImageButton
 */
public class StateImageButton extends ImageButton implements Checkable{

	private boolean mState;
	private boolean mEnabled;
	private int mImgResTrue, mImgResFalse, mImgResDisabled;
	private OnClickListener mOnClickDisabled = null;
	private OnClickListener mOnClickListener;
	private boolean mChangeImage;
	private static String LOG_TAG = "ch.hsr.eyecam.StateImageButton";
	
	/**
	 * Use this constructor if you want to set the attributes without the 
	 * xml-file
	 * @param contex
	 */
	public StateImageButton(Context contex){
		super(contex);
		mState= false;
		mEnabled = true;
		mChangeImage = true;
		setImage();
	}
	
	/**
	 * This constructor should be used if you want to set the attributes by the 
	 * xml-file. We recommend this way.
	 * @param context
	 * @param attrs
	 */
	public StateImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mState = false;
		mEnabled = true;
		mChangeImage = true;
		
		TypedArray typedArrayAttr = context.obtainStyledAttributes(attrs
				,R.styleable.StateImageButton);
		
		mImgResTrue = typedArrayAttr.getResourceId(
				R.styleable.StateImageButton_imgResTrue, R.drawable.ic_menu_sad);
		mImgResFalse = typedArrayAttr.getResourceId(
				R.styleable.StateImageButton_imgResFalse, R.drawable.ic_menu_sad);
		mImgResDisabled = typedArrayAttr.getResourceId(
				R.styleable.StateImageButton_imgResDisabled, R.drawable.ic_menu_sad);
		
		setImage();
	}
	
	private void setImage(){
		if(mState) setImageResource(mImgResTrue);
		else setImageResource(mImgResFalse);
	}
	
	/**
	 * To set the image which will display by the state true. It should be set 
	 * by the xml-file. 
	 * 
	 * @param resId
	 */
	public void setImgResTrue(int resId){
		mImgResTrue = resId;
	}
	
	/**
	 * To set the image which will display by the state false. It should be set 
	 * by the xml-file. 
	 * 
	 * @param resId
	 */
	public void setImgResFalse(int resId){
		mImgResFalse = resId;
	}

	/**
	 * To set the image which will display by the state disabled. It should be set 
	 * by the xml-file. 
	 * 
	 * @param resId
	 */
	public void setImgResDisabled(int resId){
		mImgResDisabled = resId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChecked() {
		return mState;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * We added the image change.
	 */
	@Override
	public void setChecked(boolean checked) {
		if(!isEnabled())return;
		Debug.msg(LOG_TAG, "State has changed! From:"+mState +" To: "+checked);
		mState = checked;
		setImage();
	}

	/**
	 * This method should be used if you want to change the State of this
	 * Button. It handles the switch of the Image Resources (recommended)
	 */
	@Override
	public void toggle() {
		setChecked(!isChecked());
	}
	
	/**
	 * Whether or not the image should change on click.
	 * 
	 * This is particularly useful if you want the model to perform the image
	 * changes.
	 * 
	 * @param changeImage
	 */
	public void setImageChange(boolean changeImage){
		mChangeImage = changeImage;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * We overwrote this method just to add a toggle action before perfomClick.
	 */
	@Override
	public boolean performClick() {
		if (mChangeImage) toggle();
		return super.performClick();
	}
	
	/**
	 * Manage all OnClickListener and image changes which have to be done when 
	 * the state is changed.
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		if(!enabled) {
			setImageResource(mImgResDisabled);
			super.setOnClickListener(mOnClickDisabled);
		} else {
			super.setOnClickListener(mOnClickListener);
			setImage();
		}
		mEnabled = enabled;
	}
	
	/**
	 * Whether or not the StateImageButton is enabled.
	 */
	public boolean isEnabled(){
		return mEnabled;
	}
	
	/**
	 * Set your OnClickListener which will be called when the button is disabled
	 * but still show for the user.
	 * @param onClickDisabled
	 */
	public void setOnDisabledClickListener(OnClickListener onClickDisabled){
		mOnClickDisabled = onClickDisabled;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOnClickListener(OnClickListener l) {
		mOnClickListener = l;
		super.setOnClickListener(mOnClickListener);
	}
}
