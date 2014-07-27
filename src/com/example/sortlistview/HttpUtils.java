package com.example.sortlistview;

import java.io.ByteArrayOutputStream;  
import java.io.InputStream;  
import java.net.HttpURLConnection;  
import java.net.URL;  

import android.util.Log;
  
/** 
 * @author JessicaDong 从服务器请求获取到JSON数据格式的字符串 
 * 数据格式示例 [ {
 *     "area": "测试区", 
 *     "server": [
 *         {"id": 21, "name": "测试服"}
 *   ]
 *}, 
  {
       "area": "镜花缘", 
       "server": [
           {"id": 41, "name": "临江仙"}, 
           {"id": 42, "name": "江城子"}, 
           {"id": 43, "name": "水龙吟"}, 
           {"id": 44, "name": "双飞燕"}, 
           {"id": 45, "name": "相思引"}, 
           {"id": 46, "name": "浣溪沙"}
       ]
  }
  ] 
 */  
public class HttpUtils {  
  
    public HttpUtils() {  
        // TODO Auto-generated constructor stub  
    }  
  
    public static String getJsonContent(String url_path) {  
        try {  
            URL url = new URL(url_path);  
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
            connection.setConnectTimeout(3000); // 请求超时时间3s  
            connection.setRequestMethod("GET");  
            connection.setDoInput(true);  
            int code = connection.getResponseCode(); // 返回状态码  
            Log.i("code", code+"");
            if (code == 200) {  
                // 或得到输入流，此时流里面已经包含了服务端返回回来的JSON数据了,此时需要将这个流转换成字符串  
                return changeInputStream(connection.getInputStream());  
            }  
         } catch (Exception e) {  
            // TODO: handle exception 
        //	Log.i("e", e.getMessage());
        }  
        return "";  
    }  
  
    private static String changeInputStream(InputStream inputStream) {  
        // TODO Auto-generated method stub  
        String jsonString = "";  
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();  
        int length = 0;  
        byte[] data = new byte[1024];  
        try {  
            while (-1 != (length = inputStream.read(data))) {  
            	Log.i("length",length+"");
                outputStream.write(data, 0, length);  
            }  
            // inputStream流里面拿到数据写到ByteArrayOutputStream里面,  
            // 然后通过outputStream.toByteArray转换字节数组，再通过new String()构建一个新的字符串。  
            jsonString = new String(outputStream.toByteArray());  
        } catch (Exception e) {  
            // TODO: handle exception  
        }  
        return jsonString;  
    }  
}  