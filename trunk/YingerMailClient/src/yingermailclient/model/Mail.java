package yingermailclient.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Mail {

	// 在本地系统中代表该邮件的xml文件的名字, 使用UUID作为文件名：uuid.xml
	private String xmlName;
	// 发送人
	private String sender;
	// 收件人, 可以多个
	private List<String> receivers;
	// 邮件标题
	private String subject;
	// 日期
	private Date receiveDate;
	// 邮件大小
	private String size;
	// 是否已经被阅读
	private boolean hasRead;
	// 邮件正文
	private String content;
	// 抄送
	private List<String> ccs;
	// 附件
	private List<Attachment> files;
	// 邮件来源，存放该邮件的文件夹
	// 如果是从邮箱收到的, 则放在INBOX, 其他情况对应的放
	private String folder;

	private boolean isDeleted = false;

	public Mail() {

	}

	public Mail(String xmlName, List<String> receivers, String sender, String subject, Date receiveDate, String size, boolean hasRead, String content,
			String folder) {
		this.xmlName = xmlName;
		this.receivers = receivers;
		this.sender = sender;
		this.subject = subject;
		this.receiveDate = receiveDate;
		this.size = size;
		this.hasRead = hasRead;
		this.content = content;
		this.folder = folder;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public List<String> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<String> receivers) {
		this.receivers = receivers;
	}

	public String getXmlName() {
		return xmlName;
	}

	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
	}

	public boolean getHasRead() {
		return hasRead;
	}

	public void setHasRead(boolean hasRead) {
		this.hasRead = hasRead;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getCcs() {
		return ccs;
	}

	public void setCcs(List<String> ccs) {
		this.ccs = ccs;
	}

	public List<Attachment> getFiles() {
		return files;
	}

	public void setFiles(List<Attachment> files) {
		this.files = files;
	}
	
	public String getDateString(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(receiveDate);
	}

	public String getCCString() {
		StringBuffer ccString = new StringBuffer();
		for (String cc : this.ccs) {
			ccString.append(cc + ";");
		}
		return handleString(ccString);
	}

	public String getReceiverString() {
		StringBuffer sb = new StringBuffer();
		for (String rec : this.receivers) {
			sb.append(rec + ";");
		}
		return handleString(sb);
	}

	private String handleString(StringBuffer sb) {
		String result = sb.toString();
		if ("".equals(result))
			return result;
		return result.substring(0, result.lastIndexOf(";"));
	}

	// 将邮件的大小单位定为kb, 并保留两位小数
	public static String getSize(int size) {
		double d = Double.valueOf(size);
		double result = d / 1024;
		return (new java.text.DecimalFormat("#.##")).format(result);
	}

}
