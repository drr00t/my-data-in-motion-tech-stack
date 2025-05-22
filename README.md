# my-data-in-motion-tech-stack

I will release all docker compose stack form my experimentation in data in motion field. For now i don't have any tutorial or detailed documentation.   

## Current Stack

- **Kafka** - messaging system
  - **Kafka Connect** - to connect to codeless integration
  - **AKHQ** - to visualize and manage Kafka topics
- **Postgresql** - with wall enabled
- **Minio** - for local S3 standard object storage


## Before execute any docker-compose environment
Make sure you have the following installed:
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

### Follow the steps below to run the docker-compose files:

To execute any specific compose, just create a docker network called `sakila_net`

```bash
docker network create sakila_net
```

And enter the specific folder and run `docker-compose up` command.

---

## License

This project is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0). See the [LICENSE](./LICENSE) file for details.