package yingermailclient.unitTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yingermailclient.model.Mail;

public class TestUtil {

	public static List<Mail> createFakeMails() {
		List<String> receivers = new ArrayList<String>();
		receivers.add("111111@qq.com");
		receivers.add("222222@qq.com");

		Mail mail1 = new Mail("test1.xml", receivers, "my1@qq.com", "hello1", new Date(), "1024", true, "hello world", "INBOX");
		Mail mail2 = new Mail("test2.xml", receivers, "my2@qq.com", "hello2", new Date(), "1024", false, "hello world", "INBOX");
		Mail mail3 = new Mail("test3.xml", receivers, "my3@qq.com", "hello3", new Date(), "1024", true, "hello world", "INBOX");
		Mail mail4 = new Mail("test4.xml", receivers, "my4@qq.com", "hello4", new Date(), "1024", false, "hello world", "INBOX");

		List<Mail> mails = new ArrayList<Mail>();
		mails.add(mail4);
		mails.add(mail3);
		mails.add(mail2);
		mails.add(mail1);

		return mails;
	}

}
