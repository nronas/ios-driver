/*
 * Copyright 2012 ios-driver committers.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.uiautomation.ios.ide;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import org.uiautomation.ios.UIAModels.UIADriver;
import org.uiautomation.ios.client.uiamodels.impl.RemoteUIADriver;
import org.uiautomation.ios.exceptions.IOSAutomationException;
import org.uiautomation.ios.server.ExternalRequest;
import org.uiautomation.ios.server.application.IOSApplication;

public class Model {
  
  private RemoteUIADriver driver;
  private IOSApplication app;
  private String logSessionId = null;
  private JSONObject cache;
  private boolean logging = false;
  private boolean partLogging = false;


 
  public RemoteUIADriver getDriver() {
    return driver;
  }

  public void setDriver(RemoteUIADriver driver) throws IOSAutomationException {
    if (this.driver != null) {
      throw new IOSAutomationException("driver already instanciated.");
    }
    this.driver = driver;
  }

  public JSONObject getCache() {
    return cache;
  }

  public JSONObject getObjectTree() {
    JSONObject res =  cache.optJSONObject("tree");
    return res;
  }


  public void refresh() throws IOSAutomationException {
    cache = driver.logElementTree();
  }

  public InputStream getLastScreenshotInputStream() throws IOSAutomationException {
    try {
      File f = new File(cache.optString("path"));
      while (!f.exists()) {
        System.out.println("cannot find " + f+".Last screenshot cannot be found.");
        Thread.sleep(250);
      }
      InputStream is = new FileInputStream(f);
      return is;
    } catch (Exception e) {
      throw new IOSAutomationException(e);
    }


  }

  public void stop() throws IOSAutomationException {
    UIADriver d = getDriver();
    if (d == null) {
      throw new IOSAutomationException("driver not active.");
    } else {
      try {
        ExternalRequest.makeRequest("POST", "http://localhost:8181/session/" + getLogSessionId()+ "/log", null);
      } 
      catch (Exception e) {
      }
      finally{
        if(this.getApplication() != null && this.getApplication().getMessages().size() > 0){
          this.getApplication().getMessages().clear();
        }
        setLogging(false);
        setLogSessionId(null);
        driver.quit();
        driver = null;
      }
    }

  }

  public String getLanguage() {
    try {
      return cache.getString("language");
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return "English";
    }
  }

  public IOSApplication getApplication() {
    return app;
  }

  public void setAUT(IOSApplication app) {
    this.app = app;
    
  }

  public void setLogSessionId(String logSessionId){
    this.logSessionId = logSessionId;
  }
  
  public String getLogSessionId(){
    return this.logSessionId;
  }
  
  public void setLogging(boolean logging){
    this.logging = logging;
  }
  
  public boolean getLogging(){
    return this.logging;
  }
  
  public boolean getPartLogging(){
    return this.partLogging;
  }
  
  public void setPartLogging(boolean partLogging){
    this.partLogging = partLogging;
  }
}
