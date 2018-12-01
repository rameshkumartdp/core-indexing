FROM openjdk:8
ADD target/search-document-builder-0.0.1-SNAPSHOT.jar /core-indexer.jar
ADD src/main/resources /resources
# Install the AWS CLI
RUN apt-get update && \
    apt-get -y install python curl unzip && \
    cd /tmp && \
    curl "https://s3.amazonaws.com/aws-cli/awscli-bundle.zip" \
    -o "awscli-bundle.zip" && \
    unzip awscli-bundle.zip && \
    ./awscli-bundle/install -i /usr/local/aws -b /usr/local/bin/aws && \
    rm awscli-bundle.zip && \
    rm -rf awscli-bundle
RUN chmod 777 /resources
ADD entrypoint.sh /entrypoint.sh
ENV PATH /:$PATH
ENTRYPOINT ["entrypoint.sh"]
