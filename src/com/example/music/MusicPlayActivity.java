package com.example.music;

import java.text.SimpleDateFormat;
import java.util.List;

import com.example.music.bean.Music;
import com.example.music.constant.IURL;
import com.example.music.manager.MyImageLoader;
import com.example.music.util.DiskView;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MusicPlayActivity extends BaseActivity implements OnClickListener {

	List<Music> musics = null;
	Music music = null;
	int currentPosition;
	boolean isPlay = true;
	boolean touched = false;

	ImageView discImage = null;
	ImageView albumImage = null;
	ImageView pinImage = null;
	ImageView favoImage = null;
	ImageView downloadImage = null;
	ImageView backImage = null;
	ImageButton pauseButton = null;
	ImageButton backwardButton = null;
	ImageButton forwardButton = null;
	SeekBar musicSeekBar = null;
	DiskView diskview = null;
	TextView durationTextView = null;
	TextView timeTextView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_music_play);

		Intent intent = getIntent();
		musics = (List<Music>) intent.getSerializableExtra("musics");
		currentPosition = intent.getIntExtra("currentPosition", 0);
		music = musics.get(currentPosition);
		Log.i("music", music.toString());

		initialUI();
		addListener();
		
		MyBroadcastReive receiver = new MyBroadcastReive();
		IntentFilter filter = new IntentFilter();
		filter.addAction(IURL.ACTION_MUSIC_SEEK_CHANGE);
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		play();
	}

	private void play() {
		isPlay = true;
		diskview.startRotation();
		initialActionBar(R.drawable.back, music.getName(), R.drawable.statics);
		MyImageLoader.setBitmapFromCache(this, diskview.getAlbumpic(), IURL.ROOT + music.getAlbumpic());
		durationTextView.setText(music.getDurationtime());

		pauseButton.setImageResource(R.drawable.pause);

		Intent intent = new Intent();
		intent.setAction(IURL.ACTION_MUSIC_PLAY);
		intent.putExtra("music_path", IURL.ROOT + music.getMusicpath());
		sendBroadcast(intent);
	}

	private void pause() {
		isPlay = false;
		diskview.stopRotation();

		pauseButton.setImageResource(R.drawable.play);

		Intent intent = new Intent();
		intent.setAction(IURL.ACTION_MUSIC_PAUSE);
		sendBroadcast(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.music_play, menu);
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

	@Override
	protected void initialUI() {
		actionbar = (LinearLayout) findViewById(R.id.header_play_actionbar);
		initialActionBar(R.drawable.back, music.getName(), R.drawable.statics);

		diskview = (DiskView) findViewById(R.id.disc_include);
		discImage = (ImageView) findViewById(R.id.imageView_disc);
		albumImage = (ImageView) findViewById(R.id.imageView_album);
		pinImage = (ImageView) findViewById(R.id.imageView_pin);
		favoImage = (ImageView) findViewById(R.id.imageView_favo);
		downloadImage = (ImageView) findViewById(R.id.imageView_download);
		backImage = (ImageView) actionbar.findViewById(R.id.imageView_actionbar_left);
		pauseButton = (ImageButton) findViewById(R.id.imageButton_pauses);
		backwardButton = (ImageButton) findViewById(R.id.imageButton_backward);
		forwardButton = (ImageButton) findViewById(R.id.imageButton_forward);
		musicSeekBar = (SeekBar) findViewById(R.id.seekBar_music);
		durationTextView = (TextView) findViewById(R.id.textView_total_time);
		timeTextView = (TextView) findViewById(R.id.textView_current_time);
		
		musicSeekBar.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		musicSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				touched = false;
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				touched = true;
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (touched) {
					Intent intent = new Intent();
					intent.setAction(IURL.ACTION_MUSIC_SEEK);
					intent.putExtra("seekProgress", progress);
					sendBroadcast(intent);
				}
			}
		});
	}

	private void addListener() {
		pauseButton.setOnClickListener(this);
		backwardButton.setOnClickListener(this);
		forwardButton.setOnClickListener(this);
		downloadImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButton_pauses:
			if (isPlay)
				pause();
			else
				play();
			break;
		case R.id.imageButton_backward:
			if (currentPosition > 0) {
				currentPosition -= 1;
			}
			music = musics.get(currentPosition);
			play();
			break;
		case R.id.imageButton_forward:
			if (currentPosition < musics.size()-1) {
				currentPosition += 1;
			}
			music = musics.get(currentPosition);
			play();
			break;
		case R.id.imageView_download:
			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(IURL.ROOT + music.getMusicpath()));
			//指定下载路径和下载文件名
			request.setDestinationInExternalPublicDir("/download/", music.getName()+".mp3");
			//获取下载管理器
			DownloadManager downloadManager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
			//将下载任务加入下载队列，否则不会进行下载
			downloadManager.enqueue(request);
		}
	}

	public class MyBroadcastReive extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (IURL.ACTION_MUSIC_SEEK_CHANGE.equals(action)) {
				int position = intent.getIntExtra("position", 0);
				int duration = intent.getIntExtra("duration", 0);
				timeTextView.setText(new SimpleDateFormat("mm:ss").format(position));
				durationTextView.setText(new SimpleDateFormat("mm:ss").format(duration));
				musicSeekBar.setMax(duration);
				musicSeekBar.setProgress(position);
			}
		}
		
	}
}
