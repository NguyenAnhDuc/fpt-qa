package com.fpt.ruby.service;

import org.springframework.stereotype.Service;

@Service
public class PersonService {
	public String name;
	public PersonService(){
		name = "hello";
	}
}
