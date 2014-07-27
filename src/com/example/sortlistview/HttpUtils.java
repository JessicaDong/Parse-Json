package com.example.sortlistview;

import java.io.ByteArrayOutputStream;  
import java.io.InputStream;  
import java.net.HttpURLConnection;  
import java.net.URL;  

import android.util.Log;
  
/** 
 * @author JessicaDong �ӷ����������ȡ��JSON���ݸ�ʽ���ַ��� 
 * ���ݸ�ʽʾ�� [ {
 *     "area": "������", 
 *     "server": [
 *         {"id": 21, "name": "���Է�"}
 *   ]
 *}, 
  {
       "area": "����Ե", 
       "server": [
           {"id": 41, "name": "�ٽ���"}, 
           {"id": 42, "name": "������"}, 
           {"id": 43, "name": "ˮ����"}, 
           {"id": 44, "name": "˫����"}, 
           {"id": 45, "name": "��˼��"}, 
           {"id": 46, "name": "�Ϫɳ"}
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
            connection.setConnectTimeout(3000); // ����ʱʱ��3s  
            connection.setRequestMethod("GET");  
            connection.setDoInput(true);  
            int code = connection.getResponseCode(); // ����״̬��  
            Log.i("code", code+"");
            if (code == 200) {  
                // ��õ�����������ʱ�������Ѿ������˷���˷��ػ�����JSON������,��ʱ��Ҫ�������ת�����ַ���  
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
            // inputStream�������õ�����д��ByteArrayOutputStream����,  
            // Ȼ��ͨ��outputStream.toByteArrayת���ֽ����飬��ͨ��new String()����һ���µ��ַ�����  
            jsonString = new String(outputStream.toByteArray());  
        } catch (Exception e) {  
            // TODO: handle exception  
        }  
        return jsonString;  
    }  
}  