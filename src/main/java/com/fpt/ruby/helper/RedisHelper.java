package com.fpt.ruby.helper;

import com.fpt.ruby.model.QuestionStructure;

import redis.clients.jedis.Jedis;

public class RedisHelper {
	private static final String HEAD_KEY = "head";
	private static final String MODIFIERS_KEY = "modifiers";
	public static String getHeadKey(String key){
		return key + ":" + HEAD_KEY;
	}
	public static String getModifiersKey(String key){
		return key + ":" + MODIFIERS_KEY;
	}
	
	
}
