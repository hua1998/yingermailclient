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

	// 这个构造方法在 ApplicationWorkbenchWindowAdvisor类中调用
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	// 这个方法很重要，用于设置窗体中的操作
	@Override
	protected void makeActions(IWorkbenchWindow window) {
		// ActionFactory：工作台操作工厂对象
		// ActionFactory.OPEN_NEW_WINDOW ：具体化了的用于创建“新建窗口”操作的操作工厂对象
		// 定义“退出”操作
		exitAction = ActionFactory.QUIT.create(window);
		exitAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		register(exitAction);

		// 定义“打开透视图”操作
		perspectiveAction = ActionFactory.OPEN_PERSPECTIVE_DIALOG.create(window);
		register(perspectiveAction);

		// 定义“关于”操作
		aboutAction = ActionFactory.ABOUT.create(window);
		aboutAction.setText("About");
		// aboutAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		aboutAction.setImageDescriptor(Activator.getImageDescriptor("/icons/gifs/about.gif"));
		register(aboutAction);

		// 定义“显示视图列表”操作
		showViewAction = ContributionItemFactory.VIEWS_SHORTLIST.create(window);

		// 定义打开“首选项”操作
		preferenceAction = ActionFactory.PREFERENCES.create(window);
		// preferenceAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_HOME_NAV));
		preferenceAction.setImageDescriptor(Activator.getImageDescriptor("/icons/icos/Security.ico"));
		register(preferenceAction);
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		// 定义File菜单
		MenuManager fileManager = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
		// 在File菜单中添加退出菜单项
		fileManager.add(exitAction);
		// 添加File菜单到菜单栏
		menuBar.add(fileManager);

		// 定义Window菜单
		MenuManager windowManager = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);
		windowManager.add(perspectiveAction);
		windowManager.add(preferenceAction);
		// Window菜单的子菜单Show View菜单，创建二级菜单
		MenuManager showViewManager = new MenuManager("&Show View", "showView");
		// 包含一个“other”，用于打开未显示的视图
		showViewManager.add(showViewAction);
		windowManager.add(showViewManager);
		menuBar.add(windowManager);

		// Help菜单
		MenuManager helpManager = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
		helpManager.add(aboutAction);
		menuBar.add(helpManager);
	}

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {

	}
}
