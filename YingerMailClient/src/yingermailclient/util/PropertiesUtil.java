package yingermailclient.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import yingermailclient.model.AccountInfo;

/**
 * ���Թ�����
 */
// TODO:�ܶ�IO�����в����쳣��û�д���
public class PropertiesUtil {

	// ���������ļ��õ���Ӧ��properties
	public static Properties getProperties(File propertyFile) {
		Properties prop = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(propertyFile);
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}

	// ���������ļ��Ķ���������AccountInfo����
	public static AccountInfo createAccountInfo(File propertyFile) {
		Properties props = getProperties(propertyFile);
		return new AccountInfo(props.getProperty("account"), props.getProperty("password"), props.getProperty("smtpHost"), Integer.valueOf(props
				.getProperty("smtpPort")), props.getProperty("pop3Host"), Integer.valueOf(props.getProperty("pop3Port")));
	}

	// ����һ��AccountInfo���� ����������д���ļ���
	public static void saveAccountInfo(AccountInfo accountInfo) {
		FileOutputStream fos = null;
		try {
			// datas\accountname\mail.properties
			Properties prop = new Properties();
			File propFile = new File(FileUtil.DATA_FOLDER + accountInfo.getAccount() + File.separator + FileUtil.CONFIG_FILE);
			if (propFile.exists()) {
				prop = getProperties(propFile);//�����޸�������Ϣ��ʱ��
			} else {
				propFile.createNewFile();// �����½�������Ϣ��ʱ��
			}
			convertAccountInfoToProperties(prop, accountInfo);
			fos = new FileOutputStream(propFile);
			prop.store(fos, "These are AccountInformations for " + accountInfo.getAccount());// �ڶ���������ע����Ϣ
			// ���ļ�ͷ���� # ע�͵�
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// ��һ���˻���Ϣ���浽������
	public static Properties convertAccountInfoToProperties(Properties prop, AccountInfo accountInfo) {
		prop.setProperty("account", accountInfo.getAccount());
		prop.setProperty("password", accountInfo.getPassword());
		prop.setProperty("smtpHost", accountInfo.getSmtpHost());
		prop.setProperty("smtpPort", String.valueOf(accountInfo.getSmtpPort()));
		prop.setProperty("pop3Host", accountInfo.getPop3Host());
		prop.setProperty("pop3Port", String.valueOf(accountInfo.getPop3Port()));
		return prop;
	}

}
