package webFrame.app.interceptor;

import java.io.Serializable;

public abstract class AbstractInterceptor implements Interceptor {

	@Override
	public boolean after(Serializable actionInvocation) {
		return true;
	}
	
	@Override
	public abstract boolean before(Serializable actionInvocation);

}
