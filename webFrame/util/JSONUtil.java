package webFrame.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aaaa.bean.Info;


import webFrame.app.control.RequestContext;
import webFrame.app.db.*;
import webFrame.report.Log;

public class JSONUtil /*extends com.googlecode.jsonplugin.JSONUtil*/ {

    /*static 方法不会被父类覆盖*/
    @SuppressWarnings("unchecked")
    public static String serialize(Object obj) {
        String reString = "";
        try {
            if (obj instanceof Record<?, ?>) {
                Map<?, ?> map = ((Record<?, ?>) obj).recMap;
                reString = com.googlecode.jsonplugin.JSONUtil.serialize(map);
            } else if (obj instanceof List<?> && ((List<?>) obj).size() > 0 && ((List<?>) obj).get(0) instanceof Record<?, ?>) {
                reString = com.googlecode.jsonplugin.JSONUtil.serialize(toList((List<Record<String, Object>>) obj));
            } else if (obj instanceof String) {
                reString = (String) obj;
            } else {
                reString = com.googlecode.jsonplugin.JSONUtil.serialize(obj);
            }
        } catch (Exception e) {
            Log.writeLog("JSONUtil.serialize", e);
        }
        return reString;
    }

    public static List<Map<String, Object>> toList(List<Record<String, Object>> recs) {
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        for (Record<String, Object> rec : recs) {
            Map<String, Object> map = rec.recMap;
            listMap.add(map);
        }
        return listMap;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> deserializeBeans(String json, Class<T> _bean) {
        List<T> list = new ArrayList<T>();
        try {
            List<Map<String, Object>> list2 = (List<Map<String, Object>>) com.googlecode.jsonplugin.JSONUtil.deserialize(json);
            for (Map<String, Object> map : list2) {
                list.add(RequestContext.normalized(map, _bean));
            }
        } catch (Exception e) {
            Log.writeLog("JSONUtil.deserializeBeans", e);
        }
        return list;
    }

    public static <T> T deserializeBean(String json, Class<T> _bean) {
        T t = null;
        try {
            Map<?, ?> map = (Map<?, ?>) com.googlecode.jsonplugin.JSONUtil.deserialize(json);
            t = RequestContext.normalized(map, _bean);
        } catch (Exception e) {
            Log.writeLog("JSONUtil.deserializeBean", e);
        }
        return t;
    }

    public static <T> List<T> deserializeBeans(File file, String encode, Class<T> _bean) {
        List<T> list = null;
        try {
            list = deserializeBeans(FileUtils.readLine(file,encode), _bean);
        } catch (Exception e) {
            Log.writeLog("JSONUtil.deserializeBeans", e);
        }
        return list;
    }

    public static <T> T deserializeBean(File file, String encode,Class<T> _bean) {
        T t = null;
        try {
            t = deserializeBean(FileUtils.readLine(file,encode), _bean);
        } catch (Exception e) {
            Log.writeLog("JSONUtil.deserializeBean", e);
        }
        return t;
    }
    
    public static <T> List<T> deserializeBeans(InputStream in, String encode,Class<T> _bean) {
        List<T> list = null;
        try {
            list = deserializeBeans(FileUtils.streamToStr(in, encode), _bean);
        } catch (Exception e) {
            Log.writeLog("JSONUtil.deserializeBeans", e);
        }
        return list;
    }

    public static <T> T deserializeBean(InputStream in, String encode,Class<T> _bean) {
        T t = null;
        try {
            t = deserializeBean(FileUtils.streamToStr(in, encode), _bean);
        } catch (Exception e) {
            Log.writeLog("JSONUtil.deserializeBean", e);
        }
        return t;
    }
    
    
    
    public static void main(String[] args) throws Exception {
//    	Info info =  JSONUtil.deserializeBean("{'name':'tomsun','id':'2','c_time':'2012-01-01 12:55:55'}",Info.class);
//    	new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(info.getF_c_time());
//        System.out.println(info);


//        List<Info> list =  JSONUtil.deserializeBeans("[{'name':'tomsun','id':'2','c_time':'2012-01-01 12:55:55'},{'name':'pan','id':'1','c_time':'2012-06-01 12:55:55'}]", Info.class);
//        System.out.println("list="+list);
        
        
        List<Info> info1 =  JSONUtil.deserializeBeans(new FileInputStream(new File("d:\\aa.txt")),"UTF-8" ,Info.class);
        List<Info> info2 =  JSONUtil.deserializeBeans(new File("d:\\aa.txt"),"UTF-8" ,Info.class);
        System.out.println(info1);
        System.out.println(info2);
    	
//    	Map<String, InputStream> map =  FileUtils.unZipFile(new File("d:\\package.zip"));
//    	for(String str : map.keySet()){
//    		InputStream in = map.get(str);
//    		System.out.println(str);
//    		System.out.println("--"+FileUtils.streamToStr(in)); //关闭in
//    	}
    	
        
    	
	}


}
