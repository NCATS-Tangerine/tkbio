package bio.knowledge.web.view.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.http.HttpStatus;

import bio.knowledge.client.ApiCallback;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.model.BeaconConcept;
import bio.knowledge.client.model.BeaconStatementsQuery;
import bio.knowledge.client.model.BeaconStatementsQueryBeaconStatus;
import bio.knowledge.client.model.BeaconStatementsQueryResult;
import bio.knowledge.service.beacon.KnowledgeBeaconService;
import bio.knowledge.web.view.StatementsViewPresenter;

public class StatementsQueryListener implements QueryPollingListener {
	
	KnowledgeBeaconService kbService;
	
	private ApiCallback<BeaconStatementsQueryResult> cb;
	private StatementsViewPresenter presenter;
	private BeaconConcept sourceConcept;
	private BeaconStatementsQuery query;
	private List<Integer> beacons;
	private AtomicBoolean isInProgress = new AtomicBoolean(false);
	private AtomicBoolean isDone = new AtomicBoolean(false);
	
	public StatementsQueryListener(BeaconStatementsQuery query, List<Integer> beacons, StatementsViewPresenter presenter) {
		this.presenter = presenter;
		this.kbService = presenter.getKbService();
		this.query = query;
		this.beacons = beacons;
		sourceConcept = presenter.getCurrentConcept();
	}
	
	@Override
	public boolean isDone() {
		return isDone.get();
	}
	
	@Override
	public void update() {
		if (isInProgress.get() || isDone.get()) {
			return;
		}
		List<BeaconStatementsQueryBeaconStatus> beaconStatuses =  kbService.getStatementsQueryStatus(query.getQueryId(), beacons);
		boolean isDataReady = checkStatus(beaconStatuses);
		if (isDataReady) {
			List<Integer> beacons = getBeaconsWithResults(beaconStatuses);
			isInProgress.compareAndSet(false, true);
			kbService.getStatementsAsync(query.getQueryId(), beacons, 1, 100, createCallback());
		}
	}

	private boolean checkStatus(List<BeaconStatementsQueryBeaconStatus> beaconStatuses) {
		boolean ready = true;
		for (BeaconStatementsQueryBeaconStatus status : beaconStatuses) {
			if (status.getStatus() == HttpStatus.PROCESSING.value()) {
				ready = false;
				break;
			}
		}
		return ready;
	}
	
	private List<Integer> getBeaconsWithResults(List<BeaconStatementsQueryBeaconStatus> beaconStatuses) {
		List<Integer> beacons = new ArrayList<>();
		for (BeaconStatementsQueryBeaconStatus beaconStatus : beaconStatuses) {
			int status = beaconStatus.getStatus();
			if (status == HttpStatus.OK.value() || status == HttpStatus.CREATED.value()) {
				beacons.add(beaconStatus.getBeacon());
			}
		}
		return beacons;
	}
	
	private ApiCallback<BeaconStatementsQueryResult> createCallback() {
		return new ApiCallback<BeaconStatementsQueryResult>() {

			@Override
			public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
				// TODO Auto-generated method stub
				e.printStackTrace();
				isDone.compareAndSet(false, true);
				isInProgress.compareAndSet(true, false);
				
			}

			@Override
			public void onSuccess(BeaconStatementsQueryResult result, int statusCode,
					Map<String, List<String>> responseHeaders) {
				isDone.compareAndSet(false, true);
				isInProgress.compareAndSet(true, false);
				presenter.addStatements(sourceConcept, result.getResults());
			}

			@Override
			public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
}
