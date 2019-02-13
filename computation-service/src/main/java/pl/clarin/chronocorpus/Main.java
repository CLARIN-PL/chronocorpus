/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronocorpus;


import java.util.logging.Logger;
import javax.json.JsonObject;
import org.ini4j.Ini;

import pl.clarin.chronocorpus.document.control.DocumentFileLoader;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.query.boundary.ConcordanceQuery;
import pl.clarin.chronocorpus.task.boundary.TaskLookUp;
import pl.clarin.chronocorpus.task.entity.Task;
import pl.clarin.chronocorpus.worker.Service;
import pl.clarin.chronocorpus.worker.Worker;

/**
 *
 * @author Tomasz Walkowiak
 */
public class Main extends Worker {
    private static final Logger LOGGER = Logger.getLogger(Worker.class.getName());
    private TaskLookUp lookup;
    
    @Override
    public void init() throws Exception
    {  lookup=new TaskLookUp();
    }
    
    @Override
    public JsonObject process(JsonObject input) throws Exception {
       
       
       Task task = lookup.getTask(input); 
       JsonObject res=null;
       if (task!=null)
            res = task.doTask();
       return res;
       
        
        
    }
    
    
    public static void main(String[] args) {
        System.out.println(new ConcordanceQuery().withBase("śnieg").getJsonString());
        new Service<>(Main.class);
    }

   @Override
    public void static_init(Ini init) throws Exception {
       
        if (DocumentStore.getInstance().hasNoStoredDocuments()) {
            try {
                DocumentStore.getInstance().setDocuments(DocumentFileLoader.getInstance().load());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
       
    }

            
    
}
