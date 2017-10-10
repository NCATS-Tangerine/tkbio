#!/bin/sh
# This script downloads swagger-codegen-cli.jar into a hidden directory and then uses it to generate a Java project stub
# https://github.com/swagger-api/swagger-codegen

#############################
###       Constants       ###
#############################

# This is a whitespace (space or tab) delimited list of lines that will be added to .swagger-codegen-ignore prior to code generation, if they are not already present.
IGNORE_LIST="Swagger2SpringBoot.java	README.md"

# This is where we will download swagger-codegen-cli.jar from
SWAGGER_JAR_LOCATION="http://central.maven.org/maven2/io/swagger/swagger-codegen-cli/2.2.2/swagger-codegen-cli-2.2.2.jar -O .codegen/swagger-codegen-cli.jar"

# Here we define the package structure of the server and client
SERVER_BASE_PACKAGE="bio.knowledge.server"
CLIENT_BASE_PACKAGE="bio.knowledge.client"

SERVER_CONFIG_PACKAGE="$SERVER_BASE_PACKAGE.configuration"
SERVER_MODEL_PACKAGE="$SERVER_BASE_PACKAGE.model"
SERVER_API_PACKAGE="$SERVER_BASE_PACKAGE.api"

CLIENT_CONFIG_PACKAGE="$CLIENT_BASE_PACKAGE.configuration"
CLIENT_MODEL_PACKAGE="$CLIENT_BASE_PACKAGE.model"
CLIENT_API_PACKAGE="$CLIENT_BASE_PACKAGE.api"

# Here we define the names of the directories that the server and client projects will be generated into
SERVER_OUTPUT_DIR="server"
CLIENT_OUTPUT_DIR="client"


#############################
###        Methods        ###
#############################

# Called when there is an error with how the script is being used
usage() {
echo "usage: $(basename "$0") <command> <specification> -- uses swagger to generate a Java project stub

	command:
		server		to generate a server
		client		to generate a client
		clean		to delete .codegen/swagger-codegen-cli.jar

	specification:
		path to a json or yaml specification file to be used for generating the server or client project stub"
	exit 1
}

# Ensures that the .swagger-codegen-ignore file is set up and in place
ensureValidIgnoreFile() {
	DIRECTORY="$1"
	IGNORE_FILE="$DIRECTORY/.swagger-codegen-ignore"

	if [ ! -f "$IGNORE_FILE" ]; then
		# Swagger automatically creates an empty .swagger-codegen-ignore file, so this must mean that the project hasn't ever been generated before.
		# The ignore functionality applies not just to overwriting preexisting files, but also creating files. So we don't want an ignore file active in this case.
		return
	fi

	for FILE_NAME in $IGNORE_LIST; do
		ANY_DIR_FILE_NAME='**/'"$FILE_NAME"

		if ! grep -q -x "$ANY_DIR_FILE_NAME" "$IGNORE_FILE" ; then
			echo "$ANY_DIR_FILE_NAME" >> "$IGNORE_FILE"
		fi

		if ! grep -q -x "$FILE_NAME" "$IGNORE_FILE" ; then
			echo "$FILE_NAME" >> "$IGNORE_FILE"
		fi
	done
	
}

#############################
###     Script Logic      ###
#############################

# Get the command
if [ -z "$1" ]; then
	usage
else
	COMMAND="$1"

	if   [ "$COMMAND" = client ]; then
		:
	elif [ "$COMMAND" = server ]; then
		:
	elif [ "$COMMAND" = clean ]; then
		# redirect output to /dev/null to prevent it from printing
		rm .codegen/swagger-codegen-cli.jar 2> /dev/null || echo "There is nothing to clean"
		exit 0
	else
		echo "Invalid command\n"
		usage
	fi
fi

# Get the specification file
if [ -z "$2" ]; then
	usage
else
	SPECIFICATION_FILE_PATH="$2"

	if [ "${SPECIFICATION_FILE_PATH#*.yaml}" != $SPECIFICATION_FILE_PATH ]; then
		:
	elif [ "${SPECIFICATION_FILE_PATH#*.json}" != $SPECIFICATION_FILE_PATH ]; then
		:
	else
		echo "Invalid specification file\n"
		usage
	fi
fi

# Attempt to download swagger-codegen-cli.jar if it doesn't already exist
if [ -f ".codegen/swagger-codegen-cli.jar" ]; then
	:
else
	mkdir -p .codegen

	# wget creates a file whether or not it's able to download it, so if there's a download error we want to delete the file that was created.
	wget $SWAGGER_JAR_LOCATION || rm -f .codegen/swagger-codegen-cli.jar
	
	# If download failed then exit
	if [ ! -f .codegen/swagger-codegen-cli.jar ]; then
		echo "Failed to download swagger-codegen-cli.jar"
		exit 1
	fi
fi

# Use swagger-codegen-cli.jar to generate the server and client stub
if [ "$COMMAND" = client ]; then
	ensureValidIgnoreFile "$CLIENT_OUTPUT_DIR"
	
	java -jar .codegen/swagger-codegen-cli.jar generate -i $SPECIFICATION_FILE_PATH -l java -o $CLIENT_OUTPUT_DIR --model-package $CLIENT_MODEL_PACKAGE --api-package $CLIENT_API_PACKAGE --additional-properties basePackage=$CLIENT_BASE_PACKAGE,configPackage=$CLIENT_CONFIG_PACKAGE

	exit 0

elif [ "$COMMAND" = server ]; then
	ensureValidIgnoreFile "$SERVER_OUTPUT_DIR"
	
	java -jar .codegen/swagger-codegen-cli.jar generate -i $SPECIFICATION_FILE_PATH -l spring -o $SERVER_OUTPUT_DIR --model-package $SERVER_MODEL_PACKAGE --api-package $SERVER_API_PACKAGE --additional-properties basePackage=$SERVER_BASE_PACKAGE,configPackage=$SERVER_CONFIG_PACKAGE

	exit 0

else
	echo "Something went wrong, script should have terminated already"
	exit 1
fi


