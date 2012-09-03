<%@ page import="java.net.URLEncoder" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>批量导入软件信息</title>
        <%@include file="/inc/head.jsp"%>
        <script type="text/javascript" src="<%=path %>/js.css.frame/autocomplete/suggest.js"></script>
        <script type="text/javascript" src="<%=path %>/js.css.frame/autocomplete/pullmenu.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=path %>/js.css.frame/autocomplete/suggest.css"/>

        <style type="text/css">
        input{
            margin: 20px;
            width: 150px;
            height: 20px;
        }


		</style>

        <script type="text/javascript">
            $(function() {
                $("#helo").suggest({
                    width:200,
                    url: path + '/goDemo$autoComplete.do',
                    format: function(item){  //格式化显示字段
                        var a = item.split('|');
                        return a[1];
                    }

                });

                $("#aa").suggest({
                    width:300,
                    url: path + '/goDemo$autoComplete.do',
                    onEnter: function(obj){ //回车事件
                        alert(obj[0].value);
                    }
                })
            })

        </script>


	</head>
	<body>
    <label>类baidu下拉建议</label>
    <input type="text" id='helo' /> 输入'软件','demo','系统','终端' 查看效果
    <p></p>
    <label>类baidu下拉建议</label>
    <input type="text" id='aa' /> 输入'软件','demo','系统','终端' 查看效果
	</body>

</html>