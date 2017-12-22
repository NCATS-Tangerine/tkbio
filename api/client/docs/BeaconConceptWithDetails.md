
# BeaconConceptWithDetails

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**clique** | **String** | CURIE identifying the equivalent concept clique to which the concept belongs.  |  [optional]
**name** | **String** | Canonical human readable name of the key concept of the clique  |  [optional]
**type** | **String** | Concept semantic type as a CURIE into a data type ontology  |  [optional]
**taxon** | **String** | NCBI identifier of Taxon associated the concept (if applicable)  |  [optional]
**aliases** | **List&lt;String&gt;** | set of alias CURIES in the equivalent concept clique of the concept  |  [optional]
**entries** | [**List&lt;BeaconConceptBeaconEntry&gt;**](BeaconConceptBeaconEntry.md) | List of details specifically harvested from beacons, indexed by beacon  |  [optional]



