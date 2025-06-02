Modeules: 

- https://github.com/valkey-io/valkey-bloom - create and check keys group belongs to
- https://github.com/valkey-io/valkey-json - can natively store, query, and modify JSON data structures using the JSONPath query language
- https://github.com/valkey-io/valkey-search



docker run -it --network sakila_net --rm valkey/valkey valkey-cli -h valkey-server

# meu server
docker run -it --network sakila_net --name valkey-cli  --rm valkey/valkey:8 valkey-cli -h 192.168.15.11