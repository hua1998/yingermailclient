package yingermailclient.editorInput;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import yingermailclient.model.AccountInfo;
import yingermailclient.model.Mail;

public class MailListEditorInput implements IEditorInput {

	private String name = "";// nameò�����޷��޸ĵģ�
	private AccountInfo accountInfo = null;
	private List<Mail> mails = new ArrayList<Mail>();// ��input���������õ�editor��

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
	// ע����дequals�����������ж�ʲô���������editor��һ���ģ�
	// ֻ�е�����editor���˻��������ļ��ж�����ͬ��ʱ�������ͬ��
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
