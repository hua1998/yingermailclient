package yingermailclient.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import yingermailclient.model.AccountInfo;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

// TODO����ʱû�н��д�����,����,����Ҫ����
public class AccountDialog extends Dialog {

	protected Object result = null;
	protected Shell shell;
	private Text txt_account;
	private Text txt_password;
	private Text txt_smtphost;
	private Text txt_smtpport;
	private Text txt_pop3host;
	private Text txt_pop3port;
	private AccountInfo accountInfo;
	private boolean changed = false;

	public AccountDialog(Shell parent, int style) {
		super(parent, style);
	}

	public AccountInfo getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}

	public Object open() {
		createContents();
		setInitInfo();
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

	// ��ʼ����Ϣ
	public void setInitInfo() {
		if (accountInfo != null) {
			txt_account.setText(accountInfo.getAccount());
			txt_password.setText(accountInfo.getPassword());
			txt_smtphost.setText(accountInfo.getSmtpHost());
			txt_smtpport.setText(String.valueOf(accountInfo.getSmtpPort()));
			txt_pop3host.setText(accountInfo.getPop3Host());
			txt_pop3port.setText(String.valueOf(accountInfo.getPop3Port()));
		}
	}

	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(453, 348);
		shell.setText(getText());

		Group grpAccountSettings = new Group(shell, SWT.NONE);
		grpAccountSettings.setText("Account Settings");
		grpAccountSettings.setBounds(23, 20, 402, 104);

		Label lblAccount = new Label(grpAccountSettings, SWT.NONE);
		lblAccount.setBounds(21, 31, 61, 17);
		lblAccount.setText("Account");

		txt_account = new Text(grpAccountSettings, SWT.BORDER);
		txt_account.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				changed = true;
			}
		});
		txt_account.setBounds(88, 31, 287, 23);
		txt_account.setText("yingermailclient@163.com");
		if (getText().equalsIgnoreCase("Update Account")) {
			txt_account.setEditable(false);// ������޸��˻���Ϣ����ô�˻����ǲ������޸ĵ�
		}

		Label lblPassword = new Label(grpAccountSettings, SWT.NONE);
		lblPassword.setBounds(21, 60, 61, 17);
		lblPassword.setText("Password");

		txt_password = new Text(grpAccountSettings, SWT.BORDER | SWT.PASSWORD);
		txt_password.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				changed = true;
			}
		});
		txt_password.setBounds(88, 60, 287, 23);
		txt_password.setText("yingermail");

		Group grpServersSettings = new Group(shell, SWT.NONE);
		grpServersSettings.setText("Servers Settings");
		grpServersSettings.setBounds(23, 130, 402, 127);

		Label lblSmtpHost = new Label(grpServersSettings, SWT.NONE);
		lblSmtpHost.setBounds(10, 36, 75, 17);
		lblSmtpHost.setText("SMTP Host");

		txt_smtphost = new Text(grpServersSettings, SWT.BORDER);
		txt_smtphost.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				changed = true;
			}
		});
		txt_smtphost.setBounds(91, 36, 164, 23);
		txt_smtphost.setText("smtp.163.com");

		Label lblPort = new Label(grpServersSettings, SWT.NONE);
		lblPort.setBounds(280, 36, 31, 17);
		lblPort.setText("Port");

		txt_smtpport = new Text(grpServersSettings, SWT.BORDER);
		txt_smtpport.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				changed = true;
			}
		});
		txt_smtpport.setText("25");
		txt_smtpport.setBounds(319, 36, 73, 23);

		Label lblPopHost = new Label(grpServersSettings, SWT.NONE);
		lblPopHost.setBounds(10, 82, 75, 17);
		lblPopHost.setText("POP3 Host");

		txt_pop3host = new Text(grpServersSettings, SWT.BORDER);
		txt_pop3host.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				changed = true;
			}
		});
		txt_pop3host.setBounds(91, 76, 164, 23);
		txt_pop3host.setText("pop.163.com");

		Label label = new Label(grpServersSettings, SWT.NONE);
		label.setText("Port");
		label.setBounds(280, 82, 31, 17);

		txt_pop3port = new Text(grpServersSettings, SWT.BORDER);
		txt_pop3port.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				changed = true;
			}
		});
		txt_pop3port.setText("110");
		txt_pop3port.setBounds(319, 76, 73, 23);

		Button btnTestConnection = new Button(shell, SWT.NONE);
		btnTestConnection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// ��������! û��д 
			}
		});
		btnTestConnection.setBounds(23, 276, 129, 27);
		btnTestConnection.setText("Test Connection");

		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getInputInfo();
				shell.dispose();// dispose֮��ſ��Է���
			}
		});
		btnOk.setBounds(258, 276, 80, 27);
		btnOk.setText("OK");

		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnCancel.setBounds(344, 276, 80, 27);
		btnCancel.setText("Cancel");
	}

	// �õ�������Ϣ
	protected void getInputInfo() {
		accountInfo = new AccountInfo(txt_account.getText().trim(), txt_password.getText().trim(), txt_smtphost.getText().trim(),
				Integer.valueOf(txt_smtpport.getText().trim()), txt_pop3host.getText().trim(), Integer.valueOf(txt_pop3port.getText().trim()));
		// �������Ӧ�÷��ں��棬��Ȼ�������� reset û���ã����ǻ�����Ϊ false
		if (changed) {
			accountInfo.setReset(true);// ����Ϊ������������Ϣ
		} else {
			accountInfo.setReset(false);// û���޸���Ϣ
		}
		result = accountInfo;
	}

}
