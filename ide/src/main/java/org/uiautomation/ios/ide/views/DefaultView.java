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
package org.uiautomation.ios.ide.views;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.uiautomation.ios.exceptions.IOSAutomationException;
import org.uiautomation.ios.ide.Model;
import org.uiautomation.ios.server.servlet.Message;

public class DefaultView implements View {

  private final Model model;
  
  public DefaultView(Model model) {
    this.model = model;
  }

  public void render(HttpServletResponse response) {
    try {
      StringBuilder b = new StringBuilder();
      b.append("<html>");
      b.append("<head>");

      b.append(" <link rel='stylesheet' href='" + getResource("ide.css") + "'  type='text/css'/>");
      b.append("<script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js'></script>");
      b.append("<script src='" + getResource("iphone-style-checkboxes.js") + "' type='text/javascript'></script>");
      b.append("<link rel='stylesheet' href='" + getResource("style.css") + "' type='text/css' media='screen' />");
      b.append("<script type='text/javascript' src='" + getResource("jquery.jstree.js")
          + "'></script>");
      b.append("<script type='text/javascript' src='" + getResource("ide.js") + "'></script>");



      b.append("</head>");


      b.append("<body>");
      b.append("<html>");
      b.append("<div id ='highlight' ></div>");

      b.append("<div id='frame' ><img src='" + getResource("frame.png") + " '/></div>");
      b.append("<div id='mouseOver' ></div>");
      b.append("<div id='screen' ><img src='" + getResource("lastScreen.png") + "' /></div>");


      b.append("<div id ='tree' ></div>");

      b.append("<div id ='details'>details</div>");

      b.append("<div id ='notification-container'>"+this.renderPartial()+"</div>");

      b.append("<div id ='actions'>actions");
      b.append("<form action='tap' method='GET'>");

      b.append("<div id ='reference'></div>");

      b.append(" <input type='submit' value='tap'>");
      b.append("</form>");
      
      b.append("<form action='debug' method='GET'>");

      b.append("X: <input type='text' name='x' >");
      b.append("Y: <input type='text' name='y' >");

      b.append("<input type='submit' value='debugTap'>");
      b.append("</form>");
      b.append("</div>");


      b.append("</body>");
      b.append("</html>");


      response.setContentType("text/html");
      response.setCharacterEncoding("UTF-8");
      response.setStatus(200);
      
      if(model.getApplication() != null && model.getApplication().getMessages().size() > 0){
        model.getApplication().getMessages().clear();
      }
      
      response.getWriter().print(b.toString());
    } catch (Exception e) {
      throw new IOSAutomationException(e);
    }
    
  }
  
