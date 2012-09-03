<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/inc/head.jsp" %>
<title>My JSP 'QueryDetails.jsp' starting page</title>
<style type="text/css">
html {
	overflow-y: auto;
}

a {
	text-decoration: none;
}

a:link {
	text-decoration: none;
	color: blue;
}

a:active {
	text-decoration: blink;
}

a:hover {
	text-decoration: underline;
	color: red;
}

a:visited {
	text-decoration: none;
	color: green;
}
</style>

<!-- 照片查看 -->
<style type="text/css">
body {
	font-size: 12px;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

ul,li {
	list-style: none;
	margin: 0px;
	padding: 0px;
}

img {
	border: 0;
}

.focus {
	width: 440px;
	height: 330px;
	overflow: hidden;
	position: relative;
	margin: 10px auto;
}

.focus ul {
	height: 380px;
	position: absolute;
}

.focus ul li {
	float: left;
	width: 440px;
	height: 330px;
	overflow: hidden;
	position: relative;
	background: #000;
}

.focus ul li div {
	position: absolute;
	overflow: hidden;
}

.focus .btnBg {
	position: absolute;
	width: 440px;
	height: 20px;
	left: 0;
	bottom: 0;
	background: #000;
	display: none;
}

.focus .btn {
	position: absolute;
	width: 420px;
	height: 23px;
	padding: 0px 10px 0px 10px;
	right: 0;
	bottom: 6px;
	text-align: right;
}

.focus .btn span {
	display: inline-block;
	_display: inline;
	_zoom: 1;
	width: 25px;
	height: 24px;
	line-height: 24px;
	text-align: center;
	_font-size: 0;
	margin-left: 5px;
	cursor: pointer;
	background: #fff;
}

.focus .btn span.on {
	background: #fff;
}

.focus .preNext {
	width: 45px;
	height: 100px;
	position: absolute;
	top: 90px;
	background: url(../common/css/imgs/sprite.png) no-repeat 0 0;
	cursor: pointer;
}

.focus .pre {
	left: 0;
}

.focus .next {
	right: 0;
	background-position: right top;
}
</style>
<script>
	function RYClick() {
		CloseAllDiv();
		$("#divRY").show();
		getrys();
	}
	function JDHClick() {
		CloseAllDiv();
		$("#divJDH").show();
		getjdhs();
	}
	function KCHClick() {
		CloseAllDiv();
		$("#divKCH").show();
		getkchs();
	}
	function ZPClick() {
		CloseAllDiv();
		$("#divZP").show();
		getpics();
	}

	function CloseAllDiv() {
		$("#divRY").hide();
		$("#divJDH").hide();
		$("#divZP").hide();
		$("#divKCH").hide();
	}
</script>

<script type="text/javascript">
	function sl() {
		jQuery.focus = function(slid) {
			var sWidth = $(slid).width(); //获取焦点图的宽度（显示面积）
			var len = $(slid).find("ul li").length; //获取焦点图个数
			var index = 0;
			var picTimer = 1;

			//以下代码添加数字按钮和按钮后的半透明条，还有上一页、下一页两个按钮
			var btn = "<div class='btnBg'></div><div class='btn'>";
			for ( var i = 0; i < len-1; i++) {//修改   把多加的list去除
				var ii = i + 1;
				btn += "<span>" + ii + "</span>";
			}
			btn += "</div><div class='preNext pre'></div><div class='preNext next'></div>";
			$(slid).append(btn);
			$(slid).find("div.btnBg").css("opacity", 0.5);

			//为小按钮添加鼠标滑入事件，以显示相应的内容
			$(slid + " div.btn span").css("opacity", 0.4).mouseenter(
					function() {
						index = $(slid + " .btn span").index(this);
						showPics(index);	
					}).eq(0).trigger("mouseenter");

			//上一页、下一页按钮透明度处理
			$(slid + " .preNext").css("opacity", 0.2).hover(function() {
				$(this).stop(true, false).animate({
					"opacity" : "0.5"
				}, 300);
			}, function() {
				$(this).stop(true, false).animate({
					"opacity" : "0.2"
				}, 300);
			});

			//上一页按钮
			$(slid + " .pre").click(function() {
				index -= 1;
				if (index == -1) {
					index = len - 1;
				}
				showPics(index);
			});

			//下一页按钮
			$(slid + " .next").click(function() {
				index += 1;
				if (index == len) {
					index = 0;
				}
				showPics(index);
			});

			//本例为左右滚动，即所有li元素都是在同一排向左浮动，所以这里需要计算出外围ul元素的宽度
			$(slid + " ul").css("width", sWidth * (len));

			//鼠标滑上焦点图时停止自动播放，滑出时开始自动播放
			$(slid).hover(function() {
			
				clearInterval(picTimer);
			}, function() {
				picTimer = setInterval(function() {
					showPics(index);
					index++;
					if (index == len) {
						index = 0;
					}
				}, 4000); //此4000代表自动播放的间隔，单位：毫秒
			}).trigger("mouseleave");

			//显示图片函数，根据接收的index值显示相应的内容
			function showPics(index) { //普通切换
				var nowLeft = -index * sWidth-10*index; //根据index值计算ul元素的left值
				$(slid + " ul").stop(true, false).animate({
					"left" : nowLeft
				}, 300); //通过animate()调整ul元素滚动到计算出的position
				$(slid + " .btn span").removeClass("on").eq(index).addClass(
						"on"); //为当前的按钮切换到选中的效果
				$(slid + " .btn span").stop(true, false).animate({
					"opacity" : "0.4"
				}, 300).eq(index).stop(true, false).animate({
					"opacity" : "1"
				}, 300); //为当前的按钮切换到选中的效果
			}
		};
	}

function download(){ 
	var id = $("#jdxxid").val();
	this.d1.action = "<%=path%>poi/exprotAllRys.action?id="+id;
	this.d1.submit();
}
function downfile(url){
	window.location.href = "<%=path%>Files/Silverlight.exe";
}
function downloadpic(imagePathURL){
        //如果中间IFRAME不存在，则添加   
        if(!document.getElementById("_SAVEASIMAGE_TEMP_FRAME"))   
            $('<iframe style="display:none;" id="_SAVEASIMAGE_TEMP_FRAME" name="_SAVEASIMAGE_TEMP_FRAME" onload="_doSaveAsImage();" width="0" height="0" src="about:blank"></iframe>').appendTo("body");           
           
        if(document.all._SAVEASIMAGE_TEMP_FRAME.src!=imagePathURL){   
            //图片地址发生变化，加载图片   
            document.all._SAVEASIMAGE_TEMP_FRAME.src = imagePathURL;   
        }else{   
            //图片地址没有变化，直接另存为   
            _doSaveAsImage();   
        }   
    }   
    function _doSaveAsImage(){           
        if(document.all._SAVEASIMAGE_TEMP_FRAME.src!="about:blank")   
            document.frames("_SAVEASIMAGE_TEMP_FRAME").document.execCommand("SaveAs");           
}
</script>
<style type="text/css">
img {
	border: none;
}

ul,li {
	margin: 0;
	padding: 0;
}

li {
	list-style: none;
	float: left;
	display: inline;
	margin-right: 10px;
}

#imglist img {
	width: 150px;
	height: 120px;
}

#imgshow {
	position: absolute;
	border: 1px solid #ccc;
	background: #333;
	padding: 5px;
	color: #fff;
	display: none;
	width: 100px;
	height: 100px;
}
</style>

</head>

<body>
	<form action="${pageContext.request.contextPath}/poi/exprotAllRys.action" id="d1"  accept-charset="GBK" name="d1" method="post">
	<table id="divsup" class="sups" width="100%" border="0" cellspacing="0"
		cellpadding="0">
		<tr>
			<td
				style="background-image: url('/djjd/common/css/imgs/tab_03.gif'); background-repeat: no-repeat;
                            width: 12px; height: 30px;"></td>
			<td
				style="background-image: url('/djjd/common/css/imgs/tab_05.gif'); background-repeat: repeat-x;"><img
				src="/djjd/common/css/imgs/clipped_id.gif" width="16" height="16" />&nbsp;&nbsp;接待信息查询</td>
			<td
				style="background-image: url('/djjd/common/css/imgs/tab_07.gif'); background-repeat: no-repeat;
                            width: 16px;"></td>
		</tr>
		<tr>
			<td
				style="background-image: url('/djjd/common/css/imgs/tab_12.gif'); background-repeat: repeat-y;"></td>
			<td>
				<table width="100%" border="0" cellspacing="1" cellpadding="0"
					bgcolor="b5d6e6" style="margin-top: 3px;">

					<tr>

						<td class="td" width="25%">接待方案名称：</td>
						<td bgcolor="#FFFFFF"><input value="${jdxx.JDMC}"
							disabled="disabled" type="text"> <input id="jdxxid"
							value="${jdxx.ID}" style="display: none;" /></td>



						<td class="td" width="25%">接待部门：</td>
						<td bgcolor="#FFFFFF"><input value="${jdxx.JDDW}"
							disabled="disabled" type="text"></td>
					</tr>
					<tr>


						<td class="td">到达时间：</td>
						<td bgcolor="#FFFFFF"><input value="${jdxx.JDSJ}"
							disabled="disabled" type="text"></td>

						<td class="td">离开时间：</td>
						<td bgcolor="#FFFFFF"><input value="${jdxx.LKSJ}"
							disabled="disabled" type="text">
						</td>
					</tr>
					<tr>
						<td class="td">来访单位：</td>
						<td bgcolor="#FFFFFF"><input value="${jdxx.KCDW}"
							disabled="disabled" type="text"></td>

						<td class="td">所属地区：</td>
						<td bgcolor="#FFFFFF"><select name="select" id="select"
							disabled="disabled">
								<option>${jdxx.SHENG}</option>
						</select> &nbsp;省&nbsp;&nbsp;&nbsp; <select name="select2" id="select2"
							disabled="disabled">
								<option>${jdxx.SHI}</option>
						</select> &nbsp;市</td>


					</tr>
					<tr>
						<td class="td" width="25%">考察涵：</td>
						<td bgcolor="#FFFFFF">
						<s:if test='#request.jdxx.FILESIZE.equals("0") '>无下载</s:if>	
						<s:else>
						<%-- <a href="javascript:downfile('<s:property value="#request.jdxx.JDHURL" /> ')" >${jdxx.FILENAME}</a></s:else> --%>
						<%-- <a href="<s:property value="#request.jdxx.JDHURL" /> " >${jdxx.FILENAME}</a></s:else> --%>
						aaaaaaaaaaaa <a href="<%=path%>/文档.xls" >sdfas</a></s:else>
						</td>
						<td class="td" width="25%">来访人数：</td>
						<td bgcolor="#FFFFFF"><input value="${jdxx.RS}"
							disabled="disabled" type="text"></td>
						<!-- <td bgcolor="#FFFFFF" colspan="2"></td> -->
						
					<tr>
						<td class="td">考察主题：</td>
						<td bgcolor="#FFFFFF" colspan="3"><textarea
								style="width: 850px;height: 50px;">${jdxx.KCZT}</textarea></td>
					</tr>
					<tr>
						<td class="td">备注：</td>
						<td bgcolor="#FFFFFF" colspan="3"><textarea
								style="width: 850px;height: 50px;">${jdxx.BZ}</textarea></td>
					</tr>
				</table></td>
			<td
				style="background-image: url('/djjd/common/css/imgs/tab_15.gif'); background-repeat: repeat-y; background-position: right;"></td>
		</tr>


		<tr>
			<td
				style="background-image: url('/djjd/common/css/imgs/tab_18.gif'); background-repeat: no-repeat;
                            height: 35px;"></td>
			<td
				style="background-image: url('/djjd/common/css/imgs/tab_19.gif'); background-repeat: repeat-x;"></td>
			<td
				style="background-image: url('/djjd/common/css/imgs/tab_20.gif'); background-repeat: no-repeat;
                            "></td>
		</tr>
	</table>
	</form>
	<table id="divsup" class="sups" width="100%" border="0" cellspacing="0"
		cellpadding="0">
		<tr>
			<td
				style="background-image: url('/djjd/common/css/imgs/tab_03.gif'); background-repeat: no-repeat;
                            width: 12px; height: 30px;"></td>
			<td
				style="background-image: url('/djjd/common/css/imgs/tab_05.gif'); background-repeat: repeat-x;"><img
				src="/djjd/common/css/imgs/clipped_id.gif" width="16" height="16" />&nbsp;&nbsp;接待情况</td>
			<td
				style="background-image: url('/djjd/common/css/imgs/tab_07.gif'); background-repeat: no-repeat;
                            width: 16px;"></td>
		</tr>
		<tr>
			<td
				style="background-image: url('/djjd/common/css/imgs/tab_12.gif'); background-repeat: repeat-y;"></td>
			<td>
				<table width="100%" border="0" cellspacing="1" cellpadding="0">
					<tr>
						<td>
							<!--嫌疑人Tab-->
							<div id="tab">
								<ul id="tabsup">
									<li><a class="iconok" onclick="RYClick();"
										href="javascript:change_tab(1);">&nbsp;&nbsp;&nbsp;&nbsp;来访人员</a>
									</li>
									<li><a class="iconok" onclick="JDHClick();"
										href="javascript:change_tab(2);">&nbsp;&nbsp;&nbsp;&nbsp;接待方案</a>
									</li>
									<li><a class="iconok" onclick="ZPClick();"
										href="javascript:change_tab(3);">&nbsp;&nbsp;&nbsp;&nbsp;照片</a>
									</li>
									<li><a class="iconok" onclick="download();"
										>&nbsp;&nbsp;&nbsp;&nbsp;人员导出</a>
									</li>
									<!-- 	<li><a class="iconok" onclick="KCHClick();"
										href="javascript:change_tab(4);">&nbsp;&nbsp;&nbsp;&nbsp;考察涵</a>
									</li> -->
								</ul>
							</div></td>
					</tr>
					<tr>
						<td>

							<div id="divRY" style="display: none;">
								<!--上传文件列表-->
								<div id="datalist1" style="display: none;">
									<table id="tablerys" width="100%" border="0" cellpadding="0"
										cellspacing="1" bgcolor="b5d6e6" style="margin-top:6px;">
									</table>
								</div>
								<script type="text/javascript">
									//初始化上传空间

									//获取文件列表
									function getrys(id) {
										if (id == "") {
											alert("嫌疑人编号和案件编号以及电子证据编号不能为空");
											return;
										}
										var jdxxid = $("#jdxxid").val();
										$
												.ajax({
													type : "POST",
													url : "${pageContext.request.contextPath}/Query/GetRYs.action",
													timeout : 60000,
													data : "jdxxid=" + jdxxid,
													error : function(
															xmlhttprequest,
															textstatus,
															errorthrown) {
														alert(errorthrown);
													},
													success : function(data) {
														getrycomlete(data);
													},
													complete : function(data) {
													},
													dataType : 'json'
												});
									}

									function getrycomlete(data) {
										$("#tablerys").empty();
										if (data == null || data == "") {

										} else {
											if (typeof (data.error) != "undefined") {
												alert(data.des);
												return;
											}
											//初始化案件嫌疑人	
											$("#tablerys")
													.append(
															$("<tr align=\"center\">"
																	+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\"></td>"
																	+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">序号</td>"
																	+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">姓名</td>"
																	+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">性别</td>"
																	+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">职务</td>"
																	+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">电话</td>"
																	+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">备注</td>"
																	+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">照片预览</td>"
																	+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">操作</td>"
																	+ "</tr>"));
											for ( var i = 0; i < data.length; i++) {
												var $li_1 ="";
												var img = "<img id=\"fileimg"+data[i].id+"\" width=\"15\" height=\"15\" alt='备注已完成' src='/djjd/common/css/imgs/ok.png'/>";
												if(data[i].IMGSIZE == "0"){
														$li_1 = $("<tr height=\"22\" class=\"file\" align='center'><td bgcolor='White'>"
															+ img
															+ "</td><td bgcolor='White'>"
															+ (i + 1)
															+ "</td><td bgcolor='White'>"
															+ data[i].XM
															+ "</td><td bgcolor='White'>"
															+ data[i].XBname
															+ "</td><td bgcolor='White'>"
															+ data[i].ZW
															+ "</td><td bgcolor='White'>"
															+ data[i].LXDH
															+ "</td><td bgcolor='White'>"
															+ data[i].BZ
															+" </td> "
															+"<td bgcolor='White'><img width='20' height='20' src='/djjd/common/css/imgs/clipped_id.gif' /></td>"
															+ "<td bgcolor='White'>无下载 &nbsp; &nbsp;</td>"
															+ " ");
												}else{
													 $li_1 = $("<tr height=\"22\" class=\"file\" align='center'><td bgcolor='White'>"
															+ img
															+ "</td><td bgcolor='White'>"
															+ (i + 1)
															+ "</td><td bgcolor='White'>"
															+ data[i].XM
															+ "</td><td bgcolor='White'>"
															+ data[i].XBname
															+ "</td><td bgcolor='White'>"
															+ data[i].ZW
															+ "</td><td bgcolor='White'>"
															+ data[i].LXDH
															+ "</td><td bgcolor='White'>"
															+ data[i].BZ
															+" </td> "
														/* 	+ "</td><td bgcolor='White'><a class='tt'><img width='16' height='16' src='/djjd/common/css/imgs/clipped_id.gif' alt=' "
															+ data[i].ID */
															+"<td bgcolor='White'><a class='tt'><img width='20' height='20' src='"
															+ data[i].URL
															+ "'/></a></td><td bgcolor='White'> "
															/* + "<a onclick='GetDetails("
															+ data[i].ID
															+ ")');'><img src='/djjd/common/css/imgs/edt.gif' width='15' height='15' />查看详情</a>&nbsp; &nbsp; " */
															+ "<img src='/djjd/common/css/imgs/edt.gif' width='15' height='15' /><a href=\"javascript:downloadpic('"+data[i].URL+"');\" >下载</a>"
															/* + data[i].URL */
															+ " &nbsp; &nbsp;</tr>");
												
												}
													
												$("#tablerys").append($li_1);
											}
										}
										if ($("#tablerys").html() != "") {
											$("#datalist1").show();
														$(function() {
														xOffset = 10;
														yOffset = 30;
														$(".tt").find("img").hover(
																function(e) {
																var s = $(this).attr("src");
																//var id = Number(str);
																jQuery("<img id='imgshow' src='" + s + "' />")
																.appendTo("body");
																jQuery("#imgshow").css("top", (e.pageY - xOffset-100) + "px")
																.css("left", (e.pageX + yOffset) + "px").fadeIn(
																		"fast");
													}, function() {
														jQuery("#imgshow").remove();
													});
															
														$(".tt").find("img").mousemove(	
																function(e) {
																	jQuery("#imgshow").css("top", (e.pageY - xOffset-100) + "px")
																			.css("left", (e.pageX + yOffset) + "px");
																});
													});
													
													
												/* $.ajax({
													type : "POST",
													url : "${pageContext.request.contextPath}/Query/GetRYZPDetails.action",
													timeout : 60000,
													data : "ryid=" + id,
													error : function(
															xmlhttprequest,
															textstatus,
															errorthrown) {
														alert(errorthrown);
													},
													success : function(data) {
														jQuery("<img id='imgshow' src='" + data[0].URL + "' />")
																.appendTo("body");
													},
													complete : function(data) {
													},
													dataType : 'json'
												});
														jQuery("#imgshow").css("top", (e.pageY - xOffset) + "px")
																.css("left", (e.pageX + yOffset) + "px").fadeIn(
																		"fast");
													}, function() {
														jQuery("#imgshow").remove();
													});
															
														$(".tt").find("img").mousemove(	
																function(e) {
																	jQuery("#imgshow").css("top", (e.pageY - xOffset) + "px")
																			.css("left", (e.pageX + yOffset) + "px");
																});
													}); */
										}
									}

									function GetDetails(id) {
										$("#rydetails").show();

										if (id == "") {
											alert("编号不能为空");
											return;
										}
										var ryid = id;
										$
												.ajax({
													type : "POST",
													url : "${pageContext.request.contextPath}/Query/GetRYDetails.action",
													timeout : 60000,
													data : "ryid=" + ryid,
													error : function(
															xmlhttprequest,
															textstatus,
															errorthrown) {
														alert(errorthrown);
													},
													success : function(data) {
														getrydetialscomlete(data);
													},
													complete : function(data) {
													},
													dataType : 'json'
												});

										$
												.ajax({
													type : "POST",
													url : "${pageContext.request.contextPath}/Query/GetRYZPDetails.action",
													timeout : 60000,
													data : "ryid=" + ryid,
													error : function(
															xmlhttprequest,
															textstatus,
															errorthrown) {
														alert(errorthrown);
													},
													success : function(data) {
														getryzpdetialscomlete(data);
													},
													complete : function(data) {
													},
													dataType : 'json'
												});

									}

									function getrydetialscomlete(data) {
										if (data == null || data == "") {

										} else {
											$("#xm").val(data[0].XM);
											$("#xb").val(data[0].XB);
											$("#lxdh").val(data[0].LXDH);
											$("#zw").val(data[0].ZW);
											$("#bz").val(data[0].BZ);
										}
									}
									function getryzpdetialscomlete(data) {
										if (data == null || data == "") {

										} else {
											$("#ryzp").attr("src", data[0].URL);
										}
									}
								</script>

								<br /> <br />
								<div id="rydetails" style="display: none">
									<table width="100%" border="0" cellspacing="1" cellpadding="0"
										bgcolor="b5d6e6">
										<tr>
											<td class="td" width="25%">姓名：</td>
											<td bgcolor="#FFFFFF" width="25%"><input id="xm" disabled="disabled" type="text"></td>
											<td class="td" width="25%">性别：</td>
											<td bgcolor="#FFFFFF" width="25%"><select id="xb"
												disabled="disabled">
													<option value="true">男</option>
													<option value="false">女</option>
											</select></td>
										</tr>
										<tr>
											<td class="td" width="25%">联系方式：</td>
											<td bgcolor="#FFFFFF" width="25%"><input id="lxdh"
												disabled="disabled" type="text">  
											</td>

											<td class="td" width="25%">职务：</td>
											<td bgcolor="#FFFFFF" width="25%"><input id="zw"
												disabled="disabled" type="text"></td>
										</tr>
										<tr>
											<td class="td" width="25%">照片：</td>
											<td bgcolor="#FFFFFF" width="25%" colspan="3"><img
												width="330px" height="440px" id="ryzp" alt="" src="">
											</td>
										</tr>
										<tr>
											<td class="td" width="25%">备注：</td>
											<td bgcolor="#FFFFFF" width="25%" colspan="3"><input
												width="200px" height="150px" disabled="disabled" id="bz"
												type="text"></td>
										</tr>
									</table>
								</div>
							</div>
							<div id="divJDH" style="display: none;">
								<!--上传文件列表-->
								<div id="datalist2" style="display: none;">
									<table id="tablejdhs" width="100%" border="0" cellpadding="0"
										cellspacing="1" bgcolor="b5d6e6" style="margin-top:6px;">
									</table>
								</div>
								<script type="text/javascript">
									//初始化上传空间

									//获取文件列表
									function getjdhs() {
										var jdxxid = $("#jdxxid").val();
										$
												.ajax({
													type : "POST",
													url : "${pageContext.request.contextPath}/Query/GetJDHs.action",
													timeout : 60000,
													data : "jdxxid=" + jdxxid,
													error : function(
															xmlhttprequest,
															textstatus,
															errorthrown) {
														alert(errorthrown);
													},
													success : function(data) {
														getjdhcomlete(data);
													},
													complete : function(data) {
													},
													dataType : 'json'
												});
									}

									function getjdhcomlete(data) {
										$("#tablejdhs").empty();
										if (data == null || data == "") {

										} else {
											if (typeof (data.error) != "undefined") {
												alert(data.des);
												return;
											}
											//初始化案件嫌疑人	
											$("#tablejdhs")
													.append(
															$("<tr align=\"center\">"
																	+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\"></td>"
																	+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">文件名</td>"
																	/* 	+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">标题</td>"
																		+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">姓名</td>"
																		+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">性别</td>"
																		+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">单位</td>"
																		+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">联系方式</td>" */
																	/* + "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">备注</td>" */
																	+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">下载</td>"
																	+ "</tr>"));
											for ( var i = 0; i < data.length; i++) {
												var img = "<img id=\"fileimg"+data[i].id+"\" width=\"15\" height=\"15\" alt='备注已完成' src='/djjd/common/css/imgs/ok.png'/>";
												var $li_1 = $("<tr height=\"22\" class=\"file\" align='center'><td bgcolor='White'>"
														+ img
														+ "</td><td bgcolor='White'>"
														+ data[i].FILENAME
														/* 			+ "</td><td bgcolor='White'>"
																	+ data[i].TITLE
																	+ "</td><td bgcolor='White'>"
																	+ data[i].XM
																	+ "</td><td bgcolor='White'>"
																	+ data[i].XB
																	+ "</td><td bgcolor='White'>"
																	+ data[i].DW
																	+ "</td><td bgcolor='White'>"
																	+ data[i].LXFS */
													/* 	+ "</td><td bgcolor='White'>"
														+ data[i].BZ */
														+ "</td><td bgcolor='White'><div class='STYLE1'><img src='/djjd/common/css/imgs/edt.gif' width='15' height='15' /><a href=\""+data[i].URL+"\" >下载</a>&nbsp; &nbsp;</div></td></tr>");
												$("#tablejdhs").append($li_1);
											}
										}
										if ($("#tablepics").html() != "") {
											$("#datalist2").show();
											//$("").hide();//备注
										}
									}
								</script>

							</div>

							<div id="divKCH" style="display: none;">
								<!--上传文件列表-->
								<div id="datalist2kc" style="display: none;">
									<table id="tablekchs" width="100%" border="0" cellpadding="0"
										cellspacing="1" bgcolor="b5d6e6" style="margin-top:6px;">
									</table>
								</div>
								<script type="text/javascript">
									//初始化上传空间

									//获取文件列表
									function getkchs() {
										var jdxxid = $("#jdxxid").val();
										$
												.ajax({
													type : "POST",
													url : "${pageContext.request.contextPath}/Query/GetKCHs.action",
													timeout : 60000,
													data : "jdxxid=" + jdxxid,
													error : function(
															xmlhttprequest,
															textstatus,
															errorthrown) {
														alert(errorthrown);
													},
													success : function(data) {
														getkchcomlete(data);
													},
													complete : function(data) {
													},
													dataType : 'json'
												});
									}

									function getkchcomlete(data) {
										$("#tablekchs").empty();
										if (data == null || data == "") {

										} else {
											if (typeof (data.error) != "undefined") {
												alert(data.des);
												return;
											}
											//初始化案件嫌疑人	
											$("#tablekchs")
													.append(
															$("<tr align=\"center\">"
																	+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\"></td>"
																	+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">文件名</td>"
																	/* + "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">标题</td>" */
																	/* + "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">备注</td>" */
																	+ "<td height=\"22\" background=\"/djjd/common/css/imgs/bg.gif\">下载</td>"
																	+ "</tr>"));
											for ( var i = 0; i < data.length; i++) {
												var img = "<img id=\"fileimg"+data[i].id+"\" width=\"15\" height=\"15\" alt='备注已完成' src='/djjd/common/css/imgs/ok.png'/>";
												var $li_1 = $("<tr height=\"22\" class=\"file\" align='center'><td bgcolor='White'>"
														+ img
														+ "</td><td bgcolor='White'>"
														+ data[i].FILENAME
														/* 	+ "</td><td bgcolor='White'>"
															+ data[i].TITLE */
														/* 	+ "</td><td bgcolor='White'>"
															+ data[i].BZ */
														+ "</td><td bgcolor='White'><div class='STYLE1'><img src='/djjd/common/css/imgs/edt.gif' width='15' height='15' /><a href=\""+data[i].URL+"\" >下载</a>&nbsp; &nbsp;</div></td></tr>");
												$("#tablekchs").append($li_1);
											}
										}
										if ($("#tablekchs").html() != "") {
											$("#datalist2kc").show();
											//$("").hide();//备注
										}
									}
								</script>

							</div>

							<div id="divZP" style="display: none; ">
								<!--上传文件列表-->
								<div class="focus" id="focus001">
									<ul id="piclist">
											
									</ul>

								</div>

								<script type="text/javascript">
									//获取文件列表
									function getpics() {
										if ($("#piclist").html() != "") {

											return;
										}
										var jdxxid = $("#jdxxid").val();
										$
												.ajax({
													type : "POST",
													url : "${pageContext.request.contextPath}/Query/GetPicFiles.action",
													timeout : 60000,
													data : "jdxxid=" + jdxxid,
													error : function(
															xmlhttprequest,
															textstatus,
															errorthrown) {
														alert(errorthrown);
													},
													success : function(data) {

														getpiccomlete(data);
													},
													complete : function(data) {

													},
													dataType : 'json'
												});
									}

									function getpiccomlete(data) {

										if (data == null || data == "") {

										} else {
											if (typeof (data.error) != "undefined") {
												alert(data.des);
												return;
											}
											//$("#piclist").append("<li><img src='"+data[0]['URL']+"' /></li>");
											//初始化案件嫌疑人	
											for ( var i = 0; i <= data.length; i++) {
												//temp=temp+"<li><img src='"+data[i]["URL"]+"' /></li>";
												if(data[i]== null){
													$("#piclist")
														.append(
																"<li><img src='' width='100%' height='100%'/></li>");
												}else{
													$("#piclist")
														.append(
																"<li><img src='"+data[i]['URL']+"' width='100%' height='100%'/></li>");
												}
												
											}

											sl();
											$.focus("#focus001");
											$.focus("#focus002");
											//alert(document.getElementById("piclist").innerHTML);
										}

									}
								</script>

							</div>
						</td>
					</tr>
				</table>
			</td>
			<td
				style="background-image: url('/djjd/common/css/imgs/tab_15.gif'); background-repeat: repeat-y; background-position: right;"></td>
		</tr>


		<tr>
			<td
				style="background-image: url('/djjd/common/css/imgs/tab_18.gif'); background-repeat: no-repeat;
                            height: 35px;"></td>
			<td
				style="background-image: url('/djjd/common/css/imgs/tab_19.gif'); background-repeat: repeat-x;"></td>
			<td
				style="background-image: url('/djjd/common/css/imgs/tab_20.gif'); background-repeat: no-repeat;
                            "></td>
		</tr>
	</table>
</body>
</html>
