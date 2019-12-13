/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronocorpus.rest.pagination;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author twalkow
 */
public class Storage {
    
    
    private JSONArray data;
    
    public JSONArray get()
    {
        return data;
    }
    
    public JSONObject getPages(int page,int size)
    {
        JSONObject result=new JSONObject();
        JSONArray rows=new JSONArray();
        for (int i=page*size;i<Math.min(data.length(),page*size+size);i++)
           rows.put(data.get(i));
        result.put("page",page);
        result.put("size",size);
        result.put("numberOfPages", (data.length()+1)/size+1);
                
        result.put("rows", rows);
        return result;
    }
    
    public Storage(JSONArray data)
    {
        this.data=data;
    }
    
    public Storage(String file)
    {
        
    }
    
    
    
}

