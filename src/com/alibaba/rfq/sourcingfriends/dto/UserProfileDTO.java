package com.alibaba.rfq.sourcingfriends.dto;

import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;

public class UserProfileDTO {

	private int id;
	private String userName;
	private String passwd;
	private Bitmap photo;
	private String companyName;
	//private List<String> feedList;
	//private String latestFeed;
	//private Date latestTime;

	
	public UserProfileDTO(String name,String passwd,String company,Bitmap photo) {
		this.userName = name;
		this.passwd=passwd;
		this.companyName = company;
		this.photo = photo;
	}
	
	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public UserProfileDTO(int id,String name,String passwd,String company,Bitmap photo) {
		this.id = id;
		this.passwd=passwd;
		this.userName = name;
		this.companyName = company;
		this.photo = photo;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

//	public List<String> getFeedList() {
//		return feedList;
//	}
//
//	public void setFeedList(List<String> feedList) {
//		this.feedList = feedList;
//	}
//
//	public String getLatestFeed() {
//		return latestFeed;
//	}
//
//	public void setLatestFeed(String latestFeed) {
//		this.latestFeed = latestFeed;
//	}
//
//	public Date getLatestTime() {
//		return latestTime;
//	}
//
//	public void setLatestTime(Date latestTime) {
//		this.latestTime = latestTime;
//	}
}
