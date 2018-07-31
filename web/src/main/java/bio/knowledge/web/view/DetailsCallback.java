package bio.knowledge.web.view;

import java.util.List;
import java.util.Map;

import bio.knowledge.client.ApiCallback;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.model.BeaconConceptWithDetails;
import bio.knowledge.client.model.BeaconStatementDetails;

public class DetailsCallback {
	
	public ApiCallback<BeaconConceptWithDetails> createConceptDetailsCallback(ConceptDetailsWindow window) {
		return new ApiCallback<BeaconConceptWithDetails>() {
	
			@Override
			public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
				if (window.getUI() != null) {
					window.getUI().access(() -> {
						if (window.getUI() != null) {
							window.showError(e.toString());
						}
					});
				}
			}
	
			@Override
			public void onSuccess(BeaconConceptWithDetails result, int statusCode,
					Map<String, List<String>> responseHeaders) {
				if (window.getUI() != null) {
					window.getUI().access(() -> {
						if (window.getUI() != null) {
							window.showDetails(result);
						}
					});
				}
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
	
	public ApiCallback<BeaconStatementDetails> createStatementDetailsCallback(StatementDetailsWindow window) {
		return new ApiCallback<BeaconStatementDetails>() {
	
			@Override
			public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
				if (window != null) {
					window.getUI().access(() -> {
						if (window != null) {
							window.showError(e.toString());
						}
					});
				}
			}
	
			@Override
			public void onSuccess(BeaconStatementDetails result, int statusCode,
					Map<String, List<String>> responseHeaders) {
				if (window != null) {
					window.getUI().access(() -> {
						if (window != null) {
							window.showDetails(result);
						}
					});
				}
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
