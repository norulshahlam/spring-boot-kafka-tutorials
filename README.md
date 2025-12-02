# Kafka tutorials

[Reference #1](https://www.udemy.com/course/spring-boot-microservices-and-spring-cloud/)  
[Reference #2](https://www.udemy.com/course/kafka-fundamentals-for-java-developers/)  
[Reference #2](https://stackoverflow.com/questions/38024514/understanding-kafka-topics-and-partitions)  
[Migrating from Zookper to KRaft](https://medium.com/@youssefali6212/apache-kafka-study-notes-3-zookeeper-vs-kraft-8eb683a8f6aa)  


### Architecture

![Image](./library-service-producer/src/main/resources/kafka-architecture.png)

### Terminologies

![Image](./library-service-producer/src/main/resources/record-partition-topic.png)


<details>
<summary><b>Topic</b></summary>
    Topic is entity in Kafka with a name. Think of it like a table in DB.
</details>

<details>
<summary><b>Partition</b></summary>
    Where the message lives inside the topic. When you create a topic, you can define one or more partitions and replication factor. Kafka distributes the partitions of a particular topic across multiple brokers IF you have >1 broker. If you only have 1, then these 3 partitions will live on the same broker. Rule of thumb: Brokers should be equal to or greater than replication factor else topic creation fails due to lack of brokers.
</details>

<details>
<summary><b>Record</b></summary>

A single unit of message to be sent / receive. This unit is an object which contains several fields. There are 2 types of records - [ProducerRecord](https://kafka.apache.org/23/javadoc/org/apache/kafka/clients/producer/ProducerRecord.html) & [ConsumerRecords](https://lankydan.dev/intro-to-kafka-consumers). ConsumerRecords have several extra fields and a few that become required compared to ProducerRecords.

![Image](https://lankydan.dev/static/afe807fbb5018fd70077474651a5039e/fbf9a/kafka-consumer-record-vs-producer-record.png)

</details>

<details>
<summary><b>Brokers</b></summary>

- A Kafka cluster is a group of multiple Kafka brokers.
- A Kafka broker is a server in the cluster this will receive and send the data.
- Each Kafka broker is identified with an ID (integer).
- Each broker will have certain topic partitions.
- All the topic partitions data is Distributed across all brokers(load balanced).
- After connecting to any broker (bootstrap broker) you can have connectivity to the entire cluster.

How brokers and topics are related?

Consider a scenario
- Topic-A has three partitions and 
- Topic B has two partitions. 
- Brokers 101, 102, and 103 are the final three Kafka brokers. 
- Broker 101 will therefore have Topic-A, Partition 0, while 
- Broker 102 will have Topic-A, Partition 2. 
- This is not an error. Broker 103 is then discussing Topic-A, Partition 1. 
- Therefore, as we can see, the subject divisions will be distributed among all brokers in any sequence. 
- We also have Topic-B, Partition 1 on Broker 101, and 
- Topic-B, Partition 0 on Broker 102 for this topic. 
- Thus, in this instance, We can see that the data is spread, and since the two partitions have already been added to our Kafka broker, it is expected that Broker 103 does not have any Topic-B data partitions. And this is Kafka's power. 

As you can see from the example, the data and your partitions will be distributed throughout all brokers. This is how Kafka scales, and it is what is referred to as horizontal scaling. The more partitions and brokers we add, the more evenly the data will be dispersed throughout our whole cluster. We also take note of the fact that the brokers only have the data that they ought to have—not all of it.

[Further reading](https://www.linkedin.com/pulse/apache-kafka-all-broker-saikrishna-cheruvu/)


</details>


<details>
<summary><b>Consumer group</b></summary>

- Consumer group is a bunch of consumers that form a group in order to cooperate and consume messages from a set of topics.  
- Consumer groups are formed when you have multiple instances of the same application.
- Consumer groups are the foundation for scalable message consumption.

Example: 
If two consumers have subscribed to the same topic and are present in the same consumer group, then these two consumers would be assigned a different set of partitions and none of these two consumers would receive the same messages.

![Image](https://miro.medium.com/v2/resize:fit:720/format:webp/1*zTRQR9H_Glv1xbPWl_yY6w.png)

Example 2:

5 consumers in the consumer group which is more than the number of partitions of the TopicT1, then every consumer would be assigned a single partition and the remaining consumer (Consumer5) would be left idle

![Image](https://miro.medium.com/v2/resize:fit:720/format:webp/1*INW2vHXkN7v47-WvrgbhbA.png)

[Further reading](https://medium.com/javarevisited/kafka-partitions-and-consumer-groups-in-6-mins-9e0e336c6c00)

</details>

<details>
<summary><b>Replication Factor & In-Sync Replicas</b></summary>

`Replication-factor` is the total number of copies of the data stored in an Apache Kafka cluster. Replication factor 1 means each record lives on one broker only.  

`min. insync. replicas` is the minimum number of copies of the data that you are willing to have online at any time to continue running and accepting new incoming messages.

Example 1. Replication factor 3. min.insync.replicas 2. 2 copies must be in sync to accept writes. 1 broker can afford fail.  
</details><br>

`Consumer groups`
Kafka has the concept of consumer groups where several consumers are grouped to consume a given topic. Consumers in the same consumer group are assigned the same group-id value.

The consumer group concept ensures that a message is only ever read by a single consumer in the group.

When a consumer group consumes the partitions of a topic, Kafka makes sure that each partition is consumed by exactly one consumer in the group

![Image](./library-service-producer/src/main/resources/consumer-group.png)

### Setting up kafka

<details>
<summary>Click to expand</summary><br>

1. Download Kafka BINARY file from `https://kafka.apache.org/downloads`
2. If you are using Windows, use cmd and run `tar -xvzf kafka-3.3.1-src.tgz`
3. Rename the folder to shorter name (for Windows)
4. List of command can be
   found [HERE](https://github.com/dilipsundarraj1/kafka-for-developers-using-spring-boot/blob/master/SetUpKafka.md)

</details>

### Zookeeper & Broker

`Zookeeper` - Acts a Kafka cluster coordinator that manages cluster membership of brokers, producers, and consumers
participating in message transfers via Kafka. It also helps in leader election for a Kafka topic.

`Broker` - A single Kafka server is called a Kafka Broker. A Kafka broker allows consumers to fetch messages by topic,
partition and offset. Kafka brokers can create a Kafka cluster by sharing information between each other directly or
indirectly using Zookeeper. A Kafka cluster has exactly one broker that acts as the Controller.

### Start Zookeeper & Broker

<details>
<summary>Click to expand</summary><br>

1. Start up the Zookeeper. in bin/windows run:

    zookeeper-server-start.bat ..\..\config\zookeeper.properties

Start up the Kafka Broker. We will run THREE brokers based on our own server properties

2. Copy server.properties in /config & rename as follows
3. Add/change this properties

    `server-1.properties`
    broker.id=0
    listeners=PLAINTEXT://localhost:9092
    auto.create.topics.enable=false
    log.dirs=/tmp/kafka-logs-1

    `server-2.properties`
    broker.id=1
    listeners=PLAINTEXT://localhost:9093
    auto.create.topics.enable=false
    log.dirs=/tmp/kafka-logs-2

    `server-3.properties`
    broker.id=2
    listeners=PLAINTEXT://localhost:9094
    auto.create.topics.enable=false
    log.dirs=/tmp/kafka-logs-3

4. In bin/windows folder run this to start brokers:

    kafka-server-start.bat ..\..\config\server-1.properties
    kafka-server-start.bat ..\..\config\server-2.properties
    kafka-server-start.bat ..\..\config\server-3.properties

</details>

### Set up producer in Spring Boot

<details>
<summary>Click to expand</summary><br>

1. Create endpoint
2. Create topic - library-events

</details>

### Start up consumer using jar

<details>
<summary>Click to expand</summary><br>

In bin/windows folder run this to start consumer:  
Without key:

    kafka-console-producer.bat --bootstrap-server localhost:9092 --topic library-events

</details>

### Sent your first message thru Spring Boot producer!

<details>
<summary>Click to expand</summary><br>

POST `http://localhost:8080/v1/libraryevent`

    {
        "libraryEventId":null,
        "book":{
            "bookId":2,
            "bookName":"abc",
            "bookAuthor":"zzzfff"
        }
    }
Postman collections: [Click here](./library-service-producer/src/main/resources/kafka-tutorials.postman_collection.json)
</details>

You should be able to receive message from your consumer console. Note that there are 2 diff methods

    sendNewEventWithDefaultTopic()

This is the default method for sending event. The method sendEevent() doesn't require to insert topic name as the argument as it will follow the one in application property file. Also, it doesn't use key to send message which means Kafka doesn't guarantee ordering of events

    sendNewEventWithDefinedTopic()

This uses the send() method which require to insert topic name as the argument. Also it uses key to send message. If we have a key, the message will be sent through the same partition. This is really important for the ordering of the events because Kafka guarantees ordering only at the partition level. To see the difference, see which partition is used in the logs. those with key will be using the same partition.

### Create Spring boot consumer

There are 3 main things we need to configure

- key-serializer
- value-deserializer
- group-id

### What is Rebalance?

<details>
<summary>Click to expand</summary><br>
Rebalance is the re-assignment of partition ownership among consumers within a given consumer group. Remember that every consumer in a consumer group is assigned one or more topic partitions exclusively.

A Rebalance happens when:

- a consumer JOINS the group
- a consumer SHUTS DOWN cleanly
- a consumer is considered DEAD by the group coordinator. This may happen after a crash or when the consumer is busy with
a long-running processing, which means that no heartbeats has been sent in the meanwhile by the consumer to the group
coordinator within the configured session interval
- new partitions are added  

Being a group coordinator (one of the brokers in the cluster) and a group leader (the first consumer that joins a group)
designated for a consumer group, Rebalance can be more or less described as follows:

- the leader receives a list of all consumers in the group from the group coordinator (this will include all consumers
that sent a heartbeat recently and which are therefore considered alive) and is responsible for assigning a subset of
partitions to each consumer.
- After deciding on the partition assignment (Kafka has a couple built-in partition assignment policies), the group leader
sends the list of assignments to the group coordinator, which sends this information to all the consumers.

[More info](https://stackoverflow.com/questions/30988002/what-does-rebalancing-mean-in-apache-kafka-context)
</details>

### Hands-on Rebalance
<details>
<summary>Click to expand</summary><br>
Create 2 instance of the same consumer service, start the app and check the logs of the assigned partition.

2 things will happen:

the 1st instance will show (part of)

    Revoke previously assigned partitions
    protocol='range'
    Adding newly assigned partitions: library-events-1, library-events-0
the 2nd instance will show

    Adding newly assigned partitions: library-events-2

Since we have 3 brokers running, it will be distributed among the 2 instances.

</details>

### Tools used for visualizing kafka topics, events, and consumer groups
<details>
<summary>Click to expand</summary><br>

[Reference](https://www.reddit.com/r/apachekafka/comments/1dcet0g/what_tools_do_you_use_for_visualizing_kafka/)  

- Kafka Tool (http://www.kafkatool.com/) - A GUI application for managing and using Apache Kafka clusters. It provides an easy-to-use interface for browsing Kafka topics, partitions, and messages.

- Kafbat (https://kafbat.io/) - A web-based Kafka management and monitoring tool. It allows users to view topics, partitions, and messages, as well as monitor consumer groups and offsets. Kafbat UI makes your kafka data flows observable, simplifies troubleshooting, and helps optimize performance—all in a lightweight dashboard.

Run docker command:
```bash
    docker run -it -p 8080:8080 -e DYNAMIC_CONFIG_ENABLED=true ghcr.io/kafbat/kafka-ui
```

- Conduktor (https://www.conduktor.io/) - A desktop application for managing and monitoring Apache Kafka clusters. It provides a user-friendly interface for browsing topics, partitions, and messages, as well as monitoring consumer groups and offsets.

- Kpow (https://factorhouse.io/kpow/) - A web-based Kafka monitoring and management tool. It allows users to view topics, partitions, and messages, as well as monitor consumer groups and offsets. Kpow provides real-time metrics and alerts for Kafka clusters.

</details>
