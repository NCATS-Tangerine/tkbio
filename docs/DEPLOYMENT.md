# Local and Docker Deployment

See the [QUICKSTART]() guide in the main documentation.

# Alternate Remote Deployments

The application may be run as a WAR file in an external web application container run from the Eclipse-WTP framework. Locally, this could be done in a [Tomcat 8](https://tomcat.apache.org/) container (up to v8.0.46 with [Java 8 JRE](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)). Remotely, it may be run inside an [Amazon Web Services Elastic Beanstalk](https://aws.amazon.com/elasticbeanstalk/) environment. Here we assume you have Tomcat 8 installed and already have some familiarity with the AWS system.

## Running in Eclipse-WTP on Tomcat 8

To set things up, you first need to configure the 'web' sub-project as an Eclipse WTP project by running the following Gradle tasks. Run this command from you Terminal in the **tkbio/** root folder, or through your Gradle Plugin's task launcher in Eclipse:

    gradle :web:eclipseWtpComponent :web:eclipseWtpFacet

You may also want to set the web application to appear as the "root" project. This is achieved by going to the context menu associated with the application's "web" sub-project in Eclipse, then selecting:

    Properties..Web Project Settings..Context Root

setting the value to a forward slash ('/'). 

After setting things up, you can:

    Run As..Run on Server

... once again, selected from the "web" sub-project's context menu. 

This opens a wizard dialog that allows you to create a new local Tomcat 8 web server instance (ensure that it is specified with a Java 8 JRE) from within the Eclipse Servers View window. We do this by selecting:
    
    Apache..Tomcat v8.0 Server

and click *Next*. Following the instructions of the wizard, we then add the 'web' sub-project to the web container.

Note that the application's properties files still need to be properly configured, as in when you run it without Tomcat 9.

If the Tomcat server doesn't start up, you can select it in the "Servers" view and open your server's context menu to click the "Start" menu item. 

Whenever significant application changes are made to the code, including changed dependencies, and you encounter difficulties running it, you can *Remove* the "web" sub-project by selecting it under the given Tomcat server entry in the "Servers", and then perform *Run on Server* again to add it back into the web server.

### Troubleshooting

**Server Timeout**. If your recieve a dialog saying that the server couldn't start up because it timed out, you need to edit the server settings. Open the "Servers" Eclipse View, and double click on your Tomcat server. A pane showing some server configuration menus should appear. Find the one that says "Timeouts" and set the start time to something above a minute.

## Running in Amazon Web Services Elastic Beanstalk

If you have

    Run As..Run on Server 

the application locally in the Eclipse WTP on Tomcat 8, you could run the application in the Amazon Web Services cloud using Elastic Beanstalk (EB), using the AWS Toolkit for Eclipse. Refer to [the EB documentation](https://aws.amazon.com/documentation/elastic-beanstalk/) for full configuration details. Here, we'll outline TKBio's specific considerations.

The AWS plug-in integration with Eclipse is good, but it can behave cantankerously. We've found it easier to:

1. Regenerate the web WAR file (which is deposited into **/tkbio/web/build/libs**) using the `gradle clean war` command.

2. Create the environment on the AWS Dashboard then upload the generated WAR file to EB, as the source application.

3. Configure the EC2 instance to suit: a t2-micro instance or greater is sufficient for the application at the present time, assuming that the Neo4j database resides on a 2nd (pre-configured) server. You will also want your AWS IAM credentials to be configured with an EB-service access policy lenient enough to make the new EB application and environment visible to you (ask your local AWS admin for help).

For the deployment itself, you should ensure that the application properties files are present and properly configured (See the [QUICKSTART]() for details). In particular, you should point the Neo4j web URL to a distinct running instance of Neo4j running somewhere on the internet, on another AWS EC2 instance, or otherwise another server publicly visible on the web. Make sure that the username and password reflect the credentials of that remote Neo4j instance. 

If the Neo4j server is not open to the world (e.g. if there are firewall settings), then one key step is to add the public IP address of the EB EC2 instance that is created, to the Neo4j EC2 instance firewall, for custom TCP access through the Neo4j assigned ports 7474 and 7687. The former port number is the web browser access port; the latter is a hidden communication port now used in Neo4j release 3.1 and above for 'BOLT' communication (see the Neo4j docs for information on BOLT).

