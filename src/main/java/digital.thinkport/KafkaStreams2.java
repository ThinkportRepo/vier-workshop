package digital.thinkport;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.Map;
import java.util.Properties;

public class KafkaStreams2 {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,"application1");
        properties.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2);

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        KTable<String, String> nicknames = streamsBuilder.table("nicknames");

        streamsBuilder.stream("chat-messages2", Consumed.with(Serdes.String(), Serdes.String()))
                .leftJoin(nicknames, (chat, nickname) -> String.format("%s wrote: %s", nickname, chat), Joined.with(Serdes.String(), Serdes.String(), Serdes.String()))
                .to("chat-messages-with-nicknames");


        try (KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), properties)) {
            kafkaStreams.start();
        }


    }
}
