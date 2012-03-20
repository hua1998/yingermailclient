package yingermailclient.util;

import java.io.File;

import yingermailclient.model.AccountInfo;
import yingermailclient.navigatorEntity.NavigatorEntityElement;
import yingermailclient.navigatorEntity.NavigatorEntityFactory;

public class AccountUtil {

	// 添加新的账户
	public static void addNewAccount(AccountInfo accountInfo) {
		NavigatorEntityFactory.createNewAccount(accountInfo);// 新建账户
		FileUtil.createMailFolder(accountInfo);// 为账户创建文件目录
		PropertiesUtil.saveAccountInfo(accountInfo);// 保存账户的配置信息
	}

	// 根据账户信息删除账户
	public static boolean deleteAccount(AccountInfo accountInfo) {
		File accountFolder = new File(FileUtil.getAccountRoot(accountInfo));//datas/accountname/
		File[] boxFolders = accountFolder.listFiles();
		for (int j = 0; j < boxFolders.length; j++) {
			File boxFolder = boxFolders[j];//datas/account/box
			if (boxFolder.isDirectory()) {// 这里要是文件夹，保证属性文件不被删除
				File[] dataFiles = boxFolder.listFiles();
				for (int k = 0, length = dataFiles.length; k < length; k++) {
					dataFiles[k].delete();
				}
			}else{
				boxFolder.delete();
			}
		}
		return true;
	}

	// 更新指定的账户，保证前提是账户名是没有变的
	public static void updateAccount(NavigatorEntityElement element, AccountInfo accountInfo) {
		// 初始想法：首先删除原来的配置文件, 接着重新创建一个配置文件
		// 改进的方法：首先得到原来的配置文件信息，然后进行修改即可
		PropertiesUtil.saveAccountInfo(accountInfo);// 保存账户的配置信息
		// 首先是修改配置信息,接着要修改树节点的accountinfo属性
		NavigatorEntityFactory.updateSubElements(element, accountInfo);
	}

}
