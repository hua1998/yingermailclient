package yingermailclient;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private IWorkbenchAction exitAction;
	private IWorkbenchAction perspectiveAction;
	private IWorkbenchAction aboutAction;
	private IContributionItem showViewAction;
	private IWorkbenchAction preferenceAction;

	// ������췽���� ApplicationWorkbenchWindowAdvisor���е���
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	// �����������Ҫ���������ô����еĲ���
	@Override
	protected void makeActions(IWorkbenchWindow window) {
		// ActionFactory������̨������������
		// ActionFactory.OPEN_NEW_WINDOW �����廯�˵����ڴ������½����ڡ������Ĳ�����������
		// ���塰�˳�������
		exitAction = ActionFactory.QUIT.create(window);
		exitAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		register(exitAction);

		// ���塰��͸��ͼ������
		perspectiveAction = ActionFactory.OPEN_PERSPECTIVE_DIALOG.create(window);
		register(perspectiveAction);

		// ���塰���ڡ�����
		aboutAction = ActionFactory.ABOUT.create(window);
		aboutAction.setText("About");
		// aboutAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		aboutAction.setImageDescriptor(Activator.getImageDescriptor("/icons/gifs/about.gif"));
		register(aboutAction);

		// ���塰��ʾ��ͼ�б�����
		showViewAction = ContributionItemFactory.VIEWS_SHORTLIST.create(window);

		// ����򿪡���ѡ�����
		preferenceAction = ActionFactory.PREFERENCES.create(window);
		// preferenceAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_HOME_NAV));
		preferenceAction.setImageDescriptor(Activator.getImageDescriptor("/icons/icos/Security.ico"));
		register(preferenceAction);
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		// ����File�˵�
		MenuManager fileManager = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
		// ��File�˵�������˳��˵���
		fileManager.add(exitAction);
		// ���File�˵����˵���
		menuBar.add(fileManager);

		// ����Window�˵�
		MenuManager windowManager = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);
		windowManager.add(perspectiveAction);
		windowManager.add(preferenceAction);
		// Window�˵����Ӳ˵�Show View�˵������������˵�
		MenuManager showViewManager = new MenuManager("&Show View", "showView");
		// ����һ����other�������ڴ�δ��ʾ����ͼ
		showViewManager.add(showViewAction);
		windowManager.add(showViewManager);
		menuBar.add(windowManager);

		// Help�˵�
		MenuManager helpManager = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
		helpManager.add(aboutAction);
		menuBar.add(helpManager);
	}

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {

	}
}
