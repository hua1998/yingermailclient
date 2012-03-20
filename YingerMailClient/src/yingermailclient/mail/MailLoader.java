package yingermailclient.mail;

import java.util.List;

import yingermailclient.model.AccountInfo;
import yingermailclient.model.Mail;

/**
 * 收邮件的接口
 */
public interface MailLoader {

	/**
	 * 得到INBOX的所有邮件
	 */
	List<Mail> getMailList(AccountInfo accountInfo);

}
