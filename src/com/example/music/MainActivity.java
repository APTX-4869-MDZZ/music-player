package com.example.music;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	RelativeLayout startLayout = null;
	Button button = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startLayout = (RelativeLayout) findViewById(R.id.layout_start);
		button = (Button) findViewById(R.id.button_start);

		new Thread(new Runnable() {

			@Override
			public void run() {
				AlphaAnimation alphAni = new AlphaAnimation(1.0f, 0.3f);
				alphAni.setDuration(8000);
				startLayout.startAnimation(alphAni);
				alphAni.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Intent intent = new Intent(MainActivity.this, MusicActivity.class);
						startActivity(intent);
						finish();
					}
				});
			}
		}).start();

		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,-300);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 0.8f);
		animationSet.setDuration(4000);
		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(translateAnimation);
		button.startAnimation(animationSet);
		animationSet.setAnimationListener(new Animation.AnimationListener() {
		     @Override
		     public void onAnimationStart(Animation animation) {
		     }
		     
		     @Override
		     public void onAnimationRepeat(Animation animation) {
		     }
		     
		     @Override
		     public void onAnimationEnd(Animation animation) {
		         int left = button.getLeft();
		         int top = button.getTop();
		         button.clearAnimation();
		         button.layout(left, top-150, left+button.getWidth(), top-150+button.getHeight());
		         
		     }
		 });
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, MusicActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
