package digital.thinkport;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.Topology;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

public class TransactionalConsumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, Topology.AutoOffsetReset.EARLIEST.name().toLowerCase());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "my-consumer-group2");
        properties.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "consumer1");
        properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
       // properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(List.of("transactional-topic"));

        while(true) {
            ConsumerRecords<String, String> result = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
            result.forEach(System.out::println);
            consumer.commitSync();
        }


    }
}
