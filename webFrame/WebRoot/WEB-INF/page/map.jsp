<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <%@include file="/inc/head.jsp" %>
    <%@include file="/inc/easyui.jsp" %>

    <script type="text/javascript" src="https://maps.google.com/maps/api/js?sensor=true"></script>
    <script type="text/javascript" src="http://code.google.com/apis/gears/gears_init.js"></script>


    <style type="text/css">

    </style>

    <script type="text/javascript">
        $(function() {

            /*var a = $("li").find(".tabs-close").toString();

             document.getElementById("bbac");

             document.getElementsByTagName("input");

             //document.getElementsByClassName(".class"); <!-- IE 没有这个方法-->

             alert($(document.getElementsByName("edit")).val());*/


            /*google map*/
            initialize();

        });

        var initialLocation;
        var siberia = new google.maps.LatLng(60, 105);
        var newyork = new google.maps.LatLng(40.69847032728747, -73.9514422416687);
        var browserSupportFlag = new Boolean();
        var infowindow = new google.maps.InfoWindow();

        function initialize() {
            var myOptions = {
                zoom: 6,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            var map = new google.maps.Map(document.getElementById("map_google"), myOptions);

            // Try W3C Geolocation (Preferred)
            if (navigator.geolocation) {
                browserSupportFlag = true;
                navigator.geolocation.getCurrentPosition(function(position) {
                    initialLocation = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                    contentString = "your location.";
                    map.setCenter(initialLocation);
                    infowindow.setContent(contentString);
                    infowindow.setPosition(initialLocation);
                    infowindow.open(map);
                }, function() {
                    handleNoGeolocation(browserSupportFlag);
                });
                // Try Google Gears Geolocation
            } else if (google.gears) {
                browserSupportFlag = true;
                var geo = google.gears.factory.create('beta.geolocation');
                geo.getCurrentPosition(function(position) {
                    initialLocation = new google.maps.LatLng(position.latitude, position.longitude);
                    contentString = "your location.";
                    map.setCenter(initialLocation);
                    infowindow.setContent(contentString);
                    infowindow.setPosition(initialLocation);
                    infowindow.open(map);
                }, function() {
                    handleNoGeoLocation(browserSupportFlag);
                });
                // Browser doesn't support Geolocation
            } else {
                browserSupportFlag = false;
                handleNoGeolocation(browserSupportFlag);
            }
            function handleNoGeolocation(errorFlag) {
                if (errorFlag == true) {
                    contentString ="Geolocation service failed.";
                    initialLocation = newyork;
                } else {
                    contentString = "Your browser doesn't support geolocation. We've placed you in Siberia.";
                    initialLocation = siberia;
                }
                map.setCenter(initialLocation);
                infowindow.setContent(contentString);
                infowindow.setPosition(initialLocation);
                infowindow.open(map);
            }
        }

    </script>


    <script type="text/javascript" src="http://api.map.baidu.com/api?v=1.3"></script>
    <script type="text/javascript">

        $(function() {
            var map = new BMap.Map("map_baidu"); // 创建地图实例
            var point = new BMap.Point(116.404, 39.915); // 创建点坐标
            map.centerAndZoom(point, 15); // 初始化地图，设置中心点坐标和地图级别
            function myFun(result){
                var cityName = result.name;
                map.setCenter(cityName);
            }
            var myCity = new BMap.LocalCity();
            myCity.get(myFun);

        })



        
    </script>

   

</head>

<body>

<label style="font-size: 20px;color: green">google map定位 </label>
<a href="https://developers.google.com/maps/?hl=zh-CN">更多操作点击这里</a>
或
<a href="http://www.oschina.net/code/search?q=google">这里</a>


<div id="map_google" style="width:100%; height:48%"></div>


<label style="font-size: 20px;color: green">baidu map 定位 </label>
<a href="http://dev.baidu.com/wiki/static/map/API/examples/">更多操作点击这里</a>



<div id="map_baidu" style="width:100%; height:48%"></div>
</body>
</html>
