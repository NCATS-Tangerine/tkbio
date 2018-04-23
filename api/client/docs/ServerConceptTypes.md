
# ServerConceptTypes

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | the CURIE of the concept type (see [Biolink Model](https://biolink.github.io/biolink-model) |  [optional]
**iri** | **String** | the IRI of the concept type (see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of IRI) |  [optional]
**label** | **String** | the human readable label of the concept type (see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of concept type names)  |  [optional]
**description** | **String** | human readable definition assigned by the beacon for the specified concept type  |  [optional]
**beacons** | [**List&lt;ServerConceptTypesByBeacon&gt;**](ServerConceptTypesByBeacon.md) | list of metadata for beacons that support the use of this concept type  |  [optional]



