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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.ClientProtocolException;
import org.uiautomation.ios.exceptions.IOSAutomationException;
import org.uiautomation.ios.ide.Model;
import org.uiautomation.ios.ide.views.DefaultView;
import org.uiautomation.ios.ide.views.View;
import org.uiautomation.ios.server.ExternalRequest;
import org.uiautomation.ios.server.servlet.Message;

public class NotImplementedIDEController extends BaseController {

  public NotImplementedIDEController(Model model) {
    super(model);
  }


  public boolean canHandle(String pathInfo) {
    return true;
  }


  public View handle(HttpServletRequest req) throws IOSAutomationException {
    if(req.getParameter("LoggingMsg") != null){
      getModel().getApplication().addMessage(new Message(req.getParameter("LoggingMsg"), req.getParameter("LoggingMsgType")));
      if(req.getParameter("logging") != null){
        getModel().setLogging(Boolean.parseBoolean(req.getParameter("logging")));
        getModel().setPartLogging(Boolean.parseBoolean(req.getParameter("logging")));
      }
      if(req.getParameter("partLogging") != null){
        getModel().setPartLogging(Boolean.parseBoolean(req.getParameter("partLogging")));
      }
    }
    if(req.getParameter("options") != null){
      getModel().setOptions(req.getParameter("options").split(","));
    }
    
    System.err.println("no controller for that " + req.getPathInfo());
    return new DefaultView(getModel());
  }

}
