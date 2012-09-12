package webFrame.app.control;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webFrame.app.reflect.RefExtend;
import webFrame.report.Log;

public abstract class AppControl<T> implements AppModel<T> {

	public AppControl() {
	}

	/*控制器前后的方法*/
	public boolean beforeControl(RequestContext resContext, Map<String, String> o,String methodName) {
		return true;
	}

    public void afterControl(RequestContext resContext, Map<String, String> o,String methodName) {
        
    }
    
    /*action执行的方法*/
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
	
	
	public RefExtend getEx(){
		return new RefExtend() {
			
			@Override
			public Object parseAnnnotation(Object obj, Method me, Map<String, String> map) {
				return obj;
			}
		};
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
