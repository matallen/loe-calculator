package com.redhat.sso.sowgen.model;

public class Question {
  public String name;
  public String value;
  public String title;
  public String visibleIf;
  public String type;
  public String calc;
  public String initial;
  public String indent;
  public String onclick;
  public String sowText;
  public boolean isRequired;
  
  public String getIndent(){
    try{
      int indentCount=Integer.parseInt(indent);
      if (type.equalsIgnoreCase("textbox")) indentCount+=1;
      String result="";
      for(int i=0;i<indentCount;i++)
        result+="&nbsp;&nbsp;&nbsp;";
      return result;
    }catch(Exception e){}
    return "";
  }
  public String getVisibleIf(){
    if (visibleIf!=null)
      return "data-bind=\"visible: "+visibleIf+"\"";
    return "";
  }
}
