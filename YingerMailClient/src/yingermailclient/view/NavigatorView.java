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
		// 创建工具栏
		setViewToolBar();

		// 创建treeviewer
		createTreeViewer(composite);
		// 关联双击事件用于打开编辑器
		hookDoubleClickAction();
	}

	public void setViewToolBar() {
		// IActionBars:Used by a part to access its menu, toolbar, and
		// status line managers.
		IActionBars bars = getViewSite().getActionBars();
		// 定义工具栏
		IToolBarManager toolBarManager = bars.getToolBarManager();
		toolBarManager.add(new DeleteAccountAction());
		toolBarManager.add(new UpdateAccountAction());

	}

	public void createTreeViewer(Composite composite) {
		treeViewer = new TreeViewer(composite, SWT.V_SCROLL | SWT.H_SCROLL);
		// 设置内容提供其和标签提供器
		treeViewer.setContentProvider(new NavigatorTreeViewerContentProvider());
		treeViewer.setLabelProvider(new NavigatorTreeViewerLabelProvider());
		// 读入数据
		treeViewer.setInput(NavigatorEntityFactory.getAccountList());
	}

	// 这个方法实际上就是给treeviewer添加了一个处理双击事件的监听器
	public void hookDoubleClickAction() {
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = treeViewer.getSelection();
				// 得到选中的项，注意方法是将得到的选项转换成 IStructuredSelection，在调用
				// getFirstElement 方法
				Object object = ((IStructuredSelection) selection).getFirstElement();
				// 再将对象转为实际的树节点对象
				NavigatorEntityElement element = (NavigatorEntityElement) object;
				// 得到该对象的editorInput
				IEditorInput editorInput = element.getEditorInput();
				// 设置editor中的邮件列表
				((MailListEditorInput) editorInput).setMails(FileUtil.getLocalMails(element.getAccountInfo(), element.getName()));
				// 得到当前工作台的page
				IWorkbenchPage workbenchPage = getViewSite().getPage();//
				String editorID = MailListEditor.ID;
				// 这里要结合NavigatorEntityFactory类的setNavigatorEntity方法
				// IEditorPart:An editor is a visual component within a
				// workbench page.
				IEditorPart editorPart = workbenchPage.findEditor(editorInput);// null
				// 这里很重要，重新设置maillist！
				// MailUtil.setMails(FileUtil.getLocalMails(element.getAccountInfo(),
				// element.getName()));
				if (editorPart != null) {// 已经打开了所需的编辑器
					workbenchPage.bringToTop(editorPart);
				} else {// 没有打开就打开来
					try {
						editorPart = workbenchPage.openEditor(editorInput, editorID);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	// 刷新账户列表
	public void refreshAccounts() {
		treeViewer.setInput(NavigatorEntityFactory.getAccountList());
		treeViewer.refresh();
	}

	@Override
	public void setFocus() {

	}

	class DeleteAccountAction extends Action {
		public DeleteAccountAction() {
			// 设置提示文本
			this.setToolTipText("Delete Account");
			// 设置图标
			this.setImageDescriptor(Activator.getImageDescriptor("/icons/mail/delete_user.ico"));
		}

		@Override
		public void run() {
			Object object = ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
			if (object == null || !(object instanceof NavigatorEntityElement)) {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information", "Please select an account !");
			} else {
				NavigatorEntityElement element = (NavigatorEntityElement) object;
				AccountInfo accountInfo = element.getAccountInfo();//每个节点都保存了账户信息
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
			// 设置提示文本
			this.setToolTipText("Update Account");
			// 设置图标
			this.setImageDescriptor(Activator.getImageDescriptor("/icons/icos/Char.ico"));
		}

		@Override
		public void run() {
			Object object = ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
			if (object == null || !(object instanceof NavigatorEntityElement)) {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information", "Please select an account !");
			} else {
				NavigatorEntityElement element = (NavigatorEntityElement) object;
				AccountInfo accountInfo = element.getAccountInfo();//每个节点都保存了账户信息
				AccountDialog dialog = new AccountDialog(Display.getDefault().getActiveShell(), SWT.SHELL_TRIM);
				dialog.setText("Update Account");
				dialog.setAccountInfo(accountInfo);
				Object result = dialog.open();
				if (result instanceof AccountInfo) {// 返回值正确
					AccountInfo newAccountInfo = (AccountInfo) result;
					if (newAccountInfo.isReset()) {// 如果是重置了信息，那就要更新
						// 首先是修改配置信息,接着要修改树节点的accountinfo属性
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
