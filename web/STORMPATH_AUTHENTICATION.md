Knowledge.Bio user authentication uses the Stormpath (stormpath.com) 3rd party authentication libraries.

This libraries provide the following features (among others):

1) Robust code support for user management workflows
2) Secure registration and management of user account data
3) Dynamic creation and use of user groups (used in KB public maps)

This file provides the basics of Stormpath configuration of Knowledge.Bio.

Part of this tutorial is taken from the Stormpath Java quickstart: http://docs.stormpath.com/java/quickstart/. To get Stormpath working on your computer you will need to access the Stormpath website. Stormpath may change their website and how this information is accessed in the future. If this occurs and you are having trouble, it would be beneficial to look at the Stormpath Java SDK quickstart tutorial.

#LOCAL DEPLOYMENT#

1. Create and/or log into your Stormpath account at api.stormpath.com/login

2. Get an API key:
(a) Click "Manage API keys"
(b) Under the "Security Credentials" section, click "Create API Key" which will then prompt you to save a file
(c) Save the file in a hidden stormpath directory (Java will complain if it's not saved in the right location).
On Linux or OS X save it in: ~/.stormpath/apiKey.properties
On Windows save it in: C:/Users/YourName/.stormpath/apiKey.properties

(d) Change the permissions to ensure that only you can read this file. For example:
 $ chmod go-rwx ~/.stormpath/apiKey.properties

The apiKey.properties holds your API key information, and can be used to easily authenticate with the Stormpath SDK.

3. Create your application and acquire its ID:
(a) Log into your stormpath account
(b) Click on "Applications"
(c) Click on "Create Application". Leave the default settings, including "Make a directory". Name your new application appropriately (i.e. "Knowledge.Bio") then click "Create".
(d) The name of your application will appear as a hyperlink, click on it.
(e) Take note of the ID field, copy it to your clipboard or otherwise record it. You will need your applications ID for the final step.

4. Create the Groups for user permissions and functions:
(a) Click on "Groups"
(b) Click on "Create Group" and put in a group name in the "name" field, using the directory for your application. Repeat this step across the following groups ("*" excluded):
As a minimum, the following group *must* be created for automatic KB (Stormpath) user registration and related registered user restricted operations to work:

* Registered User

Optionally, the following other KB "Role" groups can be created:

* Systems Administrator
* Administrator
* Certified Laboratory
* Data Curator

However, that said, the current code base doesn't actually use these Role groups at this time (although may in the future).

5. Modify/create the application.properties file:

(a) If you haven't already, copy kb2/config/application.properties-template to kb2/web/src/main/resources/ and rename to "application.properties"

	$ mv kb2/config/application.properties-template 

to
	
	kb2/web/src/main/resources/application.properties
	
You need to give the system the Stormpath application name you are using on the web site to manage the accounts, by setting up this additional property:

	stormpath.application.name = Knowledge.Bio
	
(b) If you haven't already, copy kb2/config/stormpath.properties-template to kb2/web/src/main/resources/ and rename to "stormpath.properties"

	$ mv kb2/config/stormpath.properties-template 

to
	
	kb2/web/src/main/resources/stormpath.properties

Take a look at the line that says "stormpath.application.href = https://api.stormpath.com/v1/applications/<your stormpath application key number>". Replace "<your stormpath application key number>" with the application ID of the previous step.

For example, if your ID is 12345ABCdefGEH567890yz then your properties file should contain the line:

	stormpath.application.href = https://api.stormpath.com/v1/applications/12345ABCdefGEH567890yz

Note that if you take the default Stormpath application created, it may be something like "My Application" not "Knowledge.Bio". You need to explicitly create/rename your application to your chosen name (i.e. "Knowledge.Bio") on your Stormpath web site account.

6. Setup the password-reset email functionality:

(a) Log in to your stormpath account at api.stormpath.com/login
(b) Click on "Directories"
(c) Click on your applications directory, e.g., "Knowledge.Bio Directory"
(d) On the left side, click "Workflows & Emails"
(e) Switch to the "Password Reset" tab
(f) You should find yourself on the "Password Reset Email" tab. Change the Link Base URL to "http://mydomain/#!passwordReset". If you are running the application on localhost:8080, change it to "http://localhost:8080/#!passwordReset".

Your Stormpath account is now setup to direct the password reset emails to the right place, so that the application can handle the password reset tokens. At this point you may want to edit the email template that users will receive.

Stormpath should now be configured for your computer.

#REMOTE AWS ELASTIC BEANSTALK DEPLOYMENT#

Deploying the Stormpath enabled version of the application to Amazon Web Service Elastic Beanstalk presents some unique challenges. The following is a workaround to get things working (Hopefully, we'll figure out a better way to do things sometime soon!)

First, most of the above noted configuration steps still need to be done, with the following modifications:

1. Ensuring that the apiKey is visible is tricky. It can be manually placed onto the server via SSH. This works, but with the caveat that AWS EB generally throws away any such custom configuration when certain environment updates occur, such as a change in instance size. You've been warned!

The best place to put the apiKey.properties file for discovery on the AWS server is on the path

	/usr/share/tomcat8/.stormpath/apiKey.properties

For security, you should likely set file ownership to 'root:tomcat' with restrictive user/group read-only file permissions. Then, reload the web application.

2. The step 6. Setup the password-reset email functionality should be adjusted in your 'production' Stormpath application to the server IP or hostname you are using. For this reason, you may wish to create and use a distinct "application" in your Stormpath web site account for your production server (see 5 (c) above).

#REMOTE 'VANILLA' TOMCAT SERVER DEPLOYMENT#

If you are managing the Knowledge.Bio server in a Tomcat web application container which you have directly set up on a custom server (i.e. NOT using Elastic Beanstalk), then you should place the apiKey.properties wherever you've installed the Tomcat home directory, with appropriate tomcat user account access, e.g.

	/opt/tomcat/.stormpath/apiKey.properties



