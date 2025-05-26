## Subject Put data in motion from postgres

- kafka ecosystem for streaming integration (kafka-connect)
- minio as object store to create of a lakehouse
- create a lakehouse with iceberg table and catalog with postgres
- Spring-boot Batch that runs a Java Spark Job to process the data and create a gold layer
- Snowflake as SQL interface to golden created with Spark
- Spring-boot service to query Snowflake data


### Running postgres and setup database


```bash
docker compose -f postgres/docker-compose.yaml up
```

-  Create the database `sakila` via pgadmin "http://localhost:5050"  `user`: "admin@admin.com" `pass`: "admin" `pg master`: "postgres"
-  recreate databse via with scripts


### Running minio and setup bucket

```bash
docker compose -f minio/docker-compose.yaml up
```

- create bucket `lakehouse` via "http://localhost:9001" `user`: "minio" `pass`: "minio123
- create credentials for `lakehouse` bucket 


### Running kafka and setup connectors

```bash
docker compose -f kafka/docker-compose.yaml up
```
- create connector for postgres to kafka
- create connector for kafka to iceberg on minio

```bash
curl -s -XGET http://localhost:8083/connector-plugins | jq
```


### Setup Postgres sakila data extraction via cdc

```bash
curl -s -XPOST http://localhost:8083/connectors -H "Content-Type: application/json" -d @kafka-connect/postgres-connector.json | jq
```