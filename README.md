#The NCAT "Translator" Knowledge.Bio Release 4.0 Java Implementation#

This is the Knowledge.Bio Java-based web application for discovering, navigating, harvesting and organizing conceptual maps of (biomedical) research knowledge.

The software is a US National Institute of Health funded project initiated in 2015, as a collaboration between 

The Scripps Research Institute (TSRI), San Diego, CA, USA:

	Dr. Benjamin Good, Project Owner

and the Canadian firm
 
STAR Informatics/Delphinai Corporation Team, Port Moody, BC, Canada:	
 
	Dr. Richard Bruskiewich, CEO (2015 - present)
	Kenneth Huellas-Bruskiewicz  (2015 - present)
	Lance Hannestad (2016,2017)
	Farzin Ahmed (2015 - 2016)
	Chandan Kumar Mishra (2016)
	Jarielle Lim (2016)
	Yinglun "Colin" Qiao (2016)
	Rudy Kong Tin Lun (2016)
 			
The current (2017) effort to extend Knowledge.Bio as a tool for the NIH NCATS Translator initiative, is a collaboration with 

	Dr. Christopher Mungall

of the Lawrence Berkeley Laboratory, San Francisco, CA, USA, with direct funding brokered by NCATS Principal Investigator, 

	Dr. Christopher Chute
	
and others at the INSTITUTE for CLINICAL & TRANSLATIONAL RESEARCH (ICTR), Johns Hopkins University � School of Medicine, Baltimore, MD, USA. 

##Pre-Requisites##

The Knowledge.Bio Version 4.0 is a multi-project Gradle build of Java components, as defined in several subdirectories, and based on the Spring Framework, especially, Spring Boot, the Neo4j graph database, connected to Spring via the Spring Data Neo4j using a Neo4j Object Graph Mapping (OGM), plus a Vaadin web graphical user interface front end. The current release dependencies for this technology stack is recorded in the tkbio master build.gradle file.

The application has been developed within the Eclipse J2EE IDE platform. Your mileage may vary for other Java IDE's.   Core prerequisites to build the system within Eclipse include:

* Java 8 JDK or better
* Gradle (recent release)

Most other dependencies are imported by the Gradle build process.  However, before attempting to run the software, you should generally install a local copy of Neo4j community edition on your development machine (again, see the build.gradle file for the preferred current release of Neo4j being used).

##How to Install and Run Locally##

1) Before starting, you should ensure that the Gradle Buildship tool for Eclipse is installed into your Eclipse IDE (find it on the Eclipse Marketplace). You should also likely install the latest Gradle version from gradle.org and configure the Buildship tool, in the Eclipse "Window..Preferences" to point to the Gradle distribution folder.

2) Git clone the TKBio project from the project repository into a local Git repository on your workstation.

3) "Import from..Git Repository" to import it into the Eclipse IDE. You should be able to use the "Import Existing Eclipse Project" wizard option to create it and select "Search for nested projects". This should import the project as a working "Gradle" managed project and all the subprojects should appear as separate Eclipse projects within the package manager

Note that Eclipse may or may not show a few "Problems" tab errors. These do not usually affect the Gradle build process and execution of the application.

##Spring and Neo4j Configuration##

