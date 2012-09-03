<%@ page contentType="text/html; charset=utf8"%>
<%
response.setContentType("text/html; charset=utf8");
response.setHeader("Pragma","no-cache"); 
response.setHeader("Cache-Control","no-cache"); 
response.setDateHeader("Expires", 0); 
String src=request.getAttribute("src").toString();
System.out.println(src);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<style type="text/css">
<!--
html{
height: 100%;
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	width: 100%;
	height: 100%;
	background-image: url(images/bg.gif);
}

#winObj{
	margin-top: 50px;
	width:100%;
	height: 100%;
}
#winmsg{
	border: 0px solid #999999;
/*	background-color: #FFF7D2;*/
}
-->
</style>
<script type="text/javascript">
var timeout = 0, waitTime = 0;
function setWaitHint(){
	var stat = document.getElementById("docObj").document.readyState;
	if (stat == "complete"){
		clearTimeout(timeout);
		document.getElementById("winObj").style.display = "none";
		document.getElementById("docObj").style.visibility = "visible";
	}
	else{
		timeout = setTimeout("setWaitHint()", 100);
	}
}
function iniScreen(){
	var BH=document.body.offsetHeight;
	document.getElementById("docObj").style.height=BH;
}
//window.onresize =iniScreen;

window.onload=function(){
//iniScreen();
setWaitHint();
window.focus();
}

</script>
</head>

<body scroll="no" style="display: block;">
<table id="winObj"  border="0" align="center" cellpadding="0" cellspacing="1">
  <tr>
    <td align="center" valign="top">
    <table id="winmsg" width="70%"  border="0" cellspacing="1" cellpadding="0">
        <tr>
          <td height="150px" align="center"><img src="images/loading.gif" /> </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
 
<iframe id="docObj" style="visibility: hidden;"  src="<%= src %>" width="100%" height="100%"  scrolling="auto" frameborder="0"/>

</body>
</html>
