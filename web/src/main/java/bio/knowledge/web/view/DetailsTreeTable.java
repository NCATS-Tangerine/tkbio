package bio.knowledge.web.view;

import com.vaadin.ui.TreeTable;

public class DetailsTreeTable extends TreeTable {
	
	private static final long serialVersionUID = -2510115575396479636L;

	public DetailsTreeTable(String caption) {
		super(caption);
		this.addContainerProperty("Title", String.class, "");
		this.addContainerProperty("Value", String.class, null);
		this.setSortEnabled(false);
		this.setWidth(98, Unit.PERCENTAGE);
	}
	
	/**
	 * Adds an expanded row with given Title, and "--" in the value column
	 * @param title
	 * @return id of the added row
	 */
	public Object addTitleRow(String title) {
		Object titleRow = addItem(new Object[] {title, "--"}, null);
		setCollapsed(titleRow, false);
		return titleRow;
	}
	
	/**
	 * Adds an expanded row with given Title, parent, and "--" in the value column
	 * @param title
	 * @param parent
	 * @return id of the added row
	 */
	public Object addTitleRow(String title, Object parent) {
		Object titleRow = addTitleRow(title);
		setParent(titleRow, parent);
		return titleRow;
	}
	
	/**
	 * Adds row with no children with given title, value, and parent
	 * @param title
	 * @param value
	 * @param parent
	 */
	public void addInfoRow(String title, String value, Object parent) {
		Object newRow = addItem(new Object[] {title, value}, null);
		this.setParent(newRow, parent);
		this.setChildrenAllowed(newRow, false);
	}


}
