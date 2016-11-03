package com.jbrown.jnet.core;

import java.io.Serializable;
import java.util.List;

public class HttpRequestData implements Serializable{
  private List<String> _headers;
  private String _body;
  
  public HttpRequestData(List<String> headers, StringBuilder body){
    _headers = headers;
    _body = "";
    
    if( body != null) {
      _body = body.toString();
     }
  }
  
  public List<String> getHeader(){
    return _headers;
  }
  
  public String getBody(){
    return _body;
  }
}