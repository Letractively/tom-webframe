package webFrame.app.factory;
/**
 *	工厂
 */
public class AppFactory {

	public static <T> T getInstance(Class<T> _class) throws Exception {
		return _class.newInstance();
	}
}
