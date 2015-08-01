package com.rainy.lucence.service;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

public interface IBaseService {
	
	/**
	 * 添加索引
	 */
	public void addIndex () throws IOException, ParseException;

}
