package yingermailclient.unitTest;

/**
 * �����ʼ�
 */
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

public class SimpleMailReciever {

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
		// Store store = session.getStore("imap");
		// ȡ��pop3Э����ʼ�������
		Store store = session.getStore("pop3");
		// ����pop.qq.com�ʼ�������
		store.connect("pop.163.com", "yingermailclient@163.com", "yingermail");
		// �����ļ��ж���
		Folder folder = store.getFolder("INBOX");
		// ���ý���
		folder.open(Folder.READ_ONLY);
		// ��ȡ��Ϣ
		Message[] message = folder.getMessages();
		for (int i = 0; i < message.length; i++) {
			// ��ӡ����
			System.out.println(message[i].getSubject());
			System.out.println("-----------------------------------------");
		}
		folder.close(true);
		store.close();
	}
}