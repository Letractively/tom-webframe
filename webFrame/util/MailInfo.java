package webFrame.util;

import java.io.File;
import java.security.Security;
import java.util.Properties;

public class MailInfo {
	/*发送邮件的服务器的IP和端口*/
	private String smtphost;
	private String port = "25";


	/*邮件接收者的地址*/
	private String receiver;

	/*是否需要身份验证*/
	private boolean validate = false;

	private boolean isSSL = false;

	/*邮件发送者的地址 和密码*/
	private String sender;
	private String password;

	/*邮件主题*/
	private String subject;

	/*邮件的文本内容*/
	private String content;

	/*附件*/
	private File[] files;

	public MailInfo() {
	}

	public MailInfo(String smtp,String sender, boolean validate, boolean isSSL) {
		this.smtphost = smtp;
		this.sender = sender;
		this.validate = validate;
		this.isSSL = isSSL;
	}

	public Properties getProperties() {
		Properties p =  System.getProperties();
		if (isSSL) {
			 Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			  final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			  // Get a Properties object
			  Properties props = System.getProperties();
			  props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			  props.setProperty("mail.smtp.socketFactory.fallback", "false");
			  props.setProperty("mail.smtp.socketFactory.port", this.port);
		}
		p.setProperty("mail.smtp.host", this.smtphost);
		p.setProperty("mail.from", this.sender);
		p.setProperty("mail.smtp.port", this.port);
		p.setProperty("mail.smtp.auth", validate ? "true" : "false");
		return p;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String textContent) {
		this.content = textContent;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return sender;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiver() {
		return receiver;
	}

	public String getSmtphost() {
		return smtphost;
	}

	public void setSmtphost(String smtphost) {
		this.smtphost = smtphost;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setFiles(File... files) {
		this.files = files;
	}

	public File[] getFiles() {
		return files;
	}
}