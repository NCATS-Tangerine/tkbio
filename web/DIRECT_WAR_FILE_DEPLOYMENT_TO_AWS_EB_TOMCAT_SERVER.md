#WAR Deployment to AWS EB#

Revision: 19 Sept 2016
Author:   R. Bruskiewich

Here we describe a manual protocol for direct deployment of a WAR file of the Knowledge.Bio web application,
an Amazon Web Services Elastic Beanstalk (EB) instance (i.e. running Tomcat 8)

1. Build the clean war file (run 'gradle clean war') with a unique (updated?) version number.

2. Secure copy the war file over from your local machine over to the AWS EC2 server that was launched by EB (Windows Putty scp used here...):

        scp -i /path/to/credentials/keypair.pem  /path/to/WAR/filename  ec2-user@hostIP:filename

3. rename the WAR file to ROOT.war

4. change ownership to 'tomcat:tomcat'

        sudo chown tomcat:tomcat ROOT.war

5. copy file to tomcat webapps:

        sudo cp ROOT.war /var/lib/tomcat8/webapps

The following additional steps assume that Tomcat 'autodeploy' is not set 'on' (in EB, maybe not?)

6. stop Tomcat

        sudo /etc/init.d/tomcat8 stop

7. delete the old webapps/ROOT directory

        sudo rm -Rf /var/lib/tomcat8/webapps/ROOT

8. restart Tomcat

        sudo /etc/init.d/tomcat8 start

The new version of the application should now be running!
