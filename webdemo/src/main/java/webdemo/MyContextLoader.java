package webdemo;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyContextLoader implements ServletContextListener{

    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println(sce.getSource());
        System.out.println(sce.getServletContext());
    }

    public void contextInitialized(ServletContextEvent sce) {
        System.out.println(sce.getSource());
        System.out.println(sce.getServletContext());
    }

}
