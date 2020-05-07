package pl.clarin.geocoder.worker;

import com.rabbitmq.client.AMQP.BasicProperties;
import org.ini4j.Ini;
import pl.clarin.chronocorpus.Configuration;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tomasz Walkowiak
 */
public abstract class Worker extends Thread {

    private static final Logger LOGGER = Logger.getLogger(Worker.class.getName());

    private boolean isStopped = false;

    static boolean staticInitStarted = false;

    protected Connection connection;
    private Channel channel;
    private long lastTime;
    private BasicProperties replayProps;
    private String replayTo;
    private String id;
    private double lastProgress=0;
    
    final public void service_init(Ini init) throws Exception {
        Configuration.init(init);
        //tool inits
        if (!staticInitStarted) {
            staticInitStarted = true;

            static_init(init);
        }
        init();
    }

    final Object monitor = new Object();

    protected void updateProgress(double progress)  {
        if (System.currentTimeMillis()>lastTime+250  && progress>lastProgress)
        {   lastProgress=progress;
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("id", id);
            response.add("progress", progress);
            response.add("function", "progress");
            if (replayProps.getReplyTo() != null)
                        try {
                            channel.basicPublish("", replayTo, replayProps, response.build().toString().getBytes(StandardCharsets.UTF_8));
                        } catch (IOException ex) {
                            LOGGER.log(Level.WARNING, null, ex);
                    }
            lastTime=System.currentTimeMillis();
        }    
        
    }
    
    
    
    protected void initRabbit() throws IOException, TimeoutException {
        //Rabbit
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Configuration.HOST);
        factory.setPassword(Configuration.RABBIT_PASSWORD);
        factory.setUsername(Configuration.RABBIT_USER);

        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(Configuration.REQUEST_QUEUE_NAME, false, false, false, null);

        channel.basicQos(1);

        Consumer consumer = new DefaultConsumer(channel) {
           
            
            @Override
            public void handleDelivery(
                    String consumerTag,
                    Envelope envelope,
                    AMQP.BasicProperties properties,
                    byte[] body) throws IOException {


                BasicProperties replyProps = new BasicProperties.Builder().correlationId(properties.getCorrelationId()).build();
                JsonObjectBuilder response = Json.createObjectBuilder();
                String message = new String(body, StandardCharsets.UTF_8);
                try {

                    JsonReader reader = Json.createReader(new StringReader(message));
                    JsonObject input = reader.readObject();
                    //response.add("input", input);
                    id=input.getString("id");
                    response.add("id", input.get("id"));
                    long start = System.currentTimeMillis();
                    replayTo=properties.getReplyTo();
                    replayProps=replyProps;
                    JsonObject res = process(input);

                    long stop = System.currentTimeMillis();
                    LOGGER.log(Level.INFO, "Finish processing of task: " + properties.getCorrelationId() + " in " + (stop - start) / 1000.0 + "s.");
                    response.add("time", (stop - start) / 1000.0);
                    response.add("error", "");
                    response.add("result", res);
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Problems in task execution for :" + message,e);
                    response.add("error", e.getMessage());

                } finally {

                    if (properties.getReplyTo() != null)
                        channel.basicPublish("", properties.getReplyTo(), replyProps, response.build().toString().getBytes(StandardCharsets.UTF_8));
                    channel.basicAck(envelope.getDeliveryTag(), false);
                    synchronized (monitor) {
                        monitor.notify();
                    }

                }
            }
        };

        channel.basicConsume(Configuration.REQUEST_QUEUE_NAME, false, consumer);

    }

    final public void run() {
        try {
            LOGGER.log(Level.INFO, "Starting worker for corpus: " + Configuration.TOOL_NAME);
            initRabbit();
            while (!isStopped()) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ignore) {
                }
            }

            LOGGER.log(Level.INFO, "Finishing worker for tool: " + Configuration.TOOL_NAME);
        } catch (IOException | TimeoutException ex) {
            LOGGER.log(Level.SEVERE, "Problems with initialization of communication with Rabbit", ex);
        }
    }

    public synchronized void doStop() {
        isStopped = true;
        this.interrupt(); //break pool thread out of dequeue() call.
    }

    public synchronized boolean isStopped() {
        return isStopped;
    }

    abstract public void static_init(Ini init) throws Exception;

    abstract public void init() throws Exception;

    abstract public JsonObject process(JsonObject input) throws Exception;

}
