package com.example.music.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.music.bean.Music;

public class MusicJSONParser {
	public static List<Music> getMusics(JSONObject jsonObj) throws JSONException {
		List<Music> musics = new ArrayList<Music>();
		String result = jsonObj.getString("result");
		if ("ok".equals(result)) {
			JSONArray jsonArray = jsonObj.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				Music music = Music.createMusicFromJSON(obj);
				musics.add(music);
			}
		}
		return musics;
	}
}
