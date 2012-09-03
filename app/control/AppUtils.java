package webFrame.app.control;

import webFrame.app.listener.Variable;
import webFrame.report.Log;

public class AppUtils {
	public static String autoException(Exception _e, String _className) throws Exception {
		StackTraceElement frame = null;
		StackTraceElement[] ste = _e.getStackTrace();
		for (int i = 0; i < ste.length; i++) {
			boolean show = false;

			if ((_className != null) && (_className.length() > 0)) {
				if (ste[i].getClassName().equals(_className)) {
					show = true;
					break;
				}
			} else {
				for (int j = 0; j < Variable.exceptionClassPrefix.length; j++) {
					if (ste[i].getClassName().startsWith(Variable.exceptionClassPrefix[j])) {
						show = true;
						break;
					}

				}

				if (show) {
					for (int j = 0; j < Variable.exceptionClassPrefixNo.length; j++) {
						if (ste[i].getClassName().startsWith(Variable.exceptionClassPrefixNo[j])) {
							show = false;
							break;
						}
					}
				}
			}

			if (show) {
				frame = ste[i];
				break;
			}
		}

		if (frame == null) {
			if (_e.getCause() != null) {  //先判断上级原因是否存在,如果不存在直接跳过
				StackTraceElement[] ste_cau = _e.getCause().getStackTrace();
				for (int i = 0; i < ste_cau.length; i++) {
					boolean show = false;

					if ((_className != null) && (_className.length() > 0)) {
						if (ste_cau[i].getClassName().equals(_className)) {
							show = true;
							break;
						}
					} else {
						for (int j = 0; j < Variable.exceptionClassPrefix.length; j++) {
							if (ste_cau[i].getClassName().startsWith(Variable.exceptionClassPrefix[j])) {
								show = true;
								break;
							}
						}

						if (show) {
							for (int j = 0; j < Variable.exceptionClassPrefixNo.length; j++) {
								if (ste_cau[i].getClassName().startsWith(Variable.exceptionClassPrefixNo[j])) {
									show = false;
									break;
								}
							}
						}

					}

					if (show) {
						frame = ste_cau[i];
						break;
					}
				}
			}
		}

		if (frame == null) {
			frame = ste[0];
		}

		Log.writeLog(frame.toString(), _e);

		return frame.toString() + ": " + _e.toString();
	}
}
