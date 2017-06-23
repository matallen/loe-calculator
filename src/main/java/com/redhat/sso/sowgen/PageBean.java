package com.redhat.sso.sowgen;

import static com.google.common.base.Predicates.and;
import static com.redhat.sso.sowgen.GoogleDrive2.Predicates.byId;
import static com.redhat.sso.sowgen.GoogleDrive2.Predicates.byType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFNumbering;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
//import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
//import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr;
//import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering;
//import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;

import com.google.common.base.Predicate;
//import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.redhat.sso.sowgen.GoogleDrive2.GFile;
import com.redhat.sso.sowgen.GoogleDrive2.MimeTypes;
import com.redhat.sso.sowgen.GoogleDrive2.Predicates;
import com.redhat.sso.sowgen.model.Page;
import com.redhat.sso.sowgen.model.Question;

public class PageBean {
//  private Map<String, XQuestion> questions = null;
//  private Map<String, Cost> costs = null;
  
  public static List<Map<String, String>> content=null;
  public static Map<String, String> config=new HashMap<String, String>();
  public static Map<String, Map<String,Integer>> solutionCosts=new HashMap<String, Map<String,Integer>>();
  private static Map<String, Page> pages=new LinkedHashMap<String, Page>();
  private static Map<String, List<String>> defaults=new HashMap<String, List<String>>();
  private static final String CONFIGURATION_GOOGLEID="146ExGAQI1sv7KSu1KUqTHX_ci-4DgV7Lkwi7BogzfMg";
  private static final String TEMPLATE_GOOGLEID="1mSkZSxD7ZfIbs5hgxoh3_Du1K4z5ad8EE8iTPP7CMuI";
  public static String error="";
  
  
  public static void main(String[] args){
    new PageBean();
  }
  public PageBean() {
    load();
  }
  public void reset(){
    content=null;
    error="";
  }
  public String getError(){
    return error;
  }
  
  public String save(HttpServletRequest request) throws Exception{
    
    GoogleDrive2 drive=new GoogleDrive2();
//    drive.cleanup();
    
    // Download template & Export in docx format to the local disk
//    File srcFile = drive.pull(config.get("templateGoogleId"), "docx"); //"SoW Template"
    File srcFile = drive.gexport(config.get("templateGoogleId"), MimeTypes.DOCX); // Template
    
    // Rename to include the client name
    File destFile=new File(srcFile.getParentFile(), request.getParameter("customerName")+".docx");
    if (destFile.exists()) FileUtils.deleteQuietly(destFile);
    FileUtils.moveFile(srcFile, destFile);

    
    XWPFDocument docx = new XWPFDocument(new FileInputStream(destFile));
//    XWPFWordExtractor we = new XWPFWordExtractor(docx);
    
    // Inject quantities into template, replacing any placeholders
    findReplace(docx, "$SCONS_QTY", request.getParameter("snrconsQty"));
    findReplace(docx, "$CONS_QTY", request.getParameter("consQty"));
    findReplace(docx, "$ARCH_QTY", request.getParameter("archQty"));
    findReplace(docx, "$PM_QTY", request.getParameter("pmQty"));
    
    // Inject totals
    findReplace(docx, "$SCONS_TOT", request.getParameter("snrconsTot"));
    findReplace(docx, "$CONS_TOT", request.getParameter("consTot"));
    findReplace(docx, "$ARCH_TOT", request.getParameter("archTot"));
    findReplace(docx, "$PM_TOT", request.getParameter("pmTot"));
    
    // Inject $ value totals
    double total=Double.parseDouble(request.getParameter("consTot").replace("$", "").replace(",", ""));
    total+=Double.parseDouble(request.getParameter("snrconsTot").replace("$", "").replace(",", ""));
    total+=Double.parseDouble(request.getParameter("archTot").replace("$", "").replace(",", ""));
    total+=Double.parseDouble(request.getParameter("pmTot").replace("$", "").replace(",", ""));
    findReplace(docx, "$TOT", new DecimalFormat("#,###.00").format(total));
    
    
    // Inject all checked/entered tasks as bullet lists in the template document
    writeBulletListTasks(docx, request);
    
    // Write document to disk
    docx.write(new FileOutputStream(new File(destFile.getParent(), destFile.getName())));
    
//    // drop the extension
//    File newDestFile=new File(destFile.getParent(), FilenameUtils.getBaseName(destFile.getName()));
//    FileUtils.moveFile(destFile, newDestFile);
    
    // Push document back to Google Drive
//    boolean push=drive.push(destFile, (outputLocation.endsWith("/")?outputLocation:outputLocation+"/")+destFile.getName()); // TODO change this to push to a remote location, removing the need to push & copy remotely
//    boolean push=drive.push(destFile);
    
//    for(GFile f:drive.list());
      
//    GFile destination=Iterables.filter(drive.list(), and(byType("dir"),byName("Mats Test Approach Docs"))).iterator().next();
    GFile destination=Iterables.filter(drive.list(), and(byType("dir"),byId(config.get("destinationFolderId")))).iterator().next();
    
//    System.out.println("Destination: "+destination);
    
    // Upload, convert and move document to final location in Google Drive
    boolean success = drive.gimport(destFile, destination);
    
    System.out.println((success)?"Successfully Uploaded ["+destFile.getName()+"] to remote Google Drive location ["+destination.getName()+"]":"Failed to Upload, reason not yet established");
    
    if (!success)
      error="Failed to upload file ("+destFile.getName()+") directly to Google Drive location ("+destination.getName()+"). Please download and upload the document manually.";
    
    return destFile.getPath();
    // Move document to final location in Google Drive
//    boolean remoteCopy = drive.remoteCopy(destFile.getName(), (outputLocation.endsWith("/")?outputLocation:outputLocation+"/")+destFile.getName());
    
//    System.out.println((push && remoteCopy)?"Successfully Saved":"Failed to Save, reason not yet established");
  }
  
