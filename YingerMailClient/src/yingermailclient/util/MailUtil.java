package yingermailclient.util;

import java.io.File;
import java.util.List;

import yingermailclient.mail.MailLoaderImpl;
import yingermailclient.mail.MailSenderImpl;
import yingermailclient.model.AccountInfo;
import yingermailclient.model.Mail;
import yingermailclient.navigatorEntity.NavigatorEntityElement;
import yingermailclient.navigatorEntity.NavigatorEntityFactory;

// 这个邮件客户端的工具类，集成了很多的操作的处理办法

public class MailUtil {

	// 为所有的账户收取邮件
	public static void receiveAllMail() throws Exception {
		List accountList = NavigatorEntityFactory.getAccountList();
		for (int i = 0, size = accountList.size(); i < size; i++) {
			NavigatorEntityElement element = (NavigatorEntityElement) accountList.get(i);
			receiveAccountMail(element.getAccountInfo());
		}
	}

	// 为指定的账户收取邮件
	public static List<Mail> receiveAccountMail(AccountInfo accountInfo) throws Exception {
		MailLoaderImpl loader = new MailLoaderImpl();
		List<Mail> mails = loader.getMailList(accountInfo);// 通过MailLoaderImpl得到所有的邮件
		// 将邮件列表保存到本地的文件系统的工作放在了前台，用于进度条的显示
		for (int i = 0, size = mails.size(); i < size; i++) {
			FileUtil.convertMailToXML(accountInfo, mails.get(i), FileUtil.INBOX);// 将得到的邮件保存为xml文件，放在本地文件夹中
		}
		return mails;
	}

	// 发送邮件
	public static boolean sendMail(Mail mail) {
		// 得到它的配置文件
		File proFile = new File(FileUtil.DATA_FOLDER + mail.getSender() + File.separator + FileUtil.CONFIG_FILE);
		// 根据配置文件创建accountinfo
		AccountInfo accountInfo = PropertiesUtil.createAccountInfo(proFile);
		// mail是发件箱中的
		mail.setFolder(FileUtil.OUTBOX);
		// 首先将mail放置到发件箱中，注意，这里不会对附件进行任何操作
		FileUtil.convertMailToXML(accountInfo, mail, FileUtil.OUTBOX);
		String xmlName = mail.getXmlName();
		// 接着发送邮件
		MailSenderImpl sender = new MailSenderImpl();
		// 发送邮件需要使用accountinfo，这里要得到所有的附件，并添加到要发送的邮件message中
		Mail sentMail = sender.send(mail, accountInfo);
		if (sentMail == null) {
			return false;//为null表示发送失败
		}
		// mail是已发送的
		sentMail.setFolder(FileUtil.SENTBOX);
		// 如果发送成功，那么就把发件箱中的那个邮件挪到已发送中
		FileUtil.convertMailToXML(accountInfo, sentMail, FileUtil.SENTBOX);
		// sentMail和mail引用的是同一个对象！
		// 并且删除刚刚放到发件箱中的文件 TODO：这里应该是彻底删除邮件，但是也只是删除xml文件，如果有附件的话，附件保留
		File mailFile = new File(FileUtil.getFolderPath(accountInfo, FileUtil.OUTBOX)+xmlName);
		FileUtil.deleteFile(mailFile);//不删除附件，只是删除发件箱中的这个邮件的xml文件
		return true;
	}

}
