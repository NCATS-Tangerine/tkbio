
# BeaconStatementDetails

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | Statement identifier of the statement made in an edge (echoed back)  |  [optional]
**keywords** | **List&lt;String&gt;** | &#39;keywords&#39; string parameter to API call, echoed back  |  [optional]
**pageNumber** | **Integer** | &#39;pageNumber&#39; string parameter to API call, echoed back  |  [optional]
**pageSize** | **Integer** | &#39;pageSize&#39; string parameter to API call, echoed back  |  [optional]
**isDefinedBy** | **String** | A CURIE/URI for the translator group that wrapped this knowledge source (&#39;beacon&#39;) that publishes the statement made in an edge.  |  [optional]
**providedBy** | **String** | A CURIE prefix, e.g. Pharos, MGI, Monarch. The group that curated/asserted the statement made in an edge.  |  [optional]
**qualifiers** | **List&lt;String&gt;** | (Optional) terms representing qualifiers that modify or qualify the meaning of the statement made in an edge.  |  [optional]
**annotation** | [**List&lt;BeaconStatementAnnotation&gt;**](BeaconStatementAnnotation.md) | Extra edge properties, generally compliant with Translator Knowledge Graph Standard Specification  |  [optional]
**evidence** | [**List&lt;BeaconStatementCitation&gt;**](BeaconStatementCitation.md) |  |  [optional]



