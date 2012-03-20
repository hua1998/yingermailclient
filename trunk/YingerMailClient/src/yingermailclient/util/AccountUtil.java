package yingermailclient.util;

import java.io.File;

import yingermailclient.model.AccountInfo;
import yingermailclient.navigatorEntity.NavigatorEntityElement;
import yingermailclient.navigatorEntity.NavigatorEntityFactory;

public class AccountUtil {

	// ����µ��˻�
	public static void addNewAccount(AccountInfo accountInfo) {
		NavigatorEntityFactory.createNewAccount(accountInfo);// �½��˻�
		FileUtil.createMailFolder(accountInfo);// Ϊ�˻������ļ�Ŀ¼
		PropertiesUtil.saveAccountInfo(accountInfo);// �����˻���������Ϣ
	}

	// �����˻���Ϣɾ���˻�
	public static boolean deleteAccount(AccountInfo accountInfo) {
		File accountFolder = new File(FileUtil.getAccountRoot(accountInfo));//datas/accountname/
		File[] boxFolders = accountFolder.listFiles();
		for (int j = 0; j < boxFolders.length; j++) {
			File boxFolder = boxFolders[j];//datas/account/box
			if (boxFolder.isDirectory()) {// ����Ҫ���ļ��У���֤�����ļ�����ɾ��
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

	// ����ָ�����˻�����֤ǰ�����˻�����û�б��
	public static void updateAccount(NavigatorEntityElement element, AccountInfo accountInfo) {
		// ��ʼ�뷨������ɾ��ԭ���������ļ�, �������´���һ�������ļ�
		// �Ľ��ķ��������ȵõ�ԭ���������ļ���Ϣ��Ȼ������޸ļ���
		PropertiesUtil.saveAccountInfo(accountInfo);// �����˻���������Ϣ
		// �������޸�������Ϣ,����Ҫ�޸����ڵ��accountinfo����
		NavigatorEntityFactory.updateSubElements(element, accountInfo);
	}

}
