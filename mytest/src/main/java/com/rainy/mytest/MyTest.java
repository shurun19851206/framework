package com.rainy.mytest;

import org.springframework.util.ObjectUtils;

public class MyTest {
	
	public static void main(String[] args) {
		A a = new A("123", "456");
		System.out.println(ObjectUtils.nullSafeToString(a));
		System.out.println(ObjectUtils.nullSafeToString(new String[]{"123", "467"}));
	}

}

class A {
	private String id;
	
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public A(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public A() {
		super();
	}
}