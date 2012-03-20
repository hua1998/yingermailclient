package yingermailclient.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import yingermailclient.model.AccountInfo;
import yingermailclient.navigatorEntity.NavigatorEntityFactory;

public class Helper {

	// 初始化数据
	public static void initData() {
		// List accountList = new ArrayList();
		// 得到data根目录下的所有账户的属性文件
		List<File> propertyFiles = FileUtil.getAllPropertyFiles();
		// 从属性文件得到AccountInfo对象
		File propertyFile = null;
		for (int i = 0, size = propertyFiles.size(); i < size; i++) {
			propertyFile = propertyFiles.get(i);
			// 从一个配置文件中得到账户信息
			AccountInfo accountInfo = PropertiesUtil.createAccountInfo(propertyFile);
			// accountList.add(accountInfo);
			NavigatorEntityFactory.createNewAccount(accountInfo);
			// 通过accountList建立treeviewer，并设置树节点的信息，包括accountinfo
		}
		// return accountList;
	}

	// 处理字符串，将源字符串按指定的字符串分割
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
		System.out.println(string.indexOf(filter));// 从0开始
		System.out.println(string.substring(0, 4));// 从0开始，到3，不包括4
		List<String> strings = filterString(string, filter);
		for (String string2 : strings) {
			System.out.println(string2);
		}
	}
}
