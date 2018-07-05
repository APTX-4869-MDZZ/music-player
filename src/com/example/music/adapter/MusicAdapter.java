package com.example.music.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.music.R;
import com.example.music.bean.Music;
import com.example.music.constant.IURL;
import com.example.music.manager.MyImageLoader;
import com.example.music.util.CircleImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MusicAdapter extends BaseAdapter {

	List<Music> musics = new ArrayList<Music>();
	Context context = null;

	public MusicAdapter(Context context) {
		super();
		this.context = context;
	}

	public void addMusics(List<Music> musics) {
		if (this.musics.size() == 0) {
			this.musics.addAll(musics);
			notifyDataSetChanged();
		}
	}
	
	@Override
	public int getCount() {
		return musics.size();
	}

	@Override
	public Object getItem(int position) {
		return musics.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 在显示是时候会同时开始创建充满一个屏幕（或+1）个item，这些item是通过并行线程同时开启的，故所有item进入getView的时候convertView都是null
		HolderView holder = null;
		if (convertView == null) {
			holder = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.music_list_item, null);
			holder.image = (CircleImageView) convertView.findViewById(R.id.imageView_music);
			holder.musicNameTextView = (TextView) convertView.findViewById(R.id.textView_music_name);
			holder.singerTextView = (TextView) convertView.findViewById(R.id.textView_singer);
			holder.wordsTextView = (TextView) convertView.findViewById(R.id.textView_music_words);
			holder.tuneTextView = (TextView) convertView.findViewById(R.id.textView_tune);
			holder.timeTextView = (TextView) convertView.findViewById(R.id.textView_time);
			
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}

		Music music = (Music) getItem(position);
		String albumpic = IURL.ROOT+music.getAlbumpic();
		MyImageLoader.setBitmapFromCache(context, holder.image, albumpic);
		holder.musicNameTextView.setText(music.getName());
		holder.singerTextView.setText(music.getSinger());
		holder.wordsTextView.setText(music.getAuthor());
		holder.tuneTextView.setText(music.getComposer());
		holder.timeTextView.setText(music.getDurationtime());
		return convertView;
	}

	public class HolderView {
		CircleImageView image;
		TextView musicNameTextView;
		TextView singerTextView;
		TextView wordsTextView;
		TextView tuneTextView;
		TextView timeTextView;
	}
}
