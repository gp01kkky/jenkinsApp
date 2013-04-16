package com.example.jenkinsapp;

//Project data
public class Data {
	
	private int date, time; 
	private String title; // build title
	private int status;// successful build?
	
	public Data(int date, int time, String title, int status)
	{
		this.date = date;
		this.time = time;
		this.title = title;
		this.status = status;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
