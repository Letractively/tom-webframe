<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>

<title>My JSP 'MyJsp.jsp' starting page</title>
<%@include file="/inc/head.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath() %>/js.css.frame/upLoad/jquery.jUploader-1.0.min.js"></script>
<!--
<link rel="stylesheet" type="text/css" href="styles.css">
-->

<style type="text/css">
    body {
        margin: 30px;
        font-family: verdana, arial, helvetica, sans-serif;
        font-size: 12px;
        line-height: 18px;
        background: #373A32;
        color: #D0D0D0;
    }

    h1 {
        color: #C7D92C;
        font-size: 22px;
        font-weight: 600;
    }

    h2 {
        color: #C7D92C;
        font-size: 16px;
        font-weight: 300;
    }

    h3 {
        font-size: 13px;
        font-weight: 200;
    }

    a {
        color: #C7D92C;
    }

    a:hover, a.hover {
        color: white;
    }

    img {
        border: none;
    }

    .line {
        margin: 20px 0;
        height: 0px;
        border-bottom: 1px dashed #aaa;
    }

    .preview {
        padding: 3px;
        border: none;
        height: 128px;
        width: 128px;
        background-color: #D0D0D0;
    }

    .wrap {
        width: 100%;
        height: auto;
        min-height: 160px;
        background: none repeat scroll 0 0 #EEEEEE;
        border-radius: 5px 5px 5px 5px;
        display: inline-block;
    }

    .item {
        padding: 10px;
    }

    /* jUploader 按钮样式，应该到自己的项目中时，可以根据你的要求的写它的样式，但是类名不可以改 */
    .jUploader-button {
        width: auto;
        background: none repeat-x scroll 0 0 #222222;
        border-bottom: 1px solid rgba(0, 0, 0, 0.25);
        border-radius: 4px 4px 4px 4px;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.25);
        text-shadow: 0 -1px 1px rgba(0, 0, 0, 0.25);
        color: #FFFFFF !important;
        display: inline-block;
        font-weight: bold;
        padding: 2px 12px 3px;
        text-decoration: none;
        cursor: pointer;
    }

    .jUploader-button-hover {
        background-color: #111111;
        color: #fff;
    }
</style>


<script type="text/javascript">
    // 设置全局配置
    $.jUploader.setDefaults({
        cancelable: true,
        allowedExtensions: ['jpg', 'png', 'gif'],
        messages: {
            upload: '上传',
            cancel: '取消',
            emptyFile: "{file} 为空，请选择一个文件.",
            invalidExtension: "{file} 后缀名不合法. 只有 {extensions} 是允许的.",
            onLeave: "文件正在上传，如果你现在离开，上传将会被取消。"
        }
    });

    // 例子1
    function demo1() {
        $.jUploader({
            button: 'upload-button1', // 这里设置按钮id
            action: 'goFile$upLoad.do?test_cancel=1', // 这里设置上传处理接口，这个加了参数test_cancel=1来测试取消

            // 上传完成事件
            onComplete: function (fileName, response) {
                // response是json对象，格式可以按自己的意愿来定义，例子为： { success: true, fileUrl:'' }
                if (response.success) {
                    alert("成功");
                    $('#photo1').attr('src', "goFile$readIMG.do?fileName=" + fileName);
                    // 这里说明一下，一般还会在图片附近加添一个hidden的input来存放这个上传后的文件路径(response.fileUrl)，方便提交到服务器保存
                } else {
                    alert('上传失败');
                }
            }
        });
    }

    // 例子2
    function demo2() {
        $.jUploader({
            button: 'upload-button2', // 这里设置按钮id
            action: 'goFile$upLoad.do?test_cancel=1', // 这里设置上传处理接口

            // 开始上传事件
            onUpload: function (fileName) {
                $('#photo2').hide();
                $('#loading2').show();
            },

            // 上传完成事件
            onComplete: function (fileName, response) {
                // response是json对象，格式可以按自己的意愿来定义，例子为： { success: true, fileUrl:'' }
                if (response.success) {
                    $('#loading2').hide();
                    $('#photo2').attr('src', "goFile$readIMG.do?fileName=" + fileName).show();

                    // 这里说明一下，一般还会在图片附近加添一个hidden的input来存放这个上传后的文件路径(response.fileUrl)，方便提交到服务器保存
                } else {
                    $('#photo2').show();
                    $('#loading2').hide();
                    alert('上传失败');
                }
            },

            // 取消上传事件
            onCancel: function (fileName) {
                $('#photo2').show();
                $('#loading2').hide();
            }
        });
    }

    // 例子3
    function demo3() {
        $.jUploader({
            button: 'upload-button3', // 这里设置按钮id
            action: 'goFile$upLoad.do?test_cancel=1', // 这里设置上传处理接口

            // 开始上传事件
            onUpload: function (fileName) {
                $('#tip').text('正在上传 ' + fileName + ' ...');
            },

            // 上传完成事件
            onComplete: function (fileName, response) {
                // response是json对象，格式可以按自己的意愿来定义，例子为： { success: true, fileUrl:'' }
                if (response.success) {
                    $('#photo3').attr('src', "goFile$readIMG.do?fileName=" + fileName);
                    $('#tip').text(fileName + ' 上传成功。');

                    // 这里说明一下，一般还会在图片附近加添一个hidden的input来存放这个上传后的文件路径(response.fileUrl)，方便提交到服务器保存
                } else {
                    $('#tip').text('上传失败');
                }
            },

            // 系统信息显示（例如后缀名不合法）
            showMessage: function (message) {
                $('#tip').text(message);
            },

            // 取消上传事件
            onCancel: function (fileName) {
                $('#tip').text(fileName + ' 上传取消。');
            }
        });
    }

    // 例子4（使用jBox提示窗口）
    function demo4() {
        $.jUploader({
            button: 'upload-button4', // 这里设置按钮id
            action: 'goFile$upLoad.do?test_cancel=1', // 这里设置上传处理接口

            // 开始上传事件
            onUpload: function (fileName) {
                jBox.tip('正在上传 ' + fileName + ' ...', 'loading');
            },

            // 上传完成事件
            onComplete: function (fileName, response) {
                // response是json对象，格式可以按自己的意愿来定义，例子为： { success: true, fileUrl:'' }
                if (response.success) {
                    jBox.tip('上传成功', 'success');
                    $('#tip2').text(fileName + ' 上传成功。');
                    $('#photo4').attr('src', "goFile$readIMG.do?fileName=" + fileName);
                } else {
                    jBox.tip('上传失败', 'error');
                }
            },

            // 系统信息显示（例如后缀名不合法）
            showMessage: function (message) {
                jBox.tip(message, 'error');
            },

            // 取消上传事件
            onCancel: function (fileName) {
                jBox.tip(fileName + ' 上传取消。', 'info');
            }
        });
    }

    $(function () {
        // 初始化
        demo1();
        demo2();
        demo3();
        demo4();
    });


