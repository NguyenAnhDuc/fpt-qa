package com.fpt.ruby.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Cinema{
	@Id
	private String id;
	private String name;
	private String address;
	private String mobile;
	public String getId() {
		return id;
	}
	public void setId( String id ) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress( String address ) {
		this.address = address;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile( String mobile ) {
		this.mobile = mobile;
	}
	
	
}