After the EB environment is running, you can "Run As..Run on Server" using the existing Application and Environment as the source of the configuration. You might first want to restart Eclipse so that the AWS Explorer view gets reinitialized and discovers the new EB application and environment. From that point onwards, you can republish your application as you make coding progress.

## WAR Deployment to AWS EB

Here we describe a manual protocol for direct deployment of a WAR file of the Knowledge.Bio web application, an Amazon Web Services Elastic Beanstalk (EB) instance (i.e. running Tomcat 8).

1. Build the clean war file (run `gradle clean war`) with a unique (potentially updated) version number.

2. Secure copy the war file over from your local machine over to the AWS EC2 server that was launched by EB (Windows Putty scp used here...):

    scp -i /path/to/credentials/keypair.pem  /path/to/WAR/filename  ec2-user@hostIP:filename

3. Rename the WAR file to `ROOT.war`.

4. Change ownership to `tomcat:tomcat`.

    sudo chown tomcat:tomcat ROOT.war

5. Copy file to tomcat webapps:

    sudo cp ROOT.war /var/lib/tomcat8/webapps

The following additional steps assume that Tomcat 'autodeploy' is not set 'on' (in EB, maybe not?)

6. Stop Tomcat:

    sudo /etc/init.d/tomcat8 stop

7. Delete the old webapps/ROOT directory:

    sudo rm -Rf /var/lib/tomcat8/webapps/ROOT

8. Restart Tomcat:

    sudo /etc/init.d/tomcat8 start

The new version of the application should now be running!

## WAR Deployment to a Directly Configured Tomcat Instance

1. Install Oracle Java8:

    sudo add-apt-repository ppa:webupd8team/java
    sudo apt-get update
    sudo apt-get install oracle-java8-installer

2. Install Tomcat. See the guide on [Digital Ocean](https://www.digitalocean.com/community/tutorials/how-to-install-apache-tomcat-8-on-ubuntu-16-04) for Tomcat installation on Ubuntu.

i. Create a tomcat user

    sudo groupadd tomcat
    sudo useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat

ii. Install tomcat

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

i. Look up Java location

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

ii. When you are finished, save and close the file.

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

5. Configure Tomcat Web Management Interface:

i. Add a Tomcat admin user:

    sudo nano /opt/tomcat/conf/tomcat-users.xml

you will want to add a user who can access the manager-gui and admin-gui, e.g.

    <tomcat-users . . .>
        <user username="admin" password="password" roles="manager-gui,admin-gui"/>
    </tomcat-users>

ii. By default, newer versions of Tomcat restrict access to the Manager and Host Manager apps to connections coming from the server itself. Since we are installing on a remote machine, you will probably want to remove or alter this restriction. To change the IP address restrictions on these, open the appropriate context.xml files.

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

6. Might need to increase the file upload limit above 50MB:

    sudo nano webapps/manager/WEB-INF/web.xml

and edit (say, to 150 MB):

    <multipart-config>
        <!-- 150MB max -->
        <max-file-size>157286400</max-file-size>
        <max-request-size>157286400</max-request-size>
        <file-size-threshold>0</file-size-threshold>
    </multipart-config>

7. The Knowledge.Bio WAR file may be uploaded via the web. To ensure that the application runs, carefully review the settings of configuration properties. Refer also to the STORMPATH_AUTHENTICATION README for details on how to configure Stormpath user authentication settings on your server (i.e. placing the .stormpath/apiKey.properties under /opt/tomcat/)

8. The simplest way to get the application served as the 'root' application in tomcat is

i. to turn the server off, rename the ROOT folder to something else (e.g. 'apache'), and the KB application folder to 'ROOT', then

ii. use iptables to reroute the standard port 80 to Tomcat 8080

    sudo iptables -t nat -A PREROUTING -i eth0 -p tcp --dport 80 -j REDIRECT --to-port 8080

This command only persists for a given server session (lost upon reboot). To persist the iptables, further action is required (you can use the documentation [here](https://help.ubuntu.com/community/IptablesHowTo#Saving_iptables) for help).

iii. To update the application, simply upload (via the Apache Tomcat manager web app) a newly updated WAR file as a file named "ROOT.war" 


