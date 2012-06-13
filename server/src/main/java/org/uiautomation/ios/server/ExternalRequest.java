package org.uiautomation.ios.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.uiautomation.ios.server.servlet.Message;
import org.uiautomation.ios.server.servlet.MessageList;

public class ExternalRequest {
  
  public static void makeRequest(String method, String url, MessageList msgList) throws ClientProtocolException, IOException{
    HttpClient client = new DefaultHttpClient();
    if(msgList != null){
      List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(2);
      if(method.equals("GET")){
        HttpGet get = new HttpGet(url);
      }
      else if(method.equals("POST")){
        HttpPost post = new HttpPost(url);
        HashMap<String, String> valuesHash = new HashMap<String, String>();
        for(Object msg : msgList.getMessages()){
          valuesHash.put("typeMsg",((Message) msg).getMessageType().toUpperCase());
          valuesHash.put("logMsg",((Message) msg).getMessageBody().toUpperCase());
        }
        for(String key : valuesHash.keySet()){
          nameValuePairs.add(new BasicNameValuePair(key, valuesHash.get(key)));
        }
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = client.execute(post);
      }
    }
    else{
      if(method.equals("GET")){
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
      }
      else if(method.equals("POST")){
        HttpPost post = new HttpPost(url);
        HttpResponse response = client.execute(post);
      }
    }
  }
}