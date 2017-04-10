#JUnit Test Dependencies#

1) You need to have the relevant version of Neo4j installed and running 
locally (e.g. release 3.0.3 as of July 2016)
   
2) You need to have your configuration files - 
application-dev.properties and ogm.properties - properly configured 
to point to this local Neo4j instance. 

NOTE: Be careful: ideally, configuration files in web/src/test/resources/ should be used, but the tests may inadvertently use your regular configuration files to run, hence, may point to your production database, not the local copy for testing!

3) The tests may be run by selecting the src/test/java source folder 
and executing "Run As..JUnit Test"

#Troubleshooting#

Before running the tests, you should generally do an Eclipse "Project..Clean..." and "Gradle(STS)..Refresh All", followed by a fresh Gradle build:

	gradle clean war
	
Sometimes, running the gradle build generates a complaint about a missing 'WebContent' folder. This is probably a bug in the build system, not the application since rerunning the build results in a clean build.

However, rarely, if you've had previous releases of Neo4j running in some of your projects, Gradle may not be meticulous in refreshing transitive dependencies. This may result in puzzling "NoSuchClass" or "NoSuchMethod" errors.

Surprising as this may be, these problems might be cleared up by simply going into your 
local Gradle cache and deleting older Neo4j dependency folders, e.g. under Windows

      C:\Users\<your-user-name>\.gradle\caches\modules-2\files-2.1\org.neo4j

(or the equivalent location under OSX or Linux) and deleting older versions of Neo4j 
artifacts (i.e. older than Neo4j 3.0.3). BTW, some Neo4j subprojects like OGM have a 
different numbering (e.g. 2.0.3) so do not need purging....

