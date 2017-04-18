## Vagrant instructions:
		* Install Vagrant with an associated a virtualization application (e.g. VirtualBox, VMWare, etc.) on your local machine.

		* You need to download fresh copies of the Oracle JDK release 1.8 and Neo4j Community Server 3.* (where * should be a recent version, e.g. 3.0.3) and unzip these inside the 'export' directory of the main project root

		* Using Eclipse, make **copies** of all the "*.properties-template" files located in web/src/main/resources, as the corresponding *.properties files, and customize internal settings to suit your local installation.
		
		* Open terminal/command prompt on the kb2 directory.
		
		* Run "vagrant up db --provision" to initialize and create the db VM. When running this command the first time it automatically runs the "neo4j console" command through shell provisioning and loads the Neo4j to host on 192.168.33.12.7474.
		//(On Mac) Sometimes running the command gives an error: Too many open files - getcwd. Had to run command "ulimit -n 1024" to set the maximum number of open files to 1024 instead of 256.
		
		* Open a separate terminal/command prompt on the kb2 directory.
		//(On Mac) Opening a new terminal resets the maximum number of open files to 256. Need to run "ulimit -n 1024" to fix the error.
		
		* Run "vagrant up web --provision" to initialize and create the web VM. This also starts the web module's jar file automatically using shell provisioning which can be accessed through localhost:8082 on a web browser.
