package yingermailclient.provider;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import yingermailclient.navigatorEntity.ITreeViewerElement;

public class NavigatorTreeViewerContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	// �õ���ʾԪ��
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement != null && inputElement instanceof List) {
			return ((List) inputElement).toArray();
		} else {
			return new Object[0];
		}
	}

	// �õ�����Ԫ��
	@Override
	public Object[] getChildren(Object parentElement) {
		ITreeViewerElement treeElement = (ITreeViewerElement) parentElement;
		List children = treeElement.getChildren();
		if (children == null || children.isEmpty()) {
			return new Object[0];
		} else {
			return children.toArray();
		}
	}

	@Override
	public Object getParent(Object element) {

		return null;
	}

	// �Ƿ����Ӻ���
	@Override
	public boolean hasChildren(Object element) {
		ITreeViewerElement treeElement = (ITreeViewerElement) element;
		List children = treeElement.getChildren();
		if (children == null || children.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

}
