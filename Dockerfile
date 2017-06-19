FROM tomcat:8.0
ADD tomcat/conf/web.xml                     /usr/local/tomcat/conf/web.xml
ADD tomcat/conf/tomcat-users.xml            /usr/local/tomcat/conf/tomcat-users.xml
ADD tomcat/webapps/manager/WEB-INF/web.xml  /usr/local/tomcat/webapps/manager/WEB-INF/web.xml

