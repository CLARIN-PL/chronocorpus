/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronocorpus.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import pl.clarin.chronocorpus.rest.pagination.Storage;
import pl.clarin.chronocorpus.rest.resulter.Resulter;

/**
 *
 * @author twalkow
 */
public class Frequency {

    private final HashMap<String, String> taskDescr = new HashMap<String, String>() {
        {
            put("frequency_by_base", "{\"task_type\":\"frequency\",\"metadata_filter\":[],\"query_parameters\":[{\"name\":\"count_by_base\",\"value\":\"true\"}],\"response_parameters\":[]}");
            put("frequency_by_orth", "{\"task_type\":\"frequency\",\"metadata_filter\":[],\"query_parameters\":[{\"name\":\"count_by_base\",\"value\":\"false\"}],\"response_parameters\":[]}");
        }
    };

    private final HashMap<String, String> tasks = new HashMap<>();
    private final HashMap<String, Storage> storages = new HashMap<>();
    
    
    
    private final Resulter resulter;
    

    public Frequency(Resulter resulter) {
        this.resulter = resulter;
    
    }

    private String toId(String corpus,String taskID)
    {
        return corpus+"_"+taskID;
    }
    public JSONObject get(String corpus,String task, int page, int size) {
        String taskID=toId(corpus, task);
        JSONObject result = new JSONObject();
        result.put("id", taskID);
        result.put("error", "");
        //unknown 
        if (taskDescr.get(task) == null) {
            result.put("error", "Not existing task");
            return result;
        }

        //not finished
        if (storages.get(taskID) == null) {
            String resulterID = tasks.get(taskID);

            //if finished then get data from resulter
            if (resulter.checkstatus(resulterID).equals("DONE")) {
                JSONObject res = resulter.result(resulterID);
                if (res != null && res.has("result") && res.getJSONObject("result").has("rows")) {
                    storages.put(taskID, new Storage(res.getJSONObject("result").getJSONArray("rows")));
                    //tasks.remove(resulterID);
                    
                } else {
                    result.put("error", "Problems with task execution");
                    return result;
                }

            } else {
                String status = resulter.checkstatus(resulterID);
                result.put("status", status);
                return result;
            }
        }

        result.put("status", "DONE");

        result.put("result", storages.get(taskID).getPages(page, size));
        return result;

    }

    public String getTeskDescription(String corpus,String taskID) {
        String task=taskDescr.get(taskID);
        ///
        JSONObject result=new JSONObject(task);
        result.put("corpus", corpus);
        
        return result.toString();
    }
    
    //true = not yet processed ?
    public boolean doprocess(String corpus,String task) {
        String taskID=toId(corpus, task);
        if (taskDescr.get(task) == null) {
            return false;
        }
        return !tasks.containsKey(taskID);
    }

    public void started(String corpus,String task, String taskerID) {
        String taskID=toId(corpus, task);
        tasks.put(taskID, taskerID);
    }

    public byte[] toXLSX(String corpus,String task) 
    {   String taskID=toId(corpus, task);
        Storage storage=storages.get(taskID);
        if (storage==null)
            return null;
        byte [] res=null;
        
        try {
            res= new JSON2XLSX().process(storage.get());
        } catch (IOException ex) {
            Logger.getLogger(Frequency.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
}