  private String bullet(String indent){
    Integer x=Integer.parseInt(indent);
    String result="";
    for(int i=0;i<=x;i++){
      result+="  ";
    }
    return result+"- ";
  }
  
  private void writeBulletListTasks(XWPFDocument docx, HttpServletRequest request){
    CTAbstractNum abstractNum = CTAbstractNum.Factory.newInstance();
    
    for(Page p:getPages2()){
      
      if (p.questions.size()>0){
        // write header
        XWPFParagraph spacer = docx.createParagraph();
        spacer.createRun().addCarriageReturn();
        
        XWPFParagraph hPara = docx.createParagraph();
        XWPFRun hRun = hPara.createRun();
        hRun.setText(p.name+":");
        hRun.setBold(true);
        
        docx.createNumbering();
        XWPFNumbering numbering=null;
        numbering=docx.createNumbering();
        numbering.addNum(BigInteger.valueOf(1));
        
//        XWPFNumbering numbering=null;
//        numbering=docx.createNumbering();
//        numbering.setNumbering(CTNumbering.Factory.newInstance());
//        numbering.addNum(BigInteger.valueOf(1));
        
        // write tasks here
        for(Question q:p.getQuestions()){
          String key;
          if ("Radiobutton".equalsIgnoreCase(q.type)){
            key=q.onclick;
          }else{
            key=q.name;
          }
          String value=request.getParameter(key);
          
          if (!"Radiobutton".equalsIgnoreCase(q.type) && null!=value && value.equalsIgnoreCase("on")){
            // write bullet point text
            XWPFParagraph para = docx.createParagraph();
//            para.setNumID(addListStyle(abstractNum, docx, numbering));
//            para.getCTP().getPPr().getNumPr().addNewIlvl().setVal(BigInteger.valueOf(1));
            XWPFRun run=para.createRun();
            
            if (null!=q.sowText){
              //process the sowText if any markers exist
              if (q.sowText.contains("${")){
                Matcher m=Pattern.compile("\\$\\{(.+)\\}").matcher(q.sowText);
                while (m.find()){
                  String id=m.group(1);
                  //System.out.println("X = "+id);
                  String replaceWith=request.getParameter(id);
                  if (null!=q.sowText && replaceWith!=null){
                    q.sowText=q.sowText.replaceAll("\\$\\{"+id+"\\}", replaceWith);
                  }else if (q.sowText==null){
                    q.sowText="Not yet configured ("+q.name+")";
                  }else{
                    q.sowText=q.sowText;
                  }
                }
              }
              
              run.setText(bullet(q.indent) + q.sowText);
            }
            
//            newParagraph.getDocument().createNumbering();
//            newParagraph.getp
//            newParagraph.setNumID(addListStyle(CTAbstractNum.Factory.newInstance(), docx, numbering));
            
//            CTPPr x = newParagraph.getCTP().addNewPPr();
//            CTNumPr numPr = x.addNewNumPr();
//            CTDecimalNumber numId = numPr.addNewNumId();
//            numId.setVal(BigInteger.valueOf(1));
            
//            CTDecimalNumber ctDecimalNumber = CTDecimalNumber.Factory.newInstance();
//            ctDecimalNumber.setVal(BigInteger.valueOf(1l));
//            newParagraph.getCTP().getPPr().getNumPr().setNumId(ctDecimalNumber);
            
            
//            CTP ctp = newParagraph.getCTP();
//            if (ctp.getPPr()==null)
//              ctp.addNewPPr();
//            CTPPr ppr = ctp.getPPr();
//            if (ppr.getNumPr()==null)
//              ppr.addNewNumPr();
//            ppr.getNumPr().setNumId(ctDecimalNumber);
            
//            CTNumPr numPr = ppr.addNewNumPr();
//            CTDecimalNumber numId = numPr.addNewNumId();
//            numId.setVal(BigInteger.valueOf(2));
            
//            newParagraph.setNumID(numId.getVal());//BigInteger.valueOf(0));
//            newParagraph.setIndentationLeft(5);
//            XWPFRun runx = para.createRun();
//            runx.setText(q.sowText);
            
            System.out.println("Save:: "+key+" = "+value);
          }else if ("Radiobutton".equalsIgnoreCase(q.type)){
            if (null!=value && value.equalsIgnoreCase(q.name)){
              XWPFParagraph para = docx.createParagraph();
//              para.setNumID(addListStyle(abstractNum, docx, numbering));
//              para.getCTP().getPPr().getNumPr().addNewIlvl().setVal(BigInteger.valueOf(1));
              XWPFRun run=para.createRun();
              run.setText(bullet(q.indent) + q.sowText);
            }
          }
        }
        
//        XWPFRun createRun = hPara.createRun();
//        createRun.addCarriageReturn();
      }
    }
  }
  
