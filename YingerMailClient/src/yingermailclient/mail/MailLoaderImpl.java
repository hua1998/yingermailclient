package yingermailclient.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;

import yingermailclient.model.AccountInfo;
import yingermailclient.model.Attachment;
import yingermailclient.model.Mail;
import yingermailclient.util.FileUtil;

/**
 * ��ȡ�ʼ�ʵ����
 */
public class MailLoaderImpl implements MailLoader {

	// ��ȡ�ʼ��õ����е�Mail
	@Override
	public List<Mail> getMailList(AccountInfo accountInfo) {
		// �õ�INBOX��Ӧ��Folder
		Folder inbox = getInboxFolder(accountInfo);// INBOX POP3Folder
		try {
			// �ɶ���д�ķ�ʽ���ռ���
			inbox.open(Folder.READ_WRITE);// mode=2,opened=true,total=2,message_cache:vector<E>
			// �õ�INBOX���������Ϣ
			Message[] messages = inbox.getMessages();// com.sun.mail.pop3.POP3Message@f65b5b
			// ��Message�����װ��Mail����
			List<Mail> result = convertMessageToMail(accountInfo, messages);
			// ɾ��������ȫ�����ʼ�, ��ôÿ��ʹ���ʼ�ϵͳ, ֻ�������յ����ʼ�
			// deleteFromServer(messages);// ɾ���������˵��ʼ���TODO��������Ĳ�����������
			// ɾ���ʼ����ύɾ��״̬
			inbox.close(true);// �����൱�ڹر��ļ���
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ��javamail�е�Message��������ת������Ŀ�е�Mail����ļ���
	private List<Mail> convertMessageToMail(AccountInfo accountInfo, Message[] messages) {
		List<Mail> result = new ArrayList<Mail>();
		try {
			// ���õ���Message�����װ��Mail����
			for (Message m : messages) {
				// ��������ӣ�û������ʼ���ϵͳ�ʼ�����ȡ
				if (m.getSubject().equals("Mail Delivery Subsystem - Returned Mail")) {
					continue;
				}
				// �������
				String content = getContent(m, new StringBuffer()).toString();
				// �õ��ʼ��ĸ���ֵ
				Mail mail = new Mail(null, getAllRecipients(m), getSender(m), m.getSubject(), getReceivedDate(m), getSize(m), hasRead(m), content,
						FileUtil.INBOX);
				// Ϊmail�������ó���
				mail.setCcs(getCC(m));
				// ���ø�������
				mail.setFiles(getFiles(accountInfo, m));
				// �����ʼ����ռ����
				mail.setFolder(FileUtil.INBOX);
				// ���mail
				result.add(mail);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// �õ��ʼ��Ĵ�С
	private String getSize(Message m) throws Exception {
		double d = Double.valueOf(m.getSize());
		double result = d / 1024;
		return (new java.text.DecimalFormat("#.##")).format(result);
	}

	// �õ����յ�����, ���ȷ��ط�������, ��η�����������
	private Date getReceivedDate(Message m) throws Exception {
		if (m.getSentDate() != null)
			return m.getSentDate();
		if (m.getReceivedDate() != null)
			return m.getReceivedDate();
		return new Date();
	}

	// �õ����͵ĵ�ַ
	private List<String> getCC(Message m) throws Exception {
		Address[] addresses = m.getRecipients(Message.RecipientType.CC);
		return getAddresses(addresses);
	}

	// ����ʼ��ĸ���
	private List<Attachment> getFiles(AccountInfo accountInfo, Message m) throws Exception {
		List<Attachment> files = new ArrayList<Attachment>();
		// �ǻ������, �ͽ��д���
		if (m.isMimeType("multipart/mixed")) {
			Multipart mp = (Multipart) m.getContent();
			// �õ��ʼ����ݵ�Multipart���󲢵õ�������Part������
			int count = mp.getCount();
			for (int i = 1; i < count; i++) {
				Part part = mp.getBodyPart(i);
				// �ڱ��ش����ļ�����ӵ������
				files.add(FileUtil.createFileFromPart(accountInfo, part));
			}
		}
		return files;
	}

	// �����ʼ����� TODO��bodypart multipart
	private StringBuffer getContent(Part part, StringBuffer result) throws Exception {
		if (part.isMimeType("multipart/*")) {// ����multipart���;�Ҫ�ֳɶ������
			Multipart p = (Multipart) part.getContent();
			int count = p.getCount();
			// Multipart�ĵ�һ������text/plain, �ڶ�������text/html�ĸ�ʽ, ֻ��Ҫ������һ���ּ���
			if (count > 1) {
				count = 1;
			}
			for (int i = 0; i < count; i++) {
				BodyPart bp = p.getBodyPart(i);
				getContent(bp, result);// �ݹ����
			}
		} else if (part.isMimeType("text/*")) { // ����text/plain����text/html��ʽ,ֱ�ӵõ�����
			result.append(part.getContent());
		}
		return result;
	}

	// �ж�һ���ʼ��Ƿ��Ѷ�, true��ʾ�Ѷ�ȡ, false��ʾû�ж�ȡ
	private boolean hasRead(Message m) throws Exception {
		Flags flags = m.getFlags();
		if (flags.contains(Flags.Flag.SEEN)) {// TODO���ʼ��Ѷ��˵Ļ�����seen
			return true;
		}
		return false;
	}

	// �õ�һ���ʼ��������ռ���
	private List<String> getAllRecipients(Message m) throws Exception {
		Address[] addresses = m.getAllRecipients();
		return getAddresses(addresses);
	}

	// ����ַ�ַ��������װ�ɼ���
	private List<String> getAddresses(Address[] addresses) {
		List<String> result = new ArrayList<String>();
		if (addresses == null)
			return result;
		for (Address a : addresses) {
			result.add(a.toString());
		}
		return result;
	}

	// �õ������˵ĵ�ַ
	private String getSender(Message m) throws Exception {
		Address[] addresses = m.getFrom();
		return MimeUtility.decodeText(addresses[0].toString());// TODO:����
	}

	// �õ�����INBOX��������ͨ��pop3Э��õ��˻�����store��Ȼ��ͨ��store�õ��ռ���
	private Folder getInboxFolder(AccountInfo accountInfo) {
		Store store = accountInfo.getStore();
		try {
			return store.getFolder("INBOX");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ���ʼ���������Ϊɾ��״̬
	private void deleteFromServer(Message[] messages) throws Exception {
		for (Message m : messages) {
			m.setFlag(Flags.Flag.DELETED, true);// TODO:?
		}
	}

}
