<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
    String path = request.getContextPath();
%>
<html>
<head>
    <title>----webFrame-- </title>
    <link rel="stylesheet" type="text/css" href="<%=path %>/js.css.frame/easyUI/icon.css"/>
    <link rel="stylesheet" type="text/css" href="<%=path %>/js.css.frame/easyUI/easyui_1.css"/>
    <link rel="stylesheet" type="text/css" href="<%=path %>/js.css.frame/zTree/zTree.css"/>
    <style type="text/css">
        input.selected {
            background:#FFFFFF; color: black;cursor:pointer
        }
        input.unselected {
            background: #296F9E; color: #FFFFFF;cursor:pointer
        }
    </style>

    <script type="text/javascript" src="<%=path %>/js.css.frame/jquery-1.4.4.min.js"></script>
    <script type="text/javascript" src="<%=path %>/js.css.frame/easyUI/jquery.easyui.min1.3.js"></script>
    <script type="text/javascript" src="<%=path %>/js.css.frame/easyUI/easyui-extend.js"></script>
    <script type="text/javascript" src="<%=path %>/js.css.frame/zTree/jquery.ztree.all-3.2.min.js"></script>

    <script type="text/javascript">
        var path = "<%=path%>";

        $(document).ready(function() {

            $("#easyui-layout").layout({fit: true});

             /*加载上部菜单*/
            $.post(path + "/goLogin$getSubMenu.do", setTopMenu);

            /*初始化左侧菜单*/
            $.fn.zTree.init($("#leftMenu"), setting);

            /*初始化标签表格*/
            $('#tt').tabs({
                fit: true,
                tools:[
                    {
                        iconCls:'icon-add',
                        handler: function() {
                            alert('add');
                        }
                    }
                ],
                onAdd: loading
            });

        });


        var setting = {
            async: {
                enable: true,
                url: path + "/goLogin$getLeftMenu.do?id=1",
                /*autoParam:["id", "name"],     // 上面的id会和autoParam冲突自动传参*/
                /* contentType: "application/json;charset=UTF-8",*/
                /*otherParam:{"otherParam":"zTreeAsyncTest"}, //额外的参数*/
                dataFilter: filter
            },
            callback: {
                beforeClick: openMenu
            }
        };

        function filter(treeId, parentNode, childNodes) {
            if (!childNodes) return null;
            for (var i = 0, l = childNodes.length; i < l; i++) {
                childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
            }
            return childNodes;
        }
        /*设置头部菜单*/
        function setTopMenu(data) {
            for (var i = 0; i < data.length; i++) {
                $("#topMenu").
                  append("<td><input type='button' id='" + data[i].id +"' class='unselected' onclick='changeLeftMenu(this)' value='"+data[i].name+"'/></td>");
            }
        }

        /*单击头菜单修改左侧菜单*/
        function changeLeftMenu(obj) {
            $("#topMenu input").each(function(){
                this.className='unselected'
            });
            obj.className='selected';
            var zTree = $.fn.zTree.getZTreeObj("leftMenu");
            var url = path + "/goLogin$getLeftMenu.do?id=" + obj.id;
            if (zTree.setting.async.url == url) return;
            zTree.setting.async.url = url;
            zTree.reAsyncChildNodes(null, "refresh"); //刷新

        }

        /*左侧菜单事件*/
        function openMenu(treeId, treeNode) {
            if (treeNode.isParent) {  //如果是父菜单不打开
                return;
            }
            var tabcnt = $("li").find(".tabs-close").length;
            var tt= $('#tt');
            if (tt.tabs('exists', treeNode.name)) {   //如果tab标签已存在则选中
                tt.tabs('select', treeNode.name);
            } else if (tabcnt < 4) {    //标签个数小于4则新增
                tt.tabs('add', {
                    title:  treeNode.name,
                    iconCls : 'icon-save',
                    content: "<iframe scrolling='auto'   onload='loadered();' frameborder='0'  src='" + path + treeNode.path + "' style='width:100%;height:99.5%;'></iframe>",
                    closable: true
                });
                tt.tabs("dbclickclose");
            } else {     //标签个数大于5替换选择的标签
                var tab = tt.tabs('getSelected');
                tt.tabs('update', {
                    tab: tab,
                    options:{
                        title: treeNode.name,
                        content: "<iframe scrolling='auto' onload='loadered();' frameborder='0'  src='" + path + treeNode.path + "' style='width:100%;height:99.5%;'></iframe>"
                    }
                });
                tt.tabs("dbclickclose");
            }
        }

        function loading() {
            $("#loader").show();
        }

        function loadered() {
            $("#loader").hide();
        }
    </script>

    <style type="text/css">

    </style>


</head>
<body  <%--class="easyui-layout" fit="true"--%>>
<div style="width:1024px;height:900px; margin:0 auto">
    <div id="easyui-layout">

        <%-- 上部--%>
        <div region="north"  style="height:80px;background:#499ED2">
            <table width="300px" style="margin: 20px 20px 20px 40%; background:#499ED2" border="0" cellspacing="0" cellpadding="0">
                <tr id="topMenu">
                </tr>
            </table>
        </div>
            
        <%-- 左侧菜单--%>
        <div region="west" title="菜单" style="width:200px;padding:10px;overflow: hidden;">
            <ul id="leftMenu" class="ztree"></ul>
        </div>

        <%-- 中间主页面--%>
        <div region="center">
            <div id="tt">
                <div title="首页" iconCls="icon-add">
                    <iframe src="<%=path%>/goLogin$forward.do?page=datagrid" style="width:100%;height:99.5%;" frameborder="0"
                            scrolling="atuo"></iframe>
                </div>
            </div>
        </div>
    </div>
    
</div>


<div id="loader"  style="display: none; position: absolute;top: 0;width: 100%;height: 100%;
	background: url(<%=path%>/css/loader.gif) no-repeat 55% center ;"></div>

</body>
</html>