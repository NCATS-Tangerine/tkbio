#WAR Deployment to a Directly Configured Tomcat Instance#

January 2, 2017 KB 3.0 Direct Ubuntu Web Server Tomcat Setup

1. Install Oracle Java8:

	sudo add-apt-repository ppa:webupd8team/java
	sudo apt-get update
	sudo apt-get install oracle-java8-installer

2. Install Tomcat:

See also https://www.digitalocean.com/community/tutorials/how-to-install-apache-tomcat-8-on-ubuntu-16-04

a) Create a tomcat user

	sudo groupadd tomcat
	sudo useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat

b) Install tomcat

	curl -O http://mirror.csclub.uwaterloo.ca/apache/tomcat/tomcat-8/v8.5.9/bin/apache-tomcat-8.5.9.tar.gz

// check the integrity of the download with md5
	md5sum apache-tomcat-8.5.9.tar.gz

// e.g. b41270a64b7774c964e4bec813eea2ed  apache-tomcat-8.5.9.tar.gz for this release?

	sudo mkdir /opt/tomcat
	sudo tar xzvf apache-tomcat-8*tar.gz -C /opt/tomcat --strip-components=1

3. Set up user permissions:

	cd /opt/tomcat
	sudo chgrp -R tomcat /opt/tomcat
	sudo chmod -R g+r conf
	sudo chmod g+x conf
	sudo chown -R tomcat webapps/ work/ temp/ logs/

4. Create a systemd Service File:

a) Look up Java location

	sudo update-java-alternatives -l

e.g.

	java-8-oracle                  1081       /usr/lib/jvm/java-8-oracle

to set value of JAVA_HOME, by appending 'jre' to path, i.e.

	/usr/lib/jvm/java-8-oracle/jre

With this piece of information, we can create the systemd service file. Open a file called tomcat.service in the /etc/systemd/system directory by typing:

    sudo nano /etc/systemd/system/tomcat.service

Paste the following contents into your service file. Modify the value of JAVA_HOME if necessary to match the value you found on your system. You may also want to modify the memory allocation settings that are specified in CATALINA_OPTS:


	[Unit]
	Description=Apache Tomcat Web Application Container
	After=network.target
	
	[Service]
	Type=forking
	
	Environment=JAVA_HOME=/usr/lib/jvm/java-8-oracle/jre
	Environment=CATALINA_PID=/opt/tomcat/temp/tomcat.pid
	Environment=CATALINA_HOME=/opt/tomcat
	Environment=CATALINA_BASE=/opt/tomcat
	Environment='CATALINA_OPTS=-Xms512M -Xmx1024M -server -XX:+UseParallelGC'
	Environment='JAVA_OPTS=-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom'
	
	ExecStart=/opt/tomcat/bin/startup.sh
	ExecStop=/opt/tomcat/bin/shutdown.sh
	
	User=tomcat
	Group=tomcat
	UMask=0007
	RestartSec=10
	Restart=always
	
	[Install]
	WantedBy=multi-user.target

b) When you are finished, save and close the file.

Next, reload the systemd daemon so that it knows about our service file:

    sudo systemctl daemon-reload

Start the Tomcat service by typing:

    sudo systemctl start tomcat

Double check that it started without errors by typing:

    sudo systemctl status tomcat

Check the web site:

	http://<hostip>:8080

If you were able to successfully accessed Tomcat, now is a good time to enable the service file so that Tomcat automatically starts at boot:

    sudo systemctl enable tomcat

5) Configure Tomcat Web Management Interface:

a) Add a Tomcat admin user:

	sudo nano /opt/tomcat/conf/tomcat-users.xml

you will want to add a user who can access the manager-gui and admin-gui, e.g.

	<tomcat-users . . .>
	    <user username="admin" password="password" roles="manager-gui,admin-gui"/>
	</tomcat-users>

b) By default, newer versions of Tomcat restrict access to the Manager and Host Manager apps to connections coming from the server itself. Since we are installing on a remote machine, you will probably want to remove or alter this restriction. To change the IP address restrictions on these, open the appropriate context.xml files.

For the Manager app, type:

    sudo nano /opt/tomcat/webapps/manager/META-INF/context.xml

For the Host Manager app, type:

    sudo nano /opt/tomcat/webapps/host-manager/META-INF/context.xml

Inside, comment out the IP address restriction to allow connections from anywhere.

	<Context antiResourceLocking="false" privileged="true" >
	  <!--<Valve className="org.apache.catalina.valves.RemoteAddrValve"
	         allow="127\.\d+\.\d+\.\d+|::1|0:0:0:0:0:0:0:1" />-->
	</Context>

Save and close the files when you are finished.

To put our changes into effect, restart the Tomcat service:

    sudo systemctl restart tomcat

6) Might need to increase the file upload limit above 50MB:

	sudo nano webapps/manager/WEB-INF/web.xml

and edit (say, to 150 MB):

    <multipart-config>
      <!-- 150MB max -->
      <max-file-size>157286400</max-file-size>
      <max-request-size>157286400</max-request-size>
      <file-size-threshold>0</file-size-threshold>
    </multipart-config>


7) The Knowledge.Bio WAR file may be uploaded via the web. To ensure that the application runs, carefully review the settings of configuration properties. Refer also to the STORMPATH_AUTHENTICATION README for details on how to configure Stormpath user authentication settings on your server (i.e. placing the .stormpath/apiKey.properties under /opt/tomcat/)

8) The simplest way to get the application served as the 'root' application in tomcat is

a) to turn the server off, rename the ROOT folder to something else (e.g. 'apache'), and the KB application folder to 'ROOT', then

b) use iptables to reroute the standard port 80 to Tomcat 8080

	sudo iptables -t nat -A PREROUTING -i eth0 -p tcp --dport 80 -j REDIRECT --to-port 8080

Note that this command only persists for a given server session (lost upon reboot). To persist the iptables, further action is required (e.g. see https://help.ubuntu.com/community/IptablesHowTo#Saving_iptables)

c) To update the application, simply upload (via the Apache Tomcat manager web app) a newly updated WAR file as a file named "ROOT.war" 
