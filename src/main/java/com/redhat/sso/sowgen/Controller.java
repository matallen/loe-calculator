package com.redhat.sso.sowgen;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;


@Path("/")
public class Controller {
  
  @GET
  @Path("/reset")
  public Response findByUid() throws JsonGenerationException, JsonMappingException, IOException{
//    return find("uid", value);
    PageBean.content=null;
    return Response.status(200).build();
  }
  
  
  @GET
  @Path("/{field}/{value}")
  public Response find(@PathParam("field") String field, @PathParam("value") String value) throws JsonGenerationException, JsonMappingException, IOException{
//    List<UserService.User> result=search(field, value);
//    return Response.status(200).entity(Json.newObjectMapper(true).writeValueAsString(result)).build();
    return Response.status(200).build();
  }
  
//  public List<UserService.User> search(String field, String value) {
//    return new UserService().search(field, value);
//  }

//  public String getSurvey2Raw(){
//    return "{\n"+
//            " pages: [\n"+
//            "  {\n"+
//            "   name: 'page1',\n"+
//            "   questions: [\n"+
//            "    {\n"+
//            "     type: 'checkbox',\n"+
//            "     choices: [\n"+
//            "      {\n"+
//            "       value: '1',\n"+
//            "       text: 'Satellite'\n"+
//            "      },\n"+
//            "      {\n"+
//            "       value: '2',\n"+
//            "       text: 'Cloudforms'\n"+
//            "      },\n"+
//            "      {\n"+
//            "       value: '3',\n"+
//            "       text: 'OpenShift'\n"+
//            "      }\n"+
//            "     ],\n"+
//            "     isRequired: true,\n"+
//            "     name: 'question1',\n"+
//            "     title: 'What products are involved?'\n"+
//            "    },\n"+
//            "    {\n"+
//            "     type: 'checkbox',\n"+
//            "     choices: [\n"+
//            "      {\n"+
//            "       value: '1',\n"+
//            "       text: 'vmWare'\n"+
//            "      },\n"+
//            "      {\n"+
//            "       value: '2',\n"+
//            "       text: 'Microsoft VSCMM'\n"+
//            "      },\n"+
//            "      {\n"+
//            "       value: '3',\n"+
//            "       text: 'Red Hat VMM'\n"+
//            "      }\n"+
//            "     ],\n"+
//            "     isRequired: true,\n"+
//            "     name: 'providers',\n"+
//            "     title: 'What providers do we need to configure?',\n"+
//            "     visibleIf: '{products}.Items.length>0'\n"+
//            "    }\n"+
//            "   ]\n"+
//            "  }\n"+
//            " ],\n"+
//            " title: 'Tell us, what technologies do you use?'\n"+
//            "}";
//  }
//  
//  @GET
//  @Path("/2")
//  public Response getSurvey2(@Context HttpServletRequest request) throws JsonGenerationException, JsonMappingException, IOException{
//    return Response.status(200).entity(getSurvey2Raw()).build();
//  }
//  
//  
//  @SuppressWarnings("unchecked")
//  @GET
//  @Path("/")
//  public Response getSurvey(@Context HttpServletRequest request) throws JsonGenerationException, JsonMappingException, IOException{
//    
//    
//    boolean surveyFormat=request!=null?"true".equalsIgnoreCase(request.getParameter("surveyFormat")):true;
//    
//    Map<String,Object> x=new HashMap<String, Object>();
//    x.put("pages", new ArrayList<Page>());
//    x.put("title", "This is a title");
//    ((List<Page>)x.get("pages")).add(
//        populate(new Page(), "Standard Tasks", 
//            populate(new Question(), "question1", "Configure SmartState Analysis across VMWare providers", "checkbox", true, "",
//                populate(new Answer(), "5", "")
//            ),
//            populate(new Question(), "question2", "Create schedule for SmartState Analysis to enable drift reporting", "checkbox", true, "",
//                populate(new Answer(), "5", "")
//            ),
//            populate(new Question(), "question3", "Configure and enable the Capacity and Utilization engine", "checkbox", true, "",
//                populate(new Answer(), "5", "")
//            )
//        )
//    );
//    ((List<Page>)x.get("pages")).add(
//        populate(new Page(), "Providers", 
//            populate(new Question(), "question4", "vmWare", "checkbox", true, "",
//                populate(new Answer(), "5", "")
//            ),
//            populate(new Question(), "question4.1", "How many vCenters?", "checkbox", true, "{{question4}}.checked==true",
//                populate(new Answer(), "5", "")
//            ),
//            
//            populate(new Question(), "question5", "Openstack", "checkbox", true, "",
//                populate(new Answer(), "5", "")
//            ),
//            populate(new Question(), "question6", "Red Hat Virt Manager", "checkbox", true, "",
//                populate(new Answer(), "5", "")
//            ),
//            populate(new Question(), "question6", "Microsoft SCVMM", "checkbox", true, "",
//                populate(new Answer(), "5", "")
//            )
//        )
//    );
//    
//    
//    
////    try{
//      String json=Json.newObjectMapper(true).writeValueAsString(x);
//      
//      if(surveyFormat){
//        // trip the first two quote from each row
//        
//        String[] lines=json.split("\n");
//        for(int i=0;i<lines.length-1;i++){
//          lines[i]=lines[i].replaceFirst("\"", "").replaceFirst("\"", ""); // replace first two quotes from each row
//        }
//        // yes.. yes.. yes... should use Guava join here but this saves the library import
//        StringBuffer sb=new StringBuffer();
//        for(String line:lines)
//          sb.append(line).append("\n");
//        json=sb.toString();
//        
//      }
//      
////      System.out.println(json);
//      return Response.status(200).entity(json).build();
////    }catch(Exception e){
////      e.printStackTrace();
////    }
//  }
//  
//  
//  public Page populate(Page p, String name, Question... questions){
//    p.name=name;
//    p.questions=Arrays.asList(questions);
//    return p;
//  }
//  
//  public Question populate(Question q, String name, String title, String type, boolean isRequired, String visibleIf, Answer... choices){
//    q.name=name;
//    q.title=title;
//    q.type=type;
//    q.visibleIf=visibleIf==null?"":visibleIf;
//    q.isRequired=isRequired;
//    q.choices=Arrays.asList(choices);
//    return q;
//  }
//  
//  public Answer populate(Answer a, String value, String text){
//    a.value=value;
//    a.text=text;
//    return a;
//  }
//  
//  public void run(){
//    Map<String,Object> x=new HashMap<String, Object>();
//    x.put("pages", new ArrayList<Page>());
////    ((List)x.get("pages")).add(
////        populate(new Page(), "Page1", 
////            populate(new Question(), "question1", "title1", "checkbox", true, 
////                populate(new Answer(), "1", "Answer1"),
////                populate(new Answer(), "2", "Answer2"),
////                populate(new Answer(), "3", "Answer3")
////            ),
////            populate(new Question(), "question2", "title2", "checkbox", true, 
////                populate(new Answer(), "4", "Answer4"),
////                populate(new Answer(), "5", "Answer5"),
////                populate(new Answer(), "6", "Answer6")
////            )
////        )
////    );
//    
//    try{
//      String json=Json.newObjectMapper(true).writeValueAsString(x);
//      System.out.println(json);
//    }catch(Exception e){
//      e.printStackTrace();
//    }
//    
//  }
//  public static void main(String[] asd) throws Exception{
//    new Controller().getSurvey(null);
//  }
  
  
  
//  analytics=Json.newObjectMapper(true).readValue(DATABASE, new TypeReference<HashMap<String, Analytic>>(){});
}
