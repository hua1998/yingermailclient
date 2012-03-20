package yingermailclient.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.mail.Part;
import javax.mail.internet.MimeUtility;

import yingermailclient.model.AccountInfo;
import yingermailclient.model.Attachment;
import yingermailclient.model.Mail;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 文件工具类
 */
public class FileUtil {

	// 存放所有用户数据的目录
	public static final String DATA_FOLDER = "D:\\yinger\\My DBank\\workspace_rcp\\rcp\\YingerMailClient\\datas" + File.separator;
	// 存放具体某个账户配置的properties文件
	public static final String CONFIG_FILE = "mail.properties";
	// 收件箱的目录名
	public static final String INBOX = "Inbox";
	// 发件箱的目录名
	public static final String OUTBOX = "Outbox";
	// 已发送的目录名
	public static final String SENTBOX = "Sentbox";
	// 草稿箱的目录名
	public static final String DRAFTBOX = "Draftbox";
	// 垃圾箱的目录名
	public static final String DELETEDBOX = "Deletedbox";
	// 附件的存放目录名
	public static final String Attachment = "Attachment";
	// 创建XStream对象
	private static XStream xstream = new XStream(new DomDriver());

	// 创建用户的帐号目录和相关的子目录
	public static void createMailFolder(AccountInfo accountInfo) {
		String accountRoot = getAccountRoot(accountInfo);
		// 使用用户当前设置的帐号来生成目录
		mkdir(new File(accountRoot));
		// 创建INBOX目录
		mkdir(new File(accountRoot + INBOX));
		// 发件箱
		mkdir(new File(accountRoot + OUTBOX));
		// 已发送
		mkdir(new File(accountRoot + SENTBOX));
		// 草稿箱
		mkdir(new File(accountRoot + DRAFTBOX));
		// 垃圾箱
		mkdir(new File(accountRoot + DELETEDBOX));
		// 附件存放目录
		mkdir(new File(accountRoot + Attachment));
	}

	// 得到邮件帐号的根目录，这里是包含了 / 的
	public static String getAccountRoot(AccountInfo accountInfo) {
		String accountRoot = DATA_FOLDER + accountInfo.getAccount() + File.separator;
		return accountRoot;
	}

	// 得到某个目录名字, 例如inbox的目录，这里是包含了 / 的
	public static String getFolderPath(AccountInfo accountInfo, String folderName) {
		return getAccountRoot(accountInfo) + folderName + File.separator;
	}

