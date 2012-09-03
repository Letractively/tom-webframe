<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
   <%@include file="/inc/head.jsp" %>

    <script type="text/javascript" src="<%=path %>/js.css.frame/swfUp/swfupload.js"></script>
    <script type="text/javascript" src="<%=path %>/js.css.frame/swfUp/swfupload.queue.js"></script>
    <script type="text/javascript" src="<%=path %>/js.css.frame/swfUp/fileprogress.js"></script>
    <script type="text/javascript" src="<%=path %>/js.css.frame/swfUp/handlers.js"></script>

     <script type="text/javascript">
    $(function(){

        /*var a = $("li").find(".tabs-close").toString();

        document.getElementById("bbac");

        document.getElementsByTagName("input");

        //document.getElementsByClassName(".class"); <!-- IE 没有这个方法-->
        
        alert($(document.getElementsByName("edit")).val());*/


        var swfu;

			var settings = {
				flash_url : path+"/js.css.frame/swfUp/swfupload.swf",
				upload_url: "goFile$upLoad.do",
				post_params: {"PHPSESSID" : ""},
				file_size_limit : "600 MB",
				file_types : "*.*",
				file_types_description : "All Files",
				file_upload_limit : 100,
				file_queue_limit : 0,
				custom_settings : {
					progressTarget : "fsUploadProgress",
					cancelButtonId : "btnCancel"
				},
				debug: false,

				// Button settings
				button_image_url: "images/TestImageNoText_65x29.png"/*tpa=http://demo.swfupload.org/v220/simpledemo/images/TestImageNoText_65x29.png*/,
				button_width: "65",
				button_height: "29",
				button_placeholder_id: "spanButtonPlaceHolder",
				button_text: '<span class="theFont">Hello</span>',
				button_text_style: ".theFont { font-size: 16; }",
				button_text_left_padding: 12,
				button_text_top_padding: 3,

				// The event handler functions are defined in handlers.js
				file_queued_handler : fileQueued,
				file_queue_error_handler : fileQueueError,
				file_dialog_complete_handler : fileDialogComplete,
				upload_start_handler : uploadStart,
				upload_progress_handler : uploadProgress,
				upload_error_handler : uploadError,
				upload_success_handler : uploadSuccess,
				upload_complete_handler : uploadComplete,
				queue_complete_handler : queueComplete	// Queue plugin event
			};

			swfu = new SWFUpload(settings);


        
    })

        

    </script>

</head>

<body>
<div id="content">
	<form id="form1" action="goFile$upLoad.d" method="post" enctype="multipart/form-data">
		<div class="fieldset flash" id="fsUploadProgress">
				<span class="legend">Upload Queue</span>
		</div>
		<div id="divStatus">0 Files Uploaded</div>
		<div>
	        <span id="spanButtonPlaceHolder"></span>
	        <input id="btnCancel" type="button" value="Cancel All Uploads" onclick="swfu.cancelQueue();" disabled="disabled" style="margin-left: 2px; font-size: 8pt; height: 29px;" />
		</div>
	</form>
</div>
</body>
</html>
