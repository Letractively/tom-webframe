/**
 * 下拉菜单
 * @param options{
 *  position: left, center, right
 *  offsetX 左偏移
 *  offsetY 上下偏移
 *  zindex zindex值，默认2
 *  autoClose 是否点击其他区域后自动关闭
 *  closeAfterClick 点击后是否自动关闭
 *  target 相对的弹出元素,用于确定弹出元素的位置
 *  onClose 关闭时执行的函数
 *  autoPosition 是否当显示位置超出浏览器窗口时，自动移动
 * }
 */
$.fn.popMenu = function(options) {
    var menu = $(this);
    if (typeof options == "string") {
        //关闭
        if (options == "close") {
            menu.hide();
            $(window).unbind("resize.popmenu");
        }
        return;
    }
    var defaults = {
        position: "left",
        offsetX: 0,
        offsetY: 0,
        zindex: 2,
        autoClose: true,
        closeAfterClick: false,
        autoPosition: true
    };
    var opt = $.extend({}, defaults, options);
    var target = $(opt.target);
    menu.css("z-index", opt.zindex);
    if (opt.autoClose) {
        if (opt.closeAfterClick == false) {
            menu.unbind("mouseup.popmenu").bind("mouseup.popmenu", function(e) {
                e.stopPropagation();
            });
        }
        $(document).bind("mouseup.popmenu", function() {
            menu.popMenu("close");
            $(document).unbind("mouseup.popmenu");
            if (opt.onClose) {
                opt.onClose();
            }
        });
    }
    $(window).bind("resize.popmenu", function() {
        menu.popMenu(options);
    });

    var left = 0;
    if (opt.position == "center") {
        left = target.offset().left + target.outerWidth() / 2 - menu.outerWidth() / 2;
    } else if (opt.position == "right") {
        left = target.offset().left + target.outerWidth() - menu.outerWidth();
    } else {
        left = target.offset().left;

    }
    if (left + menu.outerWidth() > $(window).width()) {
        left = $(window).width() - menu.outerWidth();
    }
    var top = target.offset().top + target.outerHeight();
    if (opt.autoPosition && top + opt.offsetY + menu.outerHeight() > $(window).height() + $(document).scrollTop()) {
        menu.css({
            top:$(window).height() - menu.outerHeight() + $(document).scrollTop(),
            left:left + opt.offsetX
        });
    } else {
        menu.css({
            top:top + opt.offsetY,
            left:left + opt.offsetX
        });
    }
     menu.show();
};