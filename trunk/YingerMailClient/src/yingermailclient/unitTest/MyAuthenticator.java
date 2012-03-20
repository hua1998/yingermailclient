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

		// // �������Ҫ�������ʼ�
		// MailSenderInfo mailInfo = new MailSenderInfo();
		// mailInfo.setMailServerHost("smtp.163.com");
		// mailInfo.setMailServerPort("25");
		// mailInfo.setValidate(true);
		// mailInfo.setUserName("yinger090807@163.com");
		// mailInfo.setPassword("090807");// ������������
		// mailInfo.setFromAddress("yinger090807@163.com");
		// mailInfo.setToAddress("1158112684@qq.com");
		// mailInfo.setSubject("�����ʼ�");
		// mailInfo.setContent("�����ʼ�������");
		// // �������Ҫ�������ʼ�
		// SimpleMailSender sms = new SimpleMailSender();
		// sms.sendTextMail(mailInfo);// ���������ʽ
		// sms.sendHtmlMail(mailInfo);// ����html��ʽ
	}

	private static void Test() {
		// �������Ҫ�������ʼ�
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("127.0.0.1");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("yinger@yinger.com");
		mailInfo.setPassword("yinger");// ������������
		mailInfo.setFromAddress("yinger@yinger.com");
		mailInfo.setToAddress("hujiawei@yinger.com");
		mailInfo.setSubject("�����ʼ�");
		mailInfo.setContent("�����ʼ�������");
		// �������Ҫ�������ʼ�
		SimpleMailSender sms = new SimpleMailSender();
		sms.sendTextMail(mailInfo);// ���������ʽ
		// sms.sendHtmlMail(mailInfo);// ����html��ʽ
	}

}