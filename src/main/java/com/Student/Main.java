
package com.Student;

import com.Student.config.WebConfig;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8081);
        tomcat.getConnector();

        Context context = tomcat.addContext(
                "",
                new File(".").getAbsolutePath()
        );

        AnnotationConfigWebApplicationContext springContext =
                new AnnotationConfigWebApplicationContext();

        springContext.register(WebConfig.class);

        DispatcherServlet dispatcher =
                new DispatcherServlet(springContext);

        Tomcat.addServlet(context, "dispatcher", dispatcher);
        context.addServletMappingDecoded("/", "dispatcher");

        tomcat.start();

        System.out.println("Student Management System is running!");
        System.out.println("Open http://localhost:8081/student/register");

        tomcat.getServer().await();
    }
}
