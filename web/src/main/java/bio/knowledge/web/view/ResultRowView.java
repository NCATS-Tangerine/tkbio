package bio.knowledge.web.view;

import java.util.Date;
import org.ocpsoft.prettytime.PrettyTime;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

public class ResultRowView extends HorizontalLayout implements SearchResultView.Listener {

	private static final long serialVersionUID = -2841062072954936319L;

	private Label conceptLabel = new Label("Concept Name");
	private Label timeLabel = new Label("1 min ago");
	private Button detailsButton = new Button(FontAwesome.CHECK);
	private Button removeButton = new Button(FontAwesome.TIMES);

	private Date creationTime = new Date();
	PrettyTime p = new PrettyTime();
	
	private VerticalLayout titleLayout = new VerticalLayout();
	private HorizontalLayout buttonsLayout = new HorizontalLayout();
	
	public ResultRowView(String conceptName) {
		conceptLabel.setValue("<strong style = \"font-size: 120%;\">" + conceptName + "</strong>");
		conceptLabel.setStyleName("text-overflow");
		conceptLabel.setContentMode(ContentMode.HTML);
		
		timeLabel.setValue(p.format(creationTime));
		
		setSpacing(true);
		setSizeFull();
		
		titleLayout.addComponents(timeLabel, conceptLabel);
		titleLayout.setSizeFull();
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponents(detailsButton, removeButton);
		
		addComponents(titleLayout, buttonsLayout);
		setComponentAlignment(buttonsLayout, Alignment.BOTTOM_RIGHT);
		setExpandRatio(titleLayout, 1);
		initComponents();
	}
	
	private void initComponents() {
		detailsButton.setStyleName("page-button");
		detailsButton.addClickListener(e -> {
			Notification.show("Concept Details clicked!", Notification.Type.TRAY_NOTIFICATION);
		});

		removeButton.setStyleName("page-button");
		removeButton.addClickListener(e -> {
			Layout parent = (Layout) this.getParent();
			parent.removeComponent(this);
		});
	}
	
	@Override
	public void update() {
		timeLabel.setValue(p.format(creationTime));
	}
}
