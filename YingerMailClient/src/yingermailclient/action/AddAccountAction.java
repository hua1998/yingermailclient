package yingermailclient.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import yingermailclient.model.AccountInfo;
import yingermailclient.ui.AccountDialog;
import yingermailclient.util.AccountUtil;
import yingermailclient.view.NavigatorView;

// ע�⣬�������ʱ��û�в������Ӽ�������Ƿ���ȷ��
public class AddAccountAction implements IWorkbenchWindowActionDelegate {

	public static final String ID = "yingermailclient.addAccountAction";
	private IWorkbenchWindow window;

	public AddAccountAction() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public void run(IAction action) {
		// MessageDialog.openInformation(Display.getDefault().getActiveShell(),
		// "Add Account", "Add Account");
		AccountDialog dialog = new AccountDialog(Display.getDefault().getActiveShell(), SWT.SHELL_TRIM);
		dialog.setText("Add New Account");
		Object result = dialog.open();
		// �����һ���µ�accountInfo��Ҫˢ�µ�����ͼ����ϲ�������ʼ��������˵��Զ��ĸ����ˣ�
		if (result instanceof AccountInfo) {
			AccountInfo accountInfo = (AccountInfo) result;
			AccountUtil.addNewAccount(accountInfo);// �ɹ�������ɸ������
			// �Խ�����и��£�treeviewerҪ���£����������˵����ã���Ӧ�����Զ��Ķ�ʱˢ�£�
			NavigatorView view = (NavigatorView) window.getActivePage().findView(NavigatorView.ID);
			view.refreshAccounts();
		} else {
//			MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information", "Add Account failed !");
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void dispose() {

	}

}
