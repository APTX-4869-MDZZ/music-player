package com.example.music;

import java.util.ArrayList;
import java.util.List;

import com.example.music.adapter.MusicAdapter;
import com.example.music.bean.Music;
import com.example.music.constant.IURL;
import com.example.music.manager.MusicHttpManager;
import com.example.music.manager.MusicHttpManager.MusicLoadEndListener;
import com.example.music.service.MusicService;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MusicActivity extends BaseActivity implements MusicLoadEndListener{

	ListView musicListView = null;
	List<Music> musics = null;
	MusicAdapter adapter = null;
	ProgressDialog progressDidalog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_music);
		initialUI();
		MusicHttpManager.asyncLoadMusics(IURL.MUSIC_URL, this);
	}

	@Override
	protected void initialUI() {
		actionbar = (LinearLayout) findViewById(R.id.header_actionbar);
		initialActionBar(-1, "音乐列表", -1);
		
		musicListView = (ListView) findViewById(R.id.listView_musiclist);
		adapter = new MusicAdapter(this);
		musicListView.setAdapter(adapter);
		musicListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Music music = musics.get(position);
				Intent intent = new Intent(MusicActivity.this, MusicPlayActivity.class);
				intent.putExtra("currentPosition", position);
				intent.putExtra("musics", (ArrayList<Music>)musics);
				startActivity(intent);
			}
		});
		
		progressDidalog = ProgressDialog.show(this, "系统提示", "音乐数据加载中");
	}

	@Override
	public void onMusicsLoadEnd(final List<Music> musics) {
		this.musics = musics;	
		adapter.addMusics(musics);
		progressDidalog.dismiss();
		
		Intent intent = new Intent(this, MusicService.class);
		startService(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
