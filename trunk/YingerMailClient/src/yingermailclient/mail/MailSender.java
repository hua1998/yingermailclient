package yingermailclient.mail;

import yingermailclient.model.AccountInfo;
import yingermailclient.model.Mail;

/**
 * 发送邮件的接口
 */
public interface MailSender {

	/**
	 * 发送一封邮件并返回该邮件对象
	 */
	Mail send(Mail mail, AccountInfo accountInfo);
}
