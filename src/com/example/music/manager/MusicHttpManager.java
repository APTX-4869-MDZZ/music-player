package com.example.music.manager;


import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.music.bean.Music;
import com.example.music.util.MusicJSONParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.util.Log;

public class MusicHttpManager {
	public static MusicLoadEndListener listener;

	public static void asyncLoadMusics(String path, MusicLoadEndListener musicListener) {
		listener = musicListener;
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.get(path, new MusicListener());
	}
	
	public static class MusicListener extends JsonHttpResponseHandler {

		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			try {
				List<Music> musics = MusicJSONParser.getMusics(response);
				listener.onMusicsLoadEnd(musics);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			super.onFailure(statusCode, headers, responseString, throwable);
			Log.i("http", statusCode+":"+responseString);
		}
	}
	
	public interface MusicLoadEndListener {
		public void onMusicsLoadEnd(List<Music> musics);
	}
}
