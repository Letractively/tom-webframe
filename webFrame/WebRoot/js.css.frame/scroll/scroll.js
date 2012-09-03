(function($) {
    
    /*div-ul-li 横向滚动*/
    fn.scroll = function(options) {

        //默认配置
        var defaults = {
            auto:[false,3000], //是否自动滚动,第二个参数是自动滚动是切换的间隔时间
            visible:4, //可显示的数量
            speed:1000, //动画时间，可选slow，fast，数值类
            scroll:1 //每次切换的个数,此数小于等于visible值
        };

        var opts = $.extend({}, defaults, options);
        opts.scroll = opts.scroll > opts.visible ? opts.visible : opts.scroll;

        var scroll = $("<div id='JTUI_scroll' style='position: relative;overflow: hidden;padding:2px 0 0 0 ;' ></div>");
        this.wrapAll(scroll);

        var innerBlock = this.find("ul"),
                list = innerBlock.find('li'),
                listNum = list.size(),
                listWidth = list.width(),
                blockWidth = listWidth * opts.visible,//可见区域宽度
                intervalId;

        //获取已滚动个数
        function getSnum() {
            return (parseInt(innerBlock.css("margin-left")) * -1) / listWidth;
        }

        //获取滚动的距离
        function getMove(c) {
            return c >= opts.scroll ? opts.scroll * listWidth : c * listWidth;
        }

        if (!opts.auto[0]) {
            /*前后按钮*/
            var prevBtn = $("<div class='tabs-scroller-left'></div>");
            var nextBtn = $("<div class='tabs-scroller-right'></div>");
            this.before(prevBtn);
            this.before(nextBtn);
            prevBtn.show();
            nextBtn.show();


            //设置宽度样式包括,scroll,block,ul
            $("#JTUI_scroll").width(blockWidth + prevBtn.width() + nextBtn.width());
            this.width(blockWidth).find("ul").width(listWidth * listNum);

            //单击向后按钮
            nextBtn.click(function() {
                var snum = getSnum(),
                        c = listNum - snum - opts.visible,
                        m = getMove(c);
                if (listNum - snum > opts.visible) {
                    innerBlock.animate({
                        "margin-left":"-=" + m
                    }, opts.speed);
                }
            });

            //单击向前按钮
            prevBtn.click(function() {
                var snum = getSnum(),
                        m = getMove(snum);
                if (snum > 0) {
                    innerBlock.animate({
                        "margin-left":"+=" + m
                    }, opts.speed);
                }
            });
        }

        //如果自动滚动
        if (opts.auto[0]) {
            $(this).width(blockWidth);

            function auto() {
                var snum = getSnum(),
                        m = getMove(listNum - snum - opts.visible);

                if (listNum - snum > opts.visible) {
                    innerBlock.animate({
                        "margin-left":"-=" + m
                    }, opts.speed);
                } else {
                    innerBlock.css("margin-left", 0).find('li').slice(0, listNum - opts.visible).appendTo(innerBlock);
                }
            }


            //当鼠标经过滚动区域停止滚动
            this
                    .hover(function() {
                clearInterval(intervalId);
            }, function() {
                intervalId = setInterval(auto, opts.auto[1] + Math.abs(opts.speed - opts.auto[1]) + 100);
            })
                    .trigger('mouseleave');

        }


    };

})(jQuery);