In order to properly run various components of the Knowledge.Bio application (e.g. on a developer's local machine), some basic configuration variables need to be set.  The Spring Framework has many technical options for achieving this  - consult the reference documentation for full details.  These are typically found under the **".../main/resources"** folders as a set of Java properties (or equivalent) files.

That said, templates for a default set of Java Properties-based configuration files are provided for the project, under the **"tkbio/web/src/main/resources/config"** folder. Simply copy the template files, **removing** the "-template" suffix as you copy them into the various resources folders where they are needed. There are currently three such files required and for which templates are provided:

1. applications.properties
2. stormpath.properties
3. ogm.properties

The main configuration file is the *applications.properties*. Neo4j uses the *ogm.properties file*, which relates to the "Object Graph Mapping" component of Neo4j. The Stormpath-based user authentication uses the *stormpath.properties* file.

Template versions of such files are available and may also be copied, e.g. "application.properties-template" into "application.properties" and also customized. In principle, you could have distinct profiles for other contexts (e.g. "staging" or "production"). 

Note that there are actually several independent 'resources' folder contexts into which you need to place copies of these property files:

1. To run the JUnit tests, you'll need copies of the files in the "tkbio/database/src/main/resources" folder (or wherever else JUnit tests are provided in the future)

2. For the DataLoader application, you'll need to copy copies configuration property files into the "tkbio/dataloader/src/main/resources" folder

3. For the web application (copy the template configuration property files into the "tkbio/web/src/main/resources" folder

Most of the contents of these files can be kept unmodified, but there are a few properties which may need to be customized, as follows.

Typically put in such "profile" applications are site-specific database-connection parameters, and "administrative email" settings.  file.  Once defined, you need to specify which particular profile you want to use at startup, by setting the "spring.profiles.active" variable in the applications.properties file.

Particular parameters likely to be set in either file (but also review the others):

	spring.profiles.active - sets the profile (e.g. 'dev')
	
	spring.mail.host - the SMTP server being used to forward admin emails (e.g. smtp.gmail.com)
		
	spring.mail.username - username of the email account being used for forwarding admin emails
	
	spring.mail.password - password of the email account being used for forwarding admin emails
	
Importantly, one must set up a path to the Knowledge Beacons you will be using for the project in the application.properties file for 'web'. This property, 'knowledgeBeacon.table.path', must point to a csv file that contains a URL pointing to a valid Knowledge Beacon api. One such csv file is provided for example by default in the 'config' folder.

For the Neo4j back end database, some basic configuration is required. Since the current implementation uses Spring Data 4.0, a remote Neo4j server (latest release 3.1.* or better?) is assumed to be installed and on localhost port 7474.  

In addition, this release uses the Object Graph Model (OGM) protocol, so an additional configuration file is needed, generally called 'ogm.properties'. A templated version of the file is under src/web/main/resources/config. You should substitute your Neo4j server's 'user' and 'password' in the URI parameter, e.g. in

     URI=http://user:password@localhost:7474

for example,

     URI=http://neo4j:neo4j@localhost:7474

where neo4j is the default for both user and password. 

Note: the Neo4j server generally prompts you for a new password the first time you directly visit the http://localhost:7474 web link.

##Running Project JUnit Tests#

JUnit and related (e.g. Selenium?) tests are available for some of the code base (e.g. the database subproject). Although the test dependencies are covered, somewhat, by the Gradle build dependencies, to run the tests properly within Eclipse, you may need to do a bit more local configuration, as follows:

* Right click your project in Package Explorer > click Properties
* Go to Java Build Path > Libraries tab
* Click on 'Add Library' button
* Select JUnit 4
* Click Next.

##Loading Data##

Obviously, to work completely, you need to either point to an existing Knowledge.Bio database somewhere, or load some data into your local copy of the Neo4j database. 

A sample dataset based on "diabetes" disease concepts with some associated gene and drug conceptual relationships is provided under the project "tkbio/dataloader/src/test/resources/data" subfolder. These files are MySQL TKBio1 data model structured text dump of data.

See the DATALOADING_R2.3_README.md under ~/tkbio/dataloader subfolder for details about
how to load the sample data sets. In the same subfolder, a second PRODUCTION_DATABASE_README
gives additional information about how to load a full production database. 

For this dataset, type "diabetes" in the search box to start your search.

##How to Run the Knowledge.Bio Web Application Locally##

1) Make sure that you have installed, or have access to, a running installation of Neo4j release 2.3.* or better (i.e. can install locally or point to a remote version). Open neo4j community edition GUI and select the folder where the database has been saved. The first time you run it, open the the link provided (usually localhost:7474/) and change default password to a suitable password, which you will record in the application configuration files (next step).

2) Run "Gradle(STS)..Refresh All" for all the projects to pull in dependencies (assuming that your Gradle environment is set up as per standard Eclipse Gradle practices).

3) From the root tkbio project directory, Run as -> Gradle Build... -> type in "clean war" - this will build your project and sub-projects, culminating in a 'war' file, located in the
tkbio/web/build/libs folder. This 'war' file is deployable to a web application container like Apache Tomcat (e.g. running in the cloud, a.k.a. Amazon Web Services Elastic Beanstalk).

4) Making sure that the Neo4j server is running already and pointing to a copy of the database containing some data (see above), select the "bio.knowledge.Application" class located in the "web" project and "/src/main/java/"  subfolder, then "Run As..Java Application".  

5) Pointing to the URL "http://localhost:8080" should display your (locally) running copy of TKBio. If you want to access the database from a non-localhost IP, you need to go into the neo4j-server.properties or (in Linux environments, the /etc/neo4j/neo4j.conf file) to uncomment out the org.neo4j.server.webserver.address=0.0.0.0 (or dbms.connector.http.address=0.0.0.0 in the Linux file).

