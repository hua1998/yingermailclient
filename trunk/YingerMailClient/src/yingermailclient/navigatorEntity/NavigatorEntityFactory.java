package yingermailclient.navigatorEntity;

import java.util.ArrayList;
import java.util.List;

import yingermailclient.editorInput.MailListEditorInput;
import yingermailclient.model.AccountInfo;
import yingermailclient.util.FileUtil;

public class NavigatorEntityFactory {

	// 最后想想这里貌似改成是hashmap会比较好
	private static List accountList = new ArrayList();

	// 创建一个新的账户
	public static void createNewAccount(AccountInfo accountInfo) {
		NavigatorEntityElement root = new NavigatorEntityElement();
		root.setName(accountInfo.getAccount());
		root.setAccountInfo(accountInfo);
		createSubElements(root, accountInfo);// 为treeviewer创建孩子节点
		accountList.add(root);
	}

	// 创建根节点的孩子节点
	public static void createSubElements(NavigatorEntityElement root, AccountInfo accountInfo) {
		// 创建根节点的孩子节点
		NavigatorEntityElement element1 = new NavigatorEntityElement(FileUtil.INBOX);
		NavigatorEntityElement element2 = new NavigatorEntityElement(FileUtil.OUTBOX);
		NavigatorEntityElement element3 = new NavigatorEntityElement(FileUtil.DRAFTBOX);
		NavigatorEntityElement element4 = new NavigatorEntityElement(FileUtil.SENTBOX);
		NavigatorEntityElement element5 = new NavigatorEntityElement(FileUtil.DELETEDBOX);

		// 设置账户信息
		element1.setAccountInfo(accountInfo);
		element2.setAccountInfo(accountInfo);
		element3.setAccountInfo(accountInfo);
		element4.setAccountInfo(accountInfo);
		element5.setAccountInfo(accountInfo);

		// 设置编辑器
		element1.setEditorInput(new MailListEditorInput(element1.getName(), accountInfo));
		element2.setEditorInput(new MailListEditorInput(element2.getName(), accountInfo));
		element3.setEditorInput(new MailListEditorInput(element3.getName(), accountInfo));
		element4.setEditorInput(new MailListEditorInput(element4.getName(), accountInfo));
		element5.setEditorInput(new MailListEditorInput(element5.getName(), accountInfo));

		// 设置父亲节点
		element1.setParent(root);
		element2.setParent(root);
		element3.setParent(root);
		element4.setParent(root);
		element5.setParent(root);

		// 添加孩子节点
		root.addChild(element1);
		root.addChild(element2);
		root.addChild(element3);
		root.addChild(element4);
		root.addChild(element5);
	}

	// 更新账户信息之后要更新节点的accountinfo属性
	public static void updateSubElements(NavigatorEntityElement root, AccountInfo accountInfo) {
		if (!root.hasChildren()) {
			root = (NavigatorEntityElement) root.getParent();
		}
		// 重置他们的accountinfo
		root.setAccountInfo(accountInfo);
		for (int i = 0, size = root.getChildren().size(); i < size; i++) {
			((NavigatorEntityElement) root.getChildren().get(i)).setAccountInfo(accountInfo);
		}
	}

	// 得到所有的accountinfo列表
	public static List getAccountList() {
		return accountList;
	}

	// 得到账户的名称的字符串数组
	public static String[] getAccountString() {
		String[] strings = new String[accountList.size()];
		for (int i = 0, size = accountList.size(); i < size; i++) {
			strings[i] = ((NavigatorEntityElement) accountList.get(i)).getName();
		}
		return strings;
	}

}
