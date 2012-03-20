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

// 注意，这里添加时并没有测试连接检查配置是否正确！
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
		// 添加了一个新的accountInfo就要刷新导航视图，惊喜的是收邮件的下拉菜单自动的更新了！
		if (result instanceof AccountInfo) {
			AccountInfo accountInfo = (AccountInfo) result;
			AccountUtil.addNewAccount(accountInfo);// 由工具类完成各项操作
			// 对界面进行更新，treeviewer要更新，但是下拉菜单不用（它应该是自动的定时刷新）
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
