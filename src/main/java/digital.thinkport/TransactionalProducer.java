package digital.thinkport;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class TransactionalProducer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-producer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        producer.initTransactions();

        producer.beginTransaction();
        producer.send(new ProducerRecord<>("transactional-topic", "1","value"));
        producer.send(new ProducerRecord<>("transactional-topic", "1","value"));
        producer.send(new ProducerRecord<>("transactional-topic", "1","value"));
        producer.flush();
        producer.abortTransaction();

    }
}
