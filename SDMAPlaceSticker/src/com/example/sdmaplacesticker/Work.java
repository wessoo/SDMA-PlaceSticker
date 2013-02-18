package com.example.sdmaplacesticker;

import java.util.ArrayList;

public class Work {
	private ArrayList<String> title = new ArrayList<String>();
	private ArrayList<String> artist = new ArrayList<String>();
	private ArrayList<String> date = new ArrayList<String>();
	private ArrayList<String> place = new ArrayList<String>();
	private ArrayList<String> dimensions = new ArrayList<String>();
	private ArrayList<String> description = new ArrayList<String>();
	
	public ArrayList<String> getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title.add(title);
	}
	
	public ArrayList<String> getArtist() {
		return artist;
	}
	
	public void setArtist(String artist) {
		this.artist.add(artist);
	}
	
	public ArrayList<String> getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date.add(date);
	}
	
	public ArrayList<String> getPlace() {
		return place;
	}
	
	public void setPlace(String place) {
		this.place.add(place);
	}
	
	public ArrayList<String> getDimensions() {
		return dimensions;
	}
	
	public void setDimensions(String dimensions) {
		this.dimensions.add(dimensions);
	}
	
	public ArrayList<String> getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description.add(description);
	}
}
