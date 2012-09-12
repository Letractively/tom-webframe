package webFrame.app.reflect;

import java.lang.reflect.Method;
import java.util.Map;


public interface RefExtend {
	
	public Object parseAnnnotation(Object obj, Method me, Map<String,String> map);

}
