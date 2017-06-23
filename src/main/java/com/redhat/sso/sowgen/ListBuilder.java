package com.redhat.sso.sowgen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListBuilder<T> {

  private List<T> result=new ArrayList<T>();
  public ListBuilder(T[] array){
    result=Arrays.asList(array);
  }
  
  public ListBuilder<T> add(T v){
    result.add(v);
    return this;
  }
  
  public List<T> build(){
    return result;
  }
}
