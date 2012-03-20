package yingermailclient.navigatorEntity;

import java.util.List;

public interface ITreeViewerElement {

	public void setName(String name);

	public String getName();

	public void addChild(ITreeViewerElement element);

	public boolean hasChildren();

	public List getChildren();

	public void setChildren(List children);
	
	public ITreeViewerElement getParent();
	
	public void setParent(ITreeViewerElement parent);

}
