package com.fpt.ruby.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
 
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
 
@Configuration
public class SpringMongoConfig extends AbstractMongoConfiguration {
 
	@Override
	public String getDatabaseName() {
		return "yourdb";
	}
 
	@Override
	@Bean
	public Mongo mongo() throws Exception {
		return new MongoClient("10.3.9.236");
		//return new MongoClient("localhost");
	}
}