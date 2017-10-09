# TKBio Project Layout

This is a guide to the different functions of TKBio's sub-projects. To see how TKBio relates to the rest of the NCATS Initiative, see the [README]().

TKBio uses an Model-View-Controller architecture at its core, while additionally using Spring Boot's Object-Relationship Mappign facilities, instigating the Service-Repository pattern as the primary abstraction around datasets.

All of these sub-projects are found as sub-folders in the project's root. They are listed in alphabetical order.

* **api/**
    * [Swagger]() generated API package.
* **client/**
    * The web client for the Swagger API.
* **core/**
    * Service layer for accounts, saved concept maps, and Knowledge Beacons; and the cache for shared data across modules.
* **database/**
    * Repository layer for accounts, saved concept maps, and Knowledge Beacons.
* **dataloader/**
    * Loading scripts for data into a Neo4j Database with a TKBio compatible format.
* **models/**
    * The Java Beans representing data types used in TKBio, including Concepts and Statements, or user accounts and concept maps.
* **selenium/**
    * Tests for front-end code. Undeveloped. Awaiting deprecation.
* **services/**
    * Services for the properties of a Concept or Statement collated from several sources.
* **web/**
    * Contains the front-end code for the page layouts, the table components, the concept map, and other UI components. Also user authentication.
    * **authentication**. Package for authentication management.
    * **graph**. Package for the Concept Map UI component. Contains server-side classes and client-side Javascript.
    * **grid**. Custom Grid component with infinite scrolling compatibility.
    * **renderer**. Button Renderer extension. Can handle custom HTML styling.
    * **validation**. Error handling replacing the Vaadin Validator component.
    * **design**. Component fields for different TKBio Views. **Do not edit directly.**
    * **view**. TKBio Views.
    * **components**. Extended Vaadin components for TKBio.
    * **ui**. The main layout and scope for TKBio Views.

There are additional folders that store documents related to building and deployment, but are not to be edited directly.

* **build/**
    * The location where JAR and WAR packages are stored by default as a result of a Gradle Build.
* **config/**
    * Both necessary configuration file templates, application.properties and ogm.properties, are located in here.
* **export/**
    * Environment variable setting scripts.
* **WebContent/**
    * A folder containing the "[Widgetset]()" compile targets for the Vaadin Framework. See [this]() documentation for how these files are generated.

In the **tkbio/** root folder there are scripts that are used for the build system.

* build.gradle
    * This is the primary build file for TKBio. It begins the chain of dependencies in the construction of different sub-projects. Triggering a Gradle Build on this file will compile the whole project into the *build/* folder.
* settings.gradle
    * Declares the names of the different sub-projects to Gradle.
* Dockerfile
    * The script to be run before a Docker deployment. Does some reorganizing of the files in *build/* and remaps the port of the application in a Docker container.