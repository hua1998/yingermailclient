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

// �������˵���ʽչʾ�Ĳ˵�
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
		// �����˵�
		Menu menu = new Menu(parent);
		// �����˵���
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

		// �����û��������˻���Ŀ��������
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
						receiveAccountMail(accountInfo, true);//��ʾ��ʾ��Ϣ
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
		}
		return menu;
	}

	protected void receiveAccountMail(AccountInfo accountInfo, boolean flag) throws Exception {
		accountInfo.setReset(true);// ���ʼ�ǰҪ����Ϊtrue
		List<Mail> mails = MailUtil.receiveAccountMail(accountInfo);// ��ȡ�ʼ����õ��ʼ��б�
		if (mails != null && flag) {
			MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Receive Mail", "Received " + mails.size() + " Mails total !");// ��ʾ�ܹ��յ����ʼ���Ŀ
		}
	}

	protected void receiveAllMail() throws Exception {
		Job job = new Job("Receive All Mails") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				List accountList = NavigatorEntityFactory.getAccountList();
				monitor.beginTask("Receiving mails.Please waiting...", accountList.size());
				for (int i = 0, size = accountList.size(); i < size; i++) {
					if (monitor.isCanceled()) {//���ȡ���˾�ֹͣ
						break;
					}
					AccountInfo accountInfo = ((NavigatorEntityElement) accountList.get(i)).getAccountInfo();
					monitor.subTask("Receiving mail for " + accountInfo.getAccount());
					accountInfo.setReset(true);// ���ʼ�ǰҪ����Ϊtrue
					try {
						receiveAccountMail(accountInfo, false);
					} catch (Exception e) {
						e.printStackTrace();
						return Status.CANCEL_STATUS;
					}//����ʾ��ʾ��Ϣ
					monitor.worked(1);
				}
				monitor.done();
				return Status.OK_STATUS;
			}
		};
		job.setPriority(Job.SHORT);
		job.setUser(true);// ֻ�����ó����û�����ſ��Կ����н�������dialog
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
