/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronocorpus.rest;





import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.apache.log4j.RollingFileAppender;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.ini4j.Ini;




public class RESTApplication {
    private Ini init;
    
 
    
    public void start()
    {
        
        try {
            init = new Ini(new File("config.ini"));
        } catch (IOException ex) {
            Logger.getLogger(RESTApplication.class.getName()).log(Level.FATAL, "Problems with init file", ex);
            return;
        }
        initlogging();
        
        URI baseUri = UriBuilder.fromUri(init.get("service", "uri")).port(init.get("service", "port", Integer.class)).build();
        ResourceConfig config = new ResourceConfig(RESTService.class);
        config.register(CORSResponseFilter.class);
        Logger.getLogger(RESTApplication.class.getName()).log(Level.INFO, "Starting HTTP server");
        HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
        
    }
    public  void initlogging() {
        ConsoleAppender console = new ConsoleAppender(); //create appender
        Level level = Level.DEBUG;
        
        if (init.get("service").containsKey("logging"))
           level=Level.toLevel(init.get("service", "logging"));
        
        //configure the appender
        String PATTERN = "%d [%p|%c|%C{1}] %m%n";
        console.setLayout(new PatternLayout(PATTERN));
        console.setThreshold(level);
        
        console.activateOptions();
        //add appender to any Logger (here is root)
        Logger.getRootLogger().addAppender(console);
        
        
        RollingFileAppender fa=new RollingFileAppender();
        //FileAppender fa = new FileAppender();
        fa.setName("FileLogger");
        fa.setFile("service.log");
        fa.setMaximumFileSize(1000000);
        fa.setMaxBackupIndex(10);
        fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
        fa.setThreshold(level);
        fa.setAppend(true);
        fa.activateOptions();

        //add appender to any Logger (here is root)
        Logger.getRootLogger().addAppender(fa);
    }
    
   
    
   
    
    public static void main(String[] args) throws Exception {
        
    
    RESTApplication app=new RESTApplication();
    app.start();
 
    
    
    }
}
