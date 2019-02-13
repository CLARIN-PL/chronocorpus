package pl.clarin.chronocorpus.rest;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.File;
import java.io.IOException;

import java.util.concurrent.TimeoutException;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;



import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.ini4j.Ini;
import org.json.JSONObject;
import pl.clarin.chronocorpus.rest.resulter.Resulter;



@Singleton
@Path("")
public class RESTService {

    
    private Ini init;
    private Resulter resulter=new Resulter();
    
    
    @Context
    private ResourceContext resourceContext;
    private Connection connection;
    
    
    @POST
    @Consumes("application/json")
    @Path("/startTask")
    public String startTask(String data)  {
        long start = System.currentTimeMillis();
        JSONObject result=SendTask.send(data,connection,init);
        Logger.getLogger(RESTService.class.getName()).log(Level.DEBUG, "Starting in "+(System.currentTimeMillis() - start) / 1000.0 +"s");    
        if (result.has("id"))
        {   resulter.started(result.getString("id"));
            Logger.getLogger(RESTService.class.getName()).log(Level.DEBUG, "Task started "+result.getString("id"));
        }
        return result.toString();
    }

    @GET
    @Path("getStatus/{taskID}")
    public String getStatus(@PathParam("taskID") String taskID) {
        long start = System.currentTimeMillis();
        
        JSONObject res = resulter.status(taskID);
        if ((System.currentTimeMillis() - start) / 1000.0 > 0.1) {
        Logger.getLogger(RESTService.class.getName()).log(Level.DEBUG, "Status in "+(System.currentTimeMillis() - start) / 1000.0 +"s");    
        }
        
        return res.toString();
    }
    
    @GET
    @Path("getResult/{taskID}")
    public String getResult(@PathParam("taskID") String taskID) {
        long start = System.currentTimeMillis();
        JSONObject res = resulter.result(taskID);
        if ((System.currentTimeMillis() - start) / 1000.0 > 0.1) {
          Logger.getLogger(RESTService.class.getName()).log(Level.DEBUG, "Result in "+(System.currentTimeMillis() - start) / 1000.0 +"s");    
        }
        //base.close();
        return res.toString();
    }
         
    
    
    @GET
    @Path("/test")
    public String test() {
        return "Alive...";
    }

    
    
   

    
    private Connection initRabbit() throws IOException, TimeoutException {
        //Rabbit
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(init.get("service", "rabbit_host"));
        factory.setPassword(init.get("service", "rabbit_password"));
        factory.setUsername(init.get("service", "rabbit_user"));
        return factory.newConnection();

    }

    private void init() throws Exception {
        try {
            init = new Ini(new File("config.ini"));

        } catch (IOException ex) {
            Logger.getLogger(RESTService.class.getName()).log(Level.FATAL, "Problems with ini file", ex);
            return;
        }
        connection = initRabbit();
        
        resulter.start(connection, init.get("service","queue_prefix")+init.get("service","result"));
        
        Logger.getLogger(RESTApplication.class.getName()).log(Level.INFO, "Starting REST service");

    }
    
    public void close()
    {
        try {
            connection.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(RESTService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public RESTService() throws Exception {
        init();

    }

}
