package yingermailclient.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import yingermailclient.model.AccountInfo;

/**
 * 属性工具类
 */
// TODO:很多IO操作有不少异常我没有处理
public class PropertiesUtil {

	// 根据属性文件得到对应的properties
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

	// 根据配置文件的对象来构造AccountInfo对象
	public static AccountInfo createAccountInfo(File propertyFile) {
		Properties props = getProperties(propertyFile);
		return new AccountInfo(props.getProperty("account"), props.getProperty("password"), props.getProperty("smtpHost"), Integer.valueOf(props
				.getProperty("smtpPort")), props.getProperty("pop3Host"), Integer.valueOf(props.getProperty("pop3Port")));
	}

	// 保存一个AccountInfo对象， 将它的属性写入文件中
	public static void saveAccountInfo(AccountInfo accountInfo) {
		FileOutputStream fos = null;
		try {
			// datas\accountname\mail.properties
			Properties prop = new Properties();
			File propFile = new File(FileUtil.DATA_FOLDER + accountInfo.getAccount() + File.separator + FileUtil.CONFIG_FILE);
			if (propFile.exists()) {
				prop = getProperties(propFile);//用于修改配置信息的时候
			} else {
				propFile.createNewFile();// 用于新建配置信息的时候
			}
			convertAccountInfoToProperties(prop, accountInfo);
			fos = new FileOutputStream(propFile);
			prop.store(fos, "These are AccountInformations for " + accountInfo.getAccount());// 第二个参数是注释信息
			// 在文件头是用 # 注释的
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
	
	// 将一个账户信息保存到属性中
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
