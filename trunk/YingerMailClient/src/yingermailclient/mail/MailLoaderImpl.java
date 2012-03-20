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
 * 读取邮件实现类
 */
public class MailLoaderImpl implements MailLoader {

	// 收取邮件得到所有的Mail
	@Override
	public List<Mail> getMailList(AccountInfo accountInfo) {
		// 得到INBOX对应的Folder
		Folder inbox = getInboxFolder(accountInfo);// INBOX POP3Folder
		try {
			// 可读可写的方式打开收件夹
			inbox.open(Folder.READ_WRITE);// mode=2,opened=true,total=2,message_cache:vector<E>
			// 得到INBOX里的所有信息
			Message[] messages = inbox.getMessages();// com.sun.mail.pop3.POP3Message@f65b5b
			// 将Message数组封装成Mail集合
			List<Mail> result = convertMessageToMail(accountInfo, messages);
			// 删除邮箱中全部的邮件, 那么每次使用邮件系统, 只会拿新收到的邮件
			// deleteFromServer(messages);// 删除服务器端的邮件！TODO：这里真的不可以这样！
			// 删除邮件并提交删除状态
			inbox.close(true);// 这里相当于关闭文件夹
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 将javamail中的Message对象赎罪转换成项目中的Mail对象的集合
	private List<Mail> convertMessageToMail(AccountInfo accountInfo, Message[] messages) {
		List<Mail> result = new ArrayList<Mail>();
		try {
			// 将得到的Message对象封装成Mail对象
			for (Message m : messages) {
				// 特殊的例子，没有这封邮件，系统邮件不收取
				if (m.getSubject().equals("Mail Delivery Subsystem - Returned Mail")) {
					continue;
				}
				// 获得内容
				String content = getContent(m, new StringBuffer()).toString();
				// 得到邮件的各个值
				Mail mail = new Mail(null, getAllRecipients(m), getSender(m), m.getSubject(), getReceivedDate(m), getSize(m), hasRead(m), content,
						FileUtil.INBOX);
				// 为mail对象设置抄送
				mail.setCcs(getCC(m));
				// 设置附件集合
				mail.setFiles(getFiles(accountInfo, m));
				// 设置邮件是收件箱的
				mail.setFolder(FileUtil.INBOX);
				// 添加mail
				result.add(mail);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 得到邮件的大小
	private String getSize(Message m) throws Exception {
		double d = Double.valueOf(m.getSize());
		double result = d / 1024;
		return (new java.text.DecimalFormat("#.##")).format(result);
	}

	// 得到接收的日期, 优先返回发送日期, 其次返回收信日期
	private Date getReceivedDate(Message m) throws Exception {
		if (m.getSentDate() != null)
			return m.getSentDate();
		if (m.getReceivedDate() != null)
			return m.getReceivedDate();
		return new Date();
	}

	// 得到抄送的地址
	private List<String> getCC(Message m) throws Exception {
		Address[] addresses = m.getRecipients(Message.RecipientType.CC);
		return getAddresses(addresses);
	}

	// 获得邮件的附件
	private List<Attachment> getFiles(AccountInfo accountInfo, Message m) throws Exception {
		List<Attachment> files = new ArrayList<Attachment>();
		// 是混合类型, 就进行处理
		if (m.isMimeType("multipart/mixed")) {
			Multipart mp = (Multipart) m.getContent();
			// 得到邮件内容的Multipart对象并得到内容中Part的数量
			int count = mp.getCount();
			for (int i = 1; i < count; i++) {
				Part part = mp.getBodyPart(i);
				// 在本地创建文件并添加到结果中
				files.add(FileUtil.createFileFromPart(accountInfo, part));
			}
		}
		return files;
	}

	// 返回邮件正文 TODO：bodypart multipart
	private StringBuffer getContent(Part part, StringBuffer result) throws Exception {
		if (part.isMimeType("multipart/*")) {// 遇到multipart类型就要分成多个部分
			Multipart p = (Multipart) part.getContent();
			int count = p.getCount();
			// Multipart的第一部分是text/plain, 第二部分是text/html的格式, 只需要解析第一部分即可
			if (count > 1) {
				count = 1;
			}
			for (int i = 0; i < count; i++) {
				BodyPart bp = p.getBodyPart(i);
				getContent(bp, result);// 递归调用
			}
		} else if (part.isMimeType("text/*")) { // 遇到text/plain或者text/html格式,直接得到内容
			result.append(part.getContent());
		}
		return result;
	}

	// 判断一封邮件是否已读, true表示已读取, false表示没有读取
	private boolean hasRead(Message m) throws Exception {
		Flags flags = m.getFlags();
		if (flags.contains(Flags.Flag.SEEN)) {// TODO：邮件已读了的话就是seen
			return true;
		}
		return false;
	}

	// 得到一封邮件的所有收件人
	private List<String> getAllRecipients(Message m) throws Exception {
		Address[] addresses = m.getAllRecipients();
		return getAddresses(addresses);
	}

	// 将地址字符串数组封装成集合
	private List<String> getAddresses(Address[] addresses) {
		List<String> result = new ArrayList<String>();
		if (addresses == null)
			return result;
		for (Address a : addresses) {
			result.add(a.toString());
		}
		return result;
	}

	// 得到发送人的地址
	private String getSender(Message m) throws Exception {
		Address[] addresses = m.getFrom();
		return MimeUtility.decodeText(addresses[0].toString());// TODO:解码
	}

	// 得到邮箱INBOX，首先是通过pop3协议得到账户邮箱store，然后通过store得到收件箱
	private Folder getInboxFolder(AccountInfo accountInfo) {
		Store store = accountInfo.getStore();
		try {
			return store.getFolder("INBOX");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 将邮件数组设置为删除状态
	private void deleteFromServer(Message[] messages) throws Exception {
		for (Message m : messages) {
			m.setFlag(Flags.Flag.DELETED, true);// TODO:?
		}
	}

}
