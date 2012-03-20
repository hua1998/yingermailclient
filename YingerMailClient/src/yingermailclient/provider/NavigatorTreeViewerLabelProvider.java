package yingermailclient.provider;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import yingermailclient.Activator;
import yingermailclient.navigatorEntity.NavigatorEntityElement;
import yingermailclient.util.FileUtil;

public class NavigatorTreeViewerLabelProvider implements ILabelProvider {

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

	// Ҫ��ʾ��ͼƬ
	@Override
	public Image getImage(Object element) {
		//��һ��ͼƬ
//		NavigatorEntityElement entityElement = (NavigatorEntityElement) element;
//		if (entityElement.hasChildren()) {
//			return Activator.getImageDescriptor("/icons/icos/User.ico").createImage();
//		} else if (entityElement.getName().equalsIgnoreCase(FileUtil.INBOX)) {
//			return Activator.getImageDescriptor("/icons/mail/mail_get.ico").createImage();
//		} else if (entityElement.getName().equalsIgnoreCase(FileUtil.OUTBOX)) {
//			return Activator.getImageDescriptor("/icons/mail/mail_replay.ico").createImage();
//		} else if (entityElement.getName().equalsIgnoreCase(FileUtil.DRAFTBOX)) {
//			return Activator.getImageDescriptor("/icons/mail/mail_draft.ico").createImage();
//		} else if (entityElement.getName().equalsIgnoreCase(FileUtil.SENTBOX)) {
//			return Activator.getImageDescriptor("/icons/mail/mail_send.ico").createImage();
//		} else if (entityElement.getName().equalsIgnoreCase(FileUtil.DELETEDBOX)) {
//			return Activator.getImageDescriptor("/icons/mail/mail_delete.ico").createImage();
//		}
//		return Activator.getImageDescriptor("/icons/icos/Mail.ico").createImage();

		//�ڶ���ͼƬ
		NavigatorEntityElement entityElement = (NavigatorEntityElement) element;
		if (entityElement.hasChildren()) {
			return Activator.getImageDescriptor("/icons/icos/User.ico").createImage();
		} else if (entityElement.getName().equalsIgnoreCase(FileUtil.INBOX)) {
			return Activator.getImageDescriptor("/icons/mail/mail_get.ico").createImage();
		} else if (entityElement.getName().equalsIgnoreCase(FileUtil.OUTBOX)) {
			return Activator.getImageDescriptor("/icons/mail/mail_replay.ico").createImage();
		} else if (entityElement.getName().equalsIgnoreCase(FileUtil.DRAFTBOX)) {
			return Activator.getImageDescriptor("/icons/mail/mail_draft.ico").createImage();
		} else if (entityElement.getName().equalsIgnoreCase(FileUtil.SENTBOX)) {
			return Activator.getImageDescriptor("/icons/mail/mail_sent.ico").createImage();
		} else if (entityElement.getName().equalsIgnoreCase(FileUtil.DELETEDBOX)) {
			return Activator.getImageDescriptor("/icons/mail/mail_deleted.ico").createImage();
		}
		return Activator.getImageDescriptor("/icons/icos/Mail.ico").createImage();
	}

	// �õ���ʾ�ı�
	@Override
	public String getText(Object element) {
		NavigatorEntityElement entityElement = (NavigatorEntityElement) element;
		return entityElement.getName();
	}

}
