package com.redhat.sso.sowgen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayBuilder {

  private List<String> result=new ArrayList<String>();
  public ArrayBuilder(){
  }
  public ArrayBuilder(String[] array){
    for(int i=0;i<=array.length-1;i++)
      result.add(array[i]);
//    result=(List<T>)Arrays.asList(array);
  }
  
  public ArrayBuilder add(String v){
    result.add(v);
    return this;
  }
  
  public ArrayBuilder addAll(String... array){
    result.addAll(Arrays.asList(array));
    return this;
  }
  
  public String[] build(){
    return result.toArray(new String[result.size()]);
  }
}
