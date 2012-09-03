package webFrame.util;

import java.net.Socket;

public class TcpC {
	Socket client = null;
	String ip = "";
	int port = 0;
	int timeout = 10000;  //默认值

	public TcpC(Socket client) { //用于服务器端的new TcpS(8000).getClient()
		this.client = client;
	}

	public TcpC(String _ip, int _port) {
		connect(_ip, _port);
	}

	public boolean setTimeout(int _timeout) {
		try {
			this.timeout = _timeout;
			this.client.setSoTimeout(this.timeout);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Socket getTcpC() {
		return this.client;
	}

	public boolean isConnected() {
		try {
			return this.client.isConnected();
		} catch (Exception e) {
		}
		return false;
	}

	public void connect(String _ip, int _port) {
		this.ip = _ip;
		this.port = _port;
		connect();
	}

	public boolean connect() {
		try {
			this.client = new Socket(this.ip, this.port);
			this.client.setSoTimeout(this.timeout); //设置超时时间,如果在时间范围内没有收到回应,将抛错
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void close() {
		try {
			this.client.close();
		} catch (Exception localException) {
		} finally {
			this.client = null;
		}
	}

	public boolean send(String _send) {
		try {
			byte[] b = new byte[_send.length()];
			System.out.println("msg byte.length->" + b.length);
			b = _send.getBytes();
			this.client.getOutputStream().write(b);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String recv(int _length) {
		String s = "";
		try {
			byte[] b = new byte[_length];
			int i = this.client.getInputStream().read(b, 0, _length);
			if (i != -1) {
				s = new String(b, 0, i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	
}
