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
import org.uiautomation.ios.server.servlet.Message;
import org.uiautomation.ios.server.servlet.MessageList;

public class BeginController extends BaseController {

  private IOSServerConfiguration config;
  
  public BeginController(Model model,IOSServerConfiguration config) {
    super(model);
    this.config = config;
  }

  public boolean canHandle(String pathInfo) {
   return pathInfo.contains("begin");
  }
  

  @SuppressWarnings("finally")
  public View handle(HttpServletRequest req){   
    BeginView view = null;
 
    if(req.getParameter("LoggingMsg") != null){
      config.addMessage(new Message(req.getParameter("LoggingMsg"), req.getParameter("LoggingMsgType")));
      if(req.getParameter("logging") != null){
        getModel().setLogging(Boolean.parseBoolean(req.getParameter("logging")));
        getModel().setPartLogging(Boolean.parseBoolean(req.getParameter("logging")));
      }
    }
    try {
      view = new BeginView(req.getSession().getId(), getModel().getLogging(), config,config.getSupportedApps());
    } catch (IOSAutomationException e1) {
        // e.printStackTrace();
    }
    catch (Exception e2) {
      view = new BeginView(getModel().getLogSessionId(), config);
      //e2.printStackTrace();
    }
    finally{
      return view;
    }
  }
}
