<%@ page language="java" import="java.util.*" pageEncoding="uTF-8"%>
<!DOCTYPE HTML "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@include file="/inc/head.jsp" %>
      
	<link rel="stylesheet" type="text/css" >
	-->

      <style>
          .grid {
              border: 1px solid #99BBE8;
              border-collapse: collapse;
              color: #333333;
              font-size:12px;
              margin: 10px auto;
              width: 98%;
          }

          .grid .name{
              background: none repeat scroll 0 0 #E8FFE8;
          }
          .grid tr td {
               border: 1px dotted #CCCCCC;
          }
      </style>

      <script type="text/javascript">
        $(function(){
            $("#button").bind("click", function(){
                $("#tb").append("<tr><td><input/></td><td><input/></td><td><input/></td><td><input/></td></tr>");
            })
        })
          
      </script>
  </head>
  
  <body>


  <table class="grid">
      <tbody id="tb">
      <tr>
          <td> 备注：</td>
          <td colspan='3' align='center'> <%-- 两个按钮会自动居中,只要设置按钮间距--%>
            <input type='button' id='button' value='添加'/>
            <input type='button' style=' margin-left:100px' value='Button2'/>
          </td>
      </tr>
      <tr>
          <td width="13%"> 任务名称：</td>
          <td width="27%"> 还原“win7 64 万能”</td>
          <td width="13%"> 任务模式： </td>
          <td width="27%">  </td>
      </tr>
      <tr class="name">
          <td> 任务类型：</td> 
          <td> 还原 </td>
          <td> 任务状态：</td>
          <td> 成功 </td>
      </tr>
      <tr>
          <td> 还原类型：</td>
          <td> 系统分区 </td>
          <td> 执行次数：</td>
          <td>  1 </td>
      </tr>
      <tr class="name">
          <td> 所属分组：</td>
          <td> 江苏信易合 </td>
          <td> 创建时间：</td>
          <td> 2012/07/05 14:36:49 </td>
      </tr>

      </tbody>
  </table>
  </body>
</html>
