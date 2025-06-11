# Plugins that I'm currently using in my experiments

> Download and unpack the used plugins in the kafka/connect/plugins folder.

## Plugins for download

https://iceberg.apache.org/docs/nightly/kafka-connect/
https://iceberg.apache.org/docs/1.8.1/kafka-connect/#features

Kafka Connect Hub confluent
- https://www.confluent.io/hub/confluentinc/kafka-connect-jdbc
- https://www.confluent.io/hub/confluentinc/kafka-connect-s3
- https://www.confluent.io/hub/tabular/iceberg-kafka-connect

Debezium Plugins
https://debezium.io/documentation/reference/3.1/install.html

Neo4j 
- https://github.com/neo4j/neo4j-kafka-connector/releases/tag/5.1.11


scylladb

- https://docs.scylladb.com/manual/stable/using-scylla/integrations/integration-kafka.html


### remove slots de replicação
select * from pg_drop_replication_slot('debezium');

SELECT * FROM pg_replication_slots;

##

curl -X POST http://localhost:8083/connectors -H 'Content-Type: application/json' -d @connect/pipelines/connect-postgres-sakila-source.json | jq

