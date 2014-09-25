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
<<<<<<< HEAD
//		return new MongoClient("localhost");
=======
		//return new MongoClient("localhost");
>>>>>>> 33f56135217f1e611b9b306d61e76df3e80fc92a
	}
}