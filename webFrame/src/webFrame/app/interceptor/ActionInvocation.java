package webFrame.app.interceptor;

import webFrame.app.control.AppControl;
import webFrame.app.control.RequestContext;
import webFrame.app.listener.Variable;
import webFrame.report.Log;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-5-29
 * Time: 上午10:32
 */
public class ActionInvocation implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String className = null;
    private String methodName = null;
    private String suffix = null;

    public ActionInvocation(String className, String methodName, String suffix) {
        this.className = className;
        this.methodName = methodName;
        this.suffix = suffix;
    }

    public AppControl<?> loadAction() throws Exception {
        AppControl<?> control = null;
        try {
            Class<?> c = Class.forName(className);
            control = (AppControl<?>) c.newInstance();
        } catch (ClassNotFoundException e) {
            Log.writeLog("App.loadAction" + e.toString());
        } catch (Exception e) {
            throw e;
        }
        return control;

    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public RequestContext getRequestContext() {
        return RequestContext.get();
    }

    public String getSuffix() {
        return suffix;
    }

    public Map<String, String> getParaMap() {
        RequestContext requestContext =  getRequestContext();
        if("XMLHttpRequest".equals(requestContext.getRequest().getHeader("X-Requested-With"))){
            if("POST".equals(requestContext.getRequest().getMethod())){
                return requestContext.getParameterMap();
            }
        }
        return (Variable.encoding.indexOf("GB")!=-1) ?
                requestContext.getParameterMapEc() : requestContext.getParameterMapUTF();
    }


}
