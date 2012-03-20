package yingermailclient.navigatorEntity;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.IEditorInput;

import yingermailclient.model.AccountInfo;

public class NavigatorEntityElement implements ITreeViewerElement {

	private String name;
	private NavigatorEntityElement parent;
	private List children = new ArrayList();
	private IEditorInput editorInput;// 这个是添加的，配置它相应的editor
	private AccountInfo accountInfo;// 这个也是添加的，配置它相应的accountInfo

	public NavigatorEntityElement() {

	}

	public NavigatorEntityElement(String name) {
		this.name = name;
	}

	@Override
	public ITreeViewerElement getParent() {
		return parent;
	}

	@Override
	public void setParent(ITreeViewerElement parent) {
		this.parent = (NavigatorEntityElement) parent;
	}

	@Override
	public void addChild(ITreeViewerElement element) {
		children.add(element);
	}

	@Override
	public boolean hasChildren() {
		if (children != null && children.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List getChildren() {
		return children;
	}

	@Override
	public void setChildren(List children) {
		this.children = children;
	}

	public IEditorInput getEditorInput() {
		return editorInput;
	}

	public void setEditorInput(IEditorInput editorInput) {
		this.editorInput = editorInput;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public AccountInfo getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}

}
