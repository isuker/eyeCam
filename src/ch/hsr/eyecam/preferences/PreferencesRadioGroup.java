package ch.hsr.eyecam.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import ch.hsr.eyecam.Debug;
import ch.hsr.eyecam.R;

/**
 * This class extends from android.widget.RadioGroup and add the functionality
 * to add a TextView as a separator and to load the preferences of the your
 * preferences-file.
 * 
 * @author Patrice Mueller
 * 
 * @see RadioGroup
 */
public class PreferencesRadioGroup extends RadioGroup implements
		OnCheckedChangeListener {
	String mTitle, mKey;
	TypedArray mTypedArray;
	private SharedPreferences mSharedPreferences;
	boolean mEnableSeperator;
	private int mDefaultValue;

	private final static int NO_KEY = 0;
	private static final String LOG_TAG = "ch.hsr.eyecam.widget.PreferencesRadioGroup";

	public PreferencesRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);

		mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.PreferencesRadioGroup);
		mTitle = getString(R.styleable.PreferencesRadioGroup_title,
				R.string.setting_no_title);
		mEnableSeperator = mTypedArray.getBoolean(
				R.styleable.PreferencesRadioGroup_enableSeperator, true);
		mKey = getString(R.styleable.PreferencesRadioGroup_key, NO_KEY);
		mDefaultValue = mTypedArray.getInteger(
				R.styleable.PreferencesRadioGroup_defaultValue, 0);
		
		
		if (mEnableSeperator){
			Separator sep = new Separator(context, attrs);
			sep.setText(mTitle);
			addView(sep);
		}
		
		if (isInEditMode())
			return;
		
		Context appContex = getContext().getApplicationContext();
		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(appContex);
	}

	private String getString(int ResId, int resDefaultValue) {
		if (hasKey(ResId, resDefaultValue))
			return mTitle;

		int stringRes = mTypedArray.getResourceId(ResId, resDefaultValue);
		return getResources().getString(stringRes);
	}

	private boolean hasKey(int ResId, int resDefaultValu) {
		return resDefaultValu == NO_KEY && mTypedArray.getString(ResId) == null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Add the values of the preferences-file
	 */
	@Override
	protected void onFinishInflate() {
		if (isInEditMode())
			return;
		initCheckedValue();
		super.onFinishInflate();
	}

	private void initCheckedValue() {
		setOnCheckedChangeListener(this);

		PreferencesRadioButton button;
		int value = mSharedPreferences.getInt(mKey, mDefaultValue);
		Debug.msg(LOG_TAG, "trying to set value: " + value);
		for (int i = 0; i < getChildCount(); i++) {
			if (mEnableSeperator && i == 0)
				continue;
			button = (PreferencesRadioButton) getChildAt(i);
			Debug.msg(LOG_TAG, "button: " + button.getText()
					+ " containing value: " + button.getValue());
			if (button.getValue() == value) {
				button.performClick();
				Debug.msg(LOG_TAG,
						"successfully set value on: " + button.getText());
				return;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Write the changed preferences to the file and update the user interface.
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		PreferencesRadioButton button = (PreferencesRadioButton) findViewById(checkedId);
		if (button == null)
			return;
		Debug.msg(LOG_TAG, "Preference changed, key: " + mKey + " to: "
				+ button.getValue());

		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putInt(mKey, button.getValue());
		if (!editor.commit())
			Debug.msg(LOG_TAG, "Preferences " + mKey
					+ " couldn't been committed!");

		ViewGroup vg = (ViewGroup) getParent();
		if (vg != null)
			vg.invalidate();
	}

	public static class Separator extends TextView {

		public Separator(Context context) {
			super(context);
			init();
		}

		public Separator(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public Separator(Context context, String Title) {
			this(context);

			setText(Title);
		}

		private void init(){
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
			setLayoutParams(params);
		}
	}
}
