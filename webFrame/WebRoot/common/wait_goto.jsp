<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="java.net.URLDecoder"%>
<%
response.setContentType("text/html; charset=utf-8");
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
	
</head>

<body scroll="no" style="display: block;">
	    <table id="winmsg" width="70%"  border="0" cellspacing="1" cellpadding="0">
	        <tr>
	          <td height="150px" align="center"><img src="images/loading.gif" /> </td>
	        </tr>
	      </table>
</body>
</html>
<script type="text/javascript">
<!--
location.href = "<%=src%>";
//-->
</script>
