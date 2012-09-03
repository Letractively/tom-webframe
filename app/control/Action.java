package webFrame.app.control;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import webFrame.app.interceptor.ActionInvocation;
import webFrame.app.interceptor.ProxyHandle;
import webFrame.app.listener.Variable;
import webFrame.report.Log;

public final class Action extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String queryAction = req.getRequestURI();
        if ((queryAction == null) || (queryAction.length() == 0)) {
            return;
        }

        String suffix = "";
        int i = 0;

        i = queryAction.lastIndexOf(".");
        if (i != -1) {
            suffix = queryAction.substring(i + 1, queryAction.length()); // 取action后缀
            queryAction = queryAction.substring(0, i); // 去掉后缀(/webFrame/goLogin)
        }

        i = queryAction.lastIndexOf("/"); // /取action名称goLogin
        if (i != -1) {
            queryAction = queryAction.substring(i + 1);
        }
        String actionUrl = queryAction; // 保存 action$method.do的这种action
        String[] action_method = queryAction.split("\\$"); // 解析action$method
        queryAction = action_method[0];

        String className = (className = Variable.appMap.get(queryAction)) != null ? className : null;
        RequestContext requestContext = RequestContext.begin(getServletContext(), req, res);  //如果加入filter后可用filter启用
        /*System.out.println("requestContext11==="+requestContext);*/
        if (className == null) {
            Log.writeLog("配制中未找到对应的程序(" + queryAction + ")");
            requestContext.alertMsg("配制中未找到对应的程序!", "");
            return;
        }
        try {
            if ((suffix.equalsIgnoreCase("go")) || (suffix.equalsIgnoreCase("goto"))) {
                StringBuffer att = new StringBuffer();
                Map<?, ?> hm = req.getParameterMap();
                Object[] keys = hm.keySet().toArray();
                for (int iKey = 0; iKey < keys.length; iKey++) {
                    String key = keys[iKey].toString();
                    String[] values = (String[]) hm.get(key);
                    for (String value : values) { // 解决URL传参的问题
                        value = new String(value.getBytes("ISO-8859-1"), Variable.encoding); // 解决tomcat内部转码
                        value = java.net.URLEncoder.encode(value, Variable.encoding); // 解决Iframe中文乱码
                        att.append("&").append(key).append("=").append(value);
                    }
                }
                req.setAttribute("src", actionUrl + ".do?" + att);
                requestContext.gotoPage("/common/wait_" + suffix.toLowerCase() + ".jsp", false);
                return;
            }
            String methodName = null;
            try {
                methodName = action_method[1];
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }

            App appReal = new App();
            ActionInvocation actionInvocation = new ActionInvocation(className, methodName, suffix);
            /* 生成动态代理 */
            ProxyHandle proxy = new ProxyHandle();
            webFrame.app.interceptor.App app = (webFrame.app.interceptor.App) proxy.bind(appReal);
            try {
            	app.appEnter(actionInvocation); // 动态代理的参数
            } catch (Exception e) {
                e.printStackTrace();
                requestContext.alertMsg("应用程序运行错误!<br>" + AppUtils.autoException(e, ""), "");
            }finally{
              	appReal = null;
          		actionInvocation = null;
          		proxy = null;
            }
        }catch (IllegalAccessException e) {
            Log.writeLog("访问出错(" + queryAction + "-" + className + ")", e);
            requestContext.alertMsg("访问出错!<br>" + e.toString(), "");
        } catch (ClassNotFoundException e) {
            Log.writeLog("应用程序未找到(" + queryAction + "-" + className + ")", e);
            requestContext.alertMsg("应用程序未找到!<br>" + e.toString(), "");
        } catch (Exception e) {
            Log.writeLog("应用程序运行错误(" + queryAction + "-" + className + ")", e);
            requestContext.alertMsg("应用程序运行错误!<br>" + e.toString(), "");
        } finally {
           if(requestContext != null) {
        	   requestContext.end();
        	   requestContext = null;
           }
        }
    }

}
