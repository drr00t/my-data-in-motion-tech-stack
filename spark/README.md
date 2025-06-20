https://medium.com/@SaphE/testing-apache-spark-locally-docker-compose-and-kubernetes-deployment-94d35a54f222

## run spark cluster 

### create network

```console
docker network create spark_dev_net
```

### build local image

```console
docker compose build
```

### start up cluster
```console
docker compose up
```

### scale up to workers
```console
docker compose up --scale spark-worker=2
```

## execute app simple1

### local threaded execution 
```console
spark-submit  main.py 
```

### execution in local cluster
```console
spark-submit --properties-file conf/spark-defaults.conf --packages org.apache.spark:spark-hadoop-cloud_2.12:3.3.4,org.apache.hadoop:hadoop-aws:3.3.4,com.amazonaws:aws-java-sdk-bundle:1.12.262 --master spark://192.168.122.1:7077 main.py

```

#### execution in local cluster (not enough resource) stay waiting forever
```console
spark-submit --master spark://192.168.15.6:7077 --executor-memory 10G --driver-memory 10G  main.py 
```

### execute simple1_minio

#### execution local

```console
spark-submit --packages org.apache.spark:spark-hadoop-cloud_2.12:3.3.4,org.apache.hadoop:hadoop-aws:3.3.4,com.amazonaws:aws-java-sdk-bundle:1.12.262 main.py
```

#### execution local cluster

```console
spark-submit --properties-file conf/spark-defaults.conf --packages org.apache.spark:spark-hadoop-cloud_2.12:3.3.4,org.apache.hadoop:hadoop-aws:3.3.4,com.amazonaws:aws-java-sdk-bundle:1.12.262 --master spark://192.168.122.1:7077 main.py

```