package com.rainy.webdemo.mail;

import java.util.ResourceBundle;

import org.junit.Test;

import junit.framework.TestCase;

public class MailTest extends TestCase {
	
	@Test
	public void resourceBundleTest() {
		ResourceBundle.getBundle("com.gemship.config.GemshipServerConfig");
	}

}
