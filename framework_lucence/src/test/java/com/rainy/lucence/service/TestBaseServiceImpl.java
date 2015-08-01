package com.rainy.lucence.service;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;

import com.rainy.lucence.service.impl.BaseServiceImpl;

public class TestBaseServiceImpl extends TestBaseService {

	@Test
	public void testAddIndex () throws IOException, ParseException {
		IBaseService baseService = new BaseServiceImpl();
		baseService.addIndex();
	}
	
}
