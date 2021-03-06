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
package org.uiautomation.ios;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.uiautomation.ios.communication.IOSDevice;
import org.uiautomation.ios.exceptions.IOSAutomationException;

public class IOSCapabilities {



  public static final String DEVICE = "device";
  public static final String SDK_VERSION = "sdkVersion";
  public static final String IOS_SWITCHES = "ios.switches";
  public static final String LANGUAGE = "language";
  public static final String LOCALE = "locale";
  public static final String AUT = "aut";
  public static final String TIME_HACK = "timeHack";

  private final Map<String, Object> raw = new HashMap<String, Object>();

  public static IOSCapabilities iphone(String app) {
    IOSCapabilities res = new IOSCapabilities();
    res.setCapability(DEVICE, IOSDevice.iPhoneSimulator);
    res.setCapability(LANGUAGE, "en");
    res.setCapability(LOCALE, "en_GB");
    res.setCapability(AUT, app);
    return res;
  }

  public IOSCapabilities() {

  }

  public IOSCapabilities(Map<String, Object> from) {
    raw.putAll(from);
  }

  public IOSCapabilities(JSONObject json) throws JSONException {
    Iterator<String> iter = json.keys();
    while (iter.hasNext()) {
      String key = iter.next();
      Object value = json.get(key);
      raw.put(key, decode(value));
    }
  }



  public void setCapability(String key, Object value) {
    raw.put(key, value);
  }


  private Object decode(Object o) throws JSONException {
    if (o instanceof JSONArray) {
      List<Object> res = new ArrayList<Object>();
      JSONArray array = (JSONArray) o;
      for (int i = 0; i < array.length(); i++) {
        Object r = array.get(i);
        res.add(decode(r));
      }
      return res;
    } else {
      return o;
    }
  }

  public Map<String, Object> getRawCapabilities() {
    return raw;
  }

  public IOSDevice getDevice() {
    Object o = raw.get(DEVICE);
    return IOSDevice.valueOf(o);
  }

  public String getSDKVersion() {
    Object o = raw.get(SDK_VERSION);
    return ((String) o);
  }

  public String getApplication() {
    Object o = raw.get(AUT);
    return ((String) o);
  }

  public String getLocale() {
    Object o = raw.get(LOCALE);
    return ((String) o);
  }

  public String getLanguage() {
    Object o = raw.get(LANGUAGE);
    return ((String) o);

  }

  public void setDevice(IOSDevice device) {
    raw.put(DEVICE, device);

  }

  public void setSDKVersion(String sdkVersion) {
    raw.put(SDK_VERSION, sdkVersion);
  }

  public void setLocale(String locale) {
    raw.put(LOCALE, locale);

  }

  public void setLanguage(String language) {
    raw.put(LANGUAGE, language);
  }

  public List<String> getExtraSwitches() {
    List<String> res = new ArrayList<String>();
    if (raw.get(IOS_SWITCHES) != null) {
      res.addAll((Collection<String>) raw.get(IOS_SWITCHES));
    }
    return res;
  }

  public boolean isTimeHack() {
    Object o = raw.get(TIME_HACK);
    if (o == null) {
      return false;
    } else if (o instanceof Boolean) {
      return (Boolean) o;
    } else {
      return Boolean.parseBoolean((String) o);
    }
  }
}
