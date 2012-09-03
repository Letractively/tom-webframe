package webFrame.app.interceptor;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Iterator;

import webFrame.app.listener.Variable;

/**
 * 动态代理
 * @author panmg
 */
public class ProxyHandle implements InvocationHandler {
	private Object obj;

	public Object bind(Object obj) {
		this.obj = obj;

		/*
		 * 该方法的返回值是一个对象 Proxy有一个方法newProxyInstace();
		 * 有三个参数：第一个：表示所代理的真实类类的实例的类的加载器 第二个参数表示是：所要代理的真实类所实现的接口 第三个是：代理类本身
		 */
		return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);

	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object o = null;
		if (before(args)) {
			o = method.invoke(this.obj, args);
		}
		after(args);
		return o;
	}

	private boolean before(Object[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		HashMap<String, String> interceptormap = Variable.InterceptorMap;
		Iterator<String> ite = interceptormap.keySet().iterator();
		boolean flag = true;
		while (ite.hasNext()) {
			String key = ite.next();
			String value = interceptormap.get(key);
			Interceptor insterceptor = (Interceptor) Class.forName(value).newInstance();
			if (flag) {
				flag = insterceptor.before((Serializable) args[0]);
			} else {
				break;
			}
		}
		return flag;
	}

	private boolean after(Object[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		HashMap<String, String> interceptormap = Variable.InterceptorMap;
		Iterator<String> ite = interceptormap.keySet().iterator();
		boolean flag = true;
		while (ite.hasNext()) {
			String key = ite.next();
			String value = interceptormap.get(key);
			Interceptor insterceptor = (Interceptor) Class.forName(value).newInstance();
			if (flag) {
				flag = insterceptor.after((Serializable) args[0]);
			}else {
				break;
			}
		}
		return flag;
	}



}
