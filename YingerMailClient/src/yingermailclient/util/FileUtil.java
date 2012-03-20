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
 * �ļ�������
 */
public class FileUtil {

	// ��������û����ݵ�Ŀ¼
	public static final String DATA_FOLDER = "D:\\yinger\\My DBank\\workspace_rcp\\rcp\\YingerMailClient\\datas" + File.separator;
	// ��ž���ĳ���˻����õ�properties�ļ�
	public static final String CONFIG_FILE = "mail.properties";
	// �ռ����Ŀ¼��
	public static final String INBOX = "Inbox";
	// �������Ŀ¼��
	public static final String OUTBOX = "Outbox";
	// �ѷ��͵�Ŀ¼��
	public static final String SENTBOX = "Sentbox";
	// �ݸ����Ŀ¼��
	public static final String DRAFTBOX = "Draftbox";
	// �������Ŀ¼��
	public static final String DELETEDBOX = "Deletedbox";
	// �����Ĵ��Ŀ¼��
	public static final String Attachment = "Attachment";
	// ����XStream����
	private static XStream xstream = new XStream(new DomDriver());

	// �����û����ʺ�Ŀ¼����ص���Ŀ¼
	public static void createMailFolder(AccountInfo accountInfo) {
		String accountRoot = getAccountRoot(accountInfo);
		// ʹ���û���ǰ���õ��ʺ�������Ŀ¼
		mkdir(new File(accountRoot));
		// ����INBOXĿ¼
		mkdir(new File(accountRoot + INBOX));
		// ������
		mkdir(new File(accountRoot + OUTBOX));
		// �ѷ���
		mkdir(new File(accountRoot + SENTBOX));
		// �ݸ���
		mkdir(new File(accountRoot + DRAFTBOX));
		// ������
		mkdir(new File(accountRoot + DELETEDBOX));
		// �������Ŀ¼
		mkdir(new File(accountRoot + Attachment));
	}

	// �õ��ʼ��ʺŵĸ�Ŀ¼�������ǰ����� / ��
	public static String getAccountRoot(AccountInfo accountInfo) {
		String accountRoot = DATA_FOLDER + accountInfo.getAccount() + File.separator;
		return accountRoot;
	}

	// �õ�ĳ��Ŀ¼����, ����inbox��Ŀ¼�������ǰ����� / ��
	public static String getFolderPath(AccountInfo accountInfo, String folderName) {
		return getAccountRoot(accountInfo) + folderName + File.separator;
	}

