
# ServerStatementsQueryBeaconStatus

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**beacon** | **Integer** | Index number of beacon providing these statements  |  [optional]
**status** | **Integer** | Http code status of beacon API - 200 means &#39;data ready&#39;,  102 means &#39;query in progress&#39;, other codes (e.g. 500) are  server errors. Once a beacon has a &#39;200&#39; success code,  then the /statements/data endpoint may be used to retrieve it.  |  [optional]
**count** | **Integer** | When a 200 status code is returned, this integer designates  the number of statements matched by the query for the  given beacon.  |  [optional]



