package com.pramati.imaginea.webCrawler.dto;

public class MailArchivesMonthlyDTO {
	private String id;
	private String month;
	private String year;
	private int msgCount;
	private String link;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getMsgCount() {
		return msgCount;
	}
	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	@Override
	public String toString() {
		return "MailArchivesMonthlyDTO [id=" + id + ", month=" + month + ", year=" + year + ", msgCount=" + msgCount
				+ ", link=" + link + "]";
	}
	
	
}
