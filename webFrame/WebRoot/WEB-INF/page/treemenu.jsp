<%@ page import="java.net.URLEncoder" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>批量导入软件信息</title>
        <%@include file="/inc/head.jsp"%>
        <%@include file="/inc/ztree.jsp"%>

        <style type="text/css">
			.description {color: #666;}
			.description div.d {padding-left: 16px; margin-left: 8px; color: #ff3f3f;line-height: 20px;}
			.description ol {
				list-style: none;
			}
			.description ol li {
				padding-left: .5em;
				line-height: 20px;
			}
			.description a img {border: none;}
			.description a {
				display: block; color: #0000ee; margin-left: 8px; line-height: 20px;}
		</style>

        <script type="text/javascript">

            $(function(){

                $("#test").click(function(){

                  /*地址栏传参使用encodeURI(URI),或者使用encodeURIComponent(param),其他方式都不可控*/
                   

                    <%--var url = "goDemo.do?name=<%=URLEncoder.encode("你好","GBk")%>&name1=<%=URLEncoder.encode("你好","GBk")%>";--%>
                    
                    var url = encodeURI("goDemo.do?name=不是encodeurl&name=不是1encodeurl&aa=很好encodeurl");  //encodeURI

                    $.post(url, function(obj){// tomcat 不设置编码 页面自己编码 ,UTF 正常.全部 都被iso8859-1重新编过


                    });

                     $.get(url, function(obj){// tomcat 不设置编码 页面自己编码 ,UTF 正常,都被iso8859-1重新编过

                    });


                    $.post('goDemo.do',{name:'你好post',aa:'不好post',name:'你好么post'}, function(obj){
                         // tomcat 不设置编码, auto 正常,可能未被tomcat iso8859 编过
                    });


                    $.get('goDemo.do',{name:'你好get',aa:'不好get',name:'你好么get',n:Math.random()}, function(obj){
                        //get会缓存,类似最上面的url传参 tomcat 不设置编码, utf 正常 ,被tomcat iso8859-1重新编过
                    });

                    
                   /* $.ajax({
                        type:'post',
                        url: "goDemo.do?aa=坑贴",
                        data:{name:"你好post",name1:"你好post",n:Math.random()},
                        dataType:"json",
                        success: function(){}
                    });*/


                    

                   /* $.ajax({  // get方式data{}传参,jquery自动encodeURI utf-8;; post方式 data{} 传参,浏览器也有可能会转码,
                        type:'post',
                        //application/x-www-form-urlencoded; charset=UTF-8 这种方式相当于tomcat编码设置为utf-8,后台不需在转,不推荐
                        //firefox 会自动按照上面的方式转码,导致乱码
                        //解决方案,都使用URL传参,中文做encodeURIComponent
                        contentType : "application/x-www-form-urlencoded",
                        data:{name:"你好",name1:"你好1",n:Math.random()},
                        url: encodeURI("goDemo.do"),
                        success: function(){}
                    });*/
                 })
            })

        </script>


	</head>
	<body>

		<form name="form1" id="form1" action="goDemo.do" <%--enctype="multipart/form-data"--%>
			method="post">
			<div style="padding: 18px 24px; height: 230px">
				<table style="margin: 0;">
					<tr>
						<td align="right" width="80">
							软件包位置：
						</td>
						<td>
                            <input type="text" readonly value="<%=params.get("zipPath") %>">
                            什么
							<input type="text" readonly="readonly" id="path" name="zipPath" />
							<input type="button" onclick="showMenu(); return false;" value="选择"/>
						</td>
					</tr>
				</table>
			</div>

			<table class="last" align="center">
				<tr>
					<td>
						<input type="submit" class="an1" value="更新" style="margin-left:104px;"/>
						<input type="button" id='test' class="an1" value="ajax"
							 style="margin-left:40px;"/>

                        	<input type="button" name="EUpload" id="EUpload"
						onclick="window.location.href='<%=path %>/文档.xls'"
						value="下载" />
					</td>
				</tr>
			</table>
		</form>

			<div id="menuContent"  style="overflow:scroll;height:180px; display:none;position: absolute;background: #E8FFE8">
				<ul id="treeDesk" class="ztree" style="margin-top:0; width:160px;"></ul>
			</div>
        <script type="text/javascript" src="<%=path %>/js.css.frame/autocomplete/pullmenu.js"></script>
		<script type="text/javascript">

            $(document).ready(function() {
                $.fn.zTree.init($("#treeDesk"), setting);
            });

            var setting = {
                async: {
                    enable: true,
                    url: "<%=request.getContextPath()%>/goFile$getDirs.do",
                    autoParam:["path"]     // 上面的id会和autoParam冲突自动传参
                },
                callback: {
                    onClick: onClick
                }
            };


            function onClick(e, treeId, treeNode) {
                var cityObj = $("#path");
                cityObj[0].value=treeNode.path;
                hideMenu();
            }

            function showMenu(){
                $("#menuContent").popMenu({ //
                    target: $("#path"),
                    position: 'left'
                })
            }

            /*效果同上,已封装*/
           /* function showMenu() {
                var cityObj = $("#path");
                var cityOffset = $("#path").offset();
                $("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
            }*/
            function hideMenu() {
                $("#menuContent").fadeOut("fast");
            }


            function cancel() {
                location.replace('about:blank');
                return false;
            }

            $('#form1').submit(function () {
                document.getElementById('progress').style.visibility = 'visible';
            });

        </script>

	</body>

</html>