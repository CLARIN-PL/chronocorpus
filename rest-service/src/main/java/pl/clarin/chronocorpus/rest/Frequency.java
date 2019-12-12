/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronocorpus.rest;

import java.io.IOException;
import java.util.HashMap;
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

    Frequency(Resulter resulter) {
        this.resulter = resulter;
    }

    JSONObject get(String taskID, int page, int size) {
        JSONObject result = new JSONObject();
        result.put("id", taskID);
        result.put("error", "");
        //unknown 
        if (taskDescr.get(taskID) == null) {
            result.put("error", "Not existing task");
            return result;
        }

        //not started
        if (storages.get(taskID) == null) {
            String resulterID = tasks.get(taskID);

            if (resulter.checkstatus(resulterID).equals("DONE")) {
                JSONObject res = resulter.result(resulterID);
                if (res != null && res.has("result") && res.getJSONObject("result").has("rows")) {
                    storages.put(taskID, new Storage(res.getJSONObject("result").getJSONArray("rows")));
                    tasks.remove(resulterID);
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

    String getData(String taskID) {
        return taskDescr.get(taskID);
    }

    boolean doprocess(String taskID) {
        if (taskDescr.get(taskID) == null) {
            return false;
        }
        return !tasks.containsKey(taskID);
    }

    void started(String taskID, String taskerID) {
        tasks.put(taskID, taskerID);
    }

    byte[] toXLSX(String taskID) throws IOException
    {   Storage storage=storages.get(taskID);
        if (storage==null)
            return null;
        return new JSON2XLSX().process(storage.get());
    }
    
}
