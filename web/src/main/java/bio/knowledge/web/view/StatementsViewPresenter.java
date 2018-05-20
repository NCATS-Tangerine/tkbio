package bio.knowledge.web.view;

import java.util.List;
import java.util.Set;

import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SingleSelectionModel;

import bio.knowledge.client.model.BeaconConcept;
import bio.knowledge.client.model.BeaconStatementsQuery;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.beacon.KnowledgeBeaconService;

public class StatementsViewPresenter {

	private static final long serialVersionUID = -6383744406771442514L;

	private StatementsView statementsView;
	private Set<Object> selected;
	private KnowledgeBeaconService kbService;
	private KBQuery kbQuery;
	private BeaconStatementsQuery statementsQuery;

	public StatementsViewPresenter(StatementsView statementsView, KnowledgeBeaconService kbService, KBQuery kbQuery) {
		this.statementsView = statementsView;
		this.kbService = kbService;
		this.kbQuery = kbQuery;
		initConceptsSelect();
		initStatementsSelect();
	}

	/**
	 * Initializes the selection logic for the statements grid
	 */
	private void initStatementsSelect() {
		this.statementsView.getStatemtsGrid().addSelectionListener(e -> {
			Button addToGraphBtn = statementsView.getAddToGraphButton();
			selected = e.getSelected();
			if (selected.isEmpty()) {
				addToGraphBtn.setEnabled(false);
			} else {
				addToGraphBtn.setEnabled(true);
			}
		});
	}

	private void initConceptsSelect() {
		Grid conceptsGrid = this.statementsView.getConceptsGrid();
		conceptsGrid.addSelectionListener(e -> {
			SingleSelectionModel selectModel = (SingleSelectionModel) conceptsGrid.getSelectionModel();
			BeaconConcept selectedConcept = (BeaconConcept) selectModel.getSelectedRow();
			if (selectedConcept == null) {
				return;
			}
			statementsQuery = kbService.postStatementsQuery(selectedConcept.getClique(), null, null, null, kbQuery.getTypes(), kbQuery.getCustomBeacons());
		});
	}
}
