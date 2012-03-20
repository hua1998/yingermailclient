package yingermailclient;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.util.PrefUtil;

import yingermailclient.util.Helper;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(600, 500));
		// ���ò˵���
		configurer.setShowMenuBar(true);
		// ���ù�����
		configurer.setShowCoolBar(true);
		// ����״̬��
		configurer.setShowStatusLine(true);
		// ���ÿ�����ͼ��
		configurer.setShowFastViewBars(true);
		// ���ý�����������
		configurer.setShowProgressIndicator(true);
		// ��ʾ͸��ͼ
		configurer.setShowPerspectiveBar(true);

		// ����Ӧ�ó�������
		IPreferenceStore preferenceStore = PrefUtil.getAPIPreferenceStore();
		// ����ѡ�����ʽ�����Ǿ��εı߿򣬶��ǻ��ε�
		preferenceStore.setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
		// ����͸��ͼ��ť��λ�ã�Ĭ�������Ͻǣ���Ϊ���������Ͻ�
		preferenceStore.setValue(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR, IWorkbenchPreferenceConstants.TOP_RIGHT);

		doInitWork();

	}

	// ���г�ʼ������
	public void doInitWork() {
		Helper.initData();
	}
}
