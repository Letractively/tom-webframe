package webFrame.app.control;

import java.util.Map;


public interface AppModel<T> {
	 public abstract T getModel(Map<?,?> map);
}
