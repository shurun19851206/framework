package wtest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import wtest.service.TestC3P0Service;

public class TestC3P0 {
    
    private ApplicationContext ac = null;
    
    @Before
    public void setUp () {
        ac = new ClassPathXmlApplicationContext("classpath:application*.xml");
    }
    
    private TestC3P0Service testC3P0Service;
    
    public TestC3P0Service getTestC3P0Service() {
        return testC3P0Service;
    }

    public void setTestC3P0Service(TestC3P0Service testC3P0Service) {
        this.testC3P0Service = testC3P0Service;
    }

    @Test
    public void save () {
        testC3P0Service = (TestC3P0Service) ac.getBean("testC3P0Service");
        testC3P0Service.saveSlave();
        testC3P0Service.save();
    }

}
