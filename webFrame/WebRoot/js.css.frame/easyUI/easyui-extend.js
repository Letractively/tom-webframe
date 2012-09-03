(function($) {
    
    var JTUI = $.JTUI = {};
    
    /* UI 的方法,主要使用easyui*/
    JTUI.datagird = function(jq, option) {
        if (!option.remoteSort) option.remoteSort = false; // 客户端排序(默认是服务端排序)
        if (!option.striped) option.striped = true;     //条纹间隔
        if (!option.nowrap) option.nowrap = true;      //是否换行显示数据  默认true 不换行
        if (!option.pagination) option.pagination = true;   //显示分页
        /*if (!option.fitColumns) option.fitColumns = false;   //自适应宽度 默认false*/
        if (!option.fit) option.fit = true;             //自适应窗口最大最小化
        if (!option.pageSize) option.pageSize = 20;     //初始页面行数
        if (!option.pageList) option.pageList = [20,50,100,150,200];
        /*if (!option.rownumbers) option.rownumbers = true;   //是否显示rownum*/
        /* singleSelect:false, 是否单选*/
        /*option.loadMsg = "正在加载数据....";  // 加载时提示信息*/
        return jq.datagrid(option);
    };

    /**
     * 打开window,必备属性{id,title,height,width,url||href||content}
     * url为iframe,href为div,content为String代码片段
     *
     * @param option
     */
    JTUI.openWindow = function(option){
        var JTUI_window =$('#'+option.id);
        if(JTUI_window.length==0){
            JTUI_window = $("<div id='"+option.id+"'><iframe width='100%' height='100%' scrolling='auto' frameborder='0'  src='"+option.url+"'/></div>")
            .appendTo("body");
        }
        /*锁定*/
        if(!option.modal) option.modal= true;
        JTUI_window.window(option);
    };

    JTUI.openDialog = function(option){
        var JTUI_dialog =$('#'+option.id);
        if(JTUI_dialog.length==0){
            JTUI_dialog = $("<div id='"+option.id+"'><iframe width='100%' height='100%' scrolling='auto' frameborder='0'  src='"+option.url+"'/></div>")
            .appendTo("body");
        }
        /*不锁定*/
        if(!option.modal) option.modal= false;
        JTUI_dialog.dialog(option);
    };

    JTUI.covering = function(html){
          $("<div id='JTUI_mask' class='datagrid-mask'></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");
          $("<div id='JTUI_mask_msg' class='datagrid-mask-msg'></div>").html(html).appendTo("body").css({"font-size":"12px",display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
    };

    JTUI.covered = function(){
          $("#JTUI_mask").remove();
          $("#JTUI_mask_msg").remove();
    };

    JTUI.tip = function (msg,width){
        var tipDiv =$("#JTUI_tip");
        if(tipDiv.length==0){
            tipDiv = ['<div id="JTUI_tip" style="position: absolute; display: none">',
                '<div class="validatebox-tip-pointer" style="margin: 10px 0 0 0;"/>',
                '<span id="JTUI_tip_Msg" style="background:#FFFCC7;margin-left:10px;padding: 4px 8px;border: 1px solid #FFC340;font-size: 12px;position: absolute;z-index: 10000;width: '+width+'px;">'+msg+'</span>',
                '</div>'].join('');
            tipDiv=$(tipDiv).appendTo("body");
        }else{
           $("#JTUI_tip_Msg").css({width:width+'px'}).html(msg);
        }
        return tipDiv;
    };
    
})(jQuery);


/***********************************以下为easyui 扩展方法*****************************/
/**
 * 扩展panel
 * tab关闭时回收内存,效果不大
 * 2012-07-24
 */
$.fn.panel.defaults.onBeforeDestroy = function() {
	var frame = $(this).find('iframe'); // 取this对象向下寻找iframe对象
	try {
        frame.get(0).contentWindow.close();
        frame.get(0).src="";
        frame.remove();
        if ($.browser.msie) {
            CollectGarbage();
        }
	} catch (e) {
	}
};

/***
 * jquery easyui 导出excel (poi模式)
 * tomsun
 * 2012-07-11
 */

$.extend($.fn.datagrid.methods,{
    getExcelPOI: function(jq, param)  {
        /*获取表头*/
        var columns = jq.datagrid('options').columns;
        var header = jq.datagrid('options').title;
        var title='';
        var subtitle='';
        for(var i=0; i<columns.length; i++){
           var column = columns[i];
            for(var j=0; j<column.length; j++){
                var col = column[j];
                if(col.rowspan || col.colspan){
                    if(col.colspan){
                        title += col.title+'*c'+col.colspan+';^|';
                    }else{
                        title += col.title+'*r'+col.rowspan+';^|';
                    }
                }else{
                    subtitle += col.title+'^|';
                }
            }
        }


        /*获取数据*/
        var detail='';
        var rows = jq.datagrid('getRows'); //得到所有行被荣
        var column = jq.datagrid('getColumnFields'); //列字段,非名称

        for(var i=0;i<rows.length; i++){
            var row = rows[i];
            for(var j=0; j<column.length;j++){
                var field = row[column[j]] ? row[column[j]] : '' ; //取数据==row[name] row[path]
                var option = jq.datagrid('getColumnOption',column[j]); //得到列属性
                /*取数据位置*/
                var align = option.align;
                if(align){
                    if(align == 'right'){
                        align = '^2';
                    }else if(align == 'left'){
                        align = '^0'; //left
                    }else{
                        align = '^1'; //center
                    }
                }else{
                    align = '^1'; //center
                }
                /*取数据类型*/
                var type = option.type;
                if(type){
                    if (type == "int") {
                        type = "^i"; //导出int类型
                    } else if (type == "double") {
                        type = "^d"; //导出double类型
                    } else if (type == "currency") {
                        type = "^c"; //导出currency类型
                    } else {
                        type = "^s";
                    }
                }else{
                    type = '^s';
                }
                detail += field + align + type +'^|';
            }
            detail+='&&&';
        }

        var excel = {};
        excel.header = param.header ?  param.header : title;
        excel.subheader  = param.subheader ? param.subheader : '' ;
        excel.title = title + "^#" + subtitle;
        excel.detail = detail;

        var form = ['<form name="export" action="'+param.action+'" method="post">' ,
            '<input type="hidden" name="detail" value="'+excel.detail+'" />' ,
            '<input type="hidden" name="title" value="'+excel.title+'" />' ,
            '<input type="hidden" name="subheader" value="'+excel.subheader+'" />' ,
            '<input type="hidden" name="header" value="'+excel.header+'" /></form>'].join('');

        excel = null;
        $(form).appendTo("body").submit().remove();
    },
    /**
     * 增加datagrid 去掉全选的方法,包括头header-check
     * @param jq
     */
    clearAllcheckbox : function(jq){
        jq.datagrid('clearSelections');
        $('div.datagrid-header-check').find('input')[0].checked=false;
    }
});

$.extend($.fn.tabs.methods,{
/*添加双击关闭事件,在每次新增tab后调用此方法, tab个数固定只需调用一次*/
    dbclickclose: function(jq){
         jq.find("ul.tabs").find("li").bind("dblclick",function(){
                var tab = $(this).find(".tabs-title.tabs-closable.tabs-with-icon")[0].innerHTML;
                jq.tabs("close",tab);
            })
        }
});




