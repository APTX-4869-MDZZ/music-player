package com.example.music.constant;

public interface IURL {
	String ROOT = "http://192.168.43.129:8080/MusicServer/";
	String MUSIC_URL = ROOT+"loadMyMusics.jsp";
	
	String ACTION_MUSIC_PLAY = "come.example.music.MUSIC_PLAY";
	String ACTION_MUSIC_PAUSE = "come.example.music.MUSIC_PAUSE";
	String ACTION_MUSIC_SEEK = "come.example.music.MUSIC_SEEK";
	String ACTION_MUSIC_SEEK_CHANGE = "come.example.music.MUSIC_SEEK_CHANGE";
}
