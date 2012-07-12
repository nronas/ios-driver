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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.uiautomation.ios.IOSCapabilities;
import org.uiautomation.ios.communication.IOSDevice;
import org.uiautomation.ios.exceptions.IOSAutomationException;
import org.uiautomation.ios.server.instruments.ClassicCommands;
import org.uiautomation.ios.server.servlet.Message;
import org.uiautomation.ios.server.servlet.MessageList;

public class BeginView extends MainHeader implements View{

  private List<String> supportedApps = new ArrayList<String>();
  private MessageList msgList = null;
  private String[] params = null;
  private String sessionId = null;
  private boolean logging = false;
  private String[] options = null;
  
  public BeginView(String[] options, String sessionId, boolean logging, MessageList msgList, String... apps) throws IOSAutomationException {
    if (apps.length == 0) {
      throw new IOSAutomationException("no app specified.");
    }
    this.msgList = msgList;
    this.sessionId = sessionId;
    this.logging = logging;
    if(options != null){
      this.options = new String[options.length];
      System.arraycopy( options, 0, this.options, 0, options.length );
    }
    supportedApps.addAll(Arrays.asList(apps));
  }

  public BeginView(String[] options, String sessionId, MessageList msgList){
    this.msgList = msgList;
    this.sessionId = sessionId;
    if(options != null){
      this.options = new String[options.length];
      System.arraycopy( options, 0, this.options, 0, options.length );
    }
  }
  
  public BeginView(String[] params){
    this.params = params;
  }
  
