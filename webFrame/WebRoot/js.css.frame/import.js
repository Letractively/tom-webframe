function loadScript(src) {
  var script = document.createElement("script");
  script.type = "text/javascript";
  script.src = src;  //"http://maps.google.com/maps/api/js?sensor=false&callback=initialize"
  document.body.appendChild(script);
}


window.onload = loadScript;

Server.importJs("/JSFrame/jquery-1.4.4.min.js");
Server.importJs("/JSFrame/easyUI/jquery.easyui.min.js");
Server.importJs("/JSFrame/zTtree/jquery.ztree.all-3.2.js");
Server.importJs("/JSFrame/app.js");

