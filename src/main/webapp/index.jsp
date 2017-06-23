<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>

<%@page import="
java.util.Map.Entry,
java.util.Map,
java.util.Set,
java.util.HashSet,
com.redhat.sso.sowgen.Controller,
com.redhat.sso.sowgen.PageBean,
com.redhat.sso.sowgen.model.Question,
com.redhat.sso.sowgen.model.Page,
java.util.List
"%>
<%@page contentType="text/html"%>
<%

PageBean bean=new PageBean();
if (null!=request.getParameter("reload")){
  bean.reset();
}

//if (null!=request.getParameter("submit")){
//  bean.save(request);
//}

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
.green span{
	color: #339933;
}
.red span{
	color: #AA3322;
}
</style>

<form action="generate.jsp">

<input type="hidden" id="consQty" name="consQty" />
<input type="hidden" id="snrconsQty" name="snrconsQty" />
<input type="hidden" id="archQty" name="archQty" />
<input type="hidden" id="pmQty" name="pmQty" />

<input type="hidden" id="consTot" name="consTot" />
<input type="hidden" id="snrconsTot" name="snrconsTot" />
<input type="hidden" id="archTot" name="archTot" />
<input type="hidden" id="pmTot" name="pmTot" />

  <div id="main-nav"></div>
	<div id="main-header">
		<div id="header">
			<center>
				<h1>Cloud Management: Task List</h1>
				<h3>Prepared for <input type="textbox" id="customerName" name="customerName" id="customerName"/></h3>
			</center>
		</div>
	</div>
	<div id="main-area">
		<div class="container">
			<div id="content-area">
				
				<div id="totals">
					<!--
					<table border="0" style="border-style: solid; border-width: 1px; border-radius: 5px; width:1200px">
					-->
					<table id="calculations">
						<thead style="background-color: #DDDDDD; color: black; font-weight: bold;">
							<tr>
								<td>Quantity</td>
								<td>Unit</td>
								<td>Term</td>
								<td>SKU</td>
								<td>Services Purchased</td>
								<td>Per Unit Prorated Fee</td>
								<td>Per Unit Fee</td>
								<td>Total Fee</td>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td><div id="pmhours"/></td>
								<td>hours</td>
								<td></td>
								<td>CM-GPS</td>
								<td>Consulting Services - Project Manager</td>
								<td>N/A</td>
								<td>$240.00</td>
								<td><div id="pmhourstotal"/></td>
							</tr>
							<tr>
								<td><div id="architecthours"/></td>
								<td>hours</td>
								<td></td>
								<td>CM-GPS</td>
								<td>Cloud Management Services - Senior Architect</td>
								<td>N/A</td>
								<td>$315.00</td>
								<td><div id="architecthourstotal"/></td>
							</tr>
							<tr>
								<td><div id="snrconsultinghours"/></td>
								<td>hours</td>
								<td></td>
								<td>CM-GPS</td>
								<td>Cloud Management Services - Senior Consultant</td>
								<td>N/A</td>
								<td>$285.00</td>
								<td><div id="snrconsultinghourstotal"/></td>
							</tr>
							<tr>
								<td><div id="consultinghours"/></td>
								<td>hours</td>
								<td></td>
								<td>CM-GPS</td>
								<td>Cloud Management Services - Consultant</td>
								<td>N/A</td>
								<td>$200.00</td>
								<td><div id="consultinghourstotal"/></td>
							</tr>
							<tr>
								<td colspan="7"></td>
								<td><strong><div style="border-top:1px solid rgb(217, 217, 217);" id="grandtotal"></div></strong></td>
							</tr>
						</tbody>
					</table>
				</div>
				<br/>
				<div>
					<div class="section">
						<h3>Solutions:</h3>
						<div style="width: 100%; margin-right: 0px;">
						<%for (Entry<String, List<String>> e:bean.getDefaults().entrySet()){%>
							<label><input type="radio" id="solution_<%=e.getKey()%>" name="solution" onclick="setDefaults<%=e.getKey()%>(this); total();" data-bind="checked: solution" value="<%=e.getKey()%>" /><span><%=e.getKey()%></span></label>
						<%}%>
						<!--
						<label><input type="radio" name="solution" onclick="reset(); total(); this.checked=true;" data-bind="checked: solution" value="Custom" /><span>Custom</span></label>
						-->
						<span style="margin-left: 20px;"><em>(Selecting additional tasks beyond those included will increase LoE and cost)</em></span>
						</div>
					</div>
				</div>
				<%for(Page p:bean.getPages2()){%>
				<div>
				  <div class="section">
						<div class="alert alert-danger" role="alert" style="display: none;"></div>
						<!--<h3><%=p.name%><span id="<%=p.name%>_info"></span></h3>-->
						<span style="font-size:14pt; font-weight:bold"><%=p.name%></span><span id="<%=p.name%>_info"></span><br/><br/>
						<%for(Question q:p.getQuestions()){%>
						  <%if ("Checkbox".equalsIgnoreCase(q.type)){%>
						  	<div style="width: 100%; margin-right: 0px;" <%=q.getVisibleIf()%>>&nbsp;&nbsp;<label id="label_<%=q.name%>"><%=q.getIndent()%><input type="Checkbox" id="<%=q.name%>" name="<%=q.name%>" data-count="<%="".equals(q.getIndent())%>" data-bind="checked: <%=q.name%>" onclick="total();<%=q.onclick%>"/><span><%=q.title%></span></label></div>
						  <%}else if ("Textbox".equalsIgnoreCase(q.type)){%>
						  	<div style="width: 100%; margin-right: 0px;" <%=q.getVisibleIf()%>>&nbsp;&nbsp;<label id="label_<%=q.name%>"><%=q.getIndent()%><span><%=q.title%>:</span></label>&nbsp;<input type="Textbox" id="<%=q.name%>" name="<%=q.name%>" data-count="<%="".equals(q.getIndent())%>" onblur="total();" value="" data-bind="value: <%=q.name%>" /></div>
						  <%}else if ("Other".equalsIgnoreCase(q.type)){%>
						    <div style="width: 100%; margin-right: 0px;" <%=q.getVisibleIf()%>>&nbsp;&nbsp;<label id="label_<%=q.name%>"><%=q.getIndent()%><input type="Checkbox" id="<%=q.name%>" name="<%=q.name%>" data-count="<%="".equals(q.getIndent())%>" data-bind="checked: <%=q.name%>" onclick="total();<%=q.onclick%>"/><span><%=q.title%></span>&nbsp;<input type="Textbox" id="txt_<%=q.name%>" name="txt_<%=q.name%>" data-bind="visible: <%=q.name%>()" onblur="total();" value="" /></label></div>
						  <%}else if ("Radiobutton".equalsIgnoreCase(q.type)){%>
						    <div style="width: 100%; margin-right: 0px;" <%=q.getVisibleIf()%>>&nbsp;&nbsp;&nbsp;
						    	<label><input id="<%=q.name%>" type="radio" name="<%=q.onclick%>" value="<%=q.name%>" onclick="total();" data-count="true" data-bind="checked: <%=q.onclick%>"><%=q.title%></label></input>
						    	<!--
						    	data-count="<%="".equals(q.getIndent())%>"
						    	data-count="<%="".equals(q.getIndent())%>" onblur="total();" 
						    	-->
						    </div>
						  <%}else if ("Header".equalsIgnoreCase(q.type)){%>
						    <br/><strong><%=q.title%></strong><br/><%if (q.sowText!=null){%>
						    <span style="font-size:9pt;"><em><%=q.sowText%></em></span>
						    <%}%>
						<%}%><%}%>
					</div>
				</div>
				<%}%>
				<div>
					<div class="section">
						<input type="submit" name="submit" onclick="if (document.getElementById('customerName').value==''){ alert('Please enter a customer name');return false;}return true;" value="Submit">
						<input type="button" value="Reset" onclick="reset();">
					</div>
				</div>
			</div>
		</div>
	</div>
