package webFrame.app.annotation;


import java.util.Arrays;

import webFrame.app.listener.Variable;

/**
 * enum 枚举就是静态单例
 * 
 */
public enum ContentType {

	Image {

		@Override
		public String getContentType() {
			return "image/*";
		}

		@Override
		public String[] getheader() {
			String header[] = { "Cache-Control", "no-cache" };
			return header;
		}
	},
	Text {

		@Override
		public String getContentType() {
			return "Text/plain;charset="+encode;
		}

		@Override
		public String[] getheader() {
			String header[] = { "Cache-Control", "no-cache" };
			return header;
		}
	},
	File {

		@Override
		public String getContentType() {
			return "APPLICATION/OCTET-STREAM";
		}

		@Override
		public String[] getheader() {
			String[] header = { "Content-Disposition", "attachment; filename=" + fileName };
			return header;
		}

	};

	public String fileName = "downFile";
	
	public String encode = Variable.encoding;

	public abstract String getContentType();

	public abstract String[] getheader();

	public static void main(String[] args) {
		for (ContentType c : ContentType.values()) {
			System.out.print(c.getContentType() + "、");
			System.out.print(Arrays.asList(c.getheader()) + "、");
		}
	}

}