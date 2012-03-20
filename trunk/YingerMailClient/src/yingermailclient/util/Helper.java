package yingermailclient.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import yingermailclient.model.AccountInfo;
import yingermailclient.navigatorEntity.NavigatorEntityFactory;

public class Helper {

	// ��ʼ������
	public static void initData() {
		// List accountList = new ArrayList();
		// �õ�data��Ŀ¼�µ������˻��������ļ�
		List<File> propertyFiles = FileUtil.getAllPropertyFiles();
		// �������ļ��õ�AccountInfo����
		File propertyFile = null;
		for (int i = 0, size = propertyFiles.size(); i < size; i++) {
			propertyFile = propertyFiles.get(i);
			// ��һ�������ļ��еõ��˻���Ϣ
			AccountInfo accountInfo = PropertiesUtil.createAccountInfo(propertyFile);
			// accountList.add(accountInfo);
			NavigatorEntityFactory.createNewAccount(accountInfo);
			// ͨ��accountList����treeviewer�����������ڵ����Ϣ������accountinfo
		}
		// return accountList;
	}

	// �����ַ�������Դ�ַ�����ָ�����ַ����ָ�
	public static List<String> filterString(String string, String filter) {
		if (string == null || filter == null) {
			return null;
		}
		List<String> strings = new ArrayList<String>();
		int fromIndex = 0;
		int index = string.indexOf(filter, fromIndex);
		while (index != -1) {
			strings.add(string.substring(fromIndex, index));
			fromIndex = index + 1;
			index = string.indexOf(filter, fromIndex);
		}
		if (string.length() > fromIndex) {
			strings.add(string.substring(fromIndex));
		}
		return strings;
	}

	public static void main(String[] args) {
		String string = "aaaa;bbbb;cccc";
		String filter = ";";
		System.out.println(string.indexOf(filter));// ��0��ʼ
		System.out.println(string.substring(0, 4));// ��0��ʼ����3��������4
		List<String> strings = filterString(string, filter);
		for (String string2 : strings) {
			System.out.println(string2);
		}
	}
}
