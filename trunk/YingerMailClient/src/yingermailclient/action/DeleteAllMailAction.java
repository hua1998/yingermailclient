package yingermailclient.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import yingermailclient.util.FileUtil;

// 以下拉菜单形式展示的菜单
public class DeleteAllMailAction implements IWorkbenchWindowActionDelegate {

	public static final String ID = "yingermailclient.deleteAllMailAction";
	private IWorkbenchWindow window;

	public DeleteAllMailAction() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public void run(IAction action) {
		FileUtil.deleteAllDataFiles();
		MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information", "Delete successfully !");
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void dispose() {

	}

}
