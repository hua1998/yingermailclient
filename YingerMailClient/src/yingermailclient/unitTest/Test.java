package yingermailclient.unitTest;

import java.io.File;

import yingermailclient.util.FileUtil;

public class Test {

	public static void main(String[] args) {
//		deleteAllDataFiles();
		deleteAllInboxFiles();
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

	// 删除所有的数据文件
	public static void deleteAllInboxFiles() {
		File file = new File(FileUtil.DATA_FOLDER);//datas
		File[] accountFolders = file.listFiles();
		for (int i = 0; i < accountFolders.length; i++) {
			File accountFolder = accountFolders[i];//datas/account
			File[] boxFolders = accountFolder.listFiles();
			for (int j = 0; j < boxFolders.length; j++) {
				File boxFolder = boxFolders[j];//datas/account/box
				if (boxFolder.isDirectory() && boxFolder.getName().equals(FileUtil.INBOX)) {// 这里要是文件夹，保证属性文件不被删除
					File[] dataFiles = boxFolder.listFiles();
					for (int k = 0, length = dataFiles.length; k < length; k++) {
						dataFiles[k].delete();
					}
				}
			}
		}
	}

}
