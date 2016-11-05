package com.jbrown.jnet.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.jbrown.jnet.utils.StringUtils;
import com.jbrown.jnet.utils.Utils;

public class HttpRequestData implements Serializable{
  private String _method;
  private String _path;
  private Map<String, String> _queryParams;
  private List<String> _headers;
  private String _body;
  
  public HttpRequestData(String method, String path, List<String> headers,
      Map<String, String> queryParams, String body) {
    _method = method;
    _path = path;
    _headers = headers;
    _queryParams = queryParams;
    
    _body = "";
    if( !StringUtils.isEmpty(body) ) {
      _body = body.toString();
     }
  }

  public String getMethod() {
    return _method;
  }

  public String getPath() {
    return _path;
  }

  public Map<String, String> getQueryParams() {
    return _queryParams;
  }

  public List<String> getHeaders() {
    return _headers;
  }

  public String getBody() {
    return _body;
  }
}