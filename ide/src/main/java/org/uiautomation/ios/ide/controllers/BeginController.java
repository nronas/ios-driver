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
package org.uiautomation.ios.ide.controllers;

import javax.servlet.http.HttpServletRequest;

import org.uiautomation.ios.exceptions.IOSAutomationException;
import org.uiautomation.ios.ide.Model;
import org.uiautomation.ios.ide.views.BeginView;
import org.uiautomation.ios.ide.views.View;
import org.uiautomation.ios.server.IOSServerConfiguration;
import org.uiautomation.ios.server.servlet.CustomMessage;

public class BeginController extends BaseController {

  private IOSServerConfiguration config;
  
  public BeginController(Model model,IOSServerConfiguration config) {
    super(model);
    this.config = config;
  }

  public boolean canHandle(String pathInfo) {
   return pathInfo.contains("begin");
  }
  

  public View handle(HttpServletRequest req){
    BeginView view = null;    
    try {
      if(req.getAttribute("loggingResponse") != null){
        String logging_info = (String) req.getAttribute("loggingResponse");
      }
      view = new BeginView(new CustomMessage("Successfully load the resources!", "notice"), config.getSupportedApps());
    } catch (IOSAutomationException e1) {
        // e.printStackTrace();
    } catch (CustomMessage ex) {
      view = new BeginView(ex);
    } catch (Exception e2) {
        // TODO Auto-generated catch block
      e2.printStackTrace();
    }
    return view;
  }
}
