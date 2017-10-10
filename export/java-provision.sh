#!/usr/bin/env bash
# installs java (the default java version 7) on the vagrant box
# apt-get install default-jdk -y 

echo 'export JAVA_HOME=/vagrant/export/jdk1.8.0_91/' > /home/vagrant/.bash_profile
echo 'export PATH=$PATH:/$JAVA_HOME/bin' >> /home/vagrant/.bash_profile

source /home/vagrant/.bash_profile

cd /vagrant/web/build/libs

# runs the application using the jar file - minus the css
java -jar web-1.0.jar
