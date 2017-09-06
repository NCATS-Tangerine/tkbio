# The NCAT "Translator" Knowledge.Bio Release 4.0 Java Implementation

TODO: Edit image?
![Knowledge Graph for Sepiapterin Reductase](https://tkbio.ncats.io/VAADIN/themes/kb2/images/usecase.png)

This is Knowledge.Bio, a Java-based web application for discovering, navigating, harvesting and organizing conceptual maps of (biomedical) research knowledge. Try it out: tkbio.ncats.io

The software is a US [National Institute of Health](https://www.nih.gov/) funded project initiated in 2015, as a collaboration between...

* The [Scripps Research Institute](http://www.scripps.edu/) (TSRI), San Diego, CA, USA:

	Dr. Benjamin Good, Project Owner

* [STAR Informatics/Delphinai Corporation](https://starinformatics.com), Port Moody, BC, Canada:	
 
	Dr. Richard Bruskiewich, CEO (2015 - present)
	Kenneth Huellas-Bruskiewicz (2015 - present)
	Lance Hannestad (2016,2017)
	Farzin Ahmed (2015 - 2016)
	Chandan Kumar Mishra (2016)
	Jarielle Lim (2016)
	Yinglun "Colin" Qiao (2016)
	Rudy Kong Tin Lun (2016)

The current (2017) effort to extend Knowledge.Bio is in collaboration with the [Lawrence Berkeley Laboratory](http://www.lbl.gov/), San Francisco, CA, USA, care of

    Dr. Christopher Mungall

repurposing it as a tool for the [NIH NCATS Translator initiative](https://ncats.nih.gov/translator) with direct funding brokered by NCATS Principal Investigator,

	Dr. Christopher Chute

and others at the [INSTITUTE for CLINICAL & TRANSLATIONAL RESEARCH](https://ictr.johnshopkins.edu/) (ICTR), Johns Hopkins University � School of Medicine, Baltimore, MD, USA. 

# Table of Contents
#### [Introduction]()
#### [Quickstart]()
#### [Web Client Configuration]()
#### [Docker Deployment]()
#### [Localhost Deployment]()
#### [Troubleshooting]()
#### [Other links]()

# Introduction

As a project of the NCATS Translator Initiative, Knowledge.Bio is a tool for finding and mapping relationships between concepts extracted from the biomedical literature. These links and properties can be visualized, then followed or enumerated further. Researchers can directly access supporting references for a relationship from within the interface. This aides researchers in their ablity to keep track of the assocations between biomedical concepts, create webs of associations that may represent a new hypothesis, evaluate the strength of these assertions, and have jumping-off points for further exploration. 

Below is an example of a map created for Sepiapterin Reductase.

![Knowledge Graph for Sepiapterin Reductase](https://tkbio.ncats.io/VAADIN/themes/kb2/images/usecase.png)

Knowledge.Bio requires three parts to function: "Knowledge Beacons", which broadcast biomedical data according to the NCATS API and typically wrap databases; the "Beacon Aggregator", which puts the data together in one place; and the web client itself, whose purposes are outlined above. This architecture is illustrated in the image below. We provide a link to the Github projects for each of these modules [at the bottom of this document](). 

![How NCATS integrates to Knowledge.Bio](https://tkbio.ncats.io/VAADIN/themes/kb2/images/beacon-architecture-final.png)

All of these systems can be built and run locally, or otherwise point to remote versions. See the "Beacon Aggregator Setup" and "Reference Beacon Setup" guides if you would like to develop or host those components on your machine. If you have a dataset that you would like to use in particular with Knowledge.Bio, see the "Database Setup and Dataloading" guide.

This particular document outlines the process of getting your own copy of the web client up and running. However, for remote hosting, see the "Remote Deployment" guide for how to host AWS/Tomcat instances as well as deployment with Docker.

# Web Client Configuration
## Pre-Requisites

Knowledge.Bio Version 4.0 is a multi-project [Gradle](https://gradle.org/) build of Java components, as defined in multiple subdirectories, and based on [Spring Boot](https://projects.spring.io/spring-boot/) and [Spring Authentication](http://projects.spring.io/spring-security/); the [Neo4j graph database](https://neo4j.com), connected to Spring via [Spring Data Neo4j](http://projects.spring.io/spring-data-neo4j/) using a Neo4j Object Graph Mapping (OGM); plus the [Vaadin](https://vaadin.com/home) GUI framework. The current release dependencies for this technology stack are found in the tkbio [master build.gradle](https://github.com/NCATS-Tangerine/tkbio/blob/develop/build.gradle) file.

The application has been developed within the [Eclipse J2EE](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/keplersr2) IDE. We don't insure other IDEs. Core prerequisites to build the system within Eclipse include:

* [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) version 8 or greater
* [Gradle](https://gradle.org/install/) version 2.9 or greater

The Gradle build process imports the majority of our dependencies. However, before attempting to run the software, you should install a local copy of [Neo4j Commuity Edition](https://neo4j.com/download/) edition on your development machine (see the root directory's build.gradle file for the preferred current release of Neo4j being used).

## How to Install and Run Locally

1. Before starting, ensure that you install the Gradle-based [Buildship tool](https://projects.eclipse.org/projects/tools.buildship) for Eclipse into your Eclipse IDE. You should also install the latest Gradle version from gradle.org and configure the Buildship tool, in the Eclipse "Window..Preferences" to point to the Gradle distribution folder.

2. Clone the TKBio project from the project repository into a local Git repository on your workstation.

3. "Import from..Git Repository" to import it into the Eclipse IDE. You should be able to use the "Import Existing Eclipse Project" wizard option to create it and select "Search for nested projects". This should import the project as a working "Gradle" managed project and all the subprojects should appear as separate Eclipse projects within the package manager

Before working with the code, Eclipse might show "Problems" tab errors. These do not usually affect the Gradle build process and execution of the application.

## Spring and Neo4j Configuration

In order to run the application components of Knowledge.Bio, some basic configuration variables need to be set. The Spring Framework has multiple technical options for achieving this - consult the reference documentation for full details. These are found under the **"src/main/resources"** folders as a set of Java properties (or equivalent) files.

That said, the project provides templates for a default set of Java Properties-based configuration files for itself, under the **"tkbio/config"** folder. Copy the template files, **removing** the "-template" suffix as you copy them into the necessary resources folders. These folders are labeled as **"src/main/resources"** in all of **"tkbio/web/"**, **"tkbio/database/"**, and **"tkbio/dataloader/"**.

As of August 2017, these are two files required and given templates. The main configuration file is the **applications.properties**, containing the means for linking to other NCAT project services. Neo4j uses the **ogm.properties file**, necessary for linking the client to the database.

You don't need to modify most of these properties, but there are fields which need customization, as follows.

In such "profile" applications there are site-specific database-connection parameters, and "administrative email" settings file. Once defined, you need to specify which particular profile you want to use at startup, by setting the "spring.profiles.active" variable in the applications.properties file.

Particular parameters set in application.properties:

* _spring.profiles.active_ - sets the profile (e.g. 'dev')
* _spring.mail.host_ - the SMTP server being used to forward admin emails (e.g. smtp.gmail.com)		
* _spring.mail.username_ - username of the email account being used for forwarding admin emails
* _spring.mail.password_ - password of the email account being used for forwarding admin emails
	
One must set up a path to the Knowledge Beacons you will be using for the project in the **application.properties** file for 'web'. This property, _knowledgeBeacon.table.path_, must point to a csv file that contains a URL pointing to a valid Knowledge Beacon api. One csv file is provided for example by default in the 'config' folder.

For the Neo4j back end database, some basic configuration is required. Since the current implementation uses Spring Data 4.0, a remote Neo4j server is assumed to be installed and on localhost port 7474. 

In addition, this release uses the Object Graph Model (OGM) protocol, so an additional configuration file is needed, called 'ogm.properties'. A templated version of the file is under **"src/web/main/resources/config"**. You should substitute your Neo4j server's 'user' and 'password' in the URI parameter, e.g. in

	URI=http://user:password@localhost:7474

for example,

    URI=http://neo4j:neo4j@localhost:7474

where neo4j is the default for both user and password. 

## Running Project Tests

[JUnit 4](http://junit.org/junit4/) tests are available for some of the code base (e.g. the database subproject). Although the test dependencies are covered by the Gradle build dependencies, to run the tests properly within Eclipse, you might need more local configuration, as follows:

* Right click your project in Package Explorer > click Properties
* Go to Java Build Path > Libraries tab
* Click on *Add Library* button
* Select JUnit 4
* Click *Next*.

The following requirements must be fulfilled:

1. You need to have the relevant version of Neo4j installed and running locally.

2. You need to have your configuration files - application-dev.properties and ogm.properties - properly configured to point to this local Neo4j instance. 

Ideally, configuration files in web/src/test/resources/ should be used, but the tests may inadvertently use your regular configuration files to run, hence, may point to your production database, not the local copy for testing!

The tests may be run by selecting the src/test/java source folder and executing "Run As..JUnit Test".

## Loading Data

For the application to work, you need to either point to an existing Knowledge.Bio database somewhere, or load some data into your local copy of the Neo4j database. 

A sample dataset based on "diabetes" disease concepts with some associated gene and drug conceptual relationships is provided under the project **"dataloader/src/test/resources/data/"** subfolder. These files are MySQL TKBio1 data model structured text dump of data.

See the DATALOADING.md under **"dataloader/"** subfolder for details about how to load the sample data sets. The same documentation gives information about how to load a full production database as well as a local one. 

For this dataset, you will need to type "diabetes" in the search box of the application to start your search.

# How to Run Knowledge.Bio on a Docker Container

[Docker](https://www.docker.com/) is a virtualization framework that let you run self-contained instances of web applications (and others). Knowledge.Bio supports Docker out of the box. We encourage using Docker due to its versitality in both local and remote deployments. Before looking at how to deploy Knowledge.Bio with Docker, be sure that the latest version is installed. 

If you are using OS X or Windows, make sure the Docker daemon is running by using [Docker Tools](https://docs.docker.com/machine/overview/), running both `docker-machine start <your-machine>` and `eval $(docker-machine env <your-machine>)` (replacing *<your-machine>* with the name of your machine) in your console.

Then ensure that you know the internal IP of your by printing the configuration; you can use `cat docker-machine env <your-machine>` to find this IP for the property DOCKER_HOST. This will be used for accessing the deployment later.

**TODO: Exposing Volumes**

1. Build the application using `gradle clean war` in its root folder. You may also build the project in Eclipse using the Context Menu. *Run as -> Gradle Build... -> type in "clean war"*. Either of these commands will build your project and sub-projects into a WAR package, located in the **"tkbio/web/build/libs"** folder.

2. Make sure that you have installed, or have access to, a running installation of Neo4j release 2.3.* or greater. Open Neo4j Community Edition GUI and select the folder where the database has been saved. The first time you run it, open the the link provided (by default, localhost:7474) and change default password to a suitable password, which you will record in the application configuration files. Otherwise use the command line.

3. Use `docker build -t ncats:tkbio .` to build your application as a Docker image. To check whether it was successfully created, run `docker images #`. You should see "ncats" listed as an image.

4. Finally, use `docker run --rm -p 8079:8080 ncats:tkbio &` to (a) run the application, and (b) remap the container app's port to port 8079 of the broadcasted IP.

You should now be able to access Knowledge.Bio in the browser via port 8079 of your machine's internal IP (example, http://192.168.99.100:8079). You can replace that port number with whatever you like.

# How to Run Knowledge.Bio Locally

If you don't want to run Knowledge.Bio in Docker for local development, you can run the application in bare-bones Eclipse:

1. Build the project using `gradle clean war` or using the Eclipse context menu on the root folder of the project, as outlined above.

2. In the Eclipse context menu on the root project, click "Gradle (STS)..Refresh All" for all the projects to pull in dependencies.

3. As in the Docker deployment above, make sure that you have access to a running installation of Neo4j. Open up Neo4j Community Edition GUI and select the folder where the database has been saved and ensure proper password configuration. Otherwise use the command line.

4. Select the "bio.knowledge.Application" class located in the **"web/src/main/java/"** subfolder, then "Run As..Java Application". Ensure that your Neo4J database is running before this point!

5. Open up "http://localhost:8080" in your browser. Voila. If you want to access the database from a non-localhost IP, you need to go into the neo4j-server.properties or (in Linux environments, the `/etc/neo4j/neo4j.conf` file) to uncomment out the org.neo4j.server.webserver.address=0.0.0.0 (or dbms.connector.http.address=0.0.0.0 in the Linux file).

# Troubleshooting

Gradle and the Eclipse IDE live in an uneasy truce. Sometimes, when you are experiencing strange "NoSuchClassFound" errors, that suggests inconsistent library releases clashing with one another. In such circumstances, it is helpful to review the whole hierarchy of Gradle dependencies by running:

	gradle dependencies

then update these dependencies to the latest versions, as required. Afterwards, run a 

	Project..Clean...

from the popup menu. For good measure, close then reopen the root project and its related sub-projects. When desperate, exit Eclipse then restart it, then rebuild the project. This magical mixture of activities can purge the system to give a cleaner build.

# Other links

The following are links relevant for developing and maintaining Knowledge.Bio.

* **[Project Layout]()**. The purposes of the different sub-modules and scripts of Knowledge.Bio.
* **[Alternative Remote Deployments]()**. How to deploy Knowledge.Bio on Tomcat and Amazon Web Services.
* **[Vaadin Development]()**. A set of tips on how to deal with Vaadin's quirks when developing for Knowledge.Bio.

These links are for other projects which give Knowledge.Bio its back-end, as was [illustrated above]().

* **[Beacon Registry](https://github.com/NCATS-Tangerine/translator-api-registry)**. The configuration that Knowlege.Bio's Beacon Aggregator remotely calls to find data beacons for its search results.
* **[Translator Knowledge Beacon](https://github.com/NCATS-Tangerine/translator-knowledge-beacon)**. Template project forming the basis of all Beacons, including potentially your own. **Includes a list of existing Beacons**.
* **[Beacon Aggregator](https://github.com/NCATS-Tangerine/beacon-aggregator)**. Collects Beacon results.
* **[Beacon Validator](https://github.com/NCATS-Tangerine/beacon-validator)**. Ensures that a Beacon has fulfilled all the requirements of the API.
