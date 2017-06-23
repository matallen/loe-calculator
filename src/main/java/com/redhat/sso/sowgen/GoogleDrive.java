package com.redhat.sso.sowgen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFNumbering;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class GoogleDrive {
  public static final String GOOGLE_PULL_COMMAND="/home/%s/.go/bin/drive pull -export xls -quiet=true --id %s";
  public static final String GOOGLE_WORKING_FOLDER="/home/%s/Work/google_drive";
  
  public static final String GOOGLE_PULL_CMD="/home/%s/.go/bin/drive pull -export %s -force -quiet -id %s"; // user.name, format, docId
  public static final String GOOGLE_COPY_CMD="/home/%s/.go/bin/drive copy -id %s %s"; //user.name, docId, filename or destination path+filename
  public static final String[] GOOGLE_COPYBYNAME_CMD=new String[]{String.format("/home/%s/.go/bin/drive", System.getProperty("user.name")),"copy","%s","%s"}; //user.name, docName, filename or destination path+filename
  public static final String GOOGLE_PUSH_CMD="/home/%s/.go/bin/drive push -convert -force -quiet %s"; //user.name, filename
  public static final String[] GOOGLE_PUSH_CMD2=new String[]{String.format("/home/%s/.go/bin/drive", System.getProperty("user.name")), "push","-convert","-force","-quiet","%s","%s"}; //user.name, filename
  
  public static final String user=System.getProperty("user.name");
  public static final String[] GOOGLE_REMOTE_COPY=new String[]{String.format("/home/%s/.go/bin/drive", user), "copy", "-r" ,"-id"}; // template id, destination path+name
  public static final String[] GOOGLE_PULL=new String[]{String.format("/home/%s/.go/bin/drive", user), "pull"}; // pull by path
  
  
//  private Integer maxColumns=30;
  public GoogleDrive(/*Integer maxColumns*/){
//    this.maxColumns=maxColumns;
  }
  
//  copyTemplate("1mSkZSxD7ZfIbs5hgxoh3_Du1K4z5ad8EE8iTPP7CMuI", "newfile", "doc");
  
  public static void main(String[] asd) throws Exception{
    GoogleDrive drive=new GoogleDrive();
    drive.cleanup();
//    drive.copyTemplate("1mSkZSxD7ZfIbs5hgxoh3_Du1K4z5ad8EE8iTPP7CMuI", "mats-test.doc");
//    File srcFile = drive.pull("1mSkZSxD7ZfIbs5hgxoh3_Du1K4z5ad8EE8iTPP7CMuI", "docx"); //"SoW Template"
//    File destFile=new File(srcFile.getParentFile().getParentFile(), "test.docx");
//    FileUtils.moveFile(srcFile, destFile);
//    drive.push(destFile);
    
//    File srcFile = new File("SoW Template");
//    File srcFile = drive.pull("1mSkZSxD7ZfIbs5hgxoh3_Du1K4z5ad8EE8iTPP7CMuI", "docx"); //"SoW Template"
//    String outputLocation="/RHC Emerging Technology Practice/RHC Cloud Practice/Cloud Management Practice/Mats Test Approach Docs";
//    boolean remoteCopy = drive.remoteCopy(srcFile.getName(), (outputLocation.endsWith("/")?outputLocation:outputLocation+"/")+srcFile.getName());
//    System.out.println(remoteCopy);
    
    String remotePath="/RHC Emerging Technology Practice/RHC Cloud Practice/Cloud Management Practice/Mats Test Approach Docs";
    drive.copy("1QPwK7WWmZetdasI86rWtv8kXQn55T0RCOitCrmU5mdM", remotePath+"/new.docx");
    
//    drive.pull(remotePath+"/new.docx");
    
  }
  
  
//  public boolean pull(String remotePath) throws IOException, InterruptedException{
//    String[] cmd=new ArrayBuilder(GOOGLE_PULL).add(remotePath).build();
//    System.out.println("COMMAND(copy) INPUT  = ["+arrayToString(cmd)+"]");
//    String output=runCommand(cmd);
//    System.out.println("COMMAND(copy) OUTPUT = ["+output+"]");
//    boolean success=output.contains("Processing...\n");
//    System.out.println(success?"Successfully Copied ["+srcFileId+"] to Google Drive location ["+remotePath+"]":"Failure to Copy["+srcFileId+"] to Google Drive location ["+remotePath+"]: "+output);
//    return success;
//    
//  }
  
  public boolean copy(String srcFileId, String remotePath) throws IOException, InterruptedException{
    String[] cmd=new ArrayBuilder(GOOGLE_REMOTE_COPY).add(srcFileId).add(remotePath).build();
    System.out.println("COMMAND(copy) INPUT  = ["+arrayToString(cmd)+"]");
    String output=runCommand(cmd);
    System.out.println("COMMAND(copy) OUTPUT = ["+output+"]");
    boolean success=output.contains("Processing...\n");
    System.out.println(success?"Successfully Copied ["+srcFileId+"] to Google Drive location ["+remotePath+"]":"Failure to Copy["+srcFileId+"] to Google Drive location ["+remotePath+"]: "+output);
    return success;
  }
  
  
  
  @SuppressWarnings("unused")
  public void copyTemplate(String id, String newFileName/*, String exportType*/) throws IOException, InterruptedException{
    cleanup();
    
    String extn=FilenameUtils.getExtension(newFileName);
    
    String exportType=extn;
    if (extn.equals(exportType)) exportType=extn.startsWith("doc")?"doc":extn;
    if (extn.equals(exportType)) exportType=extn.startsWith("xls")?"xls":extn;
    String filename=FilenameUtils.getBaseName(newFileName);
    
    String copyCommand=String.format(GOOGLE_COPY_CMD, System.getProperty("user.name"), id, filename);
  }
  
  public void cleanup(){
    String googleDrivePath=String.format(GOOGLE_WORKING_FOLDER, System.getProperty("user.name"));
    File workingFolder=new File(googleDrivePath);
    if (googleDrivePath.contains("google")){
      System.out.println("recursively deleting: "+googleDrivePath);
      recursivelyDelete(workingFolder);
    }else{
      System.out.println("Not cleaning working folder unless it contains the name 'google' - for safety reasons");
      System.exit(0);
    }
  }
  
  private String arrayToString(String[] cmd){
    StringBuffer sb=new StringBuffer();
    for (String s:cmd){
      sb.append(s).append(" ");
    }
    return sb.toString();
  }
  
  // ---------------------------------------------
  // ------------ GOOGLE COMMANDS ----------------
  // ---------------------------------------------
  public File pull(String id, String format) throws IOException, InterruptedException{
//    String extn=FilenameUtils.getExtension(newFilename);
    String pullCommand=String.format(GOOGLE_PULL_CMD, System.getProperty("user.name"), format, id);
//    String pullCommand=String.format("/home/%s/.go/bin/drive pull -export "+format+" -force -quiet -id "+id+"", System.getProperty("user.name"));
    String pullOutput=runCommand(pullCommand);
    if (pullOutput.contains("Exported")) {
      Matcher matcher = Pattern.compile("to '(.+)'$").matcher(pullOutput);
      if (matcher.find()){
        return new File(matcher.group(1));
      }
    }
    return null;
  }
  public boolean remoteCopy(String filename, String remoteDestination) throws IOException, InterruptedException{
//    String copyCommand=String.format(GOOGLE_COPYBYNAME_CMD, System.getProperty("user.name"), filename, remoteDestination);
    
    String[] copyCommand=GOOGLE_COPYBYNAME_CMD;
    copyCommand[copyCommand.length-2]=filename;
    copyCommand[copyCommand.length-1]=remoteDestination;
    System.out.println("COMMAND INPUT  = ["+arrayToString(copyCommand)+"]");
    
//    String copyCommand=String.format("/home/%s/.go/bin/drive copy -force -quiet "+filename+" \""+remoteDestination+"\"", System.getProperty("user.name"));
//    System.out.println("COMMAND INPUT  = ["+copyCommand+"]");
    String output=runCommand(copyCommand, new File(String.format(GOOGLE_WORKING_FOLDER, System.getProperty("user.name"))));
    System.out.println("COMMAND OUTPUT = ["+output+"]");
    boolean success=output.contains("Processing...\n") && !(output.contains("Error 403") || output.contains("forbidden"));
//    boolean noPermissions=output.contains("Error 403") || output.contains("forbidden");
    System.out.println(success?"Successfully Copied ["+filename+"] to Google Drive location ["+remoteDestination+"]":"Failure to Copy["+filename+"] to Google Drive location ["+remoteDestination+"]: "+output);
    return success;
  }
  public boolean push(File file) throws IOException, InterruptedException{
    File workingFolder=file.getParentFile();
    String filename=file.getName();
//    String pushCommand=String.format("/home/%s/.go/bin/drive push -force -convert -quiet %s", System.getProperty("user.name"), filename);
//    String pushCommand=String.format(GOOGLE_PUSH_CMD, System.getProperty("user.name"), filename);
    String[] pushCommand=GOOGLE_PUSH_CMD2;
    pushCommand[pushCommand.length-1]=filename;
    
//    String pushCommand=String.format("/home/%s/.go/bin/drive push -force -quiet %s", System.getProperty("user.name"), filename);
    String output=runCommand(pushCommand, workingFolder);
    boolean success=output.contains(" 100.00 %");
    System.out.println(success?"Successfully Pushed ["+file.getName()+"] to Google Drive":"Failure to Push ["+file.getName()+"] to Google Drive");
    return success;
  }
  
//  public boolean push(File file, String remoteDestination) throws IOException, InterruptedException{
//    File workingFolder=file.getParentFile();
//    String filename=file.getName();
////    String pushCommand=String.format("/home/%s/.go/bin/drive push -force -convert -quiet %s", System.getProperty("user.name"), filename);
////    String pushCommand=String.format(GOOGLE_PUSH_CMD, System.getProperty("user.name"), filename);
//    String[] pushCommand=GOOGLE_PUSH_CMD2;
//    pushCommand[pushCommand.length-1]=filename;
////    pushCommand[pushCommand.length-2]=remoteDestination;
//    
//    System.out.println("COMMAND INPUT  = ["+arrayToString(pushCommand)+"]");
//    
////    String pushCommand=String.format("/home/%s/.go/bin/drive push -force -quiet %s", System.getProperty("user.name"), filename);
//    String output=runCommand(pushCommand, workingFolder);
//    boolean success=output.contains(" 100.00 %");
//    System.out.println(success?"Successfully Pushed ["+file.getName()+"] to Google Drive":"Failure to Push ["+file.getName()+"] to Google Drive");
//    return success;
//  }
  
  // ---------------------------------------------
  // ---------------- COMMON COMMANDS ------------
  // ---------------------------------------------
  private String runCommand(String command) throws IOException, InterruptedException{
    String googleDrivePath=String.format(GOOGLE_WORKING_FOLDER, System.getProperty("user.name"));
    File workingFolder=new File(googleDrivePath);
    return runCommand(command, workingFolder);
  }
  private String runCommand(String command, File workingFolder) throws IOException, InterruptedException{
//    String command2 = String.format(command, System.getProperty("user.name"));
    Process exec = Runtime.getRuntime().exec(command, null, workingFolder);
    exec.waitFor();
    String syserr = IOUtils.toString(exec.getErrorStream());
    String sysout = IOUtils.toString(exec.getInputStream());
    System.out.println("sysout=\""+sysout+"\"; syserr=\""+syserr+"\"");
    if (!sysout.contains("Processing...") && !sysout.contains("Resolving...") && !sysout.contains("Everything is up-to-date"))
      throw new RuntimeException("Error running google drive script: " + sysout);
    
    return sysout;
  }
  private String runCommand(String[] command) throws IOException, InterruptedException{
    return runCommand(command, new File(String.format(GOOGLE_WORKING_FOLDER, System.getProperty("user.name"))));
  }
  
  private String runCommand(String[] command, File workingFolder) throws IOException, InterruptedException{
    Process exec = Runtime.getRuntime().exec(command, null, workingFolder);
    exec.waitFor();
    String syserr = IOUtils.toString(exec.getErrorStream());
    String sysout = IOUtils.toString(exec.getInputStream());
    System.out.println("sysout=\""+sysout+"\"; syserr=\""+syserr+"\"");
    if (!sysout.contains("Processing...") && !sysout.contains("Resolving...") && !sysout.contains("Everything is up-to-date"))
      throw new RuntimeException("Error running google drive script: " + sysout);
    
    return sysout;
  }
  
  public File downloadFile(String fileId) throws IOException, InterruptedException {
    String command = String.format(GOOGLE_PULL_COMMAND, System.getProperty("user.name"), fileId);
    String googleDrivePath=String.format(GOOGLE_WORKING_FOLDER, System.getProperty("user.name"));
    
    File workingFolder=new File(googleDrivePath);
    System.out.println("Using working folder: "+workingFolder.getAbsolutePath());
    System.out.println("Downloading google file: "+fileId);
    System.out.println("Command: "+command);
    
    if (googleDrivePath.contains("google")){
      System.out.println("recursively deleting: "+googleDrivePath);
      recursivelyDelete(workingFolder);
    }else
      System.out.println("Not cleaning working folder unless it contains the name 'google' - for safety reasons");
    
    Process exec = Runtime.getRuntime().exec(command, null, workingFolder);
    
//    Process exec = Runtime.getRuntime().exec(command, null, new File("/home/mallen/Work/google_drive"));
    exec.waitFor();
    String syserr = IOUtils.toString(exec.getErrorStream());
    String sysout = IOUtils.toString(exec.getInputStream());
    System.out.println("sysout=\""+sysout+"\"; syserr=\""+syserr+"\"");
    if (!sysout.contains("Resolving...") && !sysout.contains("Everything is up-to-date"))
      throw new RuntimeException("Error running google drive script: " + sysout);
    if (!sysout.contains("Everything is up-to-date")) {
      // System.out.println("Do Nothing");
      // return null;
      // } else {
      Pattern p = Pattern.compile("to '(.+)'$");
      Matcher matcher = p.matcher(sysout);
      if (matcher.find()){
        String preFilePath = matcher.group(1);
        // System.out.println("Process the file: " + preFilePath);
        return new File(preFilePath);
      }
    }
    return null;
    // System.out.println(exec.exitValue());
  }
  
  public int getHeaderRow(){
    return 0;
  }
  
  public List<Map<String,String>> parseExcelDocument(File file, int maxColumns) {
    return parseExcelDocument(file, null, maxColumns); // default to the first sheet
  }
  
  public List<Map<String,String>> parseExcelDocument(File file, String sheetName, int maxColumns) {
    // parse excel file using apache poi
    // read out "tasks" and create/update solutions
    // use timestamp (column A) as the unique identifier (if in doubt i'll hash it with the requester's username)
    List<Map<String,String>> entries=new ArrayList<Map<String,String>>();
    FileInputStream in=null;
    try{
      in=new FileInputStream(file);
      XSSFWorkbook wb=new XSSFWorkbook(in);
      XSSFSheet s=sheetName==null?wb.getSheetAt(0):wb.getSheet(sheetName);
      
      for(int iRow=getHeaderRow()+1;iRow<=s.getLastRowNum();iRow++){
        Map<String,String> e=new HashMap<String,String>();
        for(int iColumn=0;iColumn<=maxColumns;iColumn++){
          if (s.getRow(getHeaderRow()).getCell(iColumn)==null) continue;
          String header=s.getRow(getHeaderRow()).getCell(iColumn).getStringCellValue();
          XSSFRow row = s.getRow(iRow);
          if (row==null) break; // next line/row
          XSSFCell cell=row.getCell(iColumn);
          if (cell==null) continue; // try next cell/column
          
          
          switch(cell.getCellType()){
          case XSSFCell.CELL_TYPE_NUMERIC: e.put(header, Integer.toString((int)cell.getNumericCellValue())); break;
          case XSSFCell.CELL_TYPE_STRING:  e.put(header, cell.getStringCellValue()); break;
//          case XSSFCell.CELL_TYPE_FORMULA:  break;
//          case XSSFCell.CELL_TYPE_BLANK:   e.put(header, ""); break;
          case XSSFCell.CELL_TYPE_BOOLEAN: e.put(header, cell.getStringCellValue()); break;
//          case XSSFCell.CELL_TYPE_ERROR:   e.put(header, ""); break;
          }
          
        }
        if (null!=e && e.size()==0){ // then we found an empty row, so exit?
          break;
        }
        entries.add(e);
      }
    }catch(FileNotFoundException e){
      e.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    }finally{
      IOUtils.closeQuietly(in);
    }
    return entries;
  }
  
  private void recursivelyDelete(File file){
    for(File f:file.listFiles()){
      if (!f.getName().startsWith(".") && f.isDirectory())
        recursivelyDelete(f);
      if (!f.getName().startsWith(".")){
        System.out.println("Deleting: "+f.getPath());
        f.delete();
      }
    }
  }
  
}



