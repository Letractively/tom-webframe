package webFrame.app.reflect;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URLEncoder;
import java.util.Map;

import webFrame.app.annotation.ContentType;
import webFrame.app.annotation.Annotation.*;
import webFrame.app.control.AppControl;
import webFrame.app.control.RequestContext;
import webFrame.app.factory.AppFactory;
import webFrame.report.Log;
import webFrame.util.*;

/**
 * 反射解析action,method,field,annotation
 */
public class Reflect {
    private String className = null;
    private String methodName = null;
    private RequestContext requestContext = RequestContext.get();

    public Reflect(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }


    /*action通用处理,是继承,非拦截*/
    public boolean beforeControl(AppControl<?> control, Map<?, ?> map) throws Exception {
        Method m = control.getClass().getMethod("beforeControl", RequestContext.class, Map.class, String.class);
        return (Boolean) m.invoke(control, requestContext, map, methodName == null ? "exec" : methodName);
    }
    
      /*action通用处理,是继承,非拦截*/
    public void afterControl(AppControl<?> control, Map<?, ?> map) throws Exception {
        Method m = control.getClass().getMethod("afterControl", RequestContext.class, Map.class, String.class);
        m.invoke(control, requestContext, map, methodName == null ? "exec" : methodName);
    }

    /* 得到指定的方法 */
    public Method loadMethod(Class<?> c) throws Exception {
        Method me = null;
        Method m[] = c.getMethods();
        if (methodName == null) {
            me = c.getMethod("exec", RequestContext.class, Object.class); // 这个方法不一定需要有
        }

        for (int i = 0; i < m.length; i++) {
            /* 如果是公共方法,而且名称相同则返回,如果名称相同参数不同,按先后顺序只执行第一个方法 */// 是否可以判断参数个数相同的执行
            if (m[i].getModifiers() == Modifier.PUBLIC && m[i].getName().equals(methodName)) {
                me = m[i];
                break;
            }
        }
        if (me == null) {
            throw new NoSuchMethodException("Reflect.loadMethod: Didn't find this method--> " + methodName);
        }
        return me;
    }

    /* 得到所有的属性 */
    private Field[] loadField(Class<?> c) {
        return c.getFields();
    }

    /*
      * 对属性进行解析
      */
    public void paserFieldAnotation(Object obj) throws Exception {
        Field[] fields = loadField(obj.getClass());
        for (Field field : fields) {
            Annotation[] anos = loadAnnotation(field);
            if (anos.length > 0) {
                if (field.isAnnotationPresent(Resource.class)) {
                    Resource resource = field.getAnnotation(Resource.class);
                    /* 设置属性的值 参数1-设置的实例化对象,参数2-设置的值 */
                    field.set(obj, AppFactory.getInstance(resource.value()));
                }
            }
        }
    }

    /* 得到指定的annotation数组 */
    private <T> Annotation[] loadAnnotation(T obj) {
        return ((AnnotatedElement) obj).getDeclaredAnnotations();
    }

    public Object getRuturnType(Method me, AppControl<?> control, Map<?, ?> map) throws Exception {
        Object obj = null;
        if (beforeControl(control, map)) { //预先处理
            Annotation[] anos = loadAnnotation(me);
            if (anos.length == 0) {
                obj = invoke(me, control, map);
            } else {
                obj = paserMethodAnnotation(me, control, map);
            }
        }
        afterControl(control, map);
        return obj;

    }

    /**
     * 对方法annotation进行解析
     *
     * @param me
     * @param control
     * @param map
     * @return
     * @throws Exception
     */
    private Object paserMethodAnnotation(Method me, AppControl<?> control, Map<?, ?> map) throws Exception {
        Object obj = invoke(me, control, map);
        if (me.isAnnotationPresent(JSONOutput.class)) {
        	requestContext.printJSONData(obj);
            obj = null;
        } else if (me.isAnnotationPresent(InputstreamOutput.class)) {
            if (obj instanceof InputStream) {
                InputstreamOutput stream_ano = me.getAnnotation(InputstreamOutput.class);
                if (stream_ano.value() == ContentType.Image) { // 如果是读取图片就直接返回
                    FileUtils.readImage(requestContext, (InputStream) obj);
                } else if (stream_ano.value() == ContentType.File) { // 如果返回文件下载
                    String fileName = stream_ano.value().fileName;
                    String encoding = stream_ano.value().encode;
                    stream_ano.value().fileName = URLEncoder.encode(fileName, encoding);
                    FileUtils.DownLoadComm(requestContext, (InputStream) obj, stream_ano.value().getContentType(), stream_ano.value().getheader());
                } else if (stream_ano.value() == ContentType.Text) { // 如果返回输出文字
                    FileUtils.DownLoadComm(requestContext, (InputStream) obj, stream_ano.value().getContentType(), stream_ano.value().getheader());
                }

                obj = null;
            } else {
                throw new Exception("Reflect.paserMethodAnnotation: error return type,no inputStream found");
            }
        } else {
        	obj = control.getEx().parseAnnnotation(obj, me);
        }

        return obj;

    }

    /**
     * 返回方法执行后的返回值
     *
     * @param me
     * @param control
     * @param map
     * @return
     */
    private Object invoke(Method me, AppControl<?> control, Map<?, ?> map) throws Exception {
        Type[] type = me.getGenericParameterTypes();
        int arg_l = type.length;
        Object obj = null;
        switch (arg_l) {
            case 0:
                obj = me.invoke(control);
                break;
            case 1:
                obj = me.invoke(control, requestContext);
                break;
            case 2:
                Object t = control.getModel(map);
                obj = me.invoke(control, requestContext, t);
                break;
            case 3:
                Object t1 = control.getModel(map);
                obj = me.invoke(control, requestContext.getRequest(), requestContext.getResponse(), t1);
                break;
            default:
                throw new NoSuchMethodException("error mothod typeParameters ,Refer to AppControl.class");
        }
        return obj;
    }

}
