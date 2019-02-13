/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronocorpus.rest;

import com.rabbitmq.client.AMQP;
import java.io.IOException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.ini4j.Ini;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author twalkow
 */
public class SendTask {

    private final String replyQueueName;
    private final String defaultQueue;
    private final Channel channel;
    

    public static JSONObject send(String data, Connection _conn, Ini init) {
        JSONObject result = new JSONObject();
        Channel channel = null;
        try {
            channel = _conn.createChannel();
            SendTask s = new SendTask(init, channel);
            result = s.send(data);

        } catch (Exception e) {
            result.put("error", "Error: " + e.getMessage());
            Logger.getLogger(SendTask.class.getName()).log(Level.WARN, "Error " + e.getMessage());
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (Exception e) {
                }
            }

        }   

        return result;
    }
    private final String queuePrefix;

    private SendTask(Ini init, Channel _channel) throws IOException {
        channel = _channel;
        queuePrefix = init.get("service", "queue_prefix");
        replyQueueName = queuePrefix + init.get("service", "result");
        defaultQueue=queuePrefix + init.get("service", "default");
                //channel.queueDeclare(taskQueueName, false, false, false, null);
        //channel.basicQos(1);
    }

    public JSONObject send(String data) {
        //Logger.getLogger(SendTask.class.getName()).log(Level.DEBUG, "Start sending a task: "+data); 
        JSONObject result = new JSONObject();
        JSONObject input;
        String taskQueueName = queuePrefix;
        try {
            input = new JSONObject(data);

            String error = checkInput(input);

            if (error != null && !error.isEmpty()) {
                Logger.getLogger(SendTask.class.getName()).log(Level.INFO, "Error: " + error);
                result.put("error", error);
                return result;
            }

            if (input.has("corpus")) {
                taskQueueName = taskQueueName + input.getString("corpus");
            } else {
                taskQueueName = defaultQueue;
            }
        } catch (JSONException e) {
            result.put("error", "JSON required as an input");
            Logger.getLogger(RESTService.class.getName()).log(Level.INFO, "Wrong input JSON" + e.getMessage(), e);
            return result;
        }

        String id = UUID.randomUUID().toString();
        input.put("id", id);
        result.put("id", id);
        String message = input.toString();
        try {

            try {
            AMQP.Queue.DeclareOk ok = channel.queueDeclarePassive(taskQueueName);

        } catch (IOException io) {
            Logger.getLogger(RESTService.class.getName()).log(Level.ERROR, "Non existing queue");
            
        }
            
            
            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .correlationId(id)
                    .replyTo(replyQueueName)
                    .build();
            
            channel.basicPublish("", taskQueueName, props, message.getBytes("UTF-8"));
            
            Logger.getLogger(RESTService.class.getName()).log(Level.INFO, "Sending to "+taskQueueName+ " "+message);
            
        } catch (Exception e) {
            result.put("error", "Internal service problem");
            Logger.getLogger(RESTService.class.getName()).log(Level.FATAL, e);

        }
        return result;
    }

    private String checkInput(JSONObject input) {
        return "";
    }

   

}