  public String renderPartial(){
    StringBuilder b = new StringBuilder();
    b.append("<div>");
    if(model.getApplication().getMessages() != null){
      for(Object msg : model.getApplication().getMessages()){
        if(((Message) msg).getMessageType().equals("error")){
          b.append("<div class = 'error message' id='message'>"+ ((Message) msg).getMessageBody() +"</div>");
        }
        else if(((Message) msg).getMessageType().equals("success")){
          if(((Message) msg).getMessageBody().equals("Successfully Quit logging.")){
            model.setLogSessionId(null);
          }
          b.append("<div class = 'success message' id='message'>"+ ((Message) msg).getMessageBody() +"</div>");
        }
        else if(((Message) msg).getMessageType().equals("info")){
          b.append("<div class = 'info message' id='message'>"+ ((Message) msg).getMessageBody() +"</div>");
        }
        else if(((Message) msg).getMessageType().equals("warning")){
          b.append("<div class = 'warning message' id='message'>"+ ((Message) msg).getMessageBody() +"</div>");
        }
      }
    }
    if(model.getLogSessionId() != null && model.getLogging()){
      b.append("<form action='http://" + model.getLoggingAPIHost() + ":" + model.getLoggingAPIPort() + "/session/"+model.getLogSessionId()+"/log' method='get'>");
      b.append("<input type='submit' value='Get Logs' class='button yellow'/>");
      b.append("</form>");
      b.append("<form action='http://" + model.getLoggingAPIHost() + ":" + model.getLoggingAPIPort() + "/session/"+model.getLogSessionId()+"/log' method='post'>");
      b.append("<input type='submit' value='Destroy Logging' class='button red'/>");
      b.append("</form>");
      if(model.getPartLogging()){
        b.append("<div class='on_off' session='" + model.getLogSessionId() + "'> Part-Logging: <input type='checkbox' id='on_off' checked='checked'/> </div>");
      }
      else{
        b.append("<div class='on_off' session='" + model.getLogSessionId() + "'> Part-Logging: <input type='checkbox' id='on_off'/> </div>");
      }
      b.append("<div id='update-logs-container'>");
      b.append("<form action='http://" + model.getLoggingAPIHost() + ":" + model.getLoggingAPIPort() + "/session/"+model.getLogSessionId()+"/log/update' method='post'>");
      b.append("<p>Update your logging options</p>");
      if(Arrays.asList(model.getOptions()).contains("0")){
        b.append("<input type='checkbox' name='HashOptions' value='0'  checked='checked' id='info'/> <label for='info'>INFO</label> <br />");
      }
      else{
        b.append("<input type='checkbox' name='HashOptions' value='0' id='info'/> <label for='info'>INFO </label><br />");
      }
      if(Arrays.asList(model.getOptions()).contains("1")){
        b.append("<input type='checkbox' name='HashOptions' value='1' checked='checked' id='warning'/><label for='warning'> WARNING</label> <br />");
      }
      else{
        b.append("<input type='checkbox' name='HashOptions' value='1' id='warning'/> <label for='warning'>WARNING </label><br />");
      }
      if(Arrays.asList(model.getOptions()).contains("2")){
        b.append("<input type='checkbox' name='HashOptions' value='2' checked='checked' id='error'/> <label for='error'>ERROR </label><br />");
      }
      else{
        b.append("<input type='checkbox' name='HashOptions' value='2' id='error'/> <label for='error'>ERROR </label><br />");
      }
      if(Arrays.asList(model.getOptions()).contains("3")){
        b.append("<input type='checkbox' name='HashOptions' value='3' checked='checked' id='success'/> <label for='success'>SUCCESS</label> <br />");
      }
      else{
        b.append("<input type='checkbox' name='HashOptions' value='3' id='success'/> <label for='success'>SUCCESS </label><br />");
      }
      b.append("<input type='hidden' value='" + model.getLogSessionId() + "' name='sessionId'/>");
      b.append("<input type='submit' value='Update' class='button blue'>");
      b.append("</form>");
      b.append("</div>");
    }
    else{
      b.append("<div id='log-options-default-view'>");
      b.append("Select your logging level(s)");
      b.append("<form action='http://" + model.getLoggingAPIHost() + ":" + model.getLoggingAPIPort() + "/session/log' method='post'>");
      b.append("<input type='checkbox' name='HashOptions' value='0' id='INFO'/> <label for='INFO'>INFO </label><br />");
      b.append("<input type='checkbox' name='HashOptions' value='1' id='WARNING'/> <label for='WARNING'>WARNING </label><br />");
      b.append("<input type='checkbox' name='HashOptions' value='2' id='ERROR'/> <label for='ERROR'>ERROR</label><br />");
      b.append("<input type='checkbox' name='HashOptions' value='3' id='SUCCESS'/> <label for='SUCCESS'>SUCCESS</label><br />");
      b.append("<br />");
      b.append("<input type='hidden' value='"+model.getDriver().getSession().getSessionId()+"' name='sessionId'>");
      b.append("<input type='submit' value='Logging' class='button blue'/>");
      b.append("</form>");
      b.append("</div>");
      model.setLogSessionId(model.getDriver().getSession().getSessionId());
    }
    b.append("</div>");
    return b.toString();
  }
  
  private String getResource(String name) {
    String res = "resources/" + name;
    return res;
  }

}
