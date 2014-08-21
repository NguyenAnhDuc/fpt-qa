package com.fpt.ruby.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "Spoken_Language")
public class SpokenLanguage {
	@Id
	@Column (name = "ISO_639_1", nullable = false)
	private String iso_639_1;

	@Column(name = "NAME", nullable = false)
	private String name;

	
	public String getIso_639_1() {
		return iso_639_1;
	}

	public void setIso_639_1(String iso_639_1) {
		this.iso_639_1 = iso_639_1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
