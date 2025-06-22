# Use an official Python image as a base
FROM python:3.11-slim

# Set the Spark version as an environment variable
ENV SPARK_VERSION=3.4.3

# Set the Python version as an environment variable
ENV PYTHON_VERSION=3.11
RUN set -ex; \
    apt update; \
    apt install -y rsync openssh-client openssh-server openjdk-17-jdk wget; \
    apt clean; \
    apt autoremove; \
    rm -rf /var/lib/apt/lists/*

# RUN set -ex; \
#     apt update; \
#     apt install -y rsync openssh-client openssh-server openjdk-17-jdk wget git make curl build-essential; \
#     apt clean; \
#     apt autoremove; \
#     rm -rf /var/lib/apt/lists/*

RUN curl https://sh.rustup.rs -sSf | bash -s -- -y

#RUN . $HOME/.cargo/env

RUN useradd -m -u 1000 spark

#RUN git clone https://github.com/apache/datafusion-comet.git && cd datafusion-comet && make release PROFILES="-Pspark-3.4"
#RUN cd ..

# to use external volume for for logs 
RUN addgroup --gid 1001 logs
RUN usermod -aG logs spark

RUN wget https://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop3.tgz

RUN tar -xvf spark-${SPARK_VERSION}-bin-hadoop3.tgz && \
    mv spark-${SPARK_VERSION}-bin-hadoop3 /opt/spark && \
    rm spark-${SPARK_VERSION}-bin-hadoop3.tgz

COPY spark-jars/comet-spark-spark3.4_2.12-0.1.0-SNAPSHOT.jar /opt/spark/jars
COPY spark-jars/iceberg-aws-bundle-1.5.2.jar /opt/spark/jars
COPY spark-jars/iceberg-spark-runtime-3.4_2.12-1.5.2.jar /opt/spark/jars

RUN chown -R spark:spark /opt/spark

RUN mkdir -p /opt/spark/spark-events

COPY ./entrypoint.sh /

COPY spark-defaults.conf "/opt/spark/conf/"

USER spark    

# Set the Spark home directory
ENV SPARK_HOME=/opt/spark

# Set the Python path
ENV PYTHONPATH=${SPARK_HOME}/python:${PYTHONPATH}

ENV PATH=$PATH:/opt/spark/bin

ENV SPARK_MASTER="spark://spark-master:7077" \
    SPARK_MASTER_HOST=spark-master \
    SPARK_MASTER_PORT=7077 \
    SPARK_MASTER_WEBUI_PORT=8080 \
    SPARK_WORKER_PORT=7000 \
    SPARK_WORKER_WEBUI_PORT=8080 \
    SPARK_HOME="/opt/spark"

EXPOSE 8080 7077 7000 7001 7002 7003 4040 4041 4042 4043 18080 8081 8082

ENTRYPOINT ["/bin/bash","/entrypoint.sh"]
