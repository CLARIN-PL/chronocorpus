package pl.clarin.chronocorpus.rest.resulter;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;


import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author twalkow
 */
public class Resulter implements Runnable{

    private Channel channel;
    private String resultQueueName;
    

    private final ConcurrentHashMap<String,Task>  taskMap= new ConcurrentHashMap<>();
    
    public Resulter()
    {
        
    }
    
    
    
    public void start(Connection con, String resultQueueName) throws Exception {
        this.resultQueueName=resultQueueName;
        channel = con.createChannel();
        
        channel.queueDeclare(resultQueueName, false, false, false, null);
        channel.basicQos(1);
        ///
        
        
        new Thread(this).start();
        
    }

    

    public void started(String id) {
        Task task = new Task(id,"");
        if (!taskMap.containsKey(id))
           taskMap.put(id, task);
    }
    
    
    private void processing(String id) {
        
        if (taskMap.containsKey(id))
            taskMap.get(id).setProcessing();
        else    
        { 
           Task task = new Task(id,"");
           taskMap.put(id, task);
        }
    }

    public String checkstatus(String id) {
        
        if (taskMap.containsKey(id))
           return taskMap.get(id).getStatus();
        else 
           return "UNKNOWN";
        
    } 
    
    public JSONObject status(String id) {
        JSONObject result = new JSONObject();
        result.put("id", id);
        if (taskMap.containsKey(id))
            result.put("status", taskMap.get(id).getStatus());
        else 
            result.put("status","UNKNOWN");
        Logger.getLogger(this.getClass().getName()).log(Level.DEBUG, "taskMap size " + taskMap.size());
        return result;

    }

    public JSONObject result(String id) {
        JSONObject result = new JSONObject();
        result.put("id", id);
        if (!taskMap.containsKey(id))
         result.put("error","Not existing task");
        else
        { Task task = taskMap.remove(id);
          
            switch (task.getStatus()) {
                case "ERROR":
                    result=task.getValue();
                    break;
                case "DONE":
                    result=task.getValue();
                    break;
                default:
                    result.put("error", "Not finished task");
                    break;
            }
              
        }
        return result;

    }

    private void finished(JSONObject input) {
        String id = input.getString("id");
        input.remove("function");
        Logger.getLogger(Resulter.class.getName()).log(Level.DEBUG, "Task "+ id+" finished"); 
        if (!taskMap.containsKey(id))
        {   Logger.getLogger(Resulter.class.getName()).log(Level.FATAL, "Task "+ id+" not started");    
             return;
        }
        Task task = taskMap.get(id);
        if (input.has("error") && !input.getString("error").isEmpty())
            task.setError(input);
        else
             task.setDone(input);
        
    }

    private JSONObject process(JSONObject input) {
        
        JSONObject result = new JSONObject();
        String id = input.getString("id");
       
        String function="";
        result.put("id", id);
        if (input.has("function"))
        {    
        function = input.getString("function");
        if (function.equals("started")) {
            started(id);
        }
        if (function.equals("processing")) {
            processing(id);
        }
        if (function.equals("status")) {
            result= status(id);
        }
        if (function.equals("result")) {
            result=result(id);
        }
        if (function.equals("finished")) {
            finished(input);
        }
        
        if (function.equals("error")) {
            finished(input);
        }
        }
        else
            finished(input);
        
        
        
        return result;
    }

    public void run() {
        Logger.getLogger(Resulter.class.getName()).log(Level.INFO, "Starting resulter");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                JSONObject response = new JSONObject();
                String message = new String(body, StandardCharsets.UTF_8);
                //Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Received:" + message);
                try {
                    response = process(new JSONObject(message));
                    
                } catch (Exception e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.ERROR, "Problems in task execution: " + message, e);
                    response.put("error", e.getMessage());
                } finally {
                    if (properties.getReplyTo() != null) { // for RPC
                        AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId()).build();
                        channel.basicPublish("", properties.getReplyTo(), replyProps, response.toString().getBytes(StandardCharsets.UTF_8));
                    }
                    channel.basicAck(envelope.getDeliveryTag(), false);
                    // RabbitMq consumer worker thread notifies the RPC server owner thread 
                    synchronized (this) {
                        this.notify();
                    }
                }
            }
        };
        try {
            channel.basicConsume(resultQueueName, false, consumer);
            // Wait and be prepared to consume the message from client.
            while (true) {
                synchronized (consumer) {
                    try {
                        consumer.wait();
                    } catch (InterruptedException e) {
                        Logger.getLogger(this.getClass().getName()).log(Level.FATAL, "Problems in task execution ", e);
                    }
                }
            }
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.FATAL, "Problems in task execution ", e);
        }
    }

    

}