  public void findReplace(XWPFDocument doc, String find, String replace) {
//    System.out.println("Looking for stuff to replace: \""+find+"\" with \""+replace+"\"");
    if (null==replace) return;
    for (XWPFParagraph p : doc.getParagraphs()) {

      int runCount=p.getRuns().size();
      for(int i=0;i<runCount; i++){
        XWPFRun r=p.getRuns().get(i);
        String text = r.getText(0);
        if (null!=text && text.contains(find)) {
          System.out.println("Found in paragraphs. Replacing \""+find+"\" with \""+replace+"\"");
          p.removeRun(i);
          XWPFRun r2 = p.createRun();
//          r2.setTextPosition(i);
          r2.setText(text.replace(find, replace));
        }
      }
    }
    for (XWPFTable tbl : doc.getTables()) {
      for (XWPFTableRow row : tbl.getRows()) {
        for (XWPFTableCell cell : row.getTableCells()) {
          for (XWPFParagraph p : cell.getParagraphs()) {
            int runCount=p.getRuns().size();
            for(int i=0;i<runCount; i++){
              XWPFRun r=p.getRuns().get(i);
              String text = r.getText(0);
              if (null!=text && text.contains(find)) {
                System.out.println("Found in Table. Replacing \""+find+"\" with \""+replace+"\"");
                p.removeRun(i);
                XWPFRun r2 = p.createRun();
//                r2.setTextPosition(i);
                r2.setText(text.replace(find, replace));
              }
            }
          }
        }
      }
    }
  }
  
  
  public void load(){
    error="";
    try {
      if (null==content){
        GoogleDrive2 drive=new GoogleDrive2();
//        GoogleDrive drive=new GoogleDrive();
        File file = drive.gexport(CONFIGURATION_GOOGLEID, MimeTypes.XLSX);
//        File file = drive.downloadFile(CONFIGURATION_GOOGLEID);
        
        // load configs on Sheet2
        List<Map<String, String>> rawConfigPage = drive.parseExcelDocument(file, "AppConfig", 2);
        config.clear();
        for(Map<String,String> row:rawConfigPage){
          config.put(row.get("Config Name"), row.get("Config Value"));
        }
        
        content = drive.parseExcelDocument(file, "Configuration", 10);
//        System.out.println(content);
        pages.clear();
        defaults.clear();
        for(Map<String,String> row:content){
          if(!pages.containsKey(row.get("Category Title")))
            pages.put(row.get("Category Title"), new Page());
          Page p=pages.get(row.get("Category Title"));
          p.name=row.get("Category Title");
          
          Question q=new Question();
          q.name=row.get("Name");
          q.title=row.get("Question");
          q.type=row.get("Type");
          q.visibleIf=row.get("VisibleIf");
          q.value=row.get("Cost");
          q.indent=row.get("Indent");
          q.onclick=row.get("OnClick")==null?"":row.get("OnClick");
          q.sowText=row.get("SoW Text");
          
//          q.calc=row.get("Calculation");
//          q.initial=StringUtils.isEmpty(row.get("Initial"))?"ko.observable(\"\")":row.get("Initial");
          
          if (!StringUtils.isEmpty(row.get("Solutions"))){
            String[] defaultsplits=row.get("Solutions").split(",");
            for(String defaultsplit:defaultsplits){
              defaultsplit=defaultsplit.trim();
              if (!defaults.containsKey(defaultsplit)) defaults.put(defaultsplit, new ArrayList<String>());
              defaults.get(defaultsplit).add(row.get("Name"));
            }
          }
          
          if (p.questions==null) p.questions=new ArrayList<Question>();
          p.questions.add(q);
        }
//        return pages.values();
        
        
        // Go through page questions and get a list of solution names
        
        // load configs for calculating more complex solution costs
        List<Map<String, String>> rawSolutionCosts = drive.parseExcelDocument(file, "SolutionCosts", 2);
        solutionCosts.clear();
        for(Map<String,String> row:rawSolutionCosts){
//          System.out.println(row);
          for(String solutionName:defaults.keySet()){
            if (!solutionCosts.containsKey(solutionName)) solutionCosts.put(solutionName, new HashMap<String,Integer>());
            Map<String, Integer> costs=solutionCosts.get(solutionName);
            costs.put(row.get("Category Included Activities"), Integer.parseInt(row.get(solutionName)));
            solutionCosts.put(solutionName, costs);
          }
          
        }

        
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  public Map<String, List<String>> getDefaults(){
    return defaults;
  }
  
  public Collection<Page> getPages2(){
    return pages.values();
  }
  
  public Map<String, Map<String, Integer>> getSolutionCosts(){
    return solutionCosts;
  }
  
  public Map<String, Integer> getSolutionBaseCosts(){
    return Maps.filterEntries(getSolutionCosts2(), new Predicate<Map.Entry<String, Integer>>() {
      public boolean apply(Entry<String, Integer> input) {
        return input.getKey().contains("Base Consulting Hours");
    }});
  }
  
  public static Map<String, Integer> getSolutionCosts2(){
    Map<String,Integer> result=new HashMap<String, Integer>();
    for (Entry<String, Map<String, Integer>> e:solutionCosts.entrySet()){
      for (Entry<String, Integer> ee:e.getValue().entrySet()){
        result.put(e.getKey()+"-"+ee.getKey(), ee.getValue());
      }
    }
    return result;
  }
  
  
  
  
//  public List<Page> getPages(){
//    List<Page> pages=new ArrayList<Page>();
//    
//    Page p1=new Page();
//    p1.name="Standard Tasks";
//    p1.questions=new ArrayList<Question>();
//    p1.questions.add(populate(new Question(), "p1q1", "1", "Configure SmartState Analysis across VMWare providers", "Checkbox"));
//    p1.questions.add(populate(new Question(), "p1q2", "1", "Create schedule for SmartState Analysis to enable drift reporting", "Checkbox"));
//    pages.add(p1);
//    
//    Page p2=new Page();
//    p2.name="Providers";
//    p2.questions=new ArrayList<Question>();
//    p2.questions.add(populate(new Question(), "p2q1", "1", "VMWare", "Checkbox"));
//    p2.questions.add(populate(new Question(), "p2q2", "1", "How many vCenters?", "Textbox", "p2q1()"));
//    p2.questions.add(populate(new Question(), "p2q3", "1", "Red Hat Virt Manager", "Checkbox"));
//    p2.questions.add(populate(new Question(), "p2q4", "1", "Microsoft SCVMM", "Checkbox"));
//    pages.add(p2);
//    
//    return pages;
//  }
//  
//  private Question populate(Question q, String name, String value, String title, String type){
//    return populate(q, name, value, title, type, null);
//  }
//  private Question populate(Question q, String name, String value, String title, String type, String visibleIf){
//    q.name=name;
//    q.value=value;
//    q.title=title;
//    q.type=type;
//    q.visibleIf=visibleIf;
//    return q;
//  }
//
//  
//  
//  
//  public Map<String, Cost> getCosts() {
//    if (costs==null) costs = new HashMap<String, Cost>();
//    else
//      return costs;
//    
//    Cost c1=populateCost(new Cost(), "Consultant", "123", "Senior Consultant", 265);
//    Cost c2=populateCost(new Cost(), "Architect", "456", "Senior Architect", 365);
//    Cost c3=populateCost(new Cost(), "PM", "789", "Project Manager", 220);
//    
//    costs.put(c1.getId(), c1);
//    costs.put(c2.getId(), c2);
//    costs.put(c3.getId(), c3);
//    
//    return costs;
//  }
//  
//  public Cost populateCost(Cost c, String id, String sku, String unitType, int costPerUnit){
//    c.setId(id);
//    c.setSku(sku);
//    c.setUnitType(unitType);
//    c.setCostPerUnit(costPerUnit);
//    return c;
//  }
//  
//  public Map<String, XQuestion> getQuestions() {
//    if (questions==null) questions = new HashMap<String, XQuestion>();
//    else
//      return questions;
//    
//    XQuestion q=new XQuestion();
//    q.setId("1");
//    q.setQuestion("Products Involved:");
//    q.setType("Checkbox");
//    q.getAnswers().put("Cloudforms", 10);
//    q.getAnswers().put("Satellite", 6);
//    q.getAnswers().put("OpenStack", 13);
//    q.setQuestionChain(null);
//    q.setText("Designing and architecting ${Products Involved} deployment for initial deployment");
//    questions.put(q.getQuestion(), q);
//    
//    
//    XQuestion q2=populate(new XQuestion(), 2, "Design &amp; setup base configuration to support...:", "checkbox");
//    q2.getAnswers().put("$ vmWare vCenter(s)", 10);
//    q2.getAnswers().put("Openstack", 5);
//    q2.getAnswers().put("Red Hat Virtualization Manager", 2);
//    q2.getAnswers().put("Microsoft SCVMM", 15);
//    questions.put(q2.getQuestion(), q2);
//    
//    
//    return questions;
//  }
//  
//  
//  public XQuestion populate(XQuestion q, Integer id, String questionText, String type){
//    q.setId(String.valueOf(id));
//    q.setQuestion(questionText);
//    q.setType(type);
//    return q;
//  }
  
  
  private BigInteger addListStyle(CTAbstractNum abstractNum, XWPFDocument doc, XWPFNumbering numbering) {
    try {

      XWPFAbstractNum abs = new XWPFAbstractNum(abstractNum, numbering);
      BigInteger id = BigInteger.valueOf(0);
      boolean found = false;
      while (!found) {
        Object o = numbering.getAbstractNum(id);
        found = (o == null);
        if (!found)
          id = id.add(BigInteger.ONE);
      }
      abs.getAbstractNum().setAbstractNumId(id);
      id = numbering.addAbstractNum(abs);
      return doc.getNumbering().addNum(id);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  
//  public class TestNumbering {
//    String fileName=""; 
//    InputStream in = null;
//    CTAbstractNum abstractNum = null;
//    public TestNumbering() {
//        try {
//            in = CreateWordDocument.class.getClassLoader().getResourceAsStream("numbering.xml");
//            abstractNum = CTAbstractNum.Factory.parse(in);
//        } catch (XmlException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public String createDocument(String fileName, String content) {
//        this.fileName=fileName;
//        XWPFDocument doc = new XWPFDocument();
//
//        doc.createNumbering();
//        XWPFNumbering numbering=null;
//        numbering=doc.createNumbering();
//        for(String value: content.split("@")) {
//            XWPFParagraph para = doc.createParagraph();
//            para.setVerticalAlignment(TextAlignment.CENTER);
//            para.setNumID(addListStyle(abstractNum, doc, numbering));
//            XWPFRun run=para.createRun();
//            run.setText(value);
//        }
//        try {
//            FileOutputStream out = new FileOutputStream(fileName);
//            doc.write(out);
//            out.close();
//            in.close();
//        } catch(Exception e) {}
//        return null;
//    }
//    private BigInteger addListStyle(CTAbstractNum abstractNum, XWPFDocument doc, XWPFNumbering numbering) {
//        try {
//
//            XWPFAbstractNum abs = new XWPFAbstractNum(abstractNum, numbering);
//            BigInteger id = BigInteger.valueOf(0);
//            boolean found = false;
//            while (!found) {
//                Object o = numbering.getAbstractNum(id);
//                found = (o == null);
//                if (!found)
//                    id = id.add(BigInteger.ONE);
//            }
//            abs.getAbstractNum().setAbstractNumId(id);
//            id = numbering.addAbstractNum(abs);
//            return doc.getNumbering().addNum(id);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//    public static void main(String[] args) throws Exception {
//        String fileName="Test.docx";
//        new TestNumbering().createDocument(fileName, "First Level@@Second Level@@Second Level@@First Level");
//    }
//  }
}
