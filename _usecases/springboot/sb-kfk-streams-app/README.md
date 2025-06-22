KAFKA STREAMS 


// at application builder level

Properties props = ...;
KafkaStreams streams = new KafkaStreams(topology, props);

// application level

// Start the Kafka Streams threads
streams.start();

// side-effect

streams.setUncaughtExceptionHandler((Thread thread, Throwable throwable) -> {
  // here you should examine the throwable/exception and perform an appropriate action!
});


// Stop the Kafka Streams threads
streams.close(); //missed gracefull here

// Add shutdown hook to stop the Kafka Streams threads.
// You can optionally provide a timeout to `close`.
Runtime.getRuntime().addShutdownHook(new Thread(streams::close));


# TIPs

Avoid repartition changing the keys

- Uma task para cada partiçço da entrada

![img.png](img-main-loop.png)

- config: poll.ms
  - max.poll.records
  - fetch
- no deserialization
  - config: buffered.records.per.partition
- data available for all input?
  - ts-extraction
  - (de)serialization + exceptio-handling
- config: commit.interval.ms [ctx.commit()]
![img.png](img-max-pool.png)

Task Execution
![img.png](img-task-execution.png)

Commint cuidado com timeout aqui
![img.png](img-commintg.png)
![img.png](img-transaction-timeout.png)

fault tolerance

sata store and changelog topics

standby tasks and high availability
    shadow task replica
- config: num.standby.tasks

smooth scaling with warmup tasks
- config: max.warmup.replicas (replication factory)
  - 
- globalkttables
  - 
