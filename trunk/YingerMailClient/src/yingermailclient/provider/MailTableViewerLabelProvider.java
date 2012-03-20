package yingermailclient.provider;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import yingermailclient.Activator;
import yingermailclient.model.Mail;
import yingermailclient.util.FileUtil;

public class MailTableViewerLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {

		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		Mail entityElement = (Mail) element;
		switch (columnIndex) {// ע�⣺�е������Ǵ�0��ʼ��
		case 0:
			if (entityElement.getHasRead()) {// �ʼ��Ƿ��Ѷ���
				return Activator.getImageDescriptor("/icons/mail/Select.ico").createImage();
			} else {
				return Activator.getImageDescriptor("/icons/mail/Mail.ico").createImage();
			}
		case 4:
			if (entityElement.getFiles() != null && entityElement.getFiles().size() > 0) {// �ʼ��Ƿ��и�����Attachment.png
				return Activator.getImageDescriptor("/icons/mail/Attachment.ico").createImage();
			} else {
				return Activator.getImageDescriptor("/icons/mail/NoAttachment.ico").createImage();
			}
		}
		return null;// Ĭ�Ϸ��ؿ�
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Mail mail = (Mail) element;
		switch (columnIndex) {// ע�⣺�е������Ǵ�0��ʼ��
		case 1:
			if (mail.getFolder().equalsIgnoreCase(FileUtil.INBOX)) {//������ռ����е��ʼ�����ʾ�����ߣ���������ʾ�ռ���
				return mail.getSender();
			} else {
				return mail.getReceiverString();
			}
		case 2:
			return mail.getSubject();
		case 3:
			return mail.getDateString();
		case 5:
			return mail.getSize();
		}
		return "";// Ĭ�Ϸ��ؿ��ַ���
	}

}
