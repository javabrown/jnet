package com.jbrown.jnet.core;

import static com.jbrown.jnet.utils.StringUtils.isEmpty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
 
import java.util.Map;

import com.jbrown.jnet.utils.KeysI;
import com.jbrown.jnet.utils.Utils;

class SocketIO {
  private SocketIOI _io;

  public SocketIO(Socket csocket) throws IOException {
    _io = new CharStream(csocket);
  }

  public WireData read() throws Exception {
    //String input = _io.readInput();
    String input = _io.getHttpRequestData().getPath();
    return new WireData(input);
  }

  public void writeOutput(String output) throws Exception {
    _io.writeOutput(output);
  }

  public void close() throws Exception {
    _io.close();
  }
  
  public HttpRequestData getHttpRequestData(){
    return _io.getHttpRequestData();  
  }
  
  public List<String> getHeaders() {
    List<String> list = null;
    
    if(_io.getHttpRequestData() != null){
       list = _io.getHttpRequestData().getHeaders();
    }

    return list;
  }
  
  public String getBody() {
    String body = null;

    if (_io.getHttpRequestData() != null) {
      body = _io.getHttpRequestData().getBody();
    }

    return body;
  }

  interface SocketIOI {
    String readInput() throws Exception;
    void writeOutput(String output) throws Exception;
    HttpRequestData getHttpRequestData();
    void close() throws Exception;
  }
 
}

//
//class ObjectStream implements SocketIO.SocketIOI {
//  private ObjectOutputStream _writer = null;
//  private ObjectInputStream _reader = null;
//
//  public ObjectStream(Socket csocket) throws IOException{
//     _writer = new ObjectOutputStream(csocket.getOutputStream());
//     _reader = new ObjectInputStream(csocket.getInputStream());
//  }
//
//  @Override
//  public String readInput() throws Exception {
//    Object wireRowData =  _reader.readObject();
//    return (String) wireRowData;
//  }
//
//  @Override
//  public void writeOutput(String output) throws Exception {
//    _writer.writeObject(String.format("\r%s\r", output));
//    _writer.flush();
//  }
//
//  @Override
//  public void close() throws Exception {
//    _writer.close();
//    _reader.close();
//  }
//  
//  @Override
//  public HttpRequestData getHttpRequestData() {
//    return null;
//  }
//}


class CharStream implements SocketIO.SocketIOI {
  private PrintStream _writer = null;
  private BufferedReader _reader = null;
  private HttpRequestData _httpRequestData = null;
  
  public CharStream(Socket csocket) throws IOException{
     _reader = new BufferedReader(
         new InputStreamReader(csocket.getInputStream(), KeysI.UTF_8));

     _writer = new PrintStream(csocket.getOutputStream(), true);
     
     _httpRequestData = populateHttpRequestData();
  }

  @Override
  public String readInput() throws Exception {
    String wireData = _httpRequestData.getHeaders().get(0);
    return wireData;
  }

  @Override
  public HttpRequestData getHttpRequestData() {
    return _httpRequestData;
  }
  
  private HttpRequestData populateHttpRequestData() {
    List<String> headers = getRawHeaders();
    String rowHttpPath = headers.get(0);
    String httpMethod = "";
    String url = "";
    String path = "";
    Map<String, String> queryParamMap = new HashMap<String, String>();
    String body = "";
    
    try {
      String[] strs = rowHttpPath.trim().split(KeysI.SPACE_K); //  GET /app/hello?test=1 HTTP/1.1
      httpMethod = strs[0];
      url = strs[1];
      
      String[] paths = url.split("\\" + KeysI.QUESTION_MARK_K); //  /app/hello?test=1
      path = paths[0];
      
      String queryParams = paths[1];
      
      String[] paramsKeyVals = queryParams.split(KeysI.AMPERSAND_K); //  test=1&test1=2
      
      if (paramsKeyVals != null && paramsKeyVals.length > 0) {
        
        for (int i = 0; i < paramsKeyVals.length; i++) {
          if (!isEmpty(paramsKeyVals[i].trim())) {
            //test=1
            String[] keyVal = paramsKeyVals[i].trim().split(KeysI.EQUAL_K);
            
            if ( !isEmpty(keyVal[0], keyVal[1]) ) {
               queryParamMap.put(keyVal[0], keyVal[1]);
            }
            
          }
        }
        
      }
      
    } catch (Exception ex) {
      System.out.printf("bad command %s", ex.toString());
    }

    return new HttpRequestData(httpMethod, path, headers, queryParamMap, body);
  }
  
  private List<String> getRawHeaders()  {
    BufferedReader in = _reader;

    String line;
    List<String> headers = new LinkedList<>();
    StringBuilder body = null;
    try {
      while ( (line = in.readLine()) != null) {
        if(line == null || line.isEmpty()){
           break;  
        }
        
        // - Here we test if we've reach the body part.
        if (line.isEmpty() && body == null) {
          body = new StringBuilder();
          break;
        }
        if (body != null) {
          body.append(line).append('\n');
        } else {
          headers.add(line);
        }

      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.out.println("--- Headers ---");
    for (String h : headers) {
      System.out.println(h);
    }
    System.out.println("--- Body ---"); //RK-TODOBody has some bug, will fix later
    System.out.println(body != null ? body.toString() : "");
    
    return headers;
  }

  @Override
  public void writeOutput(String output) throws Exception {
    _writer.printf("%s", Utils.toJson(output));
    _writer.flush();
  }
  
//  @Override
//  public void writeOutput(String output) throws Exception {
//    _writer.printf("\n\r%s\n\r", output);
//    _writer.printf("\n\r%s\n\r", KeysI.END_K);
//    
//    _writer.printf("\n\r%s ", KeysI.PROMPT_K1);
//    _writer.flush();
//  }

  @Override
  public void close() throws Exception {
    _writer.close();
    _reader.close();
  }
}
