<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

    <title>选择文件</title>
    <%@include file="/inc/head.jsp" %>
    <%@include file="/inc/ztree.jsp" %>

    <style type="text/css">
        body {
            margin: 0;
        }

        #dialog {
            width: 725px;
            height: 480px;
            padding: 5px;
            background-color: #f1f1f1;
            border: 1px solid #ccc;
        }

        #dirs {
            width: 200px;
            height: 480px;
            float: left;
            margin-right: 5px;
            overflow: auto;
            background-color: white;
        }

        #files {
            width: 520px;
            height: 480px;
            float: left;
            background-color: white;
        }

        #controlbar {
            float: right;
            width: 520px;
            height: 30px;
            background-color: #f1f1f1;
            line-height: 30px;
            text-align: right;
        }

        #controlbar input {
            width: 70px;
        }

        .file {
            padding-left: 20px;
            line-height: 16px;
            display: block;
            background: url(${pageContext.request.contextPath}/image/document.png) no-repeat;
        }
    </style>
</head>
<body>
<div id="dialog">
    <div id="dirs">
        <ul id="tree" class="ztree">
        </ul>

    </div>
    <div id="files">
        <table id="list"></table>
        <div id="controlbar">
            <input type="button" value="选择" onclick="selectFile();"/>
            <input type="button" value="取消" onclick="location.replace('about:blank')"/>
        </div>
    </div>
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/zTree/jquery-ztree-2.2.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/js/jQueryeasyUi/jquery.easyui.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/js/jQueryeasyUi/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
    var zTree;
    var setting = {
        async: {
            enable: true,
            url: path + "/goFile$getDirs.do",
            autoParam:["path"],     // 上面的id会和autoParam冲突自动传参
            /* contentType: "application/json;charset=UTF-8",*/
            /*otherParam:{"otherParam":"zTreeAsyncTest"}, //额外的参数*/
            dataFilter: filter
        },
        callback: {
            beforeClick: zTreeOnClick
        }
    };


    $(document).ready(function() {
        $.fn.zTree.init($("#tree"), setting);
    });
    function filter(treeId, parentNode, childNodes) {
        //console.log(childNodes);
        return childNodes;
    }

    function zTreeOnClick(treeId, treeNode) {
        alert(treeNode.name);
        alert(treeNode.path);
        $("#list").datagrid("clearSelections");
        $("#list").datagrid("reload", {path: treeNode.path});
    }

    $(function () {
        $('#list').datagrid({
            collapsible:false,
            idField:'id',
            //title:'文件列表',
            fit:false,
            iconCls:'icon-list',
            pageList:[10,20,30,40,50],
            striped: true,
            border:true,
            singleSelect:true,
            rownumbers:false,
            pagination: false,//包含分页
            width: 520,
            height: 450,
            pageSize:20,
            pageNumber:1,
            url: path + '/goFile$list.do',
            frozenColumns:[
                [

                ]
            ],
            columns:[
                [
                    {field:'name',title:'名称',width:260,align:'left', formatter: function (value) {
                        return '<a class="file">' + value + '</a>';
                    }},
                    {field:'size',title:'大小',width:90,align:'right', formatter: formatSize},
                    {field:'modified',title:'修改日期',width:120,align:'left', formatter: formatDate}
                ]
            ],
            onDblClickRow: function () {
                selectFile();
            }
        });
    });

    var units = ['KB', 'MB', 'GB'];


    function formatSize(value) {
        value /= 1024;
        var i = 0;
        while (value > 1024) {
            value /= 1024
            i++;
        }
        var s = Math.round(value * 100) + '';
        return ((value > 1) ? s.substring(0, s.length - 2) : '0') + '.' + s.substring(s.length - 2, s.length) + ' ' + units[i];
    }

    function formatDate(value) {
        var date = new Date(value);
        return date.getFullYear() + '-' + pad(date.getMonth() + 1) + '-' + pad(date.getDate()) + ' ' + pad(date.getHours()) + ':' + pad(date.getMinutes()) + ':' + pad(date.getSeconds());

    }

    function pad(n) {
        if (n < 10) n = '0' + n;
        return n;
    }

    function selectFile() {
        var file = $('#list').datagrid('getSelected');
        //top.alert(file);
        if (file) {
            if (top.selectFile) top.selectFile(file.path);
        } else {
            alert('请选择文件！');
            return;
        }
        location.replace('about:blank');
    }
</script>
</body>
</html>