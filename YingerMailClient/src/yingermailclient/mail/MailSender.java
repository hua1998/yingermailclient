package yingermailclient.mail;

import yingermailclient.model.AccountInfo;
import yingermailclient.model.Mail;

/**
 * �����ʼ��Ľӿ�
 */
public interface MailSender {

	/**
	 * ����һ���ʼ������ظ��ʼ�����
	 */
	Mail send(Mail mail, AccountInfo accountInfo);
}
