package com.rainy.quartz.config;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public interface QuartzBeanDefinition {
    
    /**
     * 其他所有的job定义
     */
    // .....
    
    interface IParseHandler {
        void handle(Element element, ParserContext parserContext);
        String getTypeFlag();
    }

}
