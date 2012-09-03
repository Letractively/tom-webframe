package webFrame.app.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javassist.ClassClassPath;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import webFrame.report.Log;

public final class BeanGet {
	public static final String BEAN = "target";

	public static <T> CtClass getBeanGetClass(Class<T> _bean, String _className) {
		try {
			Class<BeanGetIf> beanIf = BeanGetIf.class;
			String beanName = _bean.getName();

			ClassPool cpool = ClassPool.getDefault();
			try {
				CtClass beanCla = cpool.get(_className);
				return beanCla;
			} catch (Exception e) {
				cpool.importPackage("java.util");
				cpool.importPackage("java.text");
				cpool.importPackage("webFrame.app.db");

				ClassPath cpath = new ClassClassPath(beanIf);
				try {
					cpool.removeClassPath(cpath);
				} catch (Exception localException2) {
				}
				cpool.appendClassPath(cpath);

				cpath = new ClassClassPath(_bean);
				cpool.appendClassPath(cpath);

				CtClass beanCla = cpool.makeClass(_className);

				beanCla.addInterface(cpool.get(beanIf.getName()));
				CtClass objCla = cpool.get(beanName);

				CtField cfield = new CtField(objCla, "target", beanCla);
				beanCla.addField(cfield);

				CtMethod cmeth = new CtMethod(CtClass.voidType, "setTarget", new CtClass[] { cpool.get("java.lang.Object") }, beanCla);
				cmeth.setBody("target = (" + _bean.getName() + ") $1;"); // $1有用,还未弄明白
				beanCla.addMethod(cmeth);

				Field[] fields = _bean.getDeclaredFields();

				List<Object> fList = new ArrayList<Object>();
				for (int i = 0; i < fields.length; i++) {
					String name = fields[i].getName();
					if (!name.substring(0, 2).equalsIgnoreCase("f_")) {
						continue;
					}
					Object[] col = new Object[2];
					col[0] = name;
					col[1] = fields[i].getType();
					fList.add(col);
				}

				CtMethod cmeth_update = new CtMethod(beanCla.getClassPool().get("java.lang.String"), "getUpdateString", null, beanCla);

				CtMethod cmeth_insert = new CtMethod(beanCla.getClassPool().get("java.lang.String"), "getInsertString", null, beanCla);

				StringBuffer sBuf_update = new StringBuffer("");
				StringBuffer sBuf_insert = new StringBuffer("");
				String sUpdate = "";
				String sField = "";
				String sValue = "";

				sBuf_update.append("String update = \"\";\r\n");
				sBuf_insert.append("String insert = \"\";\r\n");

				for (int i = 0; i < fList.size(); i++) {
					Object[] col = (Object[]) fList.get(i);
					Class<?> type = (Class<?>) col[1];
					String fieldName = col[0].toString().substring(2, col[0].toString().length());
					String[] typeName = new String[2];
					typeName[0] = col[0].toString().substring(0, 2).toUpperCase();
					typeName[1] = fieldName;

					if (sUpdate.length() > 0) {
						sUpdate = sUpdate + " + \",\" + ";
					}
					if (sField.length() > 0) {
						sField = sField + ",";
						sValue = sValue + " + \",\" + ";
					}
					sField = sField + fieldName;
					if (type == String.class) {
						sUpdate = sUpdate + "\"" + fieldName + " = '\" + " + "target" + ".get" + typeName[0] + typeName[1] + "().replaceAll(\"'\", \"''\") + \"'\"";
						sValue = sValue + "\"'\" + target.get" + typeName[0] + typeName[1] + "().replaceAll(\"'\", \"''\") + \"'\"";
					} else if (type == Date.class) {
						sUpdate = sUpdate + "\"" + fieldName + " = '\" + new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").format(" + "target" + ".get" + typeName[0] + typeName[1] + "()) + \"'\"";
						sValue = sValue + "\"'\" + new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").format(target.get" + typeName[0] + typeName[1] + "()) + \"'\"";
					} else {
						sUpdate = sUpdate + "\"" + fieldName + " = \" + " + "target" + ".get" + typeName[0] + typeName[1] + "()";
						sValue = sValue + "target.get" + typeName[0] + typeName[1] + "()";
					}

				}

				if (sUpdate.length() > 0) {
					sBuf_update.append("update = " + sUpdate + ";\r\n");
				}
				sBuf_update.append("return update;\r\n");

				cmeth_update.setBody("{" + sBuf_update.toString() + "}");
				beanCla.addMethod(cmeth_update);

				if (sField.length() > 0) {
					sBuf_insert.append("insert = \"(" + sField + ") values (\" + " + sValue + " + \")\";\r\n");
				}
				sBuf_insert.append("return insert;\r\n");

				cmeth_insert.setBody("{" + sBuf_insert.toString() + "}");
				beanCla.addMethod(cmeth_insert);

				return beanCla;
			}
		} catch (Exception e) {
			Log.writeLog("BeanSet.getBeanGetClass", e);
		}
		return null;
	}
}
