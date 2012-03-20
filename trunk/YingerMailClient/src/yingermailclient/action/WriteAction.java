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

// 以下拉菜单形式展示的菜单
public class WriteAction implements IWorkbenchWindowActionDelegate {

	public static final String ID = "yingermailclient.writeMailAction";
	private IWorkbenchWindow window;

	public WriteAction() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	// TODO：这里发送邮件如果时间很长的话，应该是有进度条的！
	@Override
	public void run(IAction action) {
		Object object = new WriteMailDialog(Display.getDefault().getActiveShell(), SWT.SHELL_TRIM).open();
		if (object == null || !(object instanceof Mail)) {//如果返回值为null或者不是mail
			return;
		}
		// 写信窗体返回一个mail对象
		Mail mail = (Mail) object;
		// 根据accountinfo和mail发送邮件
		boolean flag = MailUtil.sendMail(mail);
		// 提示信息
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
