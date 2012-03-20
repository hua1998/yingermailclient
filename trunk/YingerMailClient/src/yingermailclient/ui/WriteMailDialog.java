package yingermailclient.ui;

import java.io.File;
import java.util.Date;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import yingermailclient.model.AccountInfo;
import yingermailclient.model.Attachment;
import yingermailclient.model.Mail;
import yingermailclient.navigatorEntity.NavigatorEntityFactory;
import yingermailclient.util.FileUtil;
import yingermailclient.util.Helper;
import yingermailclient.util.PropertiesUtil;

public class WriteMailDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text_receivers;
	private Text text_cc;
	private Mail mail;
	private Combo combo_sender;
	private StyledText styledText_content;
	private Text text_subject;
	private org.eclipse.swt.widgets.List list_attachment;

	public WriteMailDialog(Shell parent, int style) {
		super(parent, style);
		setText("Write Mail");
	}

	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(574, 590);
		shell.setText(getText());

		Label lblSender = new Label(shell, SWT.NONE);
		lblSender.setBounds(21, 25, 61, 17);
		lblSender.setText("Sender");

		combo_sender = new Combo(shell, SWT.NONE);
		combo_sender.setBounds(98, 17, 363, 25);
		combo_sender.setItems(NavigatorEntityFactory.getAccountString());//设置发信人！

		Label lblReceivers = new Label(shell, SWT.NONE);
		lblReceivers.setBounds(21, 58, 61, 17);
		lblReceivers.setText("Receivers");

		text_receivers = new Text(shell, SWT.BORDER);
		text_receivers.setBounds(98, 52, 363, 23);

		Button btnContacts = new Button(shell, SWT.NONE);
		btnContacts.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnContacts.setBounds(472, 48, 80, 27);
		btnContacts.setText("Contacts");

		Label lblCc = new Label(shell, SWT.NONE);
		lblCc.setBounds(21, 94, 61, 17);
		lblCc.setText("CCopys");

		text_cc = new Text(shell, SWT.BORDER);
		text_cc.setBounds(98, 88, 363, 23);

		Button btnContacts_cc = new Button(shell, SWT.NONE);
		btnContacts_cc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnContacts_cc.setText("Contacts");
		btnContacts_cc.setBounds(472, 86, 80, 27);

		Label lblAttachment = new Label(shell, SWT.NONE);
		lblAttachment.setBounds(21, 165, 73, 17);
		lblAttachment.setText("Attachment");

		list_attachment = new org.eclipse.swt.widgets.List(shell, SWT.BORDER | SWT.READ_ONLY);//只能选择不能输入
		list_attachment.setBounds(98, 127, 363, 106);

		Button btnAdd = new Button(shell, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 添加附件
				FileDialog fileDialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.SHELL_TRIM);
				String filePath = fileDialog.open();// 得到文件路径
				if (filePath == null) {
					return;
				}
				File file = new File(filePath);
				Attachment attachment = new Attachment(file.getName(), file);
				list_attachment.add(filePath, 0);
				// 此时对应的文件路径的文件还是本地文件，并没有放到账户的attachment文件夹中
				list_attachment.setData(filePath, attachment);//filePath
			}
		});
		btnAdd.setBounds(472, 127, 80, 27);
		btnAdd.setText("Add");

		Button btnRemove = new Button(shell, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 移除附件
				String selected[] = list_attachment.getSelection();
				for (int i = 0; i < selected.length; i++) {
					list_attachment.remove(selected[i]);
				}
			}
		});
		btnRemove.setBounds(472, 206, 80, 27);
		btnRemove.setText("Remove");

		Label lblSubject = new Label(shell, SWT.NONE);
		lblSubject.setBounds(21, 254, 61, 17);
		lblSubject.setText("Subject");

		text_subject = new Text(shell, SWT.BORDER);
		text_subject.setBounds(98, 248, 363, 23);

		styledText_content = new StyledText(shell, SWT.BORDER);
		styledText_content.setBounds(16, 314, 531, 198);

		Label lblContent = new Label(shell, SWT.NONE);
		lblContent.setBounds(21, 291, 61, 17);
		lblContent.setText("Content");

		Button btnSaveToDraft = new Button(shell, SWT.NONE);
		btnSaveToDraft.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 保存到草稿箱，首先得到输入信息  //TODO:其实可以先查看mail是否为null
				getInputInfo();
				// 得到Mail
				Mail mail = (Mail) result;
				if (mail.getSender() == null || mail.getSender().equalsIgnoreCase("")) {
					MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Send Information", "No Sender !");
					return;
				}
				// 邮件是草稿箱的了
				mail.setFolder(FileUtil.DRAFTBOX);
				//不可以这样，否则恢复邮件的时候会不知道该邮件原来是存放在哪里的
				//但是添加一个新的boolean值就可以了，isDelete表示邮件是否删除了
				//folder总是保存这个邮件的原来的目录
				// 以xml文件的形式写入到草稿箱中
				// 得到它的配置文件
				File proFile = new File(FileUtil.DATA_FOLDER + mail.getSender() + File.separator + FileUtil.CONFIG_FILE);
				// 根据配置文件创建accountinfo
				AccountInfo accountInfo = PropertiesUtil.createAccountInfo(proFile);
				boolean flag = FileUtil.convertMailToXML(accountInfo, mail, FileUtil.DRAFTBOX);
				// 提示
				if (flag) {
					MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Save Information", "Mail successfully saved !");
				} else {
					MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Send Information", "Save mail failed !");
				}
			}
		});
		btnSaveToDraft.setBounds(16, 519, 134, 27);
		btnSaveToDraft.setText("Save to Draft");

		Button btnSend = new Button(shell, SWT.NONE);
		btnSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getInputInfo();
				shell.dispose();
			}
		});
		btnSend.setBounds(354, 519, 80, 27);
		btnSend.setText("Send");

		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = null;
				shell.dispose();
			}
		});
		btnCancel.setBounds(467, 519, 80, 27);
		btnCancel.setText("Cancel");

	}

	// 得到输入的信息，注意，这里会对附件进行操作，将附件保存到attachment文件夹中
	protected void getInputInfo() {
		mail = new Mail();
		mail.setSender(combo_sender.getText());
		mail.setCcs(Helper.filterString(text_cc.getText(), ";"));
		mail.setReceivers(Helper.filterString(text_receivers.getText(), ";"));
		mail.setContent(styledText_content.getText());
		mail.setSubject(text_subject.getText());
		mail.setReceiveDate(new Date());//TODO:这里要注意，当前时间作为收到的时间
		mail.setHasRead(true);//设置为已读
		mail.setFolder(FileUtil.OUTBOX);//这里设置了邮件是发件箱的
		// 设置附件，并设置邮件的大小
		if (list_attachment.getItemCount() == 0) {
			mail.setFiles(null);
		} else {// 将附件内容添加上去，这里要把本地的文件复制到到账户的attachment文件夹中
			String[] items = list_attachment.getItems();
			int size = 0;
			java.util.List<Attachment> attachments = new java.util.ArrayList<Attachment>();
			for (int i = 0; i < items.length; i++) {
				// 这里对附件进行了操作，得到本地的附件文件，然后复制到账户目录
				// 这样，不管是保存到草稿箱，还是发送，本地都有各自的附件副本！
				Attachment attachment = (Attachment) list_attachment.getData(items[i]);
				Attachment newAttachment = FileUtil.createAttachmentFromFile(mail, attachment.getFile());
				attachments.add(newAttachment);
				size = +(int) (attachment.getFile().length() / 1024);//加上每个附件的大小
			}
			mail.setFiles(attachments);
			// 设置mail的大小
			mail.setSize(Mail.getSize(size));
		}
		result = mail;
	}
}
