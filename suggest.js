/**
 *
 * @param options{
 *  url: "", 加载的url
 *  width: 120,  宽度
 *  format: function(){}, 如何格式化显示
 *  onEnter: function(){} 选中后回车事件
 * }
 */
$.fn.suggest = function(options) {
    var target = $(this);
    var defaults = {
        width: target.outerWidth(),
        format: function(item) {
            return item;
        },
        onEnter : function(target) {
        }
    };
    var opt = $.extend({}, defaults, options);
    if (!target.data("suggest")) {
        var menu = $("<ul class='suggest_menu'></ul>").width(opt.width).appendTo('body');
        target.data("suggest", menu);
    }
    var index = -1;
    var last = "";
    target.unbind("keyup.suggest").bind("keyup.suggest",
            function(e) {
                var suggest = target.data("suggest"); // 注意这种写法,其实可以直接 var suggest = menu;
                if (e.keyCode == 40) {
                    //向下
                    e.preventDefault();
                    if (index < suggest.children().length - 1) {
                        index ++;
                        suggest.find(".selected").removeClass("selected");
                        suggest.find("#"+index).addClass("selected");
                    }
                } else if (e.keyCode == 38) {
                    //向上
                    e.preventDefault();
                    suggest.find(".selected").removeClass("selected");
                    if (index >= 0) {
                        index --;
                        suggest.find("#"+index).addClass("selected");
                    }
                } else if (e.keyCode == 13) {
                    var selected = suggest.find(".selected");
                    if (selected.length) {
                        target[0].value = selected[0].innerHTML;
                    }
                    if (opt.onEnter) {
                        opt.onEnter(target);
                    }
                    suggest.empty().popMenu("close");
                    return;
                }

                var value = target[0].value;
                if (value == "") {
                    suggest.empty().popMenu("close");
                } else if (value != last) {
                    index = -1;
                    $.get(opt.url, {q: value}, function(data) {
                        suggest.empty();
                        if (data.length == 0) {
                            suggest.popMenu("close");
                        } else {
                            for (var i = 0; i < data.length; i++) {
                                var item = data[i];
                                var itemHtml = "<li id='" + i + "' class='suggest_item' >"; 
                                itemHtml += opt.format(item) + "</li>";
                                suggest.append(itemHtml);
                            }
                            suggest.popMenu({
                                target: target
                            });
                            suggest.find(".suggest_item").bind("mousedown", function(e) {
                                e.preventDefault();
                                target[0].value = this.innerHTML;
                                if (opt.onEnter) {
                                    opt.onEnter(target);
                                }
                                suggest.empty().popMenu("close");
                            });
                        }
                    });
                }
                last = value;
            }).unbind("blur.suggest").bind("blur.suggest",
                function(e) {
                    var suggest = target.data("suggest");
                    suggest.empty().popMenu("close");
                });
};
