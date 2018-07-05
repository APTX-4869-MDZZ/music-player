package com.example.music;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseActivity extends Activity {
	
	protected LinearLayout actionbar = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected abstract void initialUI();
	
	/**
	 * @param leftId 左侧图片Id
	 * @param title
	 * @param rightId 右侧图片Id
	 */
	protected void initialActionBar(int leftId, String title, int rightId) {
		if (actionbar == null) {
			return;
		}
		ImageView leftImage = (ImageView) actionbar.findViewById(R.id.imageView_actionbar_left);
		ImageView rightImage = (ImageView) actionbar.findViewById(R.id.imageView_actionbar_right);
		TextView titleText =  (TextView) actionbar.findViewById(R.id.textView_title);
		if (leftId <= 0) {
			leftImage.setVisibility(View.INVISIBLE);
		} else {
			leftImage.setVisibility(View.VISIBLE);
			leftImage.setImageResource(leftId);
		}
		if (title == null) {
			titleText.setVisibility(View.INVISIBLE);
		} else {
			titleText.setVisibility(View.VISIBLE);
			titleText.setText(title);
		}
		if (rightId <= 0) {
			rightImage.setVisibility(View.INVISIBLE);
		} else {
			rightImage.setVisibility(View.VISIBLE);
			rightImage.setImageResource(rightId);
		}
	}
}
