package com.example.sortlistview;

import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
  
import org.json.JSONArray;  
import org.json.JSONObject;  
  
import android.util.Log;


  
/** 
 * @author xukunhui 
 * 完成对从服务端请求获得的JSON数据的解析成指定的对象. 
 */  
public class JSONTools {  
  
    public JSONTools() {  
        // TODO Auto-generated constructor stub  
    }  
      
    public static List<Address> getAddresslists( String jsonString){  
        List<Address> list = new ArrayList<Address>();  
        try {  
           // JSONObject jsonObject = new JSONObject(jsonString);  
            //返回json的数组  
        	JSONArray jsonArray = new JSONArray(jsonString);
        	
        //    JSONArray jsonArray2 = jsonObject.getJSONArray(key);  
            for(int i = 0; i < jsonArray.length(); i++){  
            	Address address = new Address();
                JSONObject AddressjsonObject=jsonArray.getJSONObject(i);  
                address.setArea(AddressjsonObject.getString("area"));  
                JSONArray ServerjsonArray = AddressjsonObject.getJSONArray("server"); 
                Log.i("jsarray", ServerjsonArray.toString());
               
                Map<String,Integer> map1=new HashMap<String, Integer>();
                for(int j=0;j<ServerjsonArray.length();++j){
                	JSONObject jsonObject3=ServerjsonArray.getJSONObject(j);
                  //  id.add(jsonObject3.getInt("id"));
                	
                	map1.put(jsonObject3.getString("name"), jsonObject3.getInt("id"));
                	//name.add(j, jsonObject3.getString("name"));
                }
                address.setseverMap(map1);
                Log.i("area", address.getArea());
                list.add(address); 
                
            }  
        } catch (Exception e) {  
            // TODO: handle exception  
        	Log.i("lists", e.getMessage());
        }  
        return list;  
    }  
}  