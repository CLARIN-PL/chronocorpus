package pl.clarin.chronocorpus.worker;

import com.rabbitmq.client.*;
import com.rabbitmq.client.AMQP.BasicProperties;
import org.ini4j.Ini;

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

    protected String toolname;

    private boolean isStopped = false;

    static boolean staticInitStarted = false;

    protected Connection connection;
    private Channel channel;
    private String requestQueueName = "nlp_dummy";

    private String host;
    private String rabbit_user;
    private String rabbit_password;

    final public void service_init(Ini init) throws Exception {   //ini file 
        toolname = init.get("service", "tool");

        host = init.get("service", "rabbit_host");
        rabbit_user = init.get("service", "rabbit_user");
        rabbit_password = init.get("service", "rabbit_password");
        requestQueueName = init.get("service", "queue_prefix") + toolname;

//tool inits
        if (!staticInitStarted) {
            staticInitStarted = true;

            static_init(init);
        }
        init();

    }

    final Object monitor = new Object();

    protected void initRabbit() throws IOException, TimeoutException {
        //Rabbit
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPassword(rabbit_password);
        factory.setUsername(rabbit_user);

        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(requestQueueName, false, false, false, null);

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
                    response.add("id", input.get("id"));
                    long start = System.currentTimeMillis();
                    JsonObject res = process(input);

                    long stop = System.currentTimeMillis();
                    LOGGER.log(Level.INFO, "Finish processing of task: " + properties.getCorrelationId() + " in " + (stop - start) / 1000.0 + "s.");
                    response.add("time", (stop - start) / 1000.0);
                    response.add("error", "");
                    response.add("result", res);
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Problems in task execution for :" + message, e);
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

        channel.basicConsume(requestQueueName, false, consumer);

    }

    final public void run() {
        try {
            LOGGER.log(Level.INFO, "Starting worker for corpus: " + toolname);
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

            LOGGER.log(Level.INFO, "Finishing worker for tool: " + toolname);
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

    //
    abstract public void static_init(Ini init) throws Exception;

    abstract public void init() throws Exception;

    abstract public JsonObject process(JsonObject input) throws Exception;

}
