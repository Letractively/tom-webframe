package webFrame.app.listener;

import java.io.*;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletContext;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import webFrame.app.db.DBPool;
import webFrame.report.Log;

public class AppInit {
	public String configDir = "";

	public String logDir = "";

	public void init(ServletContext arg0) {
		Variable.path = arg0.getRealPath("");
		this.configDir = arg0.getRealPath("\\WEB-INF\\config\\");
		this.logDir = arg0.getRealPath("\\WEB-INF\\log\\");

		Log.initLog(this.logDir); // 初始化日志,一般不需要扩展

		initSystem(); // 初始化系统第一层信息,一般需要扩展,扩展内容通过execcute(arg)实现

		initConfig(); // 读取action配置文件,使用dom4j,保存在静态map中

		initDbConn(); // 读取数据库配置文件,多个数据池通过execute(arg) 实现

		execute(arg0); // 扩展继承方式,所有需要扩展的东西都可以在里面
	}

	private void initConfig() {
		try {
			List<?> configFiles = null;
			File f = new File(this.configDir + "\\Action.cfg.xml");
			Document doc = new SAXReader().read(f); //
			Element root = doc.getRootElement();
			Log.writeLog("载入应用配置文件: "+f.getAbsolutePath());
			/*加载action配置文件*/
			configFiles = root.selectNodes("Config/Actions");
			for (int i = 0; i < configFiles.size(); i++) {
				Element file = (Element) configFiles.get(i);
				File f2 = new File(this.configDir + "\\" + file.getTextTrim());
				Document doc2 = new SAXReader().read(new InputStreamReader(new FileInputStream(f2), "UTF-8"));
				Element root2 = doc2.getRootElement();
				List<?> list2 = root2.selectNodes("AppMap/item");
				for (int j = 0; j < list2.size(); j++) {
					Element temp = (Element) list2.get(j);
					Variable.appMap.put(temp.attributeValue("queryAction"), temp.attributeValue("class"));
					Log.writeLog(temp.attributeValue("queryAction") + "--->>" + temp.attributeValue("class") + "[" + temp.getTextTrim() + "]");
				}
			}
			/*加载Interceptor配置文件*/
			configFiles = root.selectNodes("Config/Interceptors");
			for (int i = 0; i < configFiles.size(); i++) {
				Element file = (Element) configFiles.get(i);
				File f2 = new File(this.configDir + "\\" + file.getTextTrim());
				Document doc2 = new SAXReader().read(new InputStreamReader(new FileInputStream(f2), "UTF-8")); 
				Element root2 = doc2.getRootElement();
				List<?> list2 = root2.selectNodes("AppMap/item");
				for (int j = 0; j < list2.size(); j++) {
					Element temp = (Element) list2.get(j);
					Variable.InterceptorMap.put(temp.attributeValue("interceptorName"), temp.attributeValue("class"));
					Log.writeLog(temp.attributeValue("interceptorName") + "--->>" + temp.attributeValue("class") + "[" + temp.getTextTrim() + "]");
				}
			}
		} catch (Exception e) {
			Log.writeLog("载入应用配置文件失败", e);
		}
	}

	private void initDbConn() {
		Variable.dbpool = new DBPool(this.configDir + "\\DBConfig.properties");
	}

	private void initSystem() {
		Properties prop = new Properties();
		File f = new File(this.configDir + "\\System.properties");
        Log.writeLog("载入系统配置文件: "+f.getAbsolutePath());

		if (f.exists()) {
			try {
				prop.load(new FileInputStream(f));

				String temp = prop.getProperty("ExceptionClassPrefix");
				if ((temp != null) && (temp.length() > 0)) {
					Variable.exceptionClassPrefix = temp.trim().split(";");
				}

				temp = prop.getProperty("ExceptionClassPrefixNo");
				if ((temp != null) && (temp.length() > 0)) {
					Variable.exceptionClassPrefixNo = temp.trim().split(";");
				}

                temp = prop.getProperty("Encoding");
				if ((temp != null) && (temp.length() > 0)) {
					Variable.encoding = temp.trim();
				}
                
				temp = prop.getProperty("LogFileMaxSize");
				if ((temp != null) && (temp.length() > 0)) {
					Variable.logFileMaxSize = Double.parseDouble(temp.trim());
				}

				temp = prop.getProperty("MaxPageRowCount");
				if ((temp != null) && (temp.length() > 0)) {
					int itemp = Integer.parseInt(temp.trim());
					Variable.maxPageRowCount = itemp;
				}

				temp = prop.getProperty("LogLevel");
				if ((temp != null) && (temp.length() > 0)) {
					Variable.logLevel = temp;
				}

				temp = prop.getProperty("PageContext");
				if ((temp != null) && (temp.length() > 0)) {
					Variable.pageContext = temp;
				}

				temp = prop.getProperty("PageSuffix");
				if ((temp != null) && (temp.length() > 0)) {
					Variable.pageSuffix = temp;
				}
			} catch (Exception e) {
				Log.writeLog("载入系统配置失败", e);
			}
		}
		f = null;
	}

	protected void execute(ServletContext arg0) {
	}
}
