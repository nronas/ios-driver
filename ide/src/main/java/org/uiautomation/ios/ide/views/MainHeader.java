package org.uiautomation.ios.ide.views;

import java.awt.Component;

import javax.servlet.http.HttpServletResponse;

public class MainHeader{
  
  public String renderPartial(){
    StringBuilder b = new StringBuilder();
    b.append("<div>");
    b.append("<ul>");
    b.append("<li>HELLLO NOTIFICATION CENTER</li>");
    b.append("</ul>");
    b.append("</div>");
    return b.toString();
  }    
  public String renderHeaderPartial(){
    StringBuilder b = new StringBuilder();
    b.append("<div id = 'header'> <a href='http://localhost:5555/wd/hub/ide/begin'><img border='0', src='"+getResource("ebay-euqe-logo.png")+"', alt='eBay logo', width='340', height='180' /></a></div>");
    return b.toString();
  }
  
  private String getResource(String name) {
    String res = "resources/" + name;
    return res;
  }
}
