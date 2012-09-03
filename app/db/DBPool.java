package webFrame.app.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import webFrame.report.Log;
import webFrame.util.Encrypt;

public final class DBPool {
	public ComboPooledDataSource cpds = null;

	public String cfgFile = "";

	public String driverClass = "";

	public String url = "";

	public String userName = "";

	public String passWord = "";

	public int minPoolSize = 0;

	public int maxPoolSize = 5;

	public String encrypt = "";

	private int flag = 0;

	public DBPool(String _file) {
		if ((_file == null) || (_file.length() == 0))
			this.cfgFile = "";
		else {
			this.cfgFile = _file;
		}
		loadConfig();
		init();
	}

	public DBPool(String _driverClass, String _url, String _userName, String _passWord, int _minPoolSize,
			int _maxPoolSize) {
		this.driverClass = _driverClass;
		this.url = _url;
		this.userName = _userName;
		this.passWord = _passWord;
		this.minPoolSize = _minPoolSize;
		this.maxPoolSize = _maxPoolSize;
		init();
	}

	public void init() {
		this.flag += 1;
		Log.writeLog("开始第" + this.flag + "次初始化数据库连接池");
		try {
			this.cpds = new ComboPooledDataSource();
			this.cpds.setDriverClass(this.driverClass);
			this.cpds.setJdbcUrl(this.url);
			this.cpds.setUser(this.userName);
			this.cpds.setPassword(this.passWord);
            /*最小保持连接数*/
			this.cpds.setMinPoolSize(this.minPoolSize);
            /*最大连接数*/
			this.cpds.setMaxPoolSize(this.maxPoolSize);
            /*连接超时等待时间*/
			this.cpds.setCheckoutTimeout(5000);
            /*连接失败重连次数
            this.cpds.setAcquireRetryAttempts(30);*/
            /*两次连接中间隔时间
			this.cpds.setAcquireRetryDelay(10000);*/
            /*提交是检测连接的有效性
			this.cpds.setTestConnectionOnCheckout(true);*/
            /*获取连接时检测有效性*/
			this.cpds.setTestConnectionOnCheckin(true);
            /*超时连接丢弃*/
			this.cpds.setMaxIdleTime(600000);
			this.cpds.setMaxIdleTimeExcessConnections(5);
            /*测试空闲连接时间(60000秒)*/
			this.cpds.setIdleConnectionTestPeriod(60000);
            /*通过多线程实现多个操作同时被执行*/
			this.cpds.setNumHelperThreads(3);
			Log.writeLog("第" + this.flag + "次初始化数据库连接池成功");
		} catch (Exception e) {
			Log.writeLog("第" + this.flag + "次初始化数据库连接池失败!" + e.toString());
		}
	}

	private void loadConfig() {
		Properties prop = new Properties();
		File f = new File(this.cfgFile);
		if (f.exists()) {
			Log.writeLog("载入数据配置文件: " + f.getAbsoluteFile());
			try {
				prop.load(new FileInputStream(f));
				String temp = prop.getProperty("DriverClass");
				if ((temp != null) && (temp.length() > 0)) {
					this.driverClass = temp.trim();
				}
				temp = prop.getProperty("URL");
				if ((temp != null) && (temp.length() > 0)) {
					this.url = temp.trim();
				}
				temp = prop.getProperty("UserName");
				if ((temp != null) && (temp.length() > 0)) {
					this.userName = temp.trim();
				}
				temp = prop.getProperty("PassWord");
				if ((temp != null) && (temp.length() > 0)) {
					this.passWord = temp.trim();
				}
				temp = prop.getProperty("MinPoolSize");
				if ((temp != null) && (temp.length() > 0)) {
					this.minPoolSize = Integer.parseInt(temp.trim());
				}
				temp = prop.getProperty("MaxPoolSize");
				if ((temp != null) && (temp.length() > 0)) {
					this.maxPoolSize = Integer.parseInt(temp.trim());
				}
				temp = prop.getProperty("Encrypt");
				if ((temp != null) && (temp.length() > 0)) {
					this.encrypt = temp.trim();
					if (this.encrypt.length() > 0) {
						this.userName = Encrypt.decryptString(2, this.userName);
						this.passWord = Encrypt.decryptString(2, this.passWord);
					}
				}
			} catch (Exception e) {
				Log.writeLog("加载数据库配制失败" + e.toString());
			}
		} else {
			Log.writeLog("数据库配置文件未找到:" + f.getAbsoluteFile());
		}
	}
}
