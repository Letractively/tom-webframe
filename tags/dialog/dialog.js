/** jquery-dialog 用table实现
 *  功能比较完善, 代码较乱
 */
(function($){

var createId = (function (){
    var i =0;
    return function(){
       return ++i;
    }
})();
    
$.JTUI.dialog = function (option, param){
    if(typeof option == 'string'){
        return $.JTUI.dialog.methods[option](param);
    }
    
    option = $.extend({},$.JTUI.dialog.defaults, option);
    option.id  = createId();
    
    var thead = ['<thead id="thead_hand"><tr style="cursor: move;">',
                '<td width="13" height="33" class="thread_border_lt"><div style="width: 13px;"></div></td><td height="33" class="thread_border_ct">',
                '<div class="thread_border_ct_div"><a class="thread_border_title"/>'+option.title+'</div><a class="thread_border_close" id="thread_close" ></a>',
                '</td><td width="13" height="33" class="thread_border_rt"><div style="width: 13px;"></div></td></tr></thead>'
            ].join('');


    /*判断使用iframe还是直接使用String拼*/
    var main = ['<tr><td><div style="padding:4px;width: '+option.width+'px;">'];
    if(option.url){
        main.push('<iframe style="width:100%;height:99.5%;" frameborder="0"  src="'+option.url+'"></iframe>')
    }else if(option.content){
        if(option.content.substr(0,1) == '#'){ //如果是id,取id内容
            var content = $("<div id='JTUI_toString'>").append($(option.content).clone().show()).html();
            main.push(content);
        }else{ //否则直接取内容
            main.push(option.content);
        }
    }
    main.push('</div></td></tr>');


    /*tbody拼接 message main button*/
    var tbody = ['<tbody><tr><td width="13" class="tbody_boder_mlm"></td>',
                 '<td align="center" valign="top" ><table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">'];

    /*判断是否加入message*/
    if(option.message){
        var message = ['<tr style="display: block"><td height="50" valign="top">' +
                    '<div  class="message_div" style ="width:'+option.width+'px;">',
                    '<div class="message_png"/><div style="height:100%; width: 85% float: left; overflow: hidden;">'+option.message+'</div></div></td></tr>'
                  ].join('');
        tbody.push(message);
    }
    tbody.push(main.join(''));


    /*判断是否加入button*/
    if(option.button){
        var button = '<tr><td height="36" id="toolbar"><div style="text-align: right; border-top: #dadee5 1px solid; padding: 8px 20px; background-color: #f6f6f6;">';
        button += option.button;
        button += '</div></td></tr>';
    }

    tbody.push(button);
    tbody.push('</table></td><td width="13" class="tbody_boder_mrm"></td></tr></tbody>');




    var tfoot = ['<tfoot><tr><td width="13" height="13" class="tfoot_border_lb"></td>',
                    '<td class="tfoot_border_cb"></td>',
                    '<td width="13" height="13" class="tfoot_border_rb"></td></tr></tfoot>'
                ].join('');

    var table =$('<table id="JTUI_dg'+option.id+'" border="0"  cellpadding="0" cellspacing="0" width=""></table>');

    var mask = null;
    if(option.modal){
        mask = $('<div style="position:absolute;left:0;top:0;background:#ccc;opacity:0.4;filter:alpha(opacity=30);width:100%;height:100%"></div>').appendTo('body');
    }
    
    var dialog = table.append(thead).append(tbody.join('')).append(tfoot).appendTo('body').center().draggable({handle:'#thead_hand'});

    option.initData((!option.url ? dialog : dialog.find('iframe')[0].contentWindow.document));
    
    var cancel = function (){
        if(option.url){
            var iframe = dialog.find('iframe')[0].contentWindow.document;
            option.onCancel(iframe);
        }else{
            option.onCancel(dialog);
        }
        if(mask) mask.remove();
        dialog.remove();
     };

     var ok = function () {
    	 var flag = false;
         if (option.url) {
             var iframe = dialog.find('iframe')[0].contentWindow.document;
             flag = option.onOK(iframe);
         } else {
             flag = option.onOK(dialog);
         }
         if(flag){
        	if(mask) mask.remove();
         	dialog.remove();
         }
     };
    
    $('#button_ok', dialog).bind('click', ok);
    $('#button_cancel', dialog).bind('click', cancel);
    $('#thread_close',dialog).bind('click',cancel);

    dialog.data('JTUI_dg',option); //将数据保存到自身对象上

    return dialog;

};

$.JTUI.dialog.defaults = {
        modal: false,
        width: 400,
        title: '',
        url: null,
        content: null,
        message: null,
        onCancel: function(dialog){},
        onOK: function(dialog){return true},
        initData: function(dialog){},
        button: '<input type="button" id="button_ok" value="确定"><input type="button" id="button_cancel" value="关闭">'
};

$.JTUI.dialog.setDefaults = function (defaults) {
        $.JTUI.dialog.defaults = $.extend({}, $.JTUI.dialog.defaults, defaults);
};

$.JTUI.dialog.methods = {
    close : function(dialog){
       return dialog.remove();
    },
    option : function(dialog){
        dialog.data('JTUI_dg');
    }
};

    



})(jQuery);