package webFrame.app.control;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webFrame.report.Log;

public abstract class AppControl<T> implements AppModel<T> {

	public AppControl() {
	}

	public boolean beforeControl(RequestContext resContext, Map<String, String> o,String methodName) {
		return true;
	}

	public String exec() throws Exception {
		return null;
	}

	public String exec(RequestContext resContext) throws Exception {
		return null;
	}

	public abstract String exec(RequestContext resContext, T t) throws Exception;

	public String exec(HttpServletRequest req, HttpServletResponse res, T t) throws Exception {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getModel(Map<?, ?> map) {
		T t;
		try {
            /*简单来讲就是活得父类泛型的类型,即AppModel<T> T继承的类型*/
			Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			t = RequestContext.normalized(map, entityClass);
		} catch (Exception e) {
			t = (T) map;
			Log.writeLog("App.getModel:  No generic named,Default to Map ");
		}
		return t;
	}

}
