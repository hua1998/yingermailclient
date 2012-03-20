package yingermailclient.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;

import yingermailclient.Activator;
import yingermailclient.editor.MailListEditor;
import yingermailclient.editorInput.MailListEditorInput;
import yingermailclient.model.AccountInfo;
import yingermailclient.navigatorEntity.NavigatorEntityElement;
import yingermailclient.navigatorEntity.NavigatorEntityFactory;
import yingermailclient.provider.NavigatorTreeViewerContentProvider;
import yingermailclient.provider.NavigatorTreeViewerLabelProvider;
import yingermailclient.ui.AccountDialog;
import yingermailclient.util.AccountUtil;
import yingermailclient.util.FileUtil;

public class NavigatorView extends ViewPart {

	public static final String ID = "yingermailclient.navigatorview";
	public TreeViewer treeViewer;

	public NavigatorView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		// ����������
		setViewToolBar();

		// ����treeviewer
		createTreeViewer(composite);
		// ����˫���¼����ڴ򿪱༭��
		hookDoubleClickAction();
	}

	public void setViewToolBar() {
		// IActionBars:Used by a part to access its menu, toolbar, and
		// status line managers.
		IActionBars bars = getViewSite().getActionBars();
		// ���幤����
		IToolBarManager toolBarManager = bars.getToolBarManager();
		toolBarManager.add(new DeleteAccountAction());
		toolBarManager.add(new UpdateAccountAction());

	}

	public void createTreeViewer(Composite composite) {
		treeViewer = new TreeViewer(composite, SWT.V_SCROLL | SWT.H_SCROLL);
		// ���������ṩ��ͱ�ǩ�ṩ��
		treeViewer.setContentProvider(new NavigatorTreeViewerContentProvider());
		treeViewer.setLabelProvider(new NavigatorTreeViewerLabelProvider());
		// ��������
		treeViewer.setInput(NavigatorEntityFactory.getAccountList());
	}

	// �������ʵ���Ͼ��Ǹ�treeviewer�����һ������˫���¼��ļ�����
	public void hookDoubleClickAction() {
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = treeViewer.getSelection();
				// �õ�ѡ�е��ע�ⷽ���ǽ��õ���ѡ��ת���� IStructuredSelection���ڵ���
				// getFirstElement ����
				Object object = ((IStructuredSelection) selection).getFirstElement();
				// �ٽ�����תΪʵ�ʵ����ڵ����
				NavigatorEntityElement element = (NavigatorEntityElement) object;
				// �õ��ö����editorInput
				IEditorInput editorInput = element.getEditorInput();
				// ����editor�е��ʼ��б�
				((MailListEditorInput) editorInput).setMails(FileUtil.getLocalMails(element.getAccountInfo(), element.getName()));
				// �õ���ǰ����̨��page
				IWorkbenchPage workbenchPage = getViewSite().getPage();//
				String editorID = MailListEditor.ID;
				// ����Ҫ���NavigatorEntityFactory���setNavigatorEntity����
				// IEditorPart:An editor is a visual component within a
				// workbench page.
				IEditorPart editorPart = workbenchPage.findEditor(editorInput);// null
				// �������Ҫ����������maillist��
				// MailUtil.setMails(FileUtil.getLocalMails(element.getAccountInfo(),
				// element.getName()));
				if (editorPart != null) {// �Ѿ���������ı༭��
					workbenchPage.bringToTop(editorPart);
				} else {// û�д򿪾ʹ���
					try {
						editorPart = workbenchPage.openEditor(editorInput, editorID);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	// ˢ���˻��б�
	public void refreshAccounts() {
		treeViewer.setInput(NavigatorEntityFactory.getAccountList());
		treeViewer.refresh();
	}

	@Override
	public void setFocus() {

	}

	class DeleteAccountAction extends Action {
		public DeleteAccountAction() {
			// ������ʾ�ı�
			this.setToolTipText("Delete Account");
			// ����ͼ��
			this.setImageDescriptor(Activator.getImageDescriptor("/icons/mail/delete_user.ico"));
		}

		@Override
		public void run() {
			Object object = ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
			if (object == null || !(object instanceof NavigatorEntityElement)) {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information", "Please select an account !");
			} else {
				NavigatorEntityElement element = (NavigatorEntityElement) object;
				AccountInfo accountInfo = element.getAccountInfo();//ÿ���ڵ㶼�������˻���Ϣ
				if (AccountUtil.deleteAccount(accountInfo)) {
					NavigatorEntityFactory.getAccountList().remove(treeViewer.getSelection());
					refreshAccounts();
					MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information", "Delete Account successfully !");
				} else {
					MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information", "Delete Account failed !");
				}
			}
		}
	}

	class UpdateAccountAction extends Action {
		public UpdateAccountAction() {
			// ������ʾ�ı�
			this.setToolTipText("Update Account");
			// ����ͼ��
			this.setImageDescriptor(Activator.getImageDescriptor("/icons/icos/Char.ico"));
		}

		@Override
		public void run() {
			Object object = ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
			if (object == null || !(object instanceof NavigatorEntityElement)) {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information", "Please select an account !");
			} else {
				NavigatorEntityElement element = (NavigatorEntityElement) object;
				AccountInfo accountInfo = element.getAccountInfo();//ÿ���ڵ㶼�������˻���Ϣ
				AccountDialog dialog = new AccountDialog(Display.getDefault().getActiveShell(), SWT.SHELL_TRIM);
				dialog.setText("Update Account");
				dialog.setAccountInfo(accountInfo);
				Object result = dialog.open();
				if (result instanceof AccountInfo) {// ����ֵ��ȷ
					AccountInfo newAccountInfo = (AccountInfo) result;
					if (newAccountInfo.isReset()) {// �������������Ϣ���Ǿ�Ҫ����
						// �������޸�������Ϣ,����Ҫ�޸����ڵ��accountinfo����
						AccountUtil.updateAccount(element,newAccountInfo);
						MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information", "Update account succesfully !");
					} else {
						MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information", "AccountInfo does not change !");
					}
				}
			}
		}
	}

}
