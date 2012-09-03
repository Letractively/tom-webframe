package webFrame.app.control;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.*;

import webFrame.app.db.DBUtils;
import webFrame.app.listener.Variable;
import webFrame.report.Log;


public class RequestContext {
    private ServletContext context;
    private HttpSession session;
    private HttpServletRequest req;
    private HttpServletResponse res;
    private Map<String, Cookie> cookies;
    private final static ThreadLocal<RequestContext> contexts = new ThreadLocal<RequestContext>();

    public ServletContext getContext() {
        return context;
    }

    public HttpSession getSession() {
        return session;
    }

    public HttpServletRequest getRequest() {
        return req;
    }

    public HttpServletResponse getResponse() {
        return res;
    }

    public Map<String, Cookie> getCookies() {
        return cookies;
    }

    public static RequestContext begin(ServletContext ctx, HttpServletRequest req, HttpServletResponse res) {
        RequestContext rc = new RequestContext();
        rc.context = ctx;
        rc.req = req;
        rc.res = res;
        rc.session = req.getSession(); // req.getSession(false) 默认不创建session
        rc.cookies = new HashMap<String, Cookie>();
        Cookie[] cookies = req.getCookies();
        if (cookies != null)
            for (Cookie ck : cookies) {
                rc.cookies.put(ck.getName(), ck);
            }
        contexts.set(rc);
        return rc;
    }

    public static RequestContext get() {
        return contexts.get();
    }

    public void end() {
        /*关闭时删除临时文件数据*/
        /*String tmpPath = (String)req.getAttribute(TEMP_UPLOAD_PATH_ATTR_NAME);
          if(tmpPath != null){
              try {
                  FileUtils.deleteDirectory(new File(tmpPath));
              } catch (IOException e) {
                  log.fatal("Failed to cleanup upload directory: " + tmpPath, e);
              }
          }*/
        this.context = null;
        this.req = null;
        this.res = null;
        this.session = null;
        this.cookies = null;
        contexts.remove();
    }
    
    public void gotoPage(String url, boolean config) {
        String path = "";
        if (config) {
            path = Variable.pageContext + "/" + url + Variable.pageSuffix;
        } else {
            path = url;
        }
        try {
            req.getRequestDispatcher(path).forward(req, res);
        } catch (Exception e) {
            Log.writeLog("RequestContext.gotoPage", e);
        }
    }

    public void jumpPage(String url) {
        try {
            res.sendRedirect(url);
        } catch (Exception e) {
            Log.writeLog("RequestContext.jumpPage", e);
        }
    }

    public void alertMsg(String msg, String script) {
        req.setAttribute("msg", msg);
        script = (script == null) || (script.length() == 0) ? "location='about:blank'" : script;
        req.setAttribute("script", script);
        gotoPage("/common/pageMsg.jsp", false);
    }

    public void printData(String contentType, String data) {
        res.setContentType(contentType); // "text/html; charset=utf-8"
        res.setHeader("Pragma", "no-cache");
        res.setHeader("Cache-Control", "no-cache");
        res.setDateHeader("Expires", 0L);
        try {
            res.getWriter().print(data);
            res.flushBuffer();
        } catch (IOException e) {
            Log.writeLog("RequestContext.PrintStringData", e);
        }
    }

    public void printText(String data) {
        printData("text/html; charset="+ Variable.encoding, data);
    }

    public void printJSONData(Object obj) {
        String reString = "";
        try {
            reString = webFrame.util.JSONUtil.serialize(obj);
            if ("DEBUG".indexOf(Variable.logLevel)!=-1) {
				Log.writeLog_SQL(reString);
			}
        } catch (Exception e) {
            Log.writeLog("RequestContext.printJSONData", e);
        }
        printData("application/json;charset="+ Variable.encoding, reString);
    }

    public String getParameterEc(String name) {
        String temp = req.getParameter(name);
        try {
            return (temp != null) && (temp.length() > 0) ? new String(temp.getBytes("ISO-8859-1")).trim() : "";
        } catch (Exception e) {
            Log.writeLog("RequestContext.getParameterEc", e);
        }
        return "";
    }

    public String getParameterUTF(String name) {
        String temp = req.getParameter(name);
        try {
            return (temp != null) && (temp.length() > 0) ? new String(temp.getBytes("ISO-8859-1"), "UTF-8").trim() : "";
        } catch (Exception e) {
            Log.writeLog("RequestContext.getParameterUTF", e);
        }
        return "";
    }

