package yingermailclient.editorInput;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import yingermailclient.model.AccountInfo;
import yingermailclient.model.Mail;

public class MailListEditorInput implements IEditorInput {

	private String name = "";// name貌似是无法修改的！
	private AccountInfo accountInfo = null;
	private List<Mail> mails = new ArrayList<Mail>();// 由input将数据设置到editor中

	public MailListEditorInput(String name, AccountInfo accountInfo) {
		this.name = name;
		this.accountInfo = accountInfo;
	}

	public List<Mail> getMails() {
		return mails;
	}

	public void setMails(List<Mail> mails) {
		this.mails = mails;
	}

	public AccountInfo getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return name;
	}

	@Override
	// 注意重写equals方法，用于判断什么情况下两个editor是一样的！
	// 只有当两个editor的账户和邮箱文件夹都是相同的时候才是相同的
	public boolean equals(Object obj) {
		if (obj instanceof MailListEditorInput) {
			MailListEditorInput input = (MailListEditorInput) obj;
			if (input.getName().equals(this.getName()) && input.getAccountInfo() == this.accountInfo) {
				return true;
			}
		}
		return false;
	}

}
