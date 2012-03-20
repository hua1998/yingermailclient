package yingermailclient.model;

import java.io.File;

public class Attachment {

	// Դ�ļ�����
	private String sourceName;
	// ��Ӧ���ļ�
	private File file;
	
	public Attachment(String sourceName, File file) {
		this.sourceName = sourceName;
		this.file = file;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return this.sourceName;
	}

}
