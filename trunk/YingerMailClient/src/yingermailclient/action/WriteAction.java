package yingermailclient.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import yingermailclient.model.Mail;
import yingermailclient.ui.WriteMailDialog;
import yingermailclient.util.MailUtil;

// �������˵���ʽչʾ�Ĳ˵�
public class WriteAction implements IWorkbenchWindowActionDelegate {

	public static final String ID = "yingermailclient.writeMailAction";
	private IWorkbenchWindow window;

	public WriteAction() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	// TODO�����﷢���ʼ����ʱ��ܳ��Ļ���Ӧ�����н������ģ�
	@Override
	public void run(IAction action) {
		Object object = new WriteMailDialog(Display.getDefault().getActiveShell(), SWT.SHELL_TRIM).open();
		if (object == null || !(object instanceof Mail)) {//�������ֵΪnull���߲���mail
			return;
		}
		// д�Ŵ��巵��һ��mail����
		Mail mail = (Mail) object;
		// ����accountinfo��mail�����ʼ�
		boolean flag = MailUtil.sendMail(mail);
		// ��ʾ��Ϣ
		if (!flag) {
			MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Send Information", "Send mail failed !");
		} else {
			MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Send Information", "Mail successfully sent !");
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void dispose() {

	}

}
