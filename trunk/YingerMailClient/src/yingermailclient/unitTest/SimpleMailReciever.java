package yingermailclient.unitTest;

/**
 * 接收邮件
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
		// 取得pop3协议的邮件服务器
		Store store = session.getStore("pop3");
		// 连接pop.qq.com邮件服务器
		store.connect("pop.163.com", "yingermailclient@163.com", "yingermail");
		// 返回文件夹对象
		Folder folder = store.getFolder("INBOX");
		// 设置仅读
		folder.open(Folder.READ_ONLY);
		// 获取信息
		Message[] message = folder.getMessages();
		for (int i = 0; i < message.length; i++) {
			// 打印主题
			System.out.println(message[i].getSubject());
			System.out.println("-----------------------------------------");
		}
		folder.close(true);
		store.close();
	}
}