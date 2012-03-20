package yingermailclient.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import yingermailclient.model.Attachment;
import yingermailclient.model.Mail;

public class MailView extends ViewPart {

	public static final String ID = "yingermailclient.mailView";

	private Text text_from;
	private Text text_to;
	private Text text_cc;
	private Text text_content;
	private Text text_subject;
	private List list_attachment;
	
	// TODO：少了一个 subject

	private Mail mail;

	/**
	 * @wbp.parser.constructor
	 */
	public MailView() {

	}

	public Mail getMail() {
		return mail;
	}

	public void setMail(Mail mail) {
		this.mail = mail;
		refreshMailInfo();
	}

	// 刷新显示的邮件信息
	public void refreshMailInfo() {
		if (mail==null) {
			return;
		}
		text_from.setText(mail.getSender());
		text_to.setText(mail.getReceiverString());
		text_cc.setText(mail.getCCString());
		text_subject.setText(mail.getSubject());
		text_content.setText(mail.getContent());
		java.util.List<Attachment> attachments = mail.getFiles();
		list_attachment.removeAll();//首先要清除所有的item
		if (attachments!=null) {// 附件不为空
			for (int i = 0, size = attachments.size(); i < size; i++) {
				Attachment attachment = attachments.get(i);
				list_attachment.add(attachment.getSourceName());
				list_attachment.setData(attachment.getSourceName(), attachment.getFile());
			}
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FormLayout());

		Label lblFrom = new Label(parent, SWT.NONE);
		FormData fd_lblFrom = new FormData();
		fd_lblFrom.left = new FormAttachment(0, 10);
		lblFrom.setLayoutData(fd_lblFrom);
		lblFrom.setText("From");

		text_from = new Text(parent, SWT.BORDER);
		FormData fd_text_from = new FormData();
		fd_text_from.bottom = new FormAttachment(0, 30);
		fd_text_from.left = new FormAttachment(0, 60);
		fd_text_from.right = new FormAttachment(95);
		fd_text_from.top = new FormAttachment(0, 10);
		text_from.setLayoutData(fd_text_from);

		Label lblTo = new Label(parent, SWT.NONE);
		fd_lblFrom.bottom = new FormAttachment(lblTo, -13);
		FormData fd_lblTo = new FormData();
		fd_lblTo.left = new FormAttachment(0, 10);
		lblTo.setLayoutData(fd_lblTo);
		lblTo.setText("To");

		text_to = new Text(parent, SWT.BORDER);
		FormData fd_text_to = new FormData();
		fd_text_to.top = new FormAttachment(text_from, 6);
		fd_text_to.right = new FormAttachment(95);
		fd_text_to.left = new FormAttachment(0, 60);
		text_to.setLayoutData(fd_text_to);

		Label lblCc = new Label(parent, SWT.NONE);
		fd_lblTo.bottom = new FormAttachment(lblCc, -11);
		FormData fd_lblCc = new FormData();
		fd_lblCc.left = new FormAttachment(0, 10);
		lblCc.setLayoutData(fd_lblCc);
		lblCc.setText("CC");

		text_cc = new Text(parent, SWT.BORDER);
		fd_text_to.bottom = new FormAttachment(text_cc, -6);
		fd_lblCc.bottom = new FormAttachment(text_cc, 0, SWT.BOTTOM);
		FormData fd_text_cc = new FormData();
		fd_text_cc.bottom = new FormAttachment(0, 88);
		fd_text_cc.top = new FormAttachment(0, 66);
		fd_text_cc.right = new FormAttachment(95);
		fd_text_cc.left = new FormAttachment(0, 60);
		text_cc.setLayoutData(fd_text_cc);
		
		Label lblSubject = new Label(parent, SWT.NONE);
		FormData fd_lblSubject = new FormData();
		fd_lblSubject.top = new FormAttachment(lblCc, 9);
		fd_lblSubject.left = new FormAttachment(lblFrom, 0, SWT.LEFT);
		lblSubject.setLayoutData(fd_lblSubject);
		lblSubject.setText("Subject");
		
		text_subject = new Text(parent, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.right = new FormAttachment(text_from, 0, SWT.RIGHT);
		fd_text.bottom = new FormAttachment(lblSubject, 4, SWT.BOTTOM);
		fd_text.top = new FormAttachment(0, 94);
		fd_text.left = new FormAttachment(0, 60);
		text_subject.setLayoutData(fd_text);

		Label lblAttachment = new Label(parent, SWT.NONE);
		FormData fd_lblAttachment = new FormData();
		fd_lblAttachment.top = new FormAttachment(text_subject, 26);
		fd_lblAttachment.left = new FormAttachment(lblFrom, 0, SWT.LEFT);
		lblAttachment.setLayoutData(fd_lblAttachment);
		lblAttachment.setText("Attachment");

		list_attachment = new List(parent, SWT.BORDER | SWT.V_SCROLL);
		FormData fd_list_attachment = new FormData();
		fd_list_attachment.bottom = new FormAttachment(0, 181);
		fd_list_attachment.top = new FormAttachment(0, 128);
		fd_list_attachment.right = new FormAttachment(81);
		fd_list_attachment.left = new FormAttachment(0, 81);
		list_attachment.setLayoutData(fd_list_attachment);

		Button btnOpenIt = new Button(parent, SWT.NONE);
		btnOpenIt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				File file = (File) list_attachment.getData(list_attachment.getSelection()[0]);
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.open(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		FormData fd_btnOpenIt = new FormData();
		fd_btnOpenIt.right = new FormAttachment(100, -43);
		fd_btnOpenIt.top = new FormAttachment(0, 139);
		btnOpenIt.setLayoutData(fd_btnOpenIt);
		btnOpenIt.setText("Open it");

		Label lblContent = new Label(parent, SWT.NONE);
		FormData fd_lblContent = new FormData();
		fd_lblContent.top = new FormAttachment(0, 193);
		fd_lblContent.left = new FormAttachment(0, 10);
		lblContent.setLayoutData(fd_lblContent);
		lblContent.setText("Content");
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		FormData fd_composite = new FormData();
		fd_composite.right = new FormAttachment(text_from, 0, SWT.RIGHT);
		fd_composite.left = new FormAttachment(0, 10);
		fd_composite.bottom = new FormAttachment(100, -10);
		fd_composite.top = new FormAttachment(0, 212);
		composite.setLayoutData(fd_composite);

		text_content = new Text(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		
	}

	@Override
	public void setFocus() {
	}
}
