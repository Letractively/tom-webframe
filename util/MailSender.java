package webFrame.util;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * 简单邮件发送器(可多附件)
 */
public class MailSender {

	public class MyAuthenticator extends Authenticator {
		String userName;
		String password;

		public MyAuthenticator() {
		}

		public MyAuthenticator(String username, String password) {
			this.userName = username;
			this.password = password;
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(userName, password);
		}
	}

	private static Session defaultSession = null;

	private  Session getInstance(Properties props, Authenticator authenticator) {
		if (defaultSession == null || !defaultSession.getProperty("mail.from").equals(props.getProperty("mail.from"))) {
            defaultSession = Session.getInstance(props, authenticator);
		} 
		return defaultSession;

	}

	/**
	 * 
	 * @param mailInfo
	 * @param isHTML
	 * @return
	 * @throws Exception
	 */
	private boolean sendMail(MailInfo mailInfo, boolean isHTML) throws Exception {
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		/* 如果需要身份认证，则创建一个密码验证器 */
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getSender(), mailInfo.getPassword());
		}
		try {
			/*
			 * 根据邮件会话属性和密码验证器构造一个发送邮件的
			 * getDefalutInstance() "单例"session,邮件发送服务信息将被记录,可使用getInstance()替代
			 */
			defaultSession = getInstance(pro, authenticator);
			defaultSession.setDebug(true);
			MimeMessage message = new MimeMessage(defaultSession);
			
			/*Address sender = new InternetAddress(mailInfo.getSender());
			Address receiver = new InternetAddress(mailInfo.getReceiver());
			message.setRecipient(Message.RecipientType.TO, receiver);*/
			
			message.setFrom();
			/* Message.RecipientType.TO属性表示接收者的类型为TO */
			message.setRecipients(Message.RecipientType.TO, mailInfo.getReceiver());
			message.setSubject(mailInfo.getSubject(), "UTF-8"); // 设置主题
			message.setSentDate(new Date());
			if (isHTML) {
				/* MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象 */
				Multipart m = new MimeMultipart("mixed");
				/* 创建一个包含HTML内容的MimeBodyPart */
				BodyPart bp = new MimeBodyPart();
				message.setHeader("Content-Type", "text/html; charset=UTF-8");
				bp.setContent(mailInfo.getContent(), "text/html; charset=UTF-8");
				File[] files = mailInfo.getFiles();
				if (files != null && files.length > 0) {
					for (File file : files) {
						BodyPart addkit = new MimeBodyPart(); // 创建附件
						DataHandler dh = new DataHandler(new FileDataSource(file));
						sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
						/*
						 * 参照DEBUG修改的的文件名称,否则中文乱码 Content-Type: text/plain;
						 * charset=UTF-8 Content-Transfer-Encoding: base64
						 */
						addkit.setFileName("=?UTF-8?B?" + enc.encode(file.getName().getBytes()) + "?=");
						addkit.setDataHandler(dh);
						m.addBodyPart(addkit);
					}
				}
				m.addBodyPart(bp);
				message.setContent(m);
			} else {
				message.setText(mailInfo.getContent(), "UTF-8");
			}
			Transport.send(message);
			return true;
		} catch (Exception e) {
			//Log.writeLog("MailSender.sendTextMail" + e.toString());
			throw e;
		}

	}

	public boolean sendTextMail(MailInfo mailInfo) throws Exception {
		return sendMail(mailInfo, false);
	}

	public boolean sendHtmlMail(MailInfo mailInfo) throws Exception {
		return sendMail(mailInfo, true);
	}

	public static void main(String[] args) throws Exception {

		MailInfo mailInfo = new MailInfo("smtp.exmail.qq.com","414072243@qq.com", true, false);

		mailInfo.setPassword("1120929tomsun");// 您的邮箱密码,如果不需要验证则无需密码

		mailInfo.setReceiver("414072243@qq.com");
		mailInfo.setSubject("我自己发送的邮件");
		mailInfo.setContent("嘿嘿,这是我发送的邮件!!!!");
		mailInfo.setFiles(new File[] { new File("D:\\你好.sql"), new File("D:\\你好.sql") });

		MailSender ms = new MailSender();

		 ms.sendTextMail(mailInfo);// 发送文体格
//		ms.sendHtmlMail(mailInfo);// 发送html格式
	}
}
