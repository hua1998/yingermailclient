package yingermailclient.navigatorEntity;

import java.util.ArrayList;
import java.util.List;

import yingermailclient.editorInput.MailListEditorInput;
import yingermailclient.model.AccountInfo;
import yingermailclient.util.FileUtil;

public class NavigatorEntityFactory {

	// �����������ò�Ƹĳ���hashmap��ȽϺ�
	private static List accountList = new ArrayList();

	// ����һ���µ��˻�
	public static void createNewAccount(AccountInfo accountInfo) {
		NavigatorEntityElement root = new NavigatorEntityElement();
		root.setName(accountInfo.getAccount());
		root.setAccountInfo(accountInfo);
		createSubElements(root, accountInfo);// Ϊtreeviewer�������ӽڵ�
		accountList.add(root);
	}

	// �������ڵ�ĺ��ӽڵ�
	public static void createSubElements(NavigatorEntityElement root, AccountInfo accountInfo) {
		// �������ڵ�ĺ��ӽڵ�
		NavigatorEntityElement element1 = new NavigatorEntityElement(FileUtil.INBOX);
		NavigatorEntityElement element2 = new NavigatorEntityElement(FileUtil.OUTBOX);
		NavigatorEntityElement element3 = new NavigatorEntityElement(FileUtil.DRAFTBOX);
		NavigatorEntityElement element4 = new NavigatorEntityElement(FileUtil.SENTBOX);
		NavigatorEntityElement element5 = new NavigatorEntityElement(FileUtil.DELETEDBOX);

		// �����˻���Ϣ
		element1.setAccountInfo(accountInfo);
		element2.setAccountInfo(accountInfo);
		element3.setAccountInfo(accountInfo);
		element4.setAccountInfo(accountInfo);
		element5.setAccountInfo(accountInfo);

		// ���ñ༭��
		element1.setEditorInput(new MailListEditorInput(element1.getName(), accountInfo));
		element2.setEditorInput(new MailListEditorInput(element2.getName(), accountInfo));
		element3.setEditorInput(new MailListEditorInput(element3.getName(), accountInfo));
		element4.setEditorInput(new MailListEditorInput(element4.getName(), accountInfo));
		element5.setEditorInput(new MailListEditorInput(element5.getName(), accountInfo));

		// ���ø��׽ڵ�
		element1.setParent(root);
		element2.setParent(root);
		element3.setParent(root);
		element4.setParent(root);
		element5.setParent(root);

		// ��Ӻ��ӽڵ�
		root.addChild(element1);
		root.addChild(element2);
		root.addChild(element3);
		root.addChild(element4);
		root.addChild(element5);
	}

	// �����˻���Ϣ֮��Ҫ���½ڵ��accountinfo����
	public static void updateSubElements(NavigatorEntityElement root, AccountInfo accountInfo) {
		if (!root.hasChildren()) {
			root = (NavigatorEntityElement) root.getParent();
		}
		// �������ǵ�accountinfo
		root.setAccountInfo(accountInfo);
		for (int i = 0, size = root.getChildren().size(); i < size; i++) {
			((NavigatorEntityElement) root.getChildren().get(i)).setAccountInfo(accountInfo);
		}
	}

	// �õ����е�accountinfo�б�
	public static List getAccountList() {
		return accountList;
	}

	// �õ��˻������Ƶ��ַ�������
	public static String[] getAccountString() {
		String[] strings = new String[accountList.size()];
		for (int i = 0, size = accountList.size(); i < size; i++) {
			strings[i] = ((NavigatorEntityElement) accountList.get(i)).getName();
		}
		return strings;
	}

}
