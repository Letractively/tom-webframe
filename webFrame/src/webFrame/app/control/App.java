package webFrame.app.control;

import java.lang.reflect.Method;
import java.util.Map;

import webFrame.app.interceptor.ActionInvocation;
import webFrame.app.listener.Variable;
import webFrame.app.reflect.Reflect;
import webFrame.app.db.DBUtils;
import webFrame.report.Log;

public class App implements webFrame.app.interceptor.App {

	@Override
	public void appEnter(ActionInvocation actionInvocation) throws Exception {
        long a = System.currentTimeMillis();
        String className = actionInvocation.getClassName();
        String methodName = actionInvocation.getMethodName();
        String suffix = actionInvocation.getSuffix();
        RequestContext requestContext = actionInvocation.getRequestContext();
		if (suffix == null) {
			return;
		}

		try {
            
			Map<String, String> map =  actionInvocation.getParaMap();
			String page = null;
			Reflect reflect = new Reflect(methodName);
			if (suffix.equalsIgnoreCase("do")) {
				AppControl<?> control = actionInvocation.loadAction();
				reflect.paserFieldAnotation(control); //对属性annotation进行解析
				Method me = reflect.loadMethod(control.getClass()); //得到所有的方法
				page = (String) reflect.getRuturnType(me, control, map); //得到方法的返回值

				if (page != null && !page.isEmpty()) {
                    requestContext.setAttribute("params", map);
					requestContext.gotoPage(page, true);
				}
			} else {
				throw new Exception("App.appEnter: error action suffix,only support .do or .go or .goto");
			}
		} catch (Exception e) {
			throw e;
		} finally {
            /* 每次与servlet通信系统都自动记录日志 */
			StringBuffer msg = new StringBuffer("["+(System.currentTimeMillis()-a)).append("ms]")
            .append("WEBFRAME(").append(className).append(".")
			.append(methodName == null ? "exec" : methodName).append(")->IP:")
			.append(requestContext.getRequest().getRemoteHost());
			Log.writeLog(msg.toString());
            
			try {
				DBUtils.closeConnection(DBUtils.getConnection());
                Variable.threadMap.get(Thread.currentThread()).recMap.clear();
				Variable.threadMap.remove(Thread.currentThread());
			} catch (Exception e) {}

		}
	}
}
