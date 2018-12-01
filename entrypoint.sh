#!/bin/bash
#
# docker-entrypoint for indexer
LOG_SUFFIX="$(date +'%m-%d-%Y-%H-%M-%S-%N').$$"
set -e
if [ -n "${S3_BUCKET_NAME}" ] ; then
	if [ -n "${RUNTIME_ENV}" ] ; then
	    if [ -n "${STORE}" ] ; then
	            dataDir="/data/${STORE}.${LOG_SUFFIX}"
	            echo "mkdir -p ${dataDir}"
	            mkdir -p ${dataDir}
	    	    echo "Copying data from S3"
	    	    aws s3 cp s3://${S3_BUCKET_NAME}/core/indexer/${RUNTIME_ENV}/common/ ${dataDir} --recursive
		        aws s3 cp s3://${S3_BUCKET_NAME}/core/indexer/${RUNTIME_ENV}/${STORE}/ ${dataDir}  --recursive
		        echo "Listing the contents of resources directory"
		        ls -lrth /resources
   		        echo "Listing the contents of data directory ${dataDir}"
		        ls -lrth ${dataDir}

		fi
	fi
fi
	
echo "Executing java"
echo "java -classpath /core-indexer.jar:/resources:${dataDir} -DCORE_LOG_FILE=${dataDir}/core-indexer.log -Dc2c.text.file=${dataDir}/vendor_temp_c2c_bso.txt -DfilePathForInferencer=${dataDir}/7.2k_Jan_17_data.inferencer -DfilePathForInstances=${dataDir}/7.2k_Jan_17_data.instances $@"

java -classpath /core-indexer.jar:/resources:${dataDir} -DCORE_LOG_FILE=${dataDir}/core-indexer.log -Dc2c.text.file=${dataDir}/vendor_temp_c2c_bso.txt -DfilePathForInferencer=${dataDir}/7.2k_Jan_17_data.inferencer -DfilePathForInstances=${dataDir}/7.2k_Jan_17_data.instances "$@"
returnCode=$?
# cleanup
echo "removing data dir to cleanup"
rm -rf ${dataDir}
echo "returnCode from JVM process is ${returnCode}"
exit ${returnCode}
