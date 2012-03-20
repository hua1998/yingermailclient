package yingermailclient;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import yingermailclient.view.NavigatorView;

public class Perspective implements IPerspectiveFactory {

	// �������������ʼ��͸��ͼ�Ĳ��֣��������������Ŀ�е���ͼ����ע��
	@Override
	public void createInitialLayout(IPageLayout layout) {
		// �õ��༭����id
		String editorArea = layout.getEditorArea();

		// ��ӵ�����ͼ
		// String viewId, int relationship, float ratio, String refId
		layout.addView(NavigatorView.ID, IPageLayout.LEFT, 0.4f, editorArea);

		// ���������ͼ����ӵ�������ͼ���·�
		// An IFolderLayout is used to define the initial views within a
		// folder. The folder itself is contained within an IPageLayout.
		// IFolderLayout���������е�View���һ��folder���ļ��й��ܣ������folder����Ͱ�����IPageLayout
		// IFolderLayout leftbottom = layout.createFolder("left",
		// IPageLayout.BOTTOM, 0.4f, PluginUtil.NavigatorView_ID);
		// leftbottom.addView(PluginUtil.SearchView_ID);
		//
		// // ��Ӳ�����Ϣ�ͷ�����Ϣ����ͼ����ʵ��������ͼ���ӵ�Ч��
		// IFolderLayout rightbottom = layout.createFolder("rightbottom",
		// IPageLayout.BOTTOM, 0.7f, editorArea);
		// rightbottom.addView(PluginUtil.PatientInfoView_ID);
		// rightbottom.addView(PluginUtil.ExpenseInfoView_ID);

	}
}
