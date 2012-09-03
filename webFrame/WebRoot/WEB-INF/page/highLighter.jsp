<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
   <%@include file="/inc/head.jsp" %>
   <link rel="stylesheet" type="text/css" href="<%=path %>/js.css.frame/highlighter/shCore.css"/>
    <script type="text/javascript" src="<%=path %>/js.css.frame/highlighter/shCore.js"></script>

</head>

<body>

<pre id='pre' class="brush: java; auto-links: false; ">
<%=request.getAttribute("source")%>
</pre>
</body>
</html>
