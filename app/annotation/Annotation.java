package webFrame.app.annotation;

import java.lang.annotation.*;

public class Annotation {

	/**
	 * Resource动态注入
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Resource {
		public Class<?> value();
	}

	/**
	 * response返回inputStream解析
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface InputstreamOutput {
		public ContentType value();
	}

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface JSONOutput {

	}
}
