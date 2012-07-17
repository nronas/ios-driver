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
import org.uiautomation.ios.server.servlet.CustomMessage;

public class BeginView implements View{

  private List<String> supportedApps = new ArrayList<String>();
  private CustomMessage msg = null;
  public BeginView( CustomMessage msg, String... apps) throws IOSAutomationException {
    if (apps.length == 0) {
      throw new IOSAutomationException("no app specified.");
    }
    this.msg = msg;
    supportedApps.addAll(Arrays.asList(apps));
  }
  
  public BeginView(CustomMessage msg){
    this.msg= msg;
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
    if(this.msg != null){
      if(this.msg.getType().equals("error")){
        b.append("<div class = 'error message' id='message'>"+ msg.getMessage() +"</div>");
      }
      else if(this.msg.getType().equals("notice")){
        b.append("<div class = 'notice message' id='message'>"+ msg.getMessage() +"</div>");
      }
    }
    b.append("<br />");
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
    b.append("<form action='http://localhost:8181/automation-ios-logger/session' method='post'>");
    b.append("<select multiple='multiple' name='log_options'>");
    b.append("<option value='0'>INFO</option>");
    b.append("<option value='1'>WARNING</option>");
    b.append("<option value='2'>ERROR</option>");
    b.append("<option value='3'>SUCCESS</option>");
    b.append("</select>");
    b.append("<input type='submit' value='Logging'/>");
    b.append("</form>");
    b.append("</body>");
    b.append("</html>");



    response.setContentType("text/html");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(200);
    
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

  public static void main(String[] args) {
    for (Locale l : Locale.getAvailableLocales()) {
      System.out.println(l);
    }
  }

}
