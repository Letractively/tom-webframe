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

public final class BeanSet {
	public static final String BEAN = "target";

	public static <T> CtClass getBeanSetClass(Class<T> _bean, String _className) {
		try {
			Class<BeanSetIf> beanIf = BeanSetIf.class;
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

				CtMethod cmeth = new CtMethod(beanCla.getClassPool().get("java.lang.Object"), "getTarget", null, beanCla);
				cmeth.setBody("return target;");
				beanCla.addMethod(cmeth);

				cmeth = new CtMethod(CtClass.voidType, "setTarget", new CtClass[] { cpool.get("java.lang.Object") }, beanCla);
				cmeth.setBody("target = (" + _bean.getName() + ") $1;");
				beanCla.addMethod(cmeth);

				StringBuffer sBuf = new StringBuffer("");

				cmeth = new CtMethod(CtClass.voidType, "setValue", new CtClass[] { cpool.get("java.lang.Object") }, beanCla);

				sBuf.append("Record rec = (Record) $1;\r\n");
				sBuf.append("String[] key = rec.getColumn();\r\n");
				sBuf.append("for (int i=0; i<key.length; i++) {\r\n");
				sBuf.append("String field = key[i];\r\n");

				Field[] fields = _bean.getDeclaredFields();
				Field[] fields_super =  _bean.getSuperclass().getDeclaredFields();
                Field[] fields_all =  new Field[fields.length+fields_super.length];
                
                System.arraycopy(fields,0,fields_all,0,fields.length);
                System.arraycopy(fields_super,0,fields_all,fields.length,fields_super.length);
                

				List<Object> fList = new ArrayList<Object>();
				for (int i = 0; i < fields_all.length; i++) {
					String name = fields_all[i].getName();
					if (!name.startsWith("f_") && !name.startsWith("q_")) {
						continue;
					}
					Object[] col = new Object[2];
					col[0] = name;
					col[1] = fields_all[i].getType();
					fList.add(col);
				}

				for (int j = 0; j < fList.size(); j++) {
					Object[] col = (Object[]) fList.get(j);
					Class<?> type = (Class<?>) col[1];
					String[] typeName = new String[2];
					typeName[0] = col[0].toString().substring(0, 2).toUpperCase();
					typeName[1] = col[0].toString().substring(2, col[0].toString().length());

					sBuf.append("if (field.equals(\"" + typeName[1] + "\")) {\r\n");
					if (type == Integer.TYPE)
						sBuf.append("target.set" + typeName[0] + typeName[1] + "(rec.getInt(field));\r\n");
					else if (type == Double.TYPE)
						sBuf.append("target.set" + typeName[0] + typeName[1] + "(rec.getDouble(field));\r\n");
					else if (type == Date.class)
						sBuf.append("target.set" + typeName[0] + typeName[1] + "(rec.getDate(field));\r\n");
					else {
						sBuf.append("target.set" + typeName[0] + typeName[1] + "(rec.get(field));\r\n");
					}
					sBuf.append("continue;\r\n");
					sBuf.append("}\r\n");
				}

				sBuf.append("}\r\n");

				cmeth.setBody("{" + sBuf.toString() + "}");
				beanCla.addMethod(cmeth);

				return beanCla;
			}
		} catch (Exception e) {
			Log.writeLog("BeanSet.getBeanSetClass", e);
		}
		return null;
	}
}
