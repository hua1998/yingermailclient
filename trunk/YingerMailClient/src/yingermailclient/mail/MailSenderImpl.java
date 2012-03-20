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
 * 邮件发送实现类
 */
public class MailSenderImpl implements MailSender {

	// 发送邮件，如果成功则返回发送成功的mail
	@Override
	public Mail send(Mail mail, AccountInfo accountInfo) {
		try {
			// 建立session，创建一个message
			Session session = accountInfo.getSession();
			Message mailMessage = new MimeMessage(session);
			// 设置发件人地址
			Address from = new InternetAddress("<" + accountInfo.getAccount() + ">");
			mailMessage.setFrom(from);
			// 设置所有收件人的地址
			Address[] to = getAddress(mail.getReceivers());
			mailMessage.setRecipients(Message.RecipientType.TO, to);
			// 设置抄送人地址
			Address[] cc = getAddress(mail.getCcs());
			mailMessage.setRecipients(Message.RecipientType.CC, cc);
			// 设置主题
			mailMessage.setSubject(mail.getSubject());
			// 发送日期
			mailMessage.setSentDate(new Date());
			// 构建整封邮件的容器
			Multipart main = new MimeMultipart();
			// 正文的body
			BodyPart body = new MimeBodyPart();
			// 设置内容的格式
			body.setContent(mail.getContent(), "text/html; charset=utf-8");
			main.addBodyPart(body);
			// 处理附件
			if (mail.getFiles() != null && mail.getFiles().size() > 0) {
				for (Attachment f : mail.getFiles()) {
					// 每个附件的body
					MimeBodyPart fileBody = new MimeBodyPart();
					// 添加上附件
					fileBody.attachFile(f.getFile());
					// 为文件名进行编码
					fileBody.setFileName(MimeUtility.encodeText(f.getSourceName()));
					// 将附件body添加到主body中
					main.addBodyPart(fileBody);
				}
			}
			// 将正文的Multipart对象设入Message中
			mailMessage.setContent(main);
			// 利用Transport发送邮件
			Transport.send(mailMessage);
			return mail;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;//如果发送失败，返回null
	}

	// 获得所有的收件人地址或者抄送的地址
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
