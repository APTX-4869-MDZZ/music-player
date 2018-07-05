package com.example.music.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Music implements Serializable{
	
	String album = null;
	String albumpic = null;
	String author = null;
	String composer = null;
	String downcount = null;
	String durationtime = null;
	String favcount = null;
	int id;
	String musicpath = null;
	String name = null;
	String singer = null;
	public Music(String album, String albumpic, String author, String composer, String downcount, String durationtime,
			String favcount, int id, String musicpath, String name, String singer) {
		super();
		this.album = album;
		this.albumpic = albumpic;
		this.author = author;
		this.composer = composer;
		this.downcount = downcount;
		this.durationtime = durationtime;
		this.favcount = favcount;
		this.id = id;
		this.musicpath = musicpath;
		this.name = name;
		this.singer = singer;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getAlbumpic() {
		return albumpic;
	}
	public void setAlbumpic(String albumpic) {
		this.albumpic = albumpic;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getComposer() {
		return composer;
	}
	public void setComposer(String composer) {
		this.composer = composer;
	}
	public String getDowncount() {
		return downcount;
	}
	public void setDowncount(String downcount) {
		this.downcount = downcount;
	}
	public String getDurationtime() {
		return durationtime;
	}
	public void setDurationtime(String durationtime) {
		this.durationtime = durationtime;
	}
	public String getFavcount() {
		return favcount;
	}
	public void setFavcount(String favcount) {
		this.favcount = favcount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMusicpath() {
		return musicpath;
	}
	public void setMusicpath(String musicpath) {
		this.musicpath = musicpath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	
	public static Music createMusicFromJSON(JSONObject jsonobj) throws JSONException{
		String album = jsonobj.getString("album");
		String albumpic = jsonobj.getString("albumpic");
		String author = jsonobj.getString("author");
		String composer = jsonobj.getString("composer");
		String downcount = jsonobj.getString("downcount");
		String durationtime = jsonobj.getString("durationtime");
		String favcount = jsonobj.getString("favcount");
		int id = jsonobj.getInt("id");
		String musicpath = jsonobj.getString("musicpath");
		String name = jsonobj.getString("name");
		String singer = jsonobj.getString("singer");
		return new Music(album, albumpic, author, composer, downcount, durationtime,
				favcount, id, musicpath, name, singer);
	}
	
	@Override
	public String toString() {
		return "Music [album=" + album + ", albumpic=" + albumpic + ", author=" + author + ", composer=" + composer
				+ ", downcount=" + downcount + ", durationtime=" + durationtime + ", favcount=" + favcount + ", id="
				+ id + ", musicpath=" + musicpath + ", name=" + name + ", singer=" + singer + "]";
	}
	
}
