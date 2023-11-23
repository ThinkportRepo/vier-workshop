package digital.thinkport;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.BranchedKStream;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Map;
import java.util.Properties;

public class KafkaStreams1 {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,"application1");
        properties.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2);

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        Map<String, KStream<String, String>> branches = streamsBuilder.stream("my-input-topic", Consumed.with(Serdes.String(), Serdes.String()))
                .split()
                .branch((key, value) -> value.startsWith("a"), Branched.as("aStream"))
                .branch(((key, value) -> value.startsWith("b")), Branched.as("bStream"))
                .defaultBranch(Branched.as("default"));

        branches.get("aStream")
                        .to("a-result-topic");

        branches.get("bStream")
                .to("b-result-topic");


        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), properties);
        kafkaStreams.start();


    }
}