  public void render(HttpServletResponse response) throws Exception {
    StringBuilder b = new StringBuilder();
    b.append("<html>");
    b.append("<head>");
    b.append("<script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js'></script>");
    b.append(" <link rel='stylesheet' href='" + getResource("ide.css") + "'  type='text/css'/>");
    b.append("<script type='text/javascript' src='" + getResource("begin.js") + "'></script>");

    b.append("</head>");
    b.append("<body>");
    b.append(this.renderHeaderPartial());
    b.append("<div id='main-container'>");
    if(this.msgList != null){
      for(Object msg : msgList.getMessages()){
        if(((Message) msg).getMessageType().equals("error")){
          b.append("<div class = 'error message' id='message'>"+ ((Message) msg).getMessageBody() +"</div>");
        }
        else if(((Message) msg).getMessageType().equals("success")){
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
    b.append("<br />");
    b.append("<div id='application-container'>");
    b.append("<form action='start' method='GET'>");

    b.append("<input  type='text' name='" + IOSCapabilities.DEVICE
        + "' value= '" + IOSDevice.iPhoneSimulator + "', class= 'text-field' />");
    
    b.append("<table>");
    b.append("<tr>");
    b.append(select(IOSCapabilities.LOCALE, getLocales()));
    b.append("</tr>");
    b.append(select(IOSCapabilities.LANGUAGE, getLanguage()));
    b.append("</tr>");
    b.append("<tr>");
    b.append(select(IOSCapabilities.SDK_VERSION, ClassicCommands.getInstalledSDKs()));
    b.append("</tr>");   
    b.append("<tr>");
    b.append(select(IOSCapabilities.AUT, supportedApps));
    b.append("</tr>");
    b.append("<tr>");
    b.append(select(IOSCapabilities.TIME_HACK, new String[] {"false", "true"}));
    b.append("</tr>");
    b.append("</table>");
    b.append("<br><input value= 'Start' type='submit' class= 'large button green'/>");
    b.append("</form>");
    b.append("</div>");
    b.append("<div id='log-container'>");
    if(!getLogging()){
      b.append("<div id='log-options'>");
      b.append("Select your Log level(s).");
      b.append("<form action='http://localhost:8181/session/log' method='post'>");
      b.append("<input type='checkbox' name='HashOptions' value='0' id='INFO'/> <label for='INFO'>INFO </label><br />");
      b.append("<input type='checkbox' name='HashOptions' value='1' id='WARNING'/> <label for='WARNING'>WARNING </label><br />");
      b.append("<input type='checkbox' name='HashOptions' value='2' id='ERROR'/> <label for='ERROR'>ERROR </label><br />");
      b.append("<input type='checkbox' name='HashOptions' value='3' id='SUCCESS'/> <label for='SUCCESS'>SUCCESS </label><br />");
      b.append("<input type='hidden' value='"+getSessionId()+"' name='sessionId'>");
      b.append("<input type='submit' value='Start logging' id='logging' class='button blue'/>");
      b.append("</form>");
      b.append("</div>");
    }
    else{
      b.append("<div id='log-actions-container'>");
      b.append("<form action='http://localhost:8181/session/"+getSessionId()+"/log' method='get'>");
      b.append("<input type='submit' value='Get Logs' id='getLogs' class='button yellow'/>");
      b.append("</form>");
      b.append("<form action='http://localhost:8181/session/"+getSessionId()+"/log' method='post'>");
      b.append("<input type='submit' value='Destroy Logging' id='quitLogging' class='button red'/>");
      b.append("</form>");
      b.append("</div>");
      b.append("<div id='update-logs-container-ide'>");
      b.append("<form action='http://localhost:8181/session/"+getSessionId()+"/log/update' method='post'>");
      b.append("Update your logging options");
      b.append("<br />");
      if(Arrays.asList(options).contains("0")){
        b.append("<input type='checkbox' name='HashOptions' value='0'  checked='checked'  id='INFO'/> <label for='INFO'>INFO </label> <br />");
      }
      else{
        b.append("<input type='checkbox' name='HashOptions' value='0' id='INFO'/> <label for='INFO'>INFO </label> <br />");
      }
      if(Arrays.asList(options).contains("1")){
        b.append("<input type='checkbox' name='HashOptions' value='1' checked='checked' id='WARNING'/> <label for='WARNING'>WARNING </label><br />");
      }
      else{
        b.append("<input type='checkbox' name='HashOptions' value='1' id='WARNING'/> <label for='WARNING'>WARNING</label> <br />");
      }
      if(Arrays.asList(options).contains("2")){
        b.append("<input type='checkbox' name='HashOptions' value='2' checked='checked' id='ERROR'/> <label for='ERROR'>ERROR </label><br />");
      }
      else{
        b.append("<input type='checkbox' name='HashOptions' value='2' id='ERROR'/> <label for='ERROR'>ERROR</label> <br />");
      }
      if(Arrays.asList(options).contains("3")){
        b.append("<input type='checkbox' name='HashOptions' value='3' checked='checked' id='SUCCESS'/> <label for='SUCCESS'>SUCCESS </label><br />");
      }
      else{
        b.append("<input type='checkbox' name='HashOptions' value='3' id='SUCCESS'/><label for='SUCCESS'> SUCCESS </label><br />");
      }
      b.append("<input type='hidden' value='"+getSessionId()+"' name='sessionId'/>");
      b.append("<input type='submit' value='Update' class='button blue'>");
      b.append("</form>");
      b.append("</div>");
    }
    b.append("</div>");
    b.append("</div>");
    b.append("</body>");
    b.append("</html>");


    response.setContentType("text/html");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(200);
    msgList.clear();
    response.getWriter().print(b.toString());
  }

  private String[] getLanguage() {
    return new String[] {};
  }


  private String select(String name, String[] options) {
    return select(name, Arrays.asList(options));
  }

  private String select(String name, List<String> options) {
    StringBuilder b = new StringBuilder();
    b.append("<td>" + name + "</td><td><select name='" + name + "', id = '" + name + "'>");
    boolean first = true;
    for (String option : options) {
      if(IOSCapabilities.AUT.equals(name)){
        String[] tmp = option.split("/");
        if(first){
          b.append("<option value= '" + option + "', selected = '" + option + "'>" + tmp[tmp.length-1] + "</option>");
          first = false;
        }
        else{
          b.append("<br><option value= '" + option + "'>" + tmp[tmp.length-1] + "</option>");
        }
      }
      else{
        b.append("<br><option>" + option + "</option>");
      }
    }
    b.append("</select></td>");
    return b.toString();

  }

  private String getResource(String name) {
    String res = "resources/" + name;
    return res;
  }

  private List<String> getLocales() {
    List<String> res = new ArrayList<String>();

    res.add("en_GB");
    res.add("de_CH");
    res.add("en_US");
    res.add("nl");
    res.add("fr_BE");
    res.add("es_ES");
    res.add("fr_CH");
    res.add("de_AT");
    res.add("it_IT");
    res.add("en_IE");
    res.add("nl_BE");
    res.add("fr_FR");

    return res;
  }

  private String getSessionId(){
   return this.sessionId;
  }
  
  private boolean getLogging(){
    return this.logging;
  }
  
  public static void main(String[] args) {
    for (Locale l : Locale.getAvailableLocales()) {
      System.out.println(l);
    }
  }

}
