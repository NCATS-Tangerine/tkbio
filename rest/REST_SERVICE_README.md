#This is for Knowledge.Bio Release 2.4 Neo4j REST APIs to start with#

#Basic Operation#

Start RestApplication.java using *"Run as Java application"\* to test things locally. Note that you should have copies of the appropriate application\*.properties and ogm.properties files properly configured in *kb2/rest/main/resources* subfolder.

#Application Programming Interface#

1. *Concept Rest Service:*

	Get list of concept data matching given "searchTerm".
		Launch http://localhost:8080/api/concept/data/{searchTerm}?pageNum=1&&pageSize=10
		
		{searchTerm} = Any concept name or description for example "neonatal"
		pageNum and pageSize is optional field and by default value is 1 and 10 respectively.
			
	For getting total size of matched concept given "searchTerm"
		http://localhost:8080/api/concept/count/{searchTerm}

2. *Predication Rest Service:*

	Get list of predications associated with selected concept.
    	http://localhost:8080/api/predications/data/{conceptId}?pageNum=1&&pageSize=10
   		{conceptId} can be taken from result of step 2, "id" field of concept.
   		pageNum and pageSize is optional field and by default value is 1 and 10 respectively.
   		
   	For getting total size of matched predication given concept id.
		http://localhost:8080/api/predications/count/{conceptId}

#Generating a WAR File#

To properly prepare a Spring Boot WAR file to deploy remotely, you need to run the following gradle targets on the 'rest' subproject:

       gradle clean war bootPackage

the final task is essential for annotated a proper MANIFEST for the WAR file to make it functional.

The resulting WAR file to be deployed (independently) to AWS Elastic Beanstalk (EB) may be found in the *kb2/rest/build/libs* subdirectory and will be the file with the simple *'.war'* file extension (**not** the file with the *'.war.original'* file extension). You can simply upload the file to AWS EB for direct deployment, making sure that 

When deploying to Amazon Elastic Beanstalk, you should revise the AWS EB software configuration JVM memory parameters to a suitably low size smaller than the available RAM (allowing execution of the application on a smaller, cheaper instance size) since the web application running on a larger EC2 instance likely has JVM heap, etc. settings that are very generous.
