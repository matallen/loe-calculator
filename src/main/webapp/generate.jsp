<%@page import="
java.util.Map.Entry,
java.util.Map,
com.redhat.sso.sowgen.Controller,
com.redhat.sso.sowgen.PageBean,
com.redhat.sso.sowgen.model.Question,
com.redhat.sso.sowgen.model.Page,
java.util.List
"%>
<%@page contentType="text/html"%>
<%

PageBean bean=new PageBean();

String path="";
String error="";
if (null!=request.getParameter("submit")){
  path=bean.save(request);
  error=bean.getError();
}

%>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<!--
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
-->
<script src="http://knockoutjs.com/downloads/knockout-3.2.0.js" type="text/javascript"></script>
<style>
body{
  font-family: arial;
}
.section{
  border-radius: 15px;
  background: #EEEEEE;
  padding:7px;
  margin:10px;
}
#main-area:before{
  top:100px;
  content:'';
  -webkit-transform: skew(0, -7deg);
  transform: skew(0, -7deg);
  position: absolute;
  left: 0;
  right: 0;
  top: 30px;
  height: 240px;
  background: #ffffff;
}
#main-area{
  background-color: #f7f9fb;
}
#main-header{
	padding: 20px 0;
  position: relative;
  z-index: 2;
  
}
.container{
  margin: 0 auto;
  max-width: 1120px;
  padding: 0 80px;
  padding-bottom: 10px;
  padding-top: 10px;
  box-sizing: border-box;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  position: relative;
  background-color: white;
  
      box-shadow: 0 0 30px rgba(7, 51, 84, 0.17);
    -webkit-box-shadow: 0 0 30px rgba(7, 51, 84, 0.17);
    -moz-box-shadow: 0 0 30px rgba(7, 51, 84, 0.17);
    padding: 4px 0;
    
    border-radius: 30px;
    -webkit-border-radius: 30px;
    -moz-border-radius: 30px;
    
}
#content-area{
  margin: 0;
  padding: 15px;
  display: block;
  border: 0;
  outline: 0;
  font-size: 100%;
  vertical-align: baseline;
  background: 0 0;
}
body{
    line-height: 16px;
    font-family: 'Poppins', sans-serif;
    font-size: 16px;
    text-align: left;
    color: #20292f;
    background-color: #fff;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    padding-top: 80px;
    font-weight: 400;
    transition: padding .8s ease;
    -webkit-transition: padding .8s ease;
    -moz-transition: padding .8s ease;
}
#calculations{
  border-style: solid;
  border-width: 1px;
  border-radius: 5px;
  width: 100%;
}
.error{
	color: #AA3333;
}
.mylink{
    font-family: 'Poppins', sans-serif;
    font-size: 16px;
    color: black;
}
</style>


  <div id="main-nav"></div>
	<div id="main-header">
		<div id="header">
		</div>
	</div>
	<div id="main-area">
		<div class="container">
			<div id="content-area">
			
			<center>
				<span class="error"><%=error%></span>
				<br/><br/>
				<div><span class="mylink"><a href="./Download?pathId=<%=path%>">Download</a></span><span class="details"> - Directly download to your computer and re-upload Google Drive</span></div>
				<div><span class="mylink">Open the <a href="https://drive.google.com/drive/u/0/folders/0B4Fi8y91v4gyMUM3UVZ2V21hOWM">Google Drive</a></span><span class="details"> location for LoE estimates</span>
				<div><span class="mylink"><a href="index.jsp">New Estimate</a></span><span class="details"> - Start a new LoE estimate</span>
			</center>
				
				
			</div>
		</div>
	</div>
	

<pre data-bind="text: ko.toJSON($root, null, 2)"></pre>

