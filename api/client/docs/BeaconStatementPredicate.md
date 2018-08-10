
# BeaconStatementPredicate

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**edgeLabel** | **String** | The predicate edge label associated with this statement, which should be as published by the /predicates API endpoint and must be taken from the minimal predicate (&#39;slot&#39;) list of the [Biolink Model](https://biolink.github.io/biolink-model).  |  [optional]
**relation** | **String** | The predicate relation associated with this statement, which should be as published by the /predicates API endpoint with the preferred format being a CURIE where one exists, but strings/labels acceptable. This relation may be equivalent to the edge_label (e.g. edge_label: has_phenotype, relation: RO:0002200), or a more specific relation in cases where the source provides more granularity (e.g. edge_label: molecularly_interacts_with, relation: RO:0002447) |  [optional]
**negated** | **Boolean** | (Optional) a boolean that if set to true, indicates the edge statement is negated i.e. is not true  |  [optional]