	// 为附件创建本地文件, 目录是账户附件目录
	public static Attachment createFileFromPart(AccountInfo accountInfo, Part part) {
		try {
			// 得到文件存放的目录
			String fileRepository = getFolderPath(accountInfo, Attachment);
			// 得到附件的原始名称
			String serverFileName = MimeUtility.decodeText(part.getFileName());
			// 生成UUID作为在本地系统中唯一的文件标识
			String fileName = UUID.randomUUID().toString();
			// 生成一个新的文件，相同的类型，只是文件名是唯一的随机的
			File file = new File(fileRepository + fileName + getFileSuffix(serverFileName));
			// 读写文件
			InputStream is = part.getInputStream();
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream outs = new BufferedOutputStream(fos);
			// 如果附件内容为空part.getSize为-1, 如果直接new byte, 将抛出异常
			int size = (part.getSize() > 0) ? part.getSize() : 0;
			byte[] b = new byte[size];
			is.read(b);
			outs.write(b);
			outs.close();
			is.close();
			fos.close();
			// 封装对象，file是下载到本地的文件
			Attachment attachment = new Attachment(serverFileName, file);
			return attachment;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 从相应的folder中得到全部的xml文件
	public static List<File> getXMLFiles(AccountInfo accountInfo, String folderName) {
		// 得到文件夹的路径
		String folderPath = getAccountRoot(accountInfo) + folderName;
		// 得到文件夹
		File folder = new File(folderPath);
		// 对文件夹进行后缀过滤
		List<File> files = filterFiles(folder, ".xml");
		return files;
	}

	// 从一个文件目录中, 以参数文件后缀subffix为条件, 过滤文件
	public static List<File> filterFiles(File folder, String suffix) {
		List<File> result = new ArrayList<File>();
		File[] files = folder.listFiles();
		if (files == null) {
			return new ArrayList<File>();
		}
		for (File f : files) {
			if (f.getName().endsWith(suffix)) {
				result.add(f);
			}
		}
		return result;
	}

	// 得到文件名的后缀，返回值最前面是 .
	public static String getFileSuffix(String fileName) {
		if (fileName == null || fileName.trim().equals(""))
			return "";
		if (fileName.lastIndexOf(".") != -1) {
			return fileName.substring(fileName.lastIndexOf("."));
		}
		return "";
	}

	// 将一个mail对象写到xmlFile中
	public static boolean convertMailToXML(AccountInfo accountInfo, Mail mail, String folderName) {
		// 生成UUID的文件名
		String xmlName = UUID.randomUUID().toString() + ".xml";
		mail.setXmlName(xmlName);
		// 得到对应的目录路径
		String folder = getAccountRoot(accountInfo) + folderName + File.separator;
		File xmlFile = new File(folder + xmlName);
		// 使用xstream写入到xml文件中
		try {
			writeToXML(xmlFile, mail);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// 将一个邮件对象使用XStream写到xml文件中
	public static void writeToXML(File xmlFile, Mail mail) throws Exception {
		FileOutputStream fos = null;
		OutputStreamWriter writer = null;
		if (!xmlFile.exists()) {
			xmlFile.createNewFile();
		}
		fos = new FileOutputStream(xmlFile);
		writer = new OutputStreamWriter(fos, "UTF8");//以UTF8的编码形式写入到文件中
		xstream.toXML(mail, writer);//toXML方法
		writer.close();
		fos.close();
	}

	// 将一份xml文档转换成Mail对象
	public static Mail convertXMLToMail(AccountInfo accountInfo, File xmlFile) {
		FileInputStream fis = null;
		Mail mail = null;
		try {
			fis = new FileInputStream(xmlFile);
			// 调用XStream的转换方法将文件转换成对象
			mail = (Mail) xstream.fromXML(fis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mail;
	}

	// 创建目录的工具方法, 判断目录是否存在
	public static void mkdir(File file) {
		if (!file.exists()) {
			file.mkdir();
		}
	}

	// 得到所有的属性配置文件
	public static List<File> getAllPropertyFiles() {
		List<File> proFiles = new ArrayList<File>();
		File dataFolder = new File(FileUtil.DATA_FOLDER);
		File[] accountFolders = dataFolder.listFiles();
		for (int i = 0, length = accountFolders.length; i < length; i++) {
			File account = accountFolders[i];
			if (account.isDirectory()) {
				proFiles.add(filterFiles(account, ".properties").get(0));
			}
		}
		return proFiles;
	}

	// 删除邮件，不是彻底删除，只是将邮件保存到已删除的邮件夹中
	public static void deleteMailFile(AccountInfo accountInfo, Mail mail) {
		// 这里有点复杂了，该账户可能是邮件的发送者，也有可能是接收者
		// 但是通过添加一个参数，账户信息，那么就可以了，找到相应账户的目录就行了
		// 至于是哪个文件夹就是由mail来决定，并且不改变它原来的文件夹位置
		File xmlFile = new File(getFolderPath(accountInfo, mail.getFolder()) + mail.getXmlName());
		if (!xmlFile.exists()) {
			return;
		}
		// 将mail保存到已删除的文件夹中
		File newXMLFile = new File(getFolderPath(accountInfo, FileUtil.DELETEDBOX) + mail.getXmlName());
		if (!newXMLFile.exists()) {
			try {
				newXMLFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		copyFile(xmlFile, newXMLFile);
		xmlFile.delete();
	}

	// 文件复制操作，复制文件的内容，名称是不同的
	public static void copyFile(File xmlFile, File newXMLFile) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(xmlFile);
			fos = new FileOutputStream(newXMLFile);
			byte[] bytes = new byte[1024];
			int len = fis.read(bytes);
			while (len != -1) {//eof 返回值是-1，而不是0
				fos.write(bytes, 0, len);
				len = fis.read(bytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 彻底删除邮件
	public static void completeDeleteMailFile(AccountInfo accountInfo, Mail mail) {
		// 首先要删除邮件的附件
		List<Attachment> attachments = mail.getFiles();
		// 附件存在还要去删除
		if (attachments != null && attachments.size() > 0) {
			for (int i = 0, size = attachments.size(); i < size; i++) {
				File file = attachments.get(i).getFile();
				if (file.exists()) {
					file.delete();
				}
			}
		}
		// 接着要删除邮件的xml文件
		File xmlFile = new File(getFolderPath(accountInfo, FileUtil.DELETEDBOX) + mail.getXmlName());
		if (xmlFile.exists()) {
			xmlFile.delete();
		}
	}

	// 将指定的文件复制一份，保存到账户的附件文件夹中,mail是一封要发送的邮件，所以mail的发送者一定是已有的账户
	public static Attachment createAttachmentFromFile(Mail mail, File file) {
		// 生成UUID的文件名
		String fileName = UUID.randomUUID().toString() + getFileSuffix(file.getName());
		// 得到新文件
		File newFile = new File(DATA_FOLDER + mail.getSender() + File.separator + FileUtil.Attachment + File.separator + fileName);
		if (!newFile.exists()) {
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		copyFile(file, newFile);
		return new Attachment(file.getName(), newFile);
	}

	// 得到本地的指定账户的相应文件夹中的所有邮件
	public static List<Mail> getLocalMails(AccountInfo accountInfo, String folderName) {
		List<Mail> mails = new ArrayList<Mail>();
		File folder = new File(getFolderPath(accountInfo, folderName));
		List<File> xmlFiles = filterFiles(folder, ".xml");
		if (xmlFiles != null && xmlFiles.size() > 0) {
			for (int i = 0, size = xmlFiles.size(); i < size; i++) {
				mails.add(convertXMLToMail(accountInfo, xmlFiles.get(i)));
			}
		}
		return mails;
	}

	// 删除所有的数据文件
	public static void deleteAllDataFiles() {
		File file = new File(FileUtil.DATA_FOLDER);//datas
		File[] accountFolders = file.listFiles();
		for (int i = 0; i < accountFolders.length; i++) {
			File accountFolder = accountFolders[i];//datas/account
			File[] boxFolders = accountFolder.listFiles();
			for (int j = 0; j < boxFolders.length; j++) {
				File boxFolder = boxFolders[j];//datas/account/box
				if (boxFolder.isDirectory()) {// 这里要是文件夹，保证属性文件不被删除
					File[] dataFiles = boxFolder.listFiles();
					for (int k = 0, length = dataFiles.length; k < length; k++) {
						dataFiles[k].delete();
					}
				}
			}
		}
	}

	// 删除指定的文件
	public static void deleteFile(File mailFile) {
		if (mailFile.exists()) {
			mailFile.delete();
		}
	}

}
