package com.store.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class MailUtils {

	public static void sendMail(String email, String emailMsg)
			throws AddressException, MessagingException {
		//新建存放邮件基本信息的prop文件

		Properties props = new Properties();
		//设置邮件协议，smtp为传输协议
		props.setProperty("mail.transport.protocol", "SMTP");
		
		//设置主机名和是否开启认证（认证必须开启）
		props.setProperty("mail.host", "localhost");
		props.setProperty("mail.smtp.auth", "true");// 鎸囧畾楠岃瘉涓簍rue

		// 认证，输入用户名和密码
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("service", "123");
			}
		};

		Session session = Session.getInstance(props, auth);

		//创建邮件主体
		Message message = new MimeMessage(session);

		//设置发送人邮件
		message.setFrom(new InternetAddress("service@store.com"));

		//设置收件人
		message.setRecipient(RecipientType.TO, new InternetAddress(email)); 

		//设置主题
		message.setSubject("用户激活");
		

		//设置邮件主题内容
		message.setContent(emailMsg, "text/html;charset=utf-8");

		//发送邮件
		Transport.send(message);
	}
}
