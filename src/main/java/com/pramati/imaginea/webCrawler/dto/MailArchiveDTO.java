package com.pramati.imaginea.webCrawler.dto;

public class MailArchiveDTO {
	private String link;
	private String author;
	private String subject;
	private String date;
	private String fileName;
	private String dir;
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	@Override
	public String toString() {
		return "MailArchiveDTO [link=" + link + ", author=" + author + ", subject=" + subject + ", date=" + date
				+ ", fileName=" + fileName + ", dir=" + dir + "]";
	}
	
}
