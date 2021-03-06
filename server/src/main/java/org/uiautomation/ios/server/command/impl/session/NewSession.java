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
package org.uiautomation.ios.server.command.impl.session;

import org.json.JSONObject;
import org.uiautomation.ios.IOSCapabilities;
import org.uiautomation.ios.communication.WebDriverLikeRequest;
import org.uiautomation.ios.communication.WebDriverLikeResponse;
import org.uiautomation.ios.exceptions.IOSAutomationException;
import org.uiautomation.ios.server.command.BaseCommandHandler;
import org.uiautomation.ios.server.instruments.SessionsManager;

public class NewSession extends BaseCommandHandler {

  private final JSONObject capabilities;

  public NewSession(SessionsManager sessionsManager, WebDriverLikeRequest request) {
    super(sessionsManager, request);

    try {
      JSONObject payload = request.getPayload();
      capabilities = payload.getJSONObject("desiredCapabilities");
    } catch (Exception e) {
      throw new IOSAutomationException(e);
    }
  }

  public WebDriverLikeResponse handle() throws Exception {
    IOSCapabilities cap = new IOSCapabilities(capabilities);
    getSessionsManager().createSession(cap);
    JSONObject json = new JSONObject();
    json.put("sessionId", getSessionsManager().getCurrentSessionId());
    json.put("status", 0);
    json.put("value", "");
    WebDriverLikeResponse r = new WebDriverLikeResponse(json);
    return r;
  }


  


}
