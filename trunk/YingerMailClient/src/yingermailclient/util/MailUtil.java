package yingermailclient.util;

import java.io.File;
import java.util.List;

import yingermailclient.mail.MailLoaderImpl;
import yingermailclient.mail.MailSenderImpl;
import yingermailclient.model.AccountInfo;
import yingermailclient.model.Mail;
import yingermailclient.navigatorEntity.NavigatorEntityElement;
import yingermailclient.navigatorEntity.NavigatorEntityFactory;

// ����ʼ��ͻ��˵Ĺ����࣬�����˺ܶ�Ĳ����Ĵ���취

public class MailUtil {

	// Ϊ���е��˻���ȡ�ʼ�
	public static void receiveAllMail() throws Exception {
		List accountList = NavigatorEntityFactory.getAccountList();
		for (int i = 0, size = accountList.size(); i < size; i++) {
			NavigatorEntityElement element = (NavigatorEntityElement) accountList.get(i);
			receiveAccountMail(element.getAccountInfo());
		}
	}

	// Ϊָ�����˻���ȡ�ʼ�
	public static List<Mail> receiveAccountMail(AccountInfo accountInfo) throws Exception {
		MailLoaderImpl loader = new MailLoaderImpl();
		List<Mail> mails = loader.getMailList(accountInfo);// ͨ��MailLoaderImpl�õ����е��ʼ�
		// ���ʼ��б��浽���ص��ļ�ϵͳ�Ĺ���������ǰ̨�����ڽ���������ʾ
		for (int i = 0, size = mails.size(); i < size; i++) {
			FileUtil.convertMailToXML(accountInfo, mails.get(i), FileUtil.INBOX);// ���õ����ʼ�����Ϊxml�ļ������ڱ����ļ�����
		}
		return mails;
	}

	// �����ʼ�
	public static boolean sendMail(Mail mail) {
		// �õ����������ļ�
		File proFile = new File(FileUtil.DATA_FOLDER + mail.getSender() + File.separator + FileUtil.CONFIG_FILE);
		// ���������ļ�����accountinfo
		AccountInfo accountInfo = PropertiesUtil.createAccountInfo(proFile);
		// mail�Ƿ������е�
		mail.setFolder(FileUtil.OUTBOX);
		// ���Ƚ�mail���õ��������У�ע�⣬���ﲻ��Ը��������κβ���
		FileUtil.convertMailToXML(accountInfo, mail, FileUtil.OUTBOX);
		String xmlName = mail.getXmlName();
		// ���ŷ����ʼ�
		MailSenderImpl sender = new MailSenderImpl();
		// �����ʼ���Ҫʹ��accountinfo������Ҫ�õ����еĸ���������ӵ�Ҫ���͵��ʼ�message��
		Mail sentMail = sender.send(mail, accountInfo);
		if (sentMail == null) {
			return false;//Ϊnull��ʾ����ʧ��
		}
		// mail���ѷ��͵�
		sentMail.setFolder(FileUtil.SENTBOX);
		// ������ͳɹ�����ô�Ͱѷ������е��Ǹ��ʼ�Ų���ѷ�����
		FileUtil.convertMailToXML(accountInfo, sentMail, FileUtil.SENTBOX);
		// sentMail��mail���õ���ͬһ������
		// ����ɾ���ոշŵ��������е��ļ� TODO������Ӧ���ǳ���ɾ���ʼ�������Ҳֻ��ɾ��xml�ļ�������и����Ļ�����������
		File mailFile = new File(FileUtil.getFolderPath(accountInfo, FileUtil.OUTBOX)+xmlName);
		FileUtil.deleteFile(mailFile);//��ɾ��������ֻ��ɾ���������е�����ʼ���xml�ļ�
		return true;
	}

}
