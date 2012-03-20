package yingermailclient.action;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate;

import yingermailclient.Activator;
import yingermailclient.model.AccountInfo;
import yingermailclient.model.Mail;
import yingermailclient.navigatorEntity.NavigatorEntityElement;
import yingermailclient.navigatorEntity.NavigatorEntityFactory;
import yingermailclient.util.MailUtil;

// 以下拉菜单形式展示的菜单
public class RecieveAction implements IWorkbenchWindowPulldownDelegate {

	public static final String ID = "yingermailclient.receiveMailAction";
	private IWorkbenchWindow window;

	public RecieveAction() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public Menu getMenu(Control parent) {
		// 创建菜单
		Menu menu = new Menu(parent);
		// 创建菜单项
		MenuItem item = new MenuItem(menu, SWT.NONE);
		item.setText("Receive All");
		item.setImage(Activator.getImageDescriptor("/icons/icos/Stats.ico").createImage());
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					receiveAllMail();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		// 根据用户的邮箱账户数目进行设置
		List accountList = NavigatorEntityFactory.getAccountList();
		for (int i = 0, size = accountList.size(); i < size; i++) {
			final AccountInfo accountInfo = ((NavigatorEntityElement) accountList.get(i)).getAccountInfo();
			MenuItem item1 = new MenuItem(menu, SWT.NONE);
			item1.setText("Receive " + accountInfo.getAccount());
			item1.setImage(Activator.getImageDescriptor("/icons/icos/Stats.ico").createImage());
			item1.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						receiveAccountMail(accountInfo, true);//显示提示信息
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
		}
		return menu;
	}

	protected void receiveAccountMail(AccountInfo accountInfo, boolean flag) throws Exception {
		accountInfo.setReset(true);// 收邮件前要设置为true
		List<Mail> mails = MailUtil.receiveAccountMail(accountInfo);// 收取邮件，得到邮件列表
		if (mails != null && flag) {
			MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Receive Mail", "Received " + mails.size() + " Mails total !");// 提示总共收到的邮件数目
		}
	}

	protected void receiveAllMail() throws Exception {
		Job job = new Job("Receive All Mails") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				List accountList = NavigatorEntityFactory.getAccountList();
				monitor.beginTask("Receiving mails.Please waiting...", accountList.size());
				for (int i = 0, size = accountList.size(); i < size; i++) {
					if (monitor.isCanceled()) {//如果取消了就停止
						break;
					}
					AccountInfo accountInfo = ((NavigatorEntityElement) accountList.get(i)).getAccountInfo();
					monitor.subTask("Receiving mail for " + accountInfo.getAccount());
					accountInfo.setReset(true);// 收邮件前要设置为true
					try {
						receiveAccountMail(accountInfo, false);
					} catch (Exception e) {
						e.printStackTrace();
						return Status.CANCEL_STATUS;
					}//不显示提示信息
					monitor.worked(1);
				}
				monitor.done();
				return Status.OK_STATUS;
			}
		};
		job.setPriority(Job.SHORT);
		job.setUser(true);// 只有设置成了用户级别才可以看到有进度条的dialog
		job.schedule();
	}

	@Override
	public void run(IAction action) {
		try {
			receiveAllMail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void dispose() {

	}

}
