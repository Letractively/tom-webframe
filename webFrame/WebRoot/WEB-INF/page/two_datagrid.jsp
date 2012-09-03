<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <%@include file="/inc/head.jsp" %>
    <%@include file="/inc/easyui.jsp" %>

    <script type="text/javascript">

        $(function(){

          
              $.JTUI.datagird($("table#leftData"),{
                url: path + '/goDemo$getMenuList.do',
                 columns:[
                    [
                        {field:'id',title:'ID',align:'left',width:50,sortable:true },
                        {field:'name',title:'NAME',align:'left',width:100,sortable:true},
                        {field:'path',title:'路径',align:'left',width:100,sortable:true},
                        {field:'stat',title:'状态',align:'right',width:30},
                        {field:'sort',title:'排序',align:'left',width:30}

                    ]
                ],
                onDblClickRow:function(rowIndex,row) {
                    var selectRows = $('#rightData').datagrid('appendRow',row);

                     var count = $('#rightData').datagrid('getRows').length;
                     $('#font_count').text(" (总共=" + count + "台)");

                 }
            }).datagrid("getPager").pagination({
                 onRefresh : function(){ //刷新是隐藏等待层   *
                     $("#leftData").datagrid("loaded");   //*
                 }                                       //*
             });                                         //*
                                                         //*   
            function load1(){  // 默认刷新表格不显示 等待层, 1.2.6可用
                $("a[icon='pagination-load']").click();

            }
               //setInterval(load1,500);


            $.JTUI.datagird($("table#rightData"),{
				url:'',
                 columns:[
                    [
                        {field:'id',title:'ID',align:'left',width:50,sortable:true },
                        {field:'name',title:'NAME',align:'left',width:100,sortable:true},
                        {field:'path',title:'路径',align:'left',width:100,sortable:true},
                        {field:'stat',title:'状态',align:'right',width:30},
                        {field:'sort',title:'排序',align:'left',width:30}

                    ]
                ],

                onDblClickRow:function(rowIndex,rowData) {
                    alert(rowIndex);
                    var tr=$("tr[datagrid-row-index="+rowIndex+"]");
                    alert(tr.length);
                    tr.remove();
                    var count = $('#rightData').datagrid('getRows').length;
                     $('#font_count').text(" (总共=" + count + "台)");

                 }
            }),

            $("#add").click(function(){
                var rows=$('#leftData').datagrid('getSelections');
                for(var i=0;i<rows.length;i++){
                    var selectRows = $('#rightData').datagrid('appendRow', rows[i]);
                }
                var count = $('#rightData').datagrid('getRows').length;
                $('#font_count').text(" (总共=" + count + "台)");
            }),

                    

            $("#clear").click(function(){
                $('#rightData').datagrid('loadData',[]);
            })
        })

        
        
    </script>

</head>
<body >
<form action=" " method="post">
    <div style="background: #EEF2F3; width: 98%;" >
        <div style="height: 500px; width: 100%;border:solid 0px red; padding-left:10px; padding-right:10px">

            <%--left--%>
            <div style="float: left; width: 46%; height: 100%;border:solid 0px black ">

                <div style="height: 8%; padding-top: 1px;">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="top_table">
                       <tr align='center' >
                           <td ><input type="text" ></td>
                           <td ><input type="button" value="查询"></td>
                       </tr>
                    </table>
                </div>
                <div id="div_mirrorPain" style="float: left; width: 100%; height: 90%; display: block; margin-top:-6px;">
                    <table id="leftData"></table>
                </div>
            </div>

            <%-- center--%>
            <div style="float: left; height:100%; width: 8%; border:solid 0px red">
                <br>
                <br>
                <br>
                <input type="button" id="add" name="add" value="添加" class="but1"/>
                <p></p>
                <input type="button" id="clear" name="clear" value="清空" class="but2"/>

                <p>双击左边选择</p>

                <p>双击右边删除</p>

            </div>

            <%--lright--%>
            <div style="float: left; width: 44%; height: 100%;border:solid 0px red">

                <div style="height: 8%; width: 100%; padding-top: 1px;;">
                        <span style="margin-left: 32%;">
                            已选择终端<font color="#FF0000" id="font_count"> (总共=0台)</font>
                        </span>
                </div>
                <div  style="float: left; width: 100%; height: 90%;  ">
                    <table id="rightData"></table>
                </div>
        </div>
    </div>

    <div class="last" style="width: 100%;height: 35px;">
        <input type="button" value="关&nbsp;闭" name="canle"
               id="canle"
               style="float: right;margin-right: 40px;margin-top: 7px;"/>
        <input type="button" value="提&nbsp;交" id="tj"
               name="tj"
               style="float: right; margin-right: 80px;margin-top: 7px;"/>
    </div>
    </div>
</form>

</body>
</html>