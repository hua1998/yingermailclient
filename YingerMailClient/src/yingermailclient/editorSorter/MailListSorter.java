package yingermailclient.editorSorter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import yingermailclient.model.Mail;

public class MailListSorter extends ViewerSorter {

	private int column;

	public void doSort(int column) {
		this.column = column;
	}
	
	@Override // 处理排序的方法
	public int compare(Viewer viewer, Object e1, Object e2) {
		Mail mail1 = (Mail) e1;
		Mail mail2 = (Mail) e2;
		switch (column) {
		case 2:
			return mail1.getSender().compareTo(mail2.getSender());
		case -2:
			return mail2.getSender().compareTo(mail1.getSender());
		case 4:
			return mail1.getDateString().compareTo(mail2.getDateString());
		case -4:
			return mail2.getDateString().compareTo(mail1.getDateString());
		case 6:
			return mail1.getSize().compareTo(mail2.getSize());
		case -6:
			return mail2.getSize().compareTo(mail1.getSize());
		default:
			return 0;
		}
	}

}
