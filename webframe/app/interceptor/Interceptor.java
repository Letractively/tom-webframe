package webFrame.app.interceptor;

import java.io.Serializable;


public interface Interceptor {
	public abstract boolean before (Serializable actionInvocation);
	public abstract boolean after (Serializable actionInvocation);
	
}
