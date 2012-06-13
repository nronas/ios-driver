package org.uiautomation.ios.server.servlet;

import java.util.ArrayList;
import java.util.List;

// TODO freynaud message isn't an exception
public class MessageList extends Message{

  private static List<Object> msg = null;

  public MessageList() {
    this.msg = new ArrayList<Object>();
  }

  public void addMessage(Object msg){
    this.msg.add(msg);
  }
  
  public void clear(){
    this.msg.clear();
  }
  
  public Object getMessage(int index){
    try{
      return this.msg.get(index);
    }
    catch(Exception e){
      return null;
    }
  }
  
  public void deleteMessage(int index){
    this.msg.remove(index);
  }
  
  public int getLastMessageIndex(){
    return this.msg.size()-1;
  }
  public List<Object> getMessages(){
    return this.msg;
  }
  
}
