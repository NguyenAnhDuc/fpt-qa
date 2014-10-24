package com.fpt.ruby.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import fpt.qa.type_mapper.TypeWithConfidentLevel;

@Document
public class TVProgram {
	@Id
	private String id;
	private String title;
	private String type;
	private List<TypeWithConfidentLevel> types;
	private Date start_date;
	private Date end_date;
	private String channel;
	
	public List<TypeWithConfidentLevel> getTypes() {
		return types;
	}
	public void setTypes(List<TypeWithConfidentLevel> types) {
		this.types = types;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	
	@Override
	public String toString() {
		return String.format("id = %s; title = %s; type = %s; start_date = %s; end_date = %s; channel= %s", id, title, type, start_date, end_date, channel);
	}
	
}
