# Kafka Client Exercises

## Setup a basic Kafka Producer
1. Create a new maven Project
2. Add the necessary dependencies, also a logging library
3. Create the necessary Properties Object. bootstrap.url, key.serializer and value.deserializer are sufficient at the moment
4. Create a producer that uses Integer as Key and String as value
5. Produce 5 messages to the topic my-first-topic

## Kafka Consumer
1. Read the javadoc of [KafkaConsumer](http://kafka.apache.org/20/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html) to know how to use the Consumer API (to consume messages from Kafka)
2. Create a new Project
3. Define dependency for Kafka Clients API library
   1. Use [mvnrepository](https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients/2.0.0) to know the proper entry for `kafka-clients` dependency
4. Write a Kafka consumer
   1. Name of the object: **KafkaConsumerApp**
   2. Start with an empty `Properties` object and fill out the missing properties per exceptions at runtime
   3. Use [ConsumerConfig](https://kafka.apache.org/20/javadoc/org/apache/kafka/clients/consumer/ConsumerConfig.html) constants (not string values for properties)
   4. Don't forget to `close` the consumer (so the messages are actually acknowledged to the broker)
5. Send message with your producer application to a topic of your choice and consume it with your consumer application

## Kafka Consumer Group
Objective:
Create two Java applications (consumers) that read from the same Kafka topic. These consumers should be part of the same consumer group, meaning that they will share the load of consuming messages from the topic. 
1. Create Consumer Application 1:
   1. Set up a Kafka consumer using the Java Kafka Client API.
   2. The consumer should be part of a consumer group called "group1."
   3. Subscribe to the Kafka topic, e.g., "test-topic."
   4. Print the messages consumed to the console.
2. Create Consumer Application 2:
   1. Similar to application 1, set up another Kafka consumer.
   2. This consumer should also be part of the consumer group "group1."
   3. Subscribe to the same Kafka topic, "test-topic."
   4. Print the messages consumed to the console.
3. Verify the Behavior:
   1. Produce some messages to the "test-topic."
   2. Observe how the two consumers within the same group share the load of consuming messages.

Notes:
The Kafka consumers must be properly configured to work in a group.


## Produce with callback
1. Review the available `send` methods in [KafkaProducer](https://kafka.apache.org/20/javadoc/org/apache/kafka/clients/producer/KafkaProducer.html)
2. Read the javadoc of [KafkaProducer.send​(ProducerRecord<K,V> record, Callback callback)](https://kafka.apache.org/20/javadoc/org/apache/kafka/clients/producer/KafkaProducer.html#send-org.apache.kafka.clients.producer.ProducerRecord-org.apache.kafka.clients.producer.Callback-)
   1. Explore [Callback](https://kafka.apache.org/20/javadoc/org/apache/kafka/clients/producer/Callback.html) interface
3. Check which parameters are available in the callback.

## Custom Partitioner
1. Read about the [Partitioner](https://kafka.apache.org/20/javadoc/org/apache/kafka/clients/producer/Partitioner.html) interface
2. Write a custom partitioner that sends an event to partition if the key starts with "zero", to partition one if the key starts with "one" and to partition 2 for any other key
3. Write a KafkaProducer
   1. Register the custom `Partitioner` using [ProducerConfig.PARTITIONER_CLASS_CONFIG](https://kafka.apache.org/20/javadoc/org/apache/kafka/clients/producer/ProducerConfig.html#PARTITIONER_CLASS_CONFIG) property
3. Create a topic with 3 partitions
4. Test your implementation



## Different Message Delivery Acknowledgments
1. In your producer config, change the acks mode to 'all'.
2. Create a new topic with min.insync.replicas set to 3 and replication factor of 3.
3. Pause one broker and try to produce to that topic, observe the outcome.
4. Think about the different ack modes and there up and downsides.

## Exercise: Manual Offset Committing in Kafka

## Part 1: Synchronous Commit

1. **Create a Kafka Consumer:** Configure and start a Kafka consumer. Set `"enable.auto.commit"` to `false` to disable automatic offset committing.
2. **Read Records:** Poll for records from a specific topic.
3. **Process Records:** Implement your logic to process each record.
4. **Commit Offsets Synchronously:** After processing, use the `commitSync()` method to manually commit the offset.

### Part 2: Asynchronous Commit

1. **Asynchronous Commit Without Callback:** Use the `commitAsync()` method after processing the records.
2. **Asynchronous Commit with Callback:** Implement a callback to handle success or failure scenarios in the commit process.

### Part 3: Test the application and verify the different commit modes under normal circumstances and also in error scenarios

## Transactional Producer
1. Write a transactional producer. For that, set the transactional.id property
2. Write an example where a transaction is started and committed. Check on the consumer side, that the message is received.
3. Change the isolation.leve]()l on the consumer side to read_committed. Add a delay in the producer before committing. What' your observation?


## Aufgabe: Kafka-Spielwelt "Battle of Kingdoms"

**Hintergrund:**
In der Welt von "Battle of Kingdoms" konkurrieren zwei Königreiche miteinander: KingdomA und KingdomB. Jedes Königreich kann Truppen entsenden, um das andere Königreich anzugreifen. Deine Aufgabe ist es, diese Angriffsaktionen und -ergebnisse in einem Kafka-basierten System zu modellieren.

**Ziel:**
Implementiere ein Kafka-System in Java, das Angriffsaktionen von Spielern entgegennimmt und das Ergebnis eines jeden Angriffs bestimmt und kommuniziert.

### Anforderungen:

0. **Interne Datenhaltung**:
   - Erstelle eine Map, die verschiedene Königreiche auf eine gewisse Truppenstärke mappt.

1. **Topics**:
   - `attack-commands`: Empfängt die Angriffsaktionen. Format:
      - `angreifendesKönigreich/verteidigendesKönigreich/anzahlDerTruppen`

   - `attack-results`: Kommuniziert das Ergebnis der Angriffe. Format:
      - `angreifendesKönigreich/verteidigendesKönigreich/anzahlDerTruppen/ergebnisDesAngriffs`

2. **Producer**:
   - Sendet Angriffsaktionen an das `attack-commands` Topic.
   - Nutzt Callbacks, um den erfolgreichen Versand einer Nachricht zu bestätigen oder Fehler zu protokollieren.

3. **Consumer**:
   - Liest Angriffsbefehle aus `attack-commands` und bestimmt das Ergebnis des Angriffs. Nutze dafür die Map aus Step 0.
   - Denke dir auch eine Logik aus, um die Anzahl der Truppen nach einem Angriff zu reduzieren.
   - Veröffentlicht das Ergebnis im `attack-results` Topic.
   - Nutzt manuelle Commits.

4. **Transaktionen**:
   - Angriffsergebnisse werden transaktional veröffentlicht.

---

**Beispiel-Eingabe für `attack-commands`:**
`KingdomA/KingdomB/1000`

**Beispiel-Ausgabe für `attack-results`:**
`KingdomA/KingdomB/1000/true`