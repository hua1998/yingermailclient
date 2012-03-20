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
		combo_sender.setItems(NavigatorEntityFactory.getAccountString());//���÷����ˣ�

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

		list_attachment = new org.eclipse.swt.widgets.List(shell, SWT.BORDER | SWT.READ_ONLY);//ֻ��ѡ��������
		list_attachment.setBounds(98, 127, 363, 106);

		Button btnAdd = new Button(shell, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// ��Ӹ���
				FileDialog fileDialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.SHELL_TRIM);
				String filePath = fileDialog.open();// �õ��ļ�·��
				if (filePath == null) {
					return;
				}
				File file = new File(filePath);
				Attachment attachment = new Attachment(file.getName(), file);
				list_attachment.add(filePath, 0);
				// ��ʱ��Ӧ���ļ�·�����ļ����Ǳ����ļ�����û�зŵ��˻���attachment�ļ�����
				list_attachment.setData(filePath, attachment);//filePath
			}
		});
		btnAdd.setBounds(472, 127, 80, 27);
		btnAdd.setText("Add");

		Button btnRemove = new Button(shell, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// �Ƴ�����
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
				// ���浽�ݸ��䣬���ȵõ�������Ϣ  //TODO:��ʵ�����Ȳ鿴mail�Ƿ�Ϊnull
				getInputInfo();
				// �õ�Mail
				Mail mail = (Mail) result;
				if (mail.getSender() == null || mail.getSender().equalsIgnoreCase("")) {
					MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Send Information", "No Sender !");
					return;
				}
				// �ʼ��ǲݸ������
				mail.setFolder(FileUtil.DRAFTBOX);
				//����������������ָ��ʼ���ʱ��᲻֪�����ʼ�ԭ���Ǵ���������
				//�������һ���µ�booleanֵ�Ϳ����ˣ�isDelete��ʾ�ʼ��Ƿ�ɾ����
				//folder���Ǳ�������ʼ���ԭ����Ŀ¼
				// ��xml�ļ�����ʽд�뵽�ݸ�����
				// �õ����������ļ�
				File proFile = new File(FileUtil.DATA_FOLDER + mail.getSender() + File.separator + FileUtil.CONFIG_FILE);
				// ���������ļ�����accountinfo
				AccountInfo accountInfo = PropertiesUtil.createAccountInfo(proFile);
				boolean flag = FileUtil.convertMailToXML(accountInfo, mail, FileUtil.DRAFTBOX);
				// ��ʾ
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

	// �õ��������Ϣ��ע�⣬�����Ը������в��������������浽attachment�ļ�����
	protected void getInputInfo() {
		mail = new Mail();
		mail.setSender(combo_sender.getText());
		mail.setCcs(Helper.filterString(text_cc.getText(), ";"));
		mail.setReceivers(Helper.filterString(text_receivers.getText(), ";"));
		mail.setContent(styledText_content.getText());
		mail.setSubject(text_subject.getText());
		mail.setReceiveDate(new Date());//TODO:����Ҫע�⣬��ǰʱ����Ϊ�յ���ʱ��
		mail.setHasRead(true);//����Ϊ�Ѷ�
		mail.setFolder(FileUtil.OUTBOX);//�����������ʼ��Ƿ������
		// ���ø������������ʼ��Ĵ�С
		if (list_attachment.getItemCount() == 0) {
			mail.setFiles(null);
		} else {// ���������������ȥ������Ҫ�ѱ��ص��ļ����Ƶ����˻���attachment�ļ�����
			String[] items = list_attachment.getItems();
			int size = 0;
			java.util.List<Attachment> attachments = new java.util.ArrayList<Attachment>();
			for (int i = 0; i < items.length; i++) {
				// ����Ը��������˲������õ����صĸ����ļ���Ȼ���Ƶ��˻�Ŀ¼
				// �����������Ǳ��浽�ݸ��䣬���Ƿ��ͣ����ض��и��Եĸ���������
				Attachment attachment = (Attachment) list_attachment.getData(items[i]);
				Attachment newAttachment = FileUtil.createAttachmentFromFile(mail, attachment.getFile());
				attachments.add(newAttachment);
				size = +(int) (attachment.getFile().length() / 1024);//����ÿ�������Ĵ�С
			}
			mail.setFiles(attachments);
			// ����mail�Ĵ�С
			mail.setSize(Mail.getSize(size));
		}
		result = mail;
	}
}
