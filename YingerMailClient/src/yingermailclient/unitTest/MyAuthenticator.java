package yingermailclient.unitTest;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MyAuthenticator extends Authenticator {
	String userName = null;
	String password = null;

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

	public static void main(String[] args) {
		Test();

		// // 这个类主要是设置邮件
		// MailSenderInfo mailInfo = new MailSenderInfo();
		// mailInfo.setMailServerHost("smtp.163.com");
		// mailInfo.setMailServerPort("25");
		// mailInfo.setValidate(true);
		// mailInfo.setUserName("yinger090807@163.com");
		// mailInfo.setPassword("090807");// 您的邮箱密码
		// mailInfo.setFromAddress("yinger090807@163.com");
		// mailInfo.setToAddress("1158112684@qq.com");
		// mailInfo.setSubject("测试邮件");
		// mailInfo.setContent("测试邮件的内容");
		// // 这个类主要来发送邮件
		// SimpleMailSender sms = new SimpleMailSender();
		// sms.sendTextMail(mailInfo);// 发送文体格式
		// sms.sendHtmlMail(mailInfo);// 发送html格式
	}

	private static void Test() {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("127.0.0.1");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("yinger@yinger.com");
		mailInfo.setPassword("yinger");// 您的邮箱密码
		mailInfo.setFromAddress("yinger@yinger.com");
		mailInfo.setToAddress("hujiawei@yinger.com");
		mailInfo.setSubject("测试邮件");
		mailInfo.setContent("测试邮件的内容");
		// 这个类主要来发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		sms.sendTextMail(mailInfo);// 发送文体格式
		// sms.sendHtmlMail(mailInfo);// 发送html格式
	}

}