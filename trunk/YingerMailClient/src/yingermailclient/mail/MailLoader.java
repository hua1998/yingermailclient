package yingermailclient.mail;

import java.util.List;

import yingermailclient.model.AccountInfo;
import yingermailclient.model.Mail;

/**
 * ���ʼ��Ľӿ�
 */
public interface MailLoader {

	/**
	 * �õ�INBOX�������ʼ�
	 */
	List<Mail> getMailList(AccountInfo accountInfo);

}
