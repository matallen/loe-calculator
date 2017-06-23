package com.redhat.sso.sowgen.model;

import java.util.List;

import com.redhat.sso.sowgen.model.Question;

public class Page {
  public String name;
  public List<Question> questions;
  public List<Question> getQuestions(){return questions;}
}
