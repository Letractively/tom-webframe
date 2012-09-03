package webFrame.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import webFrame.app.listener.Variable;

public final class Log {
	private static FileWriter fw = null;
	private static FileWriter fw_sql = null;

	private static String logFile = "";
	private static String logFile_sql = "";

	public static void initLog(String _parth) {
		_parth = _parth == null ? "" : _parth;
		_parth = _parth.length() > 0 ? _parth + "\\" : "";

		logFile = _parth + "log.txt";
		logFile_sql = _parth + "log_sql.txt";
		try {
            File f = new File(logFile);
			fw = new FileWriter(f, true);
			printLog( "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "]日志初始化成功: "+f.getAbsolutePath());
		} catch (IOException e) {
			printLog("初始化日志失败:" + e.getMessage());
		}
		try {
			fw_sql = new FileWriter(logFile_sql, true);
		} catch (IOException e) {
			printLog("SQL日志初始化失败:" + e.getMessage());
		}
	}

	public static void writeLog(String _msg) {
		synchronized (fw) {
			File f = null;
			try {
				f = new File(logFile);
				if ((f.isFile()) && (f.exists())) {
					if (f.length() * 1.0D / 1024.0D / 1024.0D >= Variable.logFileMaxSize){
						fw.close();
						/*超过大小后重命名,老文件重新开始写*/
						f.renameTo(new File(logFile + "-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
						fw = new FileWriter(logFile, false);
					}
				}
			} catch (Exception e) {
				printLog("读取日志文件失败:" + e.toString());
			} finally {
				f = null;
			}

			_msg = _msg == null ? "" : _msg.trim();
			_msg = "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "]" + _msg;
			if (Variable.logLevel.equals("DEBUG")) {
				printLog(_msg);
			}
			try {
				fw.write(_msg + "\r\n");
				fw.flush();
			} catch (Exception e) {
				printLog("记录日志文件失败:" + e.toString());
			}
		}
	}

	public static void writeLog(String _funcName, Exception _e) {
		String msg = _funcName + ": " + _e.toString();
		writeLog(msg);
	}

	public static void printLog(String _msg) {
		System.out.println(_msg);
	}

	public static void writeLog_SQL(String _msg) {
		synchronized (fw_sql) {
			File f;
			try {
				f = new File(logFile_sql);
				if ((f.isFile()) && (f.exists())) {
					if (f.length() * 1.0D / 1024.0D / 1024.0D >= Variable.logFileMaxSize){
						fw_sql.close();
						fw_sql = new FileWriter(logFile_sql, false); //超过大小后直接覆盖
					}
				}
			} catch (Exception e) {
				printLog("读取SQL日志文件失败:" + e.getMessage());
			} finally {
				f = null;
			}

			_msg = _msg == null ? "" : _msg.trim();
			_msg = "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "]" + _msg;
			if (Variable.logLevel.equals("DEBUG")) {
				printLog(_msg);
			}
			try {
				fw_sql.write(_msg + "\r\n");
				fw_sql.flush();
			} catch (Exception e) {
				printLog("记录SQL日志文件失败:" + e.getMessage());
			}
		}
	}
}
