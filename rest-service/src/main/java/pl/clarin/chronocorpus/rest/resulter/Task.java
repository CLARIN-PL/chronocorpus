/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronocorpus.rest.resulter;

import java.io.Serializable;
import java.util.Date;
import org.json.JSONObject;


/**
 *
 * @author twalkow
 */
public class Task  {
     
    private String id;
    java.sql.Timestamp startTime=new java.sql.Timestamp(new Date().getTime());   
    java.sql.Timestamp finishTime=new java.sql.Timestamp(new Date().getTime());
    private String task="empty";
    private String status="";
    private JSONObject value=new JSONObject();
    
    private double progress=0;
    
    public double  getProgress()
    { return progress;
        
    }
    public void updateProgress(double p)
    {
        progress=p;
    }
    
    public String getStatus()
    {
        return status;
    }
    public JSONObject getValue()
    {
        return value;
    }
   
    public Task(String _id,String _task)
    {
        startTime=new java.sql.Timestamp(new Date().getTime());
        task=_task;
        id=_id;
        status="QUEUE";
       
    }

  

 
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    void setStarted()
    {  this.status="QUEUEING";
       
    }
    
    void setError(JSONObject v)
    {  this.status="ERROR";
       this.value=v;
       this.finishTime=new java.sql.Timestamp(new Date().getTime());
    }
    void setProcessing()
    {  this.status="PROCESSING";
       
    }
    void setDone(JSONObject v) {
       this.status="DONE";
       this.finishTime=new java.sql.Timestamp(new Date().getTime());
       double diff=(finishTime.getTime()-startTime.getTime())/1000.0;
       v.put("Totaltime", diff);
       this.value=v;
       
    }

    
    
}
