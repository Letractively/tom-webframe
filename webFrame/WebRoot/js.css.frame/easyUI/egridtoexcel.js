
/***
 * jquery easyui 导出excel (poi模式)
 * tomsun
 * 2012-07-11
 */

$.extend($.fn.datagrid.methods,{
    getExcelPOI: function(jq, param)  {
        /*获取表头*/
        var columns = $(jq).datagrid('options').columns;
        var header = $(jq).datagrid('options').title;
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
        var rows = $(jq).datagrid('getRows'); //得到所有行被荣
        var column = $(jq).datagrid('getColumnFields'); //列字段,非名称

        for(var i=0;i<rows.length; i++){
            var row = rows[i];
            for(var j=0; j<column.length;j++){
                var field = row[column[j]] ? row[column[j]] : '' ; //取数据==row[name] row[path]
                var option = $(jq).datagrid('getColumnOption',column[j]); //得到列属性
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

        $(form).appendTo("body").submit().remove();
    },
    clearAllcheckbox : function(jq){
        $(jq).datagrid('clearSelections');
        $('div.datagrid-header-check input[type=checkbox]').attr("checked",false);
    }
});

