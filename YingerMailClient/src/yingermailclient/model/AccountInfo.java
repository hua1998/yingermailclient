package yingermailclient.model;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

public class AccountInfo {

	// 帐号
	private String account;
	// 密码
	private String password;
	// smtp邮件服务器
	private String smtpHost;
	// smtp端口
	private int smtpPort;
	// pop3邮件服务器
	private String pop3Host;
	// pop3的端口
	private int pop3Port;
	// 是否进行重置信息
	private boolean reset = false;
	// 该账户的邮件仓库
	private Store store;
	// 与服务器进行通信的会话
	private Session session;
	// Gmail
	private final static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
	public AccountInfo() {

	}

	public AccountInfo(String account, String password, String smtpHost, int smtpPort, String pop3Host, int pop3Port) {
		this.account = account;
		this.password = password;
		this.smtpHost = smtpHost;
		this.smtpPort = smtpPort;
		this.pop3Host = pop3Host;
		this.pop3Port = pop3Port;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public int getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getPop3Host() {
		return pop3Host;
	}

	public void setPop3Host(String pop3Host) {
		this.pop3Host = pop3Host;
	}

	public int getPop3Port() {
		return pop3Port;
	}

	public void setPop3Port(int pop3Port) {
		this.pop3Port = pop3Port;
	}

	public Store getStore() {
		// 重置了信息, 设置session为null
		if (this.reset) {
			this.store = null;
			this.session = null;
			this.reset = false;
		}
		if (this.store == null || !this.store.isConnected()) { // isConnected?
			try {
//				Properties props = System.getProperties();
				Properties props = new Properties();
				if (isGmail()) {
					props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
				}
//				props.put("mail.imaps.partialfetch", false);//TODO：这一句可能很重要！
				// 创建mail的Session
				Session session = Session.getDefaultInstance(props, getAuthenticator());
				// 使用pop3协议接收邮件
				// //pop3://yingermailclient%40163.com:yingermail@pop.163.com:110
				URLName url = new URLName("pop3", getPop3Host(), getPop3Port(), null, getAccount(), getPassword());
				// 得到邮箱的存储对象
				Store store = session.getStore(url);// POP3Store
				// isSSL=false,debug=false
				store.connect();// connected=true,name,host,portNum,user,passwd
				// System.out.println("connected");
				this.store = store;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.store;
	}

	public Session getSession() {
		// 重置了信息, 设置session为null
		if (this.reset) {
			this.session = null;
			this.store = null;
			this.reset = false;
		}
		if (this.session == null) {
			Properties props = System.getProperties();// TODO:System.getProperties()?
			if (isGmail()) {
				props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);// TODO:Gmail?
			}
			// 使用SMTP协议发送邮件
			props.put("mail.smtp.host", this.getSmtpHost());
			props.put("mail.smtp.port", this.getSmtpPort());
			props.put("mail.smtp.auth", true);
			Session sendMailSession = Session.getDefaultInstance(props, getAuthenticator());
			this.session = sendMailSession;
		}
		return this.session;
	}

	private boolean isGmail() {
		if (this.account == null || this.account.trim().equals(""))
			return false;
		if (this.account.lastIndexOf("@gmail.com") != -1) {
			return true;
		}
		return false;
	}

	public Authenticator getAuthenticator() {
		return new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(getAccount(), getPassword());
			}
		};
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

}
