package webFrame.app.listener;

import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import webFrame.app.db.DBPool;
import webFrame.app.db.Record;

public class Variable {
	
	public final static HashMap<String, String> appMap = new HashMap<String, String> (); //action 对象
	
	public final static LinkedHashMap<String, String> InterceptorMap = new LinkedHashMap<String, String> (); //拦截器对象
	
	public static final String GOBACK = "history.go(-1)";
	
	public static final String GOBLANK = "location='about:blank'";
	
    public static String encoding = "UTF-8";
	
	public static String[] exceptionClassPrefix = new String[0];

	public static String[] exceptionClassPrefixNo = new String[0];
	
	public static String path = "";

	public static DBPool dbpool = null;

	public static HashMap<Thread, Record<String, Connection>> threadMap = new HashMap<Thread, Record<String, Connection>>();

	public static int maxPageRowCount = 5000;

	public static double logFileMaxSize = 100.0D;

	public static String logLevel = "DEBUG";
	
	public static String pageContext = "/WEB-INF/page";
	
	public static String pageSuffix = ".jsp";
}