    public String getParameter(String name) {
         String temp = req.getParameter(name);
        try {
            return (temp != null) && (temp.length() > 0) ? temp.trim() : "";
        } catch (Exception e) {
            Log.writeLog("RequestContext.getParameterUTF", e);
        }
        return "";
    }

	public void setHeader(String param1, String param2) {
		res.setHeader(param1, param2);
	}

	public void setContentType(String conType) {
		res.setContentType(conType);
	}
	
	public OutputStream getOutputStream() throws IOException{
		return res.getOutputStream();
	}
    
    public Object getAttribute(String name) {
        return req.getAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        req.setAttribute(name, value);
    }

    public Object getSessionAttribute(String name) {
        return req.getSession().getAttribute(name);
    }

    public void setSessionAttribute(String name, Object value) {
        req.getSession().setAttribute(name, value);
    }

    public Object getParam(String name) {
        Object obj = req.getParameter(name);
        if (obj != null)
            return obj;

        obj = req.getAttribute(name);
        if (obj != null)
            return obj;

        obj = req.getSession().getAttribute(name);
        return obj;
    }

    public void removeParam(String name) {
        req.removeAttribute(name);
        req.getSession().removeAttribute(name);
    }

    public String[] getParameterValuesEc(String name) {
        String[] result = req.getParameterValues(name);
        try {
            for (int i = 0; i < result.length; i++) {
                String temp = result[i];
                temp = (temp != null) && (temp.length() > 0) ? new String(temp.getBytes("ISO-8859-1")).trim() : "";
                result[i] = temp;
            }
            return result;
        } catch (Exception e) {
            Log.writeLog("RequestContext.getParameterValuesEc", e);
        }
        return null;
    }

    public Map<String, String> getParameterMap() {
        Map<?, ?> hm = req.getParameterMap();
        Map<String, String> parameterMap = new HashMap<String, String>();
        Iterator<?> ite = hm.keySet().iterator();
        while (ite.hasNext()) {
            String key = ite.next().toString();
            String[] values = (String[])hm.get(key);
            String pamValues = "";
            for (String value : values) {
                try {
                    if(pamValues.length()>0) pamValues += ",";
                    pamValues += value.trim();
                    parameterMap.put(key, pamValues);
                } catch (Exception e) {
                    Log.writeLog("RequestContext.getParameterMap", e);
                }
            }
        }
        return parameterMap;
    }

    public Map<String, String> getParameterMapEc() {
        Map<?, ?> hm = req.getParameterMap();
        Map<String, String> parameterMap = new HashMap<String, String>();
        Iterator<?> ite = hm.keySet().iterator();
        while (ite.hasNext()) {
            String key = ite.next().toString();
            String[] values = (String[])hm.get(key);
            String pamValues = "";
            for (String value : values) {
                try {
                    /*value = URLDecoder.decode(value, "GBK");*/
                    value = new String(value.getBytes("ISO-8859-1"));
                    if(pamValues.length()>0)  pamValues += ",";
                    pamValues += value.trim();
                    parameterMap.put(key, pamValues);
                } catch (Exception e) {
                    Log.writeLog("RequestContext.getParameterMapEc", e);
                }
            }
        }
        return parameterMap;
    }

    public Map<String, String> getParameterMapUTF() {
        Map<?, ?> hm = req.getParameterMap();
        Map<String, String> parameterMap = new HashMap<String, String>();
        Iterator<?> ite = hm.keySet().iterator();
        while (ite.hasNext()) {
            String key = ite.next().toString();
            String[] values =(String[]) hm.get(key);
            String pamValues = "";
            for (String value : values) {
                try {
                    /*value = URLDecoder.decode(value, "UTF-8");*/
                    value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
                    if(pamValues.length()>0)  pamValues += ",";
                    pamValues += value.trim();
                    parameterMap.put(key, pamValues);
                } catch (Exception e) {
                    Log.writeLog("RequestContext.getParameterMapUTF", e);
                }
            }
        }
        return parameterMap;
    }

    public static <T> T normalized(Map<?, ?> map, Class<T> bean) {
        T t = null;
        try {
            t = DBUtils.ObjectToBean(map, bean);
        } catch (Exception e) {
            Log.writeLog("Page.normalized", e);
        }
        return t;
    }

    {
//		 Enumeration names = req.getParameterNames();
//		 while (names.hasMoreElements()) {
//		 String parm = names.nextElement().toString();
//		 System.out.println(req.getParameter(parm)+"--"+parm);

    }


}
