package bio.knowledge.web.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.http.HttpStatus;

import com.vaadin.ui.Label;

import bio.knowledge.client.model.BeaconClique;
import bio.knowledge.client.model.BeaconCliquesQuery;
import bio.knowledge.client.model.BeaconCliquesQueryBeaconStatus;
import bio.knowledge.client.model.BeaconCliquesQueryResult;
import bio.knowledge.client.model.BeaconConceptsQuery;
import bio.knowledge.client.model.BeaconConceptsQueryBeaconStatus;
import bio.knowledge.client.model.BeaconConceptsQueryResult;
import bio.knowledge.service.beacon.KnowledgeBeaconService;
import bio.knowledge.web.view.components.QueryPollingListener;

public class ConceptsQueryListener implements QueryPollingListener {

	KnowledgeBeaconService kbService;
	
	private Label timeLabel = new Label("time ago");
	private Date creationTime = new Date();
	private PrettyTime p = new PrettyTime();
	
	private SingleSearchHistoryView presenter;
	private BeaconConceptsQuery conceptsQuery;
	private BeaconCliquesQuery cliquesQuery;
	private List<Integer> beacons;
	
	private AtomicBoolean isDone = new AtomicBoolean(false);
	
	public ConceptsQueryListener(BeaconConceptsQuery conceptsQuery, BeaconCliquesQuery cliquesQuery, List<Integer> beacons, SingleSearchHistoryView presenter) {
		this.presenter = presenter;
		this.kbService = presenter.getKbService();
		this.conceptsQuery = conceptsQuery;
		this.cliquesQuery = cliquesQuery;
		this.beacons = beacons;
		
		isDone = new AtomicBoolean(false);
	}
	
	@Override
	public void update() {
		timeLabel.setValue(p.format(creationTime));
		if (isDone.get()) {
			return; 
		}

		List<BeaconConceptsQueryBeaconStatus> beaconStatuses = null;
		boolean isDataReady = checkCliquesStatus();
		if (conceptsQuery != null) {
			beaconStatuses = kbService.getConceptsQueryStatus(conceptsQuery.getQueryId(), beacons);
			isDataReady = isDataReady && checkConceptsStatus(beaconStatuses);
		}
		
		if (isDataReady) {
			
			if (conceptsQuery != null) {
				beacons = getBeaconsWithResults(beaconStatuses);
				Optional<BeaconConceptsQueryResult> results = kbService.getConcepts(conceptsQuery.getQueryId(), beacons, 1, 1000);
				presenter.setConcepts(results);
			}
			
			if (cliquesQuery != null) {
				Optional<BeaconCliquesQueryResult> results = kbService.getCliques(cliquesQuery.getQueryId());
				presenter.setConceptsFromCliques(results);
			}
			
			isDone.compareAndSet(false, true);
		}
	}

	private boolean checkCliquesStatus() {
		boolean ready = true;
		
		if (cliquesQuery != null) {
			for (BeaconCliquesQueryBeaconStatus status : kbService.getCliquesQueryStatus(cliquesQuery.getQueryId())) {
				if (status.getStatus() == HttpStatus.PROCESSING.value()) {
					ready = false;
					break;
				}
			}
		}
		return ready;
	}

	@Override
	public boolean isDone() {
		return isDone.get();
	}
	
	private boolean checkConceptsStatus(List<BeaconConceptsQueryBeaconStatus> beaconStatuses) {
		boolean ready = true;
		for (BeaconConceptsQueryBeaconStatus status : beaconStatuses) {
			if (status.getStatus() == HttpStatus.PROCESSING.value()) {
				ready = false;
				break;
			}
		}
		return ready;
	}
	
	private List<Integer> getBeaconsWithResults(List<BeaconConceptsQueryBeaconStatus> beaconStatuses) {
		List<Integer> beacons = new ArrayList<>();
		for (BeaconConceptsQueryBeaconStatus beaconStatus : beaconStatuses) {
			int status = beaconStatus.getStatus();
			if (status == HttpStatus.OK.value() || status == HttpStatus.CREATED.value()) {
				beacons.add(beaconStatus.getBeacon());
			}
		}
		return beacons;
	}
}