</script>

</head>

<body>

<div>
    <%--例子1--%>
    <div class="wrap">
        <div class="item">
            <table>
                <tr>
                    <td style="width:145px;">
                        <div class="preview"><img id="photo1" width="128" height="128" src="empty.png" alt="photo"/>
                        </div>
                    </td>
                    <td valign="bottom">
                        <div id="upload-button1"><span></span></div>
                        <!-- div里的span必须保留，用来放文字的 --></td>
                </tr>
            </table>
        </div>
    </div>
    <div class="line"></div>


    <!-- 例子2 -->
    <div class="wrap">
        <div class="item">
            <table>
                <tr>
                    <td style="width:145px;">
                        <div class="preview">
                            <img id="photo2" width="128" height="128" src="empty.png" alt="photo"/>
                            <img id="loading2" style="margin:55px 55px; display:none" width="15" height="15"
                                 src="loading.gif" alt="loading"/>
                        </div>
                    </td>
                    <td valign="bottom">
                        <div id="upload-button2"><span></span></div>
                        <!-- div里的span必须保留，用来放文字的 --></td>
                </tr>
            </table>
        </div>
    </div>
    <div class="line"></div>

    <!-- 例子3 -->
    <div class="wrap">
        <div class="item">
            <table>
                <tr>
                    <td style="width:145px;">
                        <div class="preview"><img id="photo3" width="128" height="128" src="empty.png" alt="photo"/>
                        </div>
                    </td>
                    <td valign="bottom">
                        <div id="upload-button3"><span></span></div>
                        <!-- div里的span必须保留，用来放文字的 --></td>
                    <td valign="bottom"><span id="tip" style="color:#FF6600">请选择文件</span></td>
                </tr>
            </table>
        </div>
    </div>
    <div class="line"></div>

    <!-- 例子4 -->
    <div class="wrap">
        <div class="item">
            <table>
                <tr>
                    <td style="width:145px;">
                        <div class="preview"><img id="photo4" width="128" height="128" src="empty.png" alt="photo"/>
                        </div>
                    </td>
                    <td valign="bottom">
                        <div id="upload-button4"><span></span></div>
                        <!-- div里的span必须保留，用来放文字的 --></td>
                    <td valign="bottom"><span id="tip2" style="color:#FF6600">请选择文件</span></td>
                </tr>
            </table>
        </div>
    </div>


</div>

</body>
</html>
