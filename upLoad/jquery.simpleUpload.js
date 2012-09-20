/**
 * Created by panmg
 * Date: 12-9-19
 * Time: 下午2:39
 * 1.0 上传无取消功能
 */

(function($) {
    var fn = $.fn;
    var uuid = 0;

    function handleData(iframe) { //解析返回iframe中的内容
        var data = $(iframe).contents().find('body')[0].innerHTML;
        try {
            data = window.eval('(' + data + ')');
        } catch(e) {
            data = {};
            alert("返回JSON字符串有误!");
        }
        return data;
    }

    /*手动提交,提交点自己控制,可避免冗余上传数据
    * $(input:file).sUpload()
    */
    fn.sUpload = function(url, callback) {
        var iframeId = 'simpleUpload' + ++uuid;
        var form = this.wrapAll('<form style="margin:0;display:inline" action="'+url+'" method="post" enctype="multipart/form-data" target="' + iframeId + '"/>').parent();
        var iframe = $('<iframe style="position:absolute;top:-9999px" name="' + iframeId + '"/>').appendTo('body');

        form.submit(function() {  //第一个是覆写submit,第二个引用
            iframe.load(function() {
                var data = handleData(this);
                callback.call(this, data);

                setTimeout(function() {
                    iframe.remove();
                }, 500)
            });
        }).submit();
    };


    
    /*自动提交方式,用于提交后的预览模式*/
    fn.tUpload = function(option){
        var self = this;
        var iframeId = 'tomUpload' + ++uuid;
        var defaults = {
            url: '',
            ext: '',
            extMsg: '只可上传${ext}的文件!',
            onSend: function(){},
            onComplete: function(){},
            onCancel: function(){}
        };
        option = $.extend({}, defaults, option);
        return $(this).css({"position":"relative","overflow":"hidden",direction:'ltr'}).append(createInput());



        function createInput(){
            var iframe = $('<iframe style="position:absolute;top:-9999px" name="' + iframeId + '"/>').appendTo('body');
            var inputFile =$('<input type="file"  name="tUpload" onchange="this.blur()"/>').css({
                position: 'absolute',
                 right: 0,
                 top: 0,
                 margin: 0,
                 opacity: 0,
                 padding: 0,
                 fontFamily: 'Arial',
                 fontSize: '20px',
                 verticalAlign: 'baseline',
                 cursor: 'pointer'
            });
            
            inputFile.bind("change", function(){
                var form = $('<form style="margin:0;display:inline" action="'+option.url+'" method="post" enctype="multipart/form-data" target="' + iframeId + '"></form>');
                form = inputFile.wrapAll(form).parent();
                
                var filename = checkFileName(inputFile[0].value);
                if(!filename){
                    return;
                }
                option.onSend(filename);

                form.submit(function(){
                    iframe.load(function() {
                        var data = handleData(this);
                        option.onComplete(filename,data);

                        setTimeout(function() {
                            iframe.remove();
                            form.remove();
                            $(self).tUpload(option); //重新绑定,以解决 IE onchangBug 和form冗余问题
                        }, 500);
                    });
                }).submit();
            });
            return inputFile;
        }


        function checkFileName(file){
            file = file.replace(/.*(\/|\\)/, "");
            var ext = (-1 !== file.indexOf('.')) ? file.replace(/.*[.]/, '').toLowerCase() : '';
            if(option.ext!="" && option.ext.indexOf(ext) == -1){
                alert(option.extMsg.replace("${ext}",option.ext));
                return "";
            }
            return file;
        }
    }
    


})(jQuery);