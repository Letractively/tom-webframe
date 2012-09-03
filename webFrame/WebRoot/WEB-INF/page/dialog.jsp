<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <%@include file="/inc/head.jsp" %>
    <%@include file="/inc/easyui.jsp" %>
    <script type="text/javascript" src="<%=path %>/js.css.frame/dialog/dialog.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=path %>/js.css.frame/dialog/dialog.css"/>

    <script type="text/javascript">
        function openDialog() {
            $.JTUI.openDialog({id:'d1',title:'hrefDemo',width:600,height:600,
                href: path + '/goLogin$forward.do?page=dialog',modal: true
            })
        }

        function openWindow() { //使用url 刷新时只会 弹出层中刷新,其他都是整个刷新
            $.JTUI.openWindow({id:'w1',title:"URLDemo",url: path + '/goLogin$forward.do?page=menu',height:600,width:600
            })
        }

        $(function() {
            $.JTUI.covering("waitting...");
            // alert($("a[iconCls=icon-add]").toString());
            //alert("是否IE8以上=="+$.fn.isLessThanIe8());
            setTimeout('$.JTUI.covered()', 2000);
        });

        function openDialog2() {
            $.JTUI.openDialog({
                id:'contentDemo',title:'contentDemo',width:600,height:600,content: $("#content")[0].innerHTML
            })
        }

        function open1() {
            a = $.JTUI.dialog({
                title:'标题',
                content:'#content',
                /*url: path + '/goLogin$forward.do?page=import',*/
                width:400,
                height:300,
                dragable: true, //是否拖动
                modal: false, //是否背景覆盖

                onOK: function(dialog) {
                    alert($('#name', dialog)[0].value);
                }/*,
                 onCancel: function(){
                 alert("你做了取消操作");
                 }*/
            });
        }
        function close1() {
            $.JTUI.dialog("close", a);
        }

    </script>

</head>

<body>
<div>
    <a class="easyui-linkbutton" iconCls="icon-add" onclick="openDialog();" plain="true"
       href="javascript:void(0);">opendialog</a>
    <a class="easyui-linkbutton" iconCls="icon-remove" onclick="openWindow();" plain="true"
       href="javascript:void(0);">openwindow</a>
    <a class="easyui-linkbutton" iconCls="icon-add" onclick="openDialog2();" plain="true"
       href="javascript:void(0);">opendialog</a>

    <input type='button' value="点击打开" onclick='open1()'>
<input type='button' value="点击关闭" onclick='close1()'>
</div>

<div id='content' style='display:none'>
    <table>
        <tr>
            <td>
                <lable>姓名:</lable>
                <input type='text'/></td>
        </tr>
        <tr>
            <td>
                <lable>年龄:</lable>
                <input type='text'></td>
        </tr>
        <tr>
            <td>
                <lable>性别:</lable>
                <input type='text'></td>
        </tr>

    </table>
</div>


</body>
</html>
