<%@ page contentType="text/html; charset=gbk" %>
<%@ page import="webFrame.util.*"%>
<%@ page import="webFrame.app.control.RequestContext" %>
<%
response.setContentType("text/html; charset=gbk");
response.setHeader("Pragma","no-cache"); 
response.setHeader("Cache-Control","no-cache"); 
response.setDateHeader("Expires", 0); 
String msg = (String) RequestContext.get().getAttribute("msg");
String script = (String) RequestContext.get().getAttribute("script");
%>
<html>
    <HEAD>
        <TITLE>提示信息</TITLE>
        <link href="./css/yk.css" rel="stylesheet" type="text/css">
    </HEAD>
<body scroll="no" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" background="./images/desk_01.gif">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="30%" colspan="3" align="center" valign="middle"></td>
  </tr>
  <tr>
    <td width="40%" height="250" align="center" valign="middle" background="./images/error/bg.gif"></td>
    <td width="330" height="250" align="center" valign="middle" background="./images/error/bg.gif">
      <table width="330" height="250" border="0" cellpadding="0" cellspacing="0" bgcolor="E0EFF4">
        <tr>
          <td height="65" colspan="5" background="images/error/error_title_bg.gif"><img src="./images/error/error_title.gif" width="196" height="65"></td>
        </tr>
        <tr>
          <td width="10" height="10" colspan="2" rowspan="2"><img src="./images/error/error_1.gif" width="10" height="10"></td>
          <td height="1" bgcolor="3885B9"></td>
          <td width="10" height="10" colspan="2" rowspan="2"><img src="./images/error/error_2.gif" width="10" height="10"></td>
        </tr>
        <tr>
          <td height="9"></td>
        </tr>
        <tr>
          <td width="1" bgcolor="3885B9"></td>
          <td width="9"></td>
          <td width="100%" height="100%" align="center" valign="middle"><font color="red"><%=msg %></font></td>
          <td width="9"></td>
          <td width="1" bgcolor="3885B9"></td>
        </tr>
        <tr>
          <td width="1" bgcolor="3885B9"></td>
          <td width="9"></td>
          <td width="100%" height="100%" align="center" valign="middle"><input type="button" class="button" value="确定" onclick="<%=script %>"></td>
          <td width="9"></td>
          <td width="1" bgcolor="3885B9"></td>
        </tr>
        <tr>
          <td width="10" height="10" colspan="2" rowspan="2"><img src="./images/error/error_3.gif" width="10" height="10"></td>
          <td height="9"></td>
          <td width="10" height="10" colspan="2" rowspan="2"><img src="./images/error/error_4.gif" width="10" height="10"></td>
        </tr>
        <tr>
          <td height="1" bgcolor="3885B9"></td>
        </tr>
        <tr>
          <td height="15" colspan="5"></td>
        </tr>
        <tr bgcolor="046295">
          <td height="1" colspan="5"></td>
        </tr>
      </table>

	</td>
    <td width="40%" height="250" align="center" valign="middle" background="./images/error/bg.gif"></td>
  </tr>
  <tr>
    <td height="50%" colspan="3" align="center" valign="middle">&nbsp;</td>
  </tr>
</table>
</body>
</html>