# Created by Lance Hannestad

# This shell script generates the Spring Boot server stub for our project.
# The option skip-overwrite prevents pre-existing files from being overwritten,
# and swaggerOptions.json contain the base package name and the config package name.

# This script takes a yaml or json file as an input. To run this script, do:
#	./swagger.sh specification/KSAPI_17-Apr-2017.yaml

if [$1 == ""]; then
	echo "Must have a path to a yaml or json file as an argument"
else
	java -jar swagger-codegen-cli.jar generate	\
	-i $1						\
	-l spring					\
	--model-package bio.knowledge.server.model	\
	--api-package bio.knowledge.server.api		\
	-c swaggerOptions.json				\
#	--skip-overwrite
fi
