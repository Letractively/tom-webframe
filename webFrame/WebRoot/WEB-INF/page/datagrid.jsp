<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title>Complex DataGrid - jQuery EasyUI Demo</title>
    <%@include file="/inc/head.jsp" %>
    <%@include file="/inc/easyui.jsp" %>
    <style type='text/css'>

    </style>
    <script type="text/javascript">
        $(function() {
            $('body').layout({fit: true})
            /*.layout('add',{
                    region: 'west',
                    width: 180,
                    title: 'West Title',
                    split: true,
                    tools: [{
                        iconCls:'icon-add',
                        handler:function(){alert('add')}
                    },{
                        iconCls:'icon-remove',
                        handler:function(){alert('remove')}
                    }]
                });*/



            $.JTUI.datagird($("#test"), {
                url: path + '/goDemo$getlist.do',
                frozenColumns:[
                    //冻结列
                    [
                        {field:'ck',checkbox:true}
                    ]
                ],
                columns:[
                    [
                        //{field:'ck',checkbox:true},
                        {field:'userid',title:'用户ID',align:'left',width:50,sortable:true},
                        {field:'username',title:'用户名',align:'right',width:50},
                        {field:'password',title:'密码',align:'left',width:150,formatter : function(value, rowData, rowIndex) {
                            //alert("这列的值:" + value + " 这行的值:" + rowData.username + rowData.sjjb + " 行号:" + rowIndex);
                            return "<a style='color: red'> " + value + "</a>";
                        }},
                        {field:'sjjb',title:'数据级别',align:'left',width:50},
                        {field:'id',title:'ID',align:'left',width:50,sortable:true },
                        {field:'name',title:'NAME',align:'left',width:100,sortable:true,formatter: function(value){
                            value="<a style='border:1px solid red' >"+value+"</a>";
                            value+='<a class="icon-edit" style="height:16px; width:16px; border:1px solid black;float:right"></a>';
                            return value;
                        }
                        },
                        {field:'path',title:'路径',align:'left',width:100,sortable:true},
                        {field:'stat',title:'状态',align:'right',width:30},
                        {field:'sort',title:'排序',align:'left',width:30},
                        {field:'userjb',title:'菜单级别',align:'left',width:50},
                        {field:'stat',title:'状态',align:'right',width:30},
                        {field:'sort',title:'排序',align:'left',width:30},
                        {field:'userjb',title:'菜单级别',align:'left',width:50}
                    ]
                ],
                onLoadSuccess: function (data) {
				initQuickInfoHandlers(data);
			}
            });


        });


        function initQuickInfoHandlers(data) {
            var view2= $.JTUI.tip();
            $("td").find('a.icon-edit').each(function (index) {
                this.rows = data.rows[index];
                $(this).hover(
                        function (event) {
                            var msg = '<a>'+this.rows.name+'</a><p/><a href="#">'+this.rows.path+'</a>';
                            view2 = $.JTUI.tip(msg, 120+index+"");

                            view2.css({left:event.clientX + 14 + 'px',top:event.clientY - 20 + 'px'})
                                    .show();
                        },
                        function () {
                            view2.hide();
                        }).mousemove(function (event) {
                            var style = view2.get(0).style;
                            style.top = event.clientY - 10 + 'px';
                            style.left = event.clientX + 14 + 'px';
                        }); //可以省略.增加移动效果
            });
        }


    </script>
</head>


<body>
<div id='center' region='center' style="overflow: hidden;">
    <table id="test"></table>
</div>
</body>


</html>