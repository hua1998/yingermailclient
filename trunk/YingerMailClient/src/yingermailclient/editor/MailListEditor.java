package yingermailclient.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import yingermailclient.Activator;
import yingermailclient.editorInput.MailListEditorInput;
import yingermailclient.editorSorter.MailListSorter;
import yingermailclient.model.Mail;
import yingermailclient.provider.MailTableViewerContentProvider;
import yingermailclient.provider.MailTableViewerLabelProvider;
import yingermailclient.util.FileUtil;
import yingermailclient.view.MailView;

public class MailListEditor extends EditorPart {

	public static final String ID = "yingermailclient.maillisteditor";
	public TableViewer tableViewer;
	private boolean sort = false;// 标识排序的方式

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public IEditorInput getEditorInput() {
		return super.getEditorInput();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		this.setPartName(input.getName());// 设置编辑器的name
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		// 首先创建一个ViewForm对象，它方便于控件的布局
		ViewForm viewForm = new ViewForm(parent, SWT.NONE);
		// 布局
		viewForm.setLayout(new FillLayout());
		// 创建TableViewer
		createTableViewer(viewForm);

		tableViewer.setContentProvider(new MailTableViewerContentProvider());
		tableViewer.setLabelProvider(new MailTableViewerLabelProvider());
		// tableViewer.setInput(TestUtil.createFakeMails());
		tableViewer.setInput(((MailListEditorInput) getEditorInput()).getMails());
		tableViewer.setSorter(new MailListSorter());
		// 设置一个默认的排序方式
		((MailListSorter) tableViewer.getSorter()).doSort(sort ? 4 : -4);
		tableViewer.refresh();

		// 添加编辑器的工具栏，包括了 刷新，打开，删除  三个按钮
		ToolBar toolBar = new ToolBar(viewForm, SWT.FLAT);
		ToolBarManager toolBarManager = new ToolBarManager(toolBar);
		toolBarManager.add(new refreshMailAction());
		toolBarManager.add(new openMailAction());
		toolBarManager.add(new deleteMailAction());
		toolBarManager.update(true);
		// This brings the underlying widgets up to date with any changes.

		// 设置viewform
		viewForm.setTopLeft(toolBar);
		viewForm.setContent(tableViewer.getControl());

	}

	private void createTableViewer(ViewForm viewForm) {
		tableViewer = new TableViewer(viewForm, SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION);
		// 得到表格
		Table table = tableViewer.getTable();
		// 设置显示标题
		table.setHeaderVisible(true);
		// 设置显示表格线
		table.setLinesVisible(true);
		// 建立一列
		TableColumn tc1 = new TableColumn(table, SWT.LEFT);
		// 设置列标题
		tc1.setText("Read?");
		// 设置列宽
		tc1.setWidth(50);
		// 设置布局
		tc1.setAlignment(SWT.CENTER);
		// 注意：widgetSelected方法的末尾一定要refresh，这样可以保证不用手动刷新！

		TableColumn tc2 = new TableColumn(table, SWT.LEFT);
		if (getPartName().equalsIgnoreCase(FileUtil.INBOX)) {
			tc2.setText("From");
		} else {
			tc2.setText("To");
		}
		tc2.setWidth(270);
		tc2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sort = !sort;
				((MailListSorter) tableViewer.getSorter()).doSort(sort ? -2 : 2);
				tableViewer.refresh();
			}
		});

		TableColumn tc3 = new TableColumn(table, SWT.LEFT);
		tc3.setText("Subject");
		tc3.setWidth(400);

		TableColumn tc4 = new TableColumn(table, SWT.LEFT);
		if (getPartName().equalsIgnoreCase(FileUtil.INBOX)) {
			tc4.setText("RecieveDate");
		} else {
			tc4.setText("SentDate");
		}
		tc4.setWidth(200);
		tc4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sort = !sort;
				((MailListSorter) tableViewer.getSorter()).doSort(sort ? -4 : 4);
				tableViewer.refresh();
			}
		});

		TableColumn tc5 = new TableColumn(table, SWT.LEFT);
		tc5.setText("Attachment?");
		tc5.setWidth(80);
		tc5.setAlignment(SWT.CENTER);

		TableColumn tc6 = new TableColumn(table, SWT.LEFT);
		tc6.setText("Size[KB]");
		tc6.setWidth(100);
		tc6.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sort = !sort;
				((MailListSorter) tableViewer.getSorter()).doSort(sort ? -6 : 6);
				tableViewer.refresh();
			}
		});

	}

	public void refreshMails() {
		MailListEditorInput input = (MailListEditorInput) getEditorInput();
		input.setMails(FileUtil.getLocalMails(input.getAccountInfo(), input.getName()));
		tableViewer.setInput(input.getMails());
		tableViewer.refresh();
	}

	@Override
	public void setFocus() {
		refreshMails();
	}

	class refreshMailAction extends Action {
		public refreshMailAction() {
			// 设置提示文本
			this.setToolTipText("Refresh Mails");
			// 设置图标
			this.setImageDescriptor(Activator.getImageDescriptor("/icons/icos/Refresh.ico"));
		}

		@Override
		public void run() {
			refreshMails();
		}
	}

	class openMailAction extends Action {
		public openMailAction() {
			// 设置提示文本
			this.setToolTipText("Open Mail");
			// 设置图标
			this.setImageDescriptor(Activator.getImageDescriptor("/icons/icos/Applications.ico"));
		}

		@Override
		public void run() {
			Object object = ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
			if (object==null || !(object instanceof Mail)) {
				return;
			}
			Mail mail = (Mail) object;
			mail.setHasRead(true);
			IWorkbenchPage workbenchPage = getSite().getPage();
			// 查找 邮件 视图
			IViewPart viewPart = workbenchPage.findView(MailView.ID);
			if (viewPart != null) {
				workbenchPage.bringToTop(viewPart);
			} else {
				try {
					viewPart = workbenchPage.showView(MailView.ID);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
			MailView mailView = (MailView) viewPart;
			mailView.setMail(mail);// 设置邮件视图的邮件
		}
	}

	class deleteMailAction extends Action {
		public deleteMailAction() {
			// 设置提示文本
			this.setToolTipText("Delete Mail");
			// 设置图标
			this.setImageDescriptor(Activator.getImageDescriptor("/icons/mail/Stop.ico"));
		}

		@Override
		public void run() {
			Object object = ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
			Mail mail = (Mail) object;
			if (getPartName().equals(FileUtil.DELETEDBOX)) {//如果是已经删除的邮件，那么再点击删除就要彻底删除了！
				FileUtil.completeDeleteMailFile(((MailListEditorInput) getEditorInput()).getAccountInfo(), mail);
			} else {//移动到已删除文件夹
				mail.setDeleted(true);// 设置为删除了！
				FileUtil.deleteMailFile(((MailListEditorInput) getEditorInput()).getAccountInfo(), mail);
			}
			refreshMails();//删除之后刷新一下
		}
	}

}
