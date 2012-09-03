<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title>Complex DataGrid - jQuery EasyUI Demo</title>
    <%@include file="/inc/head.jsp" %>


</head>


<body>

<div region="center">
    <%--<div style="width:100%;height:99.5%;">--%>


    <div style="width: 100%;height:auto;" id="toolbar">
        <fieldset>
            <legend>筛选</legend>
            <table class="tableForm">
                <tr>
                    <th style="">用户名称</th>
                    <td><input name="name" style=""/></td>
                    <th>&nbsp;创建时间</th>
                    <td><input name="datestart" id="dateStart"  /></td>
                    <td>-</td>
                    <td><input id="dateEnd"   />
                    </td>
                    <td>
                        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="searchFun();"
                           href="javascript:void(0);">查找</a>
                    </td>
                </tr>
            </table>
        </fieldset>
        <div style="border:solid black 1px;">
            <a class="easyui-linkbutton" iconCls="icon-edit" onclick="exportExcel();" plain="true"
               href="javascript:void(0);">导出excel</a>
            <a class="easyui-linkbutton" iconCls="icon-edit" plain="true"
               href="goExport$exportCsv.do">导出csv</a>
            <a class="easyui-linkbutton" iconCls="icon-undo" onclick="clearselected();" plain="true"
               href="javascript:void(0);">取消选中</a>
        </div>
    </div>

    <table id="menu1"></table>

</div>

<div id="menu" style="width:120px;display:none; ">
    <div onclick="allot('1',this)" iconCls="icon-add">分配给所有终端</div>
    <div onclick="allot('2',this)" iconCls="icon-remove">按分组分配</div>
    <div onclick="allot('3',this)" iconCls="icon-edit">终端分配</div>
</div>


<%@include file="/inc/easyui.jsp" %>

<script type="text/javascript">
    $(function() {
        $("body").layout({fit: true});

        $("#dateStart").datebox();

        $("#dateEnd").datetimebox();

        $.JTUI.datagird($("#menu1"), {
            toolbar:"#toolbar",
            url: path + '/goDemo$getMenuList.do',
            frozenColumns:[
                [
                    {field:'ck',checkbox:true}
                ]
            ],
            columns:[[
			        {title:'详情',colspan:3}, //跨列要看情况,跨多少了列,下面[]中就有几列为显示数据
                    {title:'详情2',colspan:2}, //跨列要看情况,跨多少了列,下面[]中就有几列为显示数据
					{field:'id',title:'ID',width:100,align:'center',sortable:true, rowspan:2}, // rowspan 最多也就是2,一般表头最多两行
                    {field:'operate',title:'操作',width:100,align:'', rowspan:2,formatter: operate} // rowspan 最多也就是2,一般表头最多两行
				],[
					{field:'name',title:'Name',width:80, align:'left',sortable:true},
					{field:'path',title:'路径',width:150,align:'right',sortable:true},
					{field:'stat',title:'状态',width:100},
                    {field:'srot',title:'排序',width:100,sortable:true},
                    {field:'userjb',title:'菜单级别',width:100}

				]],
            onLoadSuccess : showMenu,
            onRowContextMenu : showRightMenu
        });
    });

    function operate(value, rowData) {
        return "<a href='#' name='edit' value='你好' id='" + rowData.id + "' onclick=''>分配</a>";
    }

    function showMenu() {
        $(document.getElementsByName("edit")).each(function() {
            var id = this.id;
            //alert(this.value);
            $(this).mouseenter(function() {
                $("div[iconCls='icon-add']")[0].id=id;
                $("div[iconCls='icon-remove']")[0].id = id;
                $("div[iconCls='icon-edit']")[0].id = id;
            });

            $(this).menubutton({
                menu : '#menu',
                iconCls : 'icon-list'
            });

        });
    }


    function showRightMenu(e, rowIndex, rowData) {
        e.preventDefault();
        $(this).datagrid('unselectAll');
        $(this).datagrid('selectRow', rowIndex);


        $('#menu').show().menu('show', {
            left: e.pageX,
            top: e.pageY
        });

    }

    function exportExcel(){
       $("#menu1").datagrid("getExcelPOI",{header:"例子",action:"goExport.do"});

    }
    

    function clearselected(){
        $("#menu1").datagrid("clearAllcheckbox");
        //$('div.datagrid-header-check input[type=checkbox]').attr("checked",false);
    }

</script>


</body>


</html>