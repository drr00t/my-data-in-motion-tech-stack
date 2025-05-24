## Subject Put data in motion from postgres

- kakfa ecosystem for stremaing integration (kafka-connect)
- minio as object store to create of a lakehouse
- create a lakehouse with iceberg table and catalog with postgres
- Spring-boot Batch that runs a Java Spark Job to process the data and create a gold layer
- Snowflake as SQL interface to golden created with Spark
- Spring-boot service to query Snowflake data