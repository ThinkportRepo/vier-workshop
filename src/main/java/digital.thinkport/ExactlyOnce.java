package digital.thinkport;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ExactlyOnce {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "producer1");

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        String consumerGroup = "my-first-consumer-group" + UUID.randomUUID().toString();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        properties.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "consumer1");
        properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaProducer.initTransactions();
        try {
            kafkaProducer.beginTransaction();
            kafkaConsumer.subscribe(List.of("my-transactional-input"));

            ConsumerRecords<String, String> poll = kafkaConsumer.poll(Duration.of(100, ChronoUnit.MILLIS));
            for (ConsumerRecord<String, String> record : poll) {
                // Businesslogik
                kafkaProducer.send(new ProducerRecord<>("my-transactional-output",record.key(), record.value().toUpperCase()));
            }

            Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
            for (ConsumerRecord<String, String> record : poll) {
               offsets.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset()));
            }
            kafkaProducer.sendOffsetsToTransaction(offsets, new ConsumerGroupMetadata(consumerGroup));

            kafkaProducer.commitTransaction();


        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            kafkaProducer.abortTransaction();
            kafkaProducer.close();
        } catch (KafkaException e) {
            kafkaProducer.abortTransaction();
        }

    }
}
