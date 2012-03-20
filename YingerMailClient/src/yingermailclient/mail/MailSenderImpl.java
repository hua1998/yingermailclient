package yingermailclient.mail;

import java.util.Date;
import java.util.List;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import yingermailclient.model.AccountInfo;
import yingermailclient.model.Attachment;
import yingermailclient.model.Mail;

/**
 * �ʼ�����ʵ����
 */
public class MailSenderImpl implements MailSender {

	// �����ʼ�������ɹ��򷵻ط��ͳɹ���mail
	@Override
	public Mail send(Mail mail, AccountInfo accountInfo) {
		try {
			// ����session������һ��message
			Session session = accountInfo.getSession();
			Message mailMessage = new MimeMessage(session);
			// ���÷����˵�ַ
			Address from = new InternetAddress("<" + accountInfo.getAccount() + ">");
			mailMessage.setFrom(from);
			// ���������ռ��˵ĵ�ַ
			Address[] to = getAddress(mail.getReceivers());
			mailMessage.setRecipients(Message.RecipientType.TO, to);
			// ���ó����˵�ַ
			Address[] cc = getAddress(mail.getCcs());
			mailMessage.setRecipients(Message.RecipientType.CC, cc);
			// ��������
			mailMessage.setSubject(mail.getSubject());
			// ��������
			mailMessage.setSentDate(new Date());
			// ���������ʼ�������
			Multipart main = new MimeMultipart();
			// ���ĵ�body
			BodyPart body = new MimeBodyPart();
			// �������ݵĸ�ʽ
			body.setContent(mail.getContent(), "text/html; charset=utf-8");
			main.addBodyPart(body);
			// ������
			if (mail.getFiles() != null && mail.getFiles().size() > 0) {
				for (Attachment f : mail.getFiles()) {
					// ÿ��������body
					MimeBodyPart fileBody = new MimeBodyPart();
					// ����ϸ���
					fileBody.attachFile(f.getFile());
					// Ϊ�ļ������б���
					fileBody.setFileName(MimeUtility.encodeText(f.getSourceName()));
					// ������body��ӵ���body��
					main.addBodyPart(fileBody);
				}
			}
			// �����ĵ�Multipart��������Message��
			mailMessage.setContent(main);
			// ����Transport�����ʼ�
			Transport.send(mailMessage);
			return mail;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;//�������ʧ�ܣ�����null
	}

	// ������е��ռ��˵�ַ���߳��͵ĵ�ַ
	private Address[] getAddress(List<String> addList) throws Exception {
		Address[] result = new Address[addList.size()];
		for (int i = 0; i < addList.size(); i++) {
			if (addList.get(i) == null || "".equals(addList.get(i))) {
				continue;
			}
			result[i] = new InternetAddress(addList.get(i));
		}
		return result;
	}
}
