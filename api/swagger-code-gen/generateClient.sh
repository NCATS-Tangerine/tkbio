# Created by Lance Hannestad


# This script takes a yaml or json file as an input. To run this script, do:
#	./generateClient.sh specification/KSAPI_17-Apr-2017.yaml

if [ "$#" -lt 1 ]; then
	echo "generateClient.sh <path to specification file>"
else
	java -jar swagger-codegen-cli.jar generate	\
	-i $1						\
	-l java						\
	-o ../client					\
	-c clientGenerateOptions.json			\
	--skip-overwrite
fi