##Running in Eclipse-WTP and on Amazon Web Services##

The application may be run as a WAR file in an external web application container run from the Eclipse-WTP framework. Locally, this could be a Tomcat 8 (with Java 8 JRE). Remotely, it may be run inside an AWS ElasticBeanstalk environment.

To set things up, you first need to configure the 'web' subproject as an Eclipse WTP project by running the following Gradle tasks:

	gradle :web:eclipseWtpComponent :web:eclipseWtpFacet

You will likely also wish to set the web application to appear as the "root" project. This is achieved by going to the popup-up menu associated with the application's "web" subproject, then selecting:

	Properties..Web Project Settings..Context Root

setting the value to a forward slash ('/'). 

After setting things up, you can:

	Run As..Run on Server
	
, once again, selected from the "web" subproject popup-up menu. This opens a wizard dialog that allows you to create a new local Tomcat 8 web server instance (ensure that it is specified with a Java 8 JRE) from within the Eclipse Servers View window,  then add the 'web' subproject to the web container.  Notes that the application's properties files still need to be properly configured, as before. 

If the Tomcat server doesn't start up, you can select it in the "Servers" tab and display the popup-menu to click the "Start" menu item. 

Whenever several significant application changes are made to the code, including changed dependencies, and you encounter difficulties running it, you can try to "Remove" the "web" subproject by selecting it under the given Tomcat server entry in the "Servers", then performing the "Run on Server" again to add it back into the web server.

##Running in Amazon Web Services Elastic Beanstalk##

If you have successfully

	Run As..Run on Server 

the application locally in the Eclipse WTP on Tomcat 8,  you can probably attempt to run the application in the Amazon Web Services cloud using Elastic Beanstalk (EB).  Refer to the EB documentation for full configuration details. Here, we'll simply outline TKBio application specific considerations.

The AWS plug-in integration with Eclipse is good, but not without some occasional cantankerous behaviour. We've found it a bit easier to:

1) build the WAR locally then 

2) Create an EB Environment using that WAR uploaded to AWS within an existing EB Application then 

3) Configure the EB instance to suit: a t2-micro is usually sufficient for the application at the present time, assuming that the Neo4j database resides on a 2nd (pre-configured) server.  You will also want your AWS IAM credentials to be configured with an EB-service access policy sufficiently lenient to make the new EB application and environment visible to you (ask your local AWS admin for help).

For the deployment itself, first of all, you should ensure that the application properties files are present and properly configured. In particular, you should point the Neo4j web URL to a distinct running instance of Neo4j running somewhere on the internet, preferably, on another AWS EC2 instance or server otherwise publicly visible on the web. Make sure that the username and password reflect the credentials of that remote Neo4j instance. 

If the Neo4j server is not completely open to the world (i.e. if there are firewall settings), then one key step is to (eventually) add the public IP address of the EB EC2 instance that is created, to the Neo4j EC2 instance firewall, for custom TCP access through the Neo4j assigned ports 7474 and 7687. The former port number is the web browser access port; the latter is a hidden communication port now used in Neo4j release 3.1 and above for 'BOLT' communication (see the Neo4j docs for information on BOLT).

As before, you should first regenerate the web WAR file using the Gradle 'war' target (which is deposited into the /tkbio/web/build/libs folder). 

Next, you can directly create the environment on the AWS Dashboard then upload the newly generated WAR file directly up to EB, as the source application. Complete the configuration as per your general requirements to achieve a reliable initial deployment of the application.

After the EB environment is running, you can "Run As..Run on Server" using the existing Application and Environment as the source of the configuration. You might first want to restart Eclipse so that the AWS Explorer view gets properly reinitialized and discovers the new EB application and environment.  From that point onwards, you can republish your application periodically as you make coding progress.

##Troubleshooting##

Gradle and the Eclipse IDE live a mildly uneasy truce. Sometimes, when you are experiencing strange "NoSuchClassFound" errors, that suggests inconsistent library releases clashing with one another. In such circumstances, it is helpful to carefully review the whole hierarchy of Gradle dependencies by running:

	gradle dependencies

then update these dependencies to the latest versions, as required.  Afterwards, run a 

	Project..Clean...

and

	Gradle (STS)..Refresh All  

from the popup menu. For good measure, it sometimes also helps to close then reopen the root project and its related sub-projects. When desperate, exit Eclipse then restart it, then rebuild the project.  This magical mixture of activities often helps to purge the system to give a cleaner build.