</form>

<%if ("true".equalsIgnoreCase(request.getParameter("debug"))){%>
<pre data-bind="text: ko.toJSON($root, null, 2)"></pre>
<%}%>
<!--
-->

<script type="text/javascript">
<%
Set<String> radiobuttons=new HashSet<String>();
%>

  var viewModel = {
    <%for(Page p:bean.getPages2()){%>
      <%for(Question q:p.getQuestions()){%>
      	<%if ("other".equalsIgnoreCase(q.type)){%>
		      <%=q.name%>: ko.observable(""), txt_<%=q.name%>: ko.observable(""),
      	<%}else if ("Radiobutton".equalsIgnoreCase(q.type)){%>
      		<%radiobuttons.add(q.onclick);%>
      		<%=q.name%>: ko.observable(""), // redundant, could be removed if the reset method was modified to not reference it
        <%}else{%>
          <%=q.name%>: ko.observable(""),
        <%}%>
      <%}%>
    <%}%>
    
    <%for(String radio:radiobuttons){%>
    	<%=radio%>: ko.observable(),
    <%}%>
    
    solution: ko.observable("SmartStart"),
    
  };
  ko.applyBindings(viewModel);

  
  /*
  function getSolutionIncludedTasksInGroup(e){
    <%for(Entry<String, Integer> x:bean.getSolutionCosts2().entrySet()){%>if ("<%=x.getKey()%>"==e) return <%=x.getValue()%>;
    <%}%>
  }
  */
  
  
  <%for (Entry<String, List<String>> e:bean.getDefaults().entrySet()){%>
  function setDefaults<%=e.getKey()%>(e){
    reset();
    <%for(String value:e.getValue()){%>viewModel.<%=value%>(!viewModel.<%=value%>());
    <%}%>
  }
  <%}%>
  
  
  function valueOf(o){
    switch (o){
    <%for(Page p:bean.getPages2()){%><%for(Question q:p.getQuestions()){%>
  	  case "<%=q.name%>": return <%=q.value%>;<%}%><%}%>
    }
  }
  function groupOf(o){
    switch (o){
    <%for(Page p:bean.getPages2()){%><%for(Question q:p.getQuestions()){%>
  	  case "<%=q.name%>": return "<%=p.name%>";<%}%><%}%>
    }
  }
  
  
  
  function total(){
    resetx();
    var e=$('input[name="solution"]:checked').val();
    <%for (Entry<String, List<String>> e:bean.getDefaults().entrySet()){%>
    if ("<%=e.getKey()%>"==e){
      totalSolution(e);
    }else
    <%}%>
    {
      totalCustom();
    }
  }
  
  var includedSolutionTasks = {
    <%for (Entry<String, Integer> e:bean.getSolutionCosts2().entrySet()){%>'<%=e.getKey()%>': <%=e.getValue()%>,
    <%}%>
	}
	
  function totalSolution(e){
  	var base=includedSolutionTasks[e +"-Base Consulting Hours"];
	  var countMap = {};
	  
	  //use the map to determine if any 'included' tasks have been breached
    var x = document.getElementsByTagName("input");
    var t=0;
	  var i;
	  for (i = 0; i < x.length; i++) {
	    
	    if (x[i].type == "radio") {
	      if (x[i].checked){
		      var group=groupOf(x[i].id);
		      var val=parseInt(valueOf(x[i].id));
		      if (undefined==countMap[e+"-"+group]) countMap[e+"-"+group]=0;
			    if (x[i].dataset.count=="true"){
		      	countMap[e+"-"+group]=countMap[e+"-"+group]==undefined?0:countMap[e+"-"+group]+1;
		      }
		      var remaining=includedSolutionTasks[e+"-"+group]-countMap[e+"-"+group];
		      if (remaining<0){
			      if (val === parseInt(val,10)){ // integer check
			        t=t+val;
			        console.log("Adding "+val+" to the cost");
			      }
			    }
			    console.log("TotalSolution(Radio): group="+groupOf(x[i].name)+", name="+x[i].name +", type="+x[i].type +", hasLabel="+hasLabel +"");
			  }
	    }
	    
	    if (x[i].type == "checkbox") {
	      var hasLabel=document.getElementById("label_"+x[i].name)!=undefined;
	      if (hasLabel){
	        var labelText=document.getElementById("label_"+x[i].name).innerText;
	        document.getElementById("label_"+x[i].name).classList.remove("green");
	        document.getElementById("label_"+x[i].name).classList.remove("red");
	      }
	      
	      var group=groupOf(x[i].name);
	      
	      if (x[i].checked){
		      var val=parseInt(valueOf(x[i].name));
		      
		      if (undefined==countMap[e+"-"+group]) countMap[e+"-"+group]=0;
		      
		      // only do this if the item is at 0 increment
		      //console.log("X="+x[i].name+"="+x[i].dataset.count);
		      if (x[i].dataset.count=="true"){
		      	countMap[e+"-"+group]=countMap[e+"-"+group]==undefined?0:countMap[e+"-"+group]+1;
		      }
		      var remaining=includedSolutionTasks[e+"-"+group]-countMap[e+"-"+group];
		      //document.getElementById(group+"_info").innerHTML="&nbsp;&nbsp;&nbsp;&nbsp;("+remaining+" included items remaining for "+e+"'s)";
		      
		      
		      if (remaining<0){
		      	document.getElementById("label_"+x[i].name).classList.add("red");
		      }else{
		      	document.getElementById("label_"+x[i].name).classList.add("green");
		      }
		      //console.log(x[i].name+": remaining="+remaining +": classList="+document.getElementById("label_"+x[i].name).classList);
	        //console.log(x[i].name+": group="+group+": hasLabel="+hasLabel+": count?="+x[i].dataset.count+": color="+document.getElementById("label_"+x[i].name).classList);

		      
		      //TODO: calculate overage in hours by category and put that in the section header info above
		      
		      if (remaining<0){
			      if (val === parseInt(val,10)){ // integer check
			        t=t+val;
			        console.log("Adding "+val+" to the cost");
			      }
			    }
	      }
	      
	      //alert("x = ["+x[i]+"]; id = ["+x[i].name+"]; group = ["+group+"_info"+"]; obj = ["+document.getElementById(group+"_info")+"]")
	      if (group!=undefined) // can happen when the object is an "other" box
	      	document.getElementById(group+"_info").innerHTML="&nbsp;&nbsp;&nbsp;&nbsp; - "+e+"s include "+includedSolutionTasks[e+"-"+group]+" "+group+" ("+(countMap[e+"-"+group]==undefined?0:countMap[e+"-"+group])+" selected)";
	      
	      console.log("TotalSolution: group="+groupOf(x[i].name)+", name="+x[i].name +", type="+x[i].type +", hasLabel="+hasLabel +", labelClasses="+(hasLabel?document.getElementById("label_"+x[i].name).classList:""));
	      
	    }
	  }
	  //alert("extra time:"+t);
	  
	  
	  var consultinghours=base+t;
	  var architecthours=consultinghours<160?40:40+((consultinghours/40)*8);
	  var pmhours=consultinghours<40?8:(consultinghours/40)*8;
  	
  	if ("Pilot"==e){
  		return updateCosts(e, consultinghours/2, consultinghours/2, 240, 160);
  	}else{
  		return updateCosts(e, 0, consultinghours, architecthours, pmhours);
  	}
  }
  
  function resetx(){
  	// remove any green/red items
  	var x = document.getElementsByTagName("input");
	  var i;
	  for (i = 0; i < x.length; i++){
	    if (undefined!=document.getElementById("label_"+x[i].name)){
  	    document.getElementById("label_"+x[i].name).classList.remove("red");
  	    document.getElementById("label_"+x[i].name).classList.remove("green");
  	// remove any info on the sections
    	  document.getElementById(groupOf(x[i].name)+"_info").innerHTML="";
    	}
    	//console.log("Reset: "+x[i].name);
  	}
  	
  }
  
  function totalCustom(){
  	//alert("totalCustom() Called");
    var x = document.getElementsByTagName("input");
    var t=0;
	  var i;
	  for (i = 0; i < x.length; i++) {
	    if (x[i].type == "checkbox") {
	      if (x[i].checked){
		      var val=parseInt(valueOf(x[i].name));
		      var group=groupOf(x[i].name);
		      if (val === parseInt(val,10)){ // integer check
		        t=t+val;
		      }
	      }
	    }
	  }
	  
	  var consultinghours=t;
	  var architecthours=consultinghours<160?40:40+((consultinghours/40)*8);
	  var pmhours=consultinghours<40?8:(consultinghours/40)*8;
	  
	  return updateCosts(0, consultinghours, architecthours, pmhours);
  }
  
  function updateCosts(solution, consultinghours, snrconsultinghours, architecthours, pmhours){
	  var totalConsultingHours=toMoney(200*consultinghours);
	  var totalSnrConsultingHours=toMoney(285*snrconsultinghours);
	  var totalArchitectHours=toMoney(315*architecthours);
	  var totalPmHours=toMoney(240*pmhours);
	  
	  var grandtotal=(285*(snrconsultinghours+consultinghours)) + (315*architecthours) + (240*pmhours);

	  document.getElementById("consultinghours").innerHTML=consultinghours;
	  document.getElementById("consultinghourstotal").innerHTML=totalConsultingHours;
	  document.getElementById("consQty").value=document.getElementById("consultinghours").innerHTML;                             //hidden fields 
	  document.getElementById("consTot").value=document.getElementById("consultinghourstotal").innerHTML;                        //hidden fields

	  document.getElementById("snrconsultinghours").innerHTML=snrconsultinghours;
	  document.getElementById("snrconsultinghourstotal").innerHTML=totalSnrConsultingHours;
	  document.getElementById("snrconsQty").value=document.getElementById("snrconsultinghours").innerHTML;                       //hidden fields
	  document.getElementById("snrconsTot").value=document.getElementById("snrconsultinghourstotal").innerHTML;                  //hidden fields
	  
	  document.getElementById("architecthours").innerHTML=architecthours;
	  document.getElementById("architecthourstotal").innerHTML=totalArchitectHours;
	  document.getElementById("archQty").value=document.getElementById("architecthours").innerHTML;
	  document.getElementById("archTot").value=document.getElementById("architecthourstotal").innerHTML;
	  
	  document.getElementById("pmhours").innerHTML=pmhours;
	  document.getElementById("pmhourstotal").innerHTML=totalPmHours;
	  document.getElementById("pmQty").value=document.getElementById("pmhours").innerHTML;
	  document.getElementById("pmTot").value=document.getElementById("pmhourstotal").innerHTML;
	  
	  
	  document.getElementById("grandtotal").innerHTML=toMoney(grandtotal);
	  
	  
  }
  
  function toMoney(value){
    return '$' + value.toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");;
  }
  
  
  function reset(){<%for(Page p:bean.getPages2()){%><%for(Question q:p.getQuestions()){%>
    viewModel.<%=q.name%>("");<%}%><%}%>
  }
  
  $( document ).ready(function() {
    document.getElementById("solution_SmartStart").click();
  });

  
  
</script>
