package com.example.music.service;

import com.example.music.constant.IURL;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {

	MediaPlayer player = null;
	myBroadcastReceiver receiver = null;
	int seekToTime = 0;
	boolean isPause = false;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("service", "service oncreate");
		player = new MediaPlayer();
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		player.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				player.start();
			}
		});
		
		player.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.stop();
				
			}
		});
		player.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Log.i("media", what+"");
				return true;
			}
		});
		
		receiver = new myBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(IURL.ACTION_MUSIC_PLAY);
		filter.addAction(IURL.ACTION_MUSIC_PAUSE);
		filter.addAction(IURL.ACTION_MUSIC_SEEK);
		registerReceiver(receiver, filter);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					if (player.isPlaying()) {
						int position = player.getCurrentPosition();
						int duration = player.getDuration();
						Intent intent = new Intent();
						intent.setAction(IURL.ACTION_MUSIC_SEEK_CHANGE);
						intent.putExtra("position", position);
						intent.putExtra("duration", duration);
						sendBroadcast(intent);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void play(String musicPath) {
		if (isPause) {
			// 由暂停状态切换到播放状态
			player.seekTo(seekToTime);
			player.start();
			seekToTime = 0;
		} else {
			player.reset();
			try {
				player.setDataSource(musicPath);
				player.prepareAsync();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void pause() {
		isPause = true;
		seekToTime = player.getCurrentPosition();
		player.pause();
	}
	
	private class myBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (IURL.ACTION_MUSIC_PLAY.equals(action)) {
				String musicPath = intent.getStringExtra("music_path");
				play(musicPath);
			}
			if (IURL.ACTION_MUSIC_PAUSE.equals(action)) {
				Log.i("seek", player.getCurrentPosition() + "");
				pause();
			}
			if (IURL.ACTION_MUSIC_SEEK.equals(action)) {
				int progress = intent.getIntExtra("seekProgress", 0);
				seekToTime = progress;
				if (player.isPlaying()) {
					isPause = true;
					play("");
					isPause = false;
				}
			}
		}
	}

}
