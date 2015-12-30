package com.jbrown.jnet.response;


public class DefaultResponse implements ResponseI {
  String _response;

  public DefaultResponse(String response){
    _response = response;
  }

  @Override
  public Object getResponse() {
    return _response;
  }

}