	// Ϊ�������������ļ�, Ŀ¼���˻�����Ŀ¼
	public static Attachment createFileFromPart(AccountInfo accountInfo, Part part) {
		try {
			// �õ��ļ���ŵ�Ŀ¼
			String fileRepository = getFolderPath(accountInfo, Attachment);
			// �õ�������ԭʼ����
			String serverFileName = MimeUtility.decodeText(part.getFileName());
			// ����UUID��Ϊ�ڱ���ϵͳ��Ψһ���ļ���ʶ
			String fileName = UUID.randomUUID().toString();
			// ����һ���µ��ļ�����ͬ�����ͣ�ֻ���ļ�����Ψһ�������
			File file = new File(fileRepository + fileName + getFileSuffix(serverFileName));
			// ��д�ļ�
			InputStream is = part.getInputStream();
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream outs = new BufferedOutputStream(fos);
			// �����������Ϊ��part.getSizeΪ-1, ���ֱ��new byte, ���׳��쳣
			int size = (part.getSize() > 0) ? part.getSize() : 0;
			byte[] b = new byte[size];
			is.read(b);
			outs.write(b);
			outs.close();
			is.close();
			fos.close();
			// ��װ����file�����ص����ص��ļ�
			Attachment attachment = new Attachment(serverFileName, file);
			return attachment;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ����Ӧ��folder�еõ�ȫ����xml�ļ�
	public static List<File> getXMLFiles(AccountInfo accountInfo, String folderName) {
		// �õ��ļ��е�·��
		String folderPath = getAccountRoot(accountInfo) + folderName;
		// �õ��ļ���
		File folder = new File(folderPath);
		// ���ļ��н��к�׺����
		List<File> files = filterFiles(folder, ".xml");
		return files;
	}

	// ��һ���ļ�Ŀ¼��, �Բ����ļ���׺subffixΪ����, �����ļ�
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

	// �õ��ļ����ĺ�׺������ֵ��ǰ���� .
	public static String getFileSuffix(String fileName) {
		if (fileName == null || fileName.trim().equals(""))
			return "";
		if (fileName.lastIndexOf(".") != -1) {
			return fileName.substring(fileName.lastIndexOf("."));
		}
		return "";
	}

	// ��һ��mail����д��xmlFile��
	public static boolean convertMailToXML(AccountInfo accountInfo, Mail mail, String folderName) {
		// ����UUID���ļ���
		String xmlName = UUID.randomUUID().toString() + ".xml";
		mail.setXmlName(xmlName);
		// �õ���Ӧ��Ŀ¼·��
		String folder = getAccountRoot(accountInfo) + folderName + File.separator;
		File xmlFile = new File(folder + xmlName);
		// ʹ��xstreamд�뵽xml�ļ���
		try {
			writeToXML(xmlFile, mail);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// ��һ���ʼ�����ʹ��XStreamд��xml�ļ���
	public static void writeToXML(File xmlFile, Mail mail) throws Exception {
		FileOutputStream fos = null;
		OutputStreamWriter writer = null;
		if (!xmlFile.exists()) {
			xmlFile.createNewFile();
		}
		fos = new FileOutputStream(xmlFile);
		writer = new OutputStreamWriter(fos, "UTF8");//��UTF8�ı�����ʽд�뵽�ļ���
		xstream.toXML(mail, writer);//toXML����
		writer.close();
		fos.close();
	}

	// ��һ��xml�ĵ�ת����Mail����
	public static Mail convertXMLToMail(AccountInfo accountInfo, File xmlFile) {
		FileInputStream fis = null;
		Mail mail = null;
		try {
			fis = new FileInputStream(xmlFile);
			// ����XStream��ת���������ļ�ת���ɶ���
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

	// ����Ŀ¼�Ĺ��߷���, �ж�Ŀ¼�Ƿ����
	public static void mkdir(File file) {
		if (!file.exists()) {
			file.mkdir();
		}
	}

	// �õ����е����������ļ�
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

	// ɾ���ʼ������ǳ���ɾ����ֻ�ǽ��ʼ����浽��ɾ�����ʼ�����
	public static void deleteMailFile(AccountInfo accountInfo, Mail mail) {
		// �����е㸴���ˣ����˻��������ʼ��ķ����ߣ�Ҳ�п����ǽ�����
		// ����ͨ�����һ���������˻���Ϣ����ô�Ϳ����ˣ��ҵ���Ӧ�˻���Ŀ¼������
		// �������ĸ��ļ��о�����mail�����������Ҳ��ı���ԭ�����ļ���λ��
		File xmlFile = new File(getFolderPath(accountInfo, mail.getFolder()) + mail.getXmlName());
		if (!xmlFile.exists()) {
			return;
		}
		// ��mail���浽��ɾ�����ļ�����
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

	// �ļ����Ʋ����������ļ������ݣ������ǲ�ͬ��
	public static void copyFile(File xmlFile, File newXMLFile) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(xmlFile);
			fos = new FileOutputStream(newXMLFile);
			byte[] bytes = new byte[1024];
			int len = fis.read(bytes);
			while (len != -1) {//eof ����ֵ��-1��������0
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

	// ����ɾ���ʼ�
	public static void completeDeleteMailFile(AccountInfo accountInfo, Mail mail) {
		// ����Ҫɾ���ʼ��ĸ���
		List<Attachment> attachments = mail.getFiles();
		// �������ڻ�Ҫȥɾ��
		if (attachments != null && attachments.size() > 0) {
			for (int i = 0, size = attachments.size(); i < size; i++) {
				File file = attachments.get(i).getFile();
				if (file.exists()) {
					file.delete();
				}
			}
		}
		// ����Ҫɾ���ʼ���xml�ļ�
		File xmlFile = new File(getFolderPath(accountInfo, FileUtil.DELETEDBOX) + mail.getXmlName());
		if (xmlFile.exists()) {
			xmlFile.delete();
		}
	}

	// ��ָ�����ļ�����һ�ݣ����浽�˻��ĸ����ļ�����,mail��һ��Ҫ���͵��ʼ�������mail�ķ�����һ�������е��˻�
	public static Attachment createAttachmentFromFile(Mail mail, File file) {
		// ����UUID���ļ���
		String fileName = UUID.randomUUID().toString() + getFileSuffix(file.getName());
		// �õ����ļ�
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

	// �õ����ص�ָ���˻�����Ӧ�ļ����е������ʼ�
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

	// ɾ�����е������ļ�
	public static void deleteAllDataFiles() {
		File file = new File(FileUtil.DATA_FOLDER);//datas
		File[] accountFolders = file.listFiles();
		for (int i = 0; i < accountFolders.length; i++) {
			File accountFolder = accountFolders[i];//datas/account
			File[] boxFolders = accountFolder.listFiles();
			for (int j = 0; j < boxFolders.length; j++) {
				File boxFolder = boxFolders[j];//datas/account/box
				if (boxFolder.isDirectory()) {// ����Ҫ���ļ��У���֤�����ļ�����ɾ��
					File[] dataFiles = boxFolder.listFiles();
					for (int k = 0, length = dataFiles.length; k < length; k++) {
						dataFiles[k].delete();
					}
				}
			}
		}
	}

	// ɾ��ָ�����ļ�
	public static void deleteFile(File mailFile) {
		if (mailFile.exists()) {
			mailFile.delete();
		}
	}

}
