package com.rainy.quartz.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class QuartzNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        System.out.println("a");
    }
    
}
