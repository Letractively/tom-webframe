(function($) {
    var fn = $.fn;

    /*使用方法 $.toString(jq)*/
    fn.toString = function() {
        return  $("<div id='JTUI_toString'>").append(this.clone()).html();
    };

    /*元素居中*/
    fn.center = function() {
        return this.css({
            position:'absolute',
            top:($(window).height() - $(this).height() - 100 ) / 2 + $(window).scrollTop() + 'px',
            left: ( $(window).width() - $(this).width() ) / 2 + $(window).scrollLeft() + 'px'
        })
    };

    /* 判断浏览器是否是IE并且版本小于8,非IE返回浏览器类型和版本 */
    fn.isLessThanIe8 = function() {
        if ($.browser.msie) {
            return ($.browser.version > 7);
        }
        return navigator.userAgent;
    };

   


})(jQuery);






