#!/usr/bin/env bash

#sudo apt-get install unzip

#unzip jdk1.8.0_91.zip 
#unzip neo4j-community-3.0.1.zip

echo 'export JAVA_HOME=/vagrant/jdk1.8.0_91/' > /home/vagrant/.bash_profile
echo 'export PATH=$PATH:/$JAVA_HOME/bin' >> /home/vagrant/.bash_profile

echo 'export NEO4J_HOME=/vagrant/neo4j-community-3.0.1' >> /home/vagrant/.bash_profile
echo 'export PATH=$PATH:$NEO4J_HOME/bin' >> /home/vagrant/.bash_profile

source /home/vagrant/.bash_profile

neo4j console
