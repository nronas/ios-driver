package org.uiautomation.ios.server.servlet;

public class Message {
  private String type = null;
  private String body = null;
  
  public Message(String body, String type){
    this.body = body;
    this.type = type;
  }
  
  public Message(){
    this.body = "";
    this.type = "";
  }
  
  public String getMessageBody(){
    return this.body;
  }
  
  public String getMessageType(){
    return this.type;
  }
  
  public void setMessageBody(String body){
    this.body = body;
  }
  
  public void setMessageType(String type){
    this.type = type;
  }
  
}
