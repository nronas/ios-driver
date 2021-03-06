package org.uiautomation.ios.server.grid;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.uiautomation.ios.IOSCapabilities;
import org.uiautomation.ios.exceptions.IOSAutomationException;

public class RegistrationRequest {

  private String hubURL;
  private String nodeHost;
  private String appAbsolutePath;

  private List<Map<String, Object>> capabilities = new ArrayList<Map<String, Object>>();
  private Map<String, Object> configuration = new HashMap<String, Object>();


  public RegistrationRequest(String hub, String nodeHost,int port, String appAbsolutePath) {
    this.hubURL = hub;
    this.nodeHost = nodeHost;
    this.appAbsolutePath = appAbsolutePath;

    IOSCapabilities cap = IOSCapabilities.iphone(appAbsolutePath);
    cap.setCapability("browserName", "IOS Simulator");
    capabilities.add(cap.getRawCapabilities());


    configuration.put("remoteHost", "http://" + nodeHost + ":"+port);
    configuration.put("maxSession", 1);
  }

  public void registerToHub() {

    HttpClient client = new DefaultHttpClient();
    try {
      URL registration = new URL(hubURL);


      BasicHttpEntityEnclosingRequest r =
          new BasicHttpEntityEnclosingRequest("POST", registration.toExternalForm());
      
      System.out.println(getJSONRequest().toString(2));
      
      String json = getJSONRequest().toString();
      
      r.setEntity(new StringEntity(json));

      HttpHost host = new HttpHost(registration.getHost(), registration.getPort());
      HttpResponse response = client.execute(host, r);
      if (response.getStatusLine().getStatusCode() != 200) {
        throw new RuntimeException("Error sending the registration request.");
      }
    } catch (Exception e) {
      throw new IOSAutomationException("Error sending the registration request.", e);
    }

  }

  private JSONObject getJSONRequest() {
    JSONObject res = new JSONObject();
    try {
      res.put("class", getClass().getCanonicalName());
      res.put("configuration", configuration);
      JSONArray caps = new JSONArray();
      for (Map<String, Object> c : capabilities) {
        caps.put(c);
      }
      res.put("capabilities", caps);
    } catch (JSONException e) {
      throw new RuntimeException("Error encoding to JSON " + e.getMessage(), e);
    }

    return res;
  }
}
