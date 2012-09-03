package webFrame.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;

public class FtpC {
	FtpClient client = null;
	String ip = "";
	int port = 21;
	String user = "";
	String password = "";
	int timeout = 30000;

	public FtpC() {
	}

	public FtpC(String _ip, int _port, String _user, String _password) {
		connect(_ip, _port, _user, _password);
	}

	public FtpC(String _ip, int _port, String _user, String _password, int _timeout) {
		connect(_ip, _port, _user, _password, _timeout);
	}

	public void connect() {
		try {
			this.client = new FtpClient();
			this.client.setConnectTimeout(this.timeout);
			this.client.setReadTimeout(this.timeout);
			this.client.openServer(this.ip, this.port);
			this.client.login(this.user, this.password);
			this.client.binary();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void connect(String _ip, int _port, String _user, String _password) {
		connect(_ip, _port, _user, _password, this.timeout);
	}

	public void connect(String _ip, int _port, String _user, String _password, int _timeout) {
		this.ip = _ip;
		this.port = _port;
		this.user = _user;
		this.password = _password;
		this.timeout = _timeout;
		connect();
	}

	public void close() {
		try {
			this.client.closeServer();
		} catch (Exception localException) {
		} finally {
			this.client = null;
		}
	}

	public boolean isConnected() {
		try {
			return this.client.serverIsOpen();
		} catch (Exception e) {
		}
		return false;
	}

	public FtpClient getFtpC() {
		return this.client;
	}

	public boolean downFile(String _file, String _localFile) {
		try {
			int i = _localFile.lastIndexOf(File.separator);
			if (i >= 0) {
				String dir = _localFile.substring(0, i);

				File f = new File(dir);
				f.mkdirs();
				f = null;
			}

			TelnetInputStream tis = this.client.get(_file);
			FileOutputStream fos = new FileOutputStream(_localFile, false);
			byte[] b = new byte[1024];
			while ((i = tis.read(b)) > 0) {
				fos.write(b, 0, i);
			}
			fos.close();
			tis.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean upFile(String _file, String _localFile) {
		try {
			FileInputStream fis = new FileInputStream(_localFile);
			TelnetOutputStream tos = this.client.put(_file);
			byte[] b = new byte[1024];
			int i =-1;
			while ((i = fis.read(b)) != -1) {
				tos.write(b, 0, i);
			}
			fis.close();
			tos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean cd(String _dir) {
		try {
			this.client.cd(_dir);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public boolean sendServer(String _str) {
		try {
			this.client.sendServer(_str + "\r\n");
			this.client.readServerResponse();
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public boolean createDir(String _dir) {
		return sendServer("XMKD " + _dir);
	}

	public boolean deleteDir(String _dir) {
		return sendServer("XRMD " + _dir);
	}

	public boolean deleteFile(String _file) {
		return sendServer("DELE " + _file);
	}

	public List<String> getFileList(String _dir) throws Exception {
		List<String> list = new ArrayList<String>();
		TelnetInputStream tis = this.client.nameList(_dir);
		byte[] b = new byte[1024];
		int i = 0;
		String s = "";
		while ((i = tis.read(b)) > 0) {
			s = s + new String(b, 0, i);
		}
		tis.close();

		String[] name = s.split("\n", -1);
		for (i = 0; i < name.length; i++) {
			list.add(name[i]);
		}
		return list;
	}
}
