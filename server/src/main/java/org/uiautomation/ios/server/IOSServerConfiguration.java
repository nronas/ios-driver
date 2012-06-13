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

package org.uiautomation.ios.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.uiautomation.ios.server.servlet.Message;
import org.uiautomation.ios.server.servlet.MessageList;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/*
 * Class IOSServerConfiguration Configures with the given configurable arguments: -port <port> #
 * Start server in port with value <port>. Default port = 5555 -host <host_name> # Start server with
 * the provided <host_name>. Default host = 'localhost'
 * 
 * String[] getSupportedApps(): Loads the supported apps from inside the APPS_FILE. Loading failure:
 * Generates and Throws a CustomMessage.class 'error' message Loading Success: Generates and Throws
 * a CustomMessage.class 'notice' message
 */
public class IOSServerConfiguration extends MessageList{

  private final String APPS_FILE = "/supportedApps.txt";
  private String logSessionId = null;

  @Parameter(description = "port the server will listen on.", names = "-port")
  private int port = 5555;

  @Parameter(description = "if specified, will send a registration request to the given url. Example : http://localhost:4444/grid/register", names = "-hub")
  private String registrationURL = null;

  @Parameter(description = "location of the application under test.Absolute path expected.", names = {
      "-app", "-aut"}, required = true)
  private String absoluteAppPath;

  @Parameter(description = "host of the node.Needs to be specified, as guessing can be wrong complex ntw configs", names = "-host")
  private String serverHost;


  public String getRegistrationURL() {
    return registrationURL;
  }

  public String getAbsoluteAppPath() {
    return absoluteAppPath;
  }

  public String serverHost() {
    return serverHost;
  }

  //public IOSServerConfiguration(){
  //  super();
  //}
  
  /**
   * Returns a IOSServerConfiguration instance of the server configuration, from the given args
   * parameters.<br>
   * Parsing the configuration commands using {@link ServerConfigurationCommands} instance.
   * 
   * @param args
   * @return A configuration instance of the server.
   * @see ServerConfigurationCommands
   */
  public static IOSServerConfiguration create(String[] args) {
    ServerConfigurationCommands command_handler = new ServerConfigurationCommands();
    new JCommander(command_handler).parse(args);

    IOSServerConfiguration res = new IOSServerConfiguration();
    res.setHost(command_handler.getHost());
    res.setPort(command_handler.getPort());
    
    return res;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void setHost(String host) {
    this.serverHost = host;
  }

  /**
   * Returns the names for the supported app names.<br>
   * Using the {@link #ReadAppsFrom(String) ReadAppsFrom} helper method.
   * 
   * @param void
   * @return A table with the supported application names.
   * @see ReadAppsFrom
   */
  public String[] getSupportedApps() throws Exception {
    return ReadAppsFrom(getAppsReference());
  }

  /**
   * Returns the names for the supported app names into a <br>
   * Using the {@link #ReadAppsFrom(String) ReadAppsFrom()} helper method.
   * 
   * @param void
   * @return A table with the supported application names.
   * @see FileInputStream
   * @see DataInputStream
   * @see BufferedReader
   * @see InputStreamReader
   * @see ArrayList
   * @see #getFromClassPath(String) getFromClassPath()
   */
  public String[] ReadAppsFrom(String file) throws Exception {
    FileInputStream fstream;
    String[] res = null;
    fstream = new FileInputStream(file);
    DataInputStream in = new DataInputStream(fstream);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String strLine;
    ArrayList<String> lines = new ArrayList<String>();

    while ((strLine = br.readLine()) != null) {
      lines.add(getFromClassPath("/sampleApps/" + strLine).getAbsolutePath());
    }

    res = new String[lines.size()];
    res = lines.toArray(res);
    addMessage(new Message("Successfully load the resources! : [LOCATION] SERVER", "success"));
    return res;
  }

  private File getFromClassPath(String resource) throws Exception {
    File res = null;
    URL url = null;
    try {
      url = IOSServerConfiguration.class.getResource(resource);
      if (url.toExternalForm().startsWith("file:")) {
        res = new File(url.toExternalForm().replace("file:", ""));
      }
    } 
    catch (Exception e) {
      addMessage(new Message("Cannot load the resource " + resource + getLocalStackTrace(e), "error"));
      throw new Exception();
    }

    if (res == null || !res.exists()) {
      addMessage(new Message("Couldn't locate the file from " + url.toString(), "error"));
      throw new Exception();
    }
    return res;
  }

  private String getLocalStackTrace(Exception e) {
    StackTraceElement[] exceptionBody = e.getStackTrace();
    String res = "<ul>";
    for(StackTraceElement i : exceptionBody){
      String[] tmpSplitter = i.getClassName().split("\\.");
      if(tmpSplitter[1].equals("uiautomation")){
        res += "<li>"+ i.getClassName()+", "+i.getMethodName()+"("+i.getLineNumber()+")</li>";
      }
    }
    res += "</ul>";
    return res;
  }

  public String getAppsReference() throws Exception {
    return getFromClassPath(APPS_FILE).getAbsolutePath();
  }

  public int getPort() {
    return this.port;
  }

  public String getHost() {
    return this.serverHost;
  }

  public void setLogSessionId(String logSessionId) {
    this.logSessionId = logSessionId;  
  }
  
  public String getLogSessionId(){
    return this.logSessionId;
  } 
}
