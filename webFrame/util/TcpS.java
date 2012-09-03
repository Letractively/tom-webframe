package webFrame.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpS {
	private ServerSocket stcp = null;

	public TcpS(int _port) {
		try {
			this.stcp = new ServerSocket(_port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public boolean isOpened() {
		try {
			return !this.stcp.isClosed();
		} catch (Exception e) {
		}
		return false;
	}

	public void close() {
		try {
			this.stcp.close();
		} catch (Exception localException) {
		} finally {
			this.stcp = null;
		}
	}

	public Socket getClient() {
		Socket ctcp = null;
		try {
			ctcp = this.stcp.accept();
		} catch (Exception e) {
		}
		return ctcp;
	}


}
