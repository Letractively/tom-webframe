<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
   <%@include file="/inc/head.jsp" %>
   <%@include file="/inc/easyui.jsp" %>

    <style type="text/css">


.myBlock {
    margin-left: 20px;
    width:300px;
	overflow:hidden;
	float:left;
	display:inline
}
.myBlock ul {
    height:30px;
	width:100%;
	overflow:hidden;
	zoom:1;
	list-style:none
}
.myBlock ul li {
    height:30px;
	float:left;
	padding:0;
	display:inline
}


        
</style>

     <script type="text/javascript">
    $(function(){

        /*var a = $("li").find(".tabs-close").toString();

        document.getElementById("bbac");

        document.getElementsByTagName("input");

        //document.getElementsByClassName(".class"); <!-- IE 没有这个方法-->
        
        alert($(document.getElementsByName("edit")).val());*/



        	$("#product0").JTUIscroll({
								   visible:3,
								   scroll:2,
								   speed:500
								   });

			$("#product2").JTUIscroll({
								   visible:2,
								   scroll:2,
								   auto:[true,2000],
								   speed:500
								   });





    })
        

    </script>

</head>

<body>

<h2>手动滚动</h2>
  <div class="myBlock" id="product0">
    <ul class="">
      <li>
          <a href="#" style="height:100%; background: #296F9E; color: #FFFFFF;cursor:pointer;" >横向</a>
      </li>
      <li>
        <a href="#" style="background: #296F9E; color: #FFFFFF;cursor:pointer;" >横向滚动</a>
      </li>
       <li>
        <a href="#" style="background: #296F9E; color: #FFFFFF;cursor:pointer;" >横向滚动</a>
      </li>
        <li>
        <a href="#" style="background: #296F9E; color: #FFFFFF;cursor:pointer;" >横向滚动</a>
      </li>
        <li>
        <a href="#" style="background: #296F9E; color: #FFFFFF;cursor:pointer;" >横向滚动</a>
      </li>
        <li>
        <a href="#" style="background: #296F9E; color: #FFFFFF;cursor:pointer;" >横向滚动滚动滚动滚动</a>
      </li>

    </ul>
  </div>
<br />
<br />
<h2>自动滚动</h2>
  <div class="myBlock" id="product2">
    <ul>
      <li>
          <a href="#" style="height:100%; background: #296F9E; color: #FFFFFF;cursor:pointer" >横向滚动1</a>
      </li>
      <li>
        <a href="#" style="background: #296F9E; color: #FFFFFF;cursor:pointer" >横向滚动2</a>
      </li>
       <li>
        <a href="#" style="background: #296F9E; color: #FFFFFF;cursor:pointer" >横向滚动3</a>
      </li>

    </ul>
  </div>


</body>
</html>
