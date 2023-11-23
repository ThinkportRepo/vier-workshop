# Kafka streams exercises

## Filter map and peak
Create three separate Kafka Streams applications that perform various transformations on data streams. Each application must use a unique output topic and demonstrate different functionalities.

### Filter Application:

- Input Topic: "input-topic"
- Output Topic: "filter-output-topic"
- Transformation: Filter out all messages that do not have a length greater than 5.

### Map Application:

- Input Topic: "input-topic"
- Output Topic: "map-output-topic"
- Transformation: Transform the values of messages by converting them to uppercase.

### Peek and Combine Application:

- Input Topic: "input-topic"
- Output Topic: "peek-combine-output-topic"
- Transformation: Use the peek method to log the key and value of each message, filter messages that have a length greater than 5, and then transform the values to uppercase.

In your applications, be sure to include the necessary configuration properties for connecting to your Kafka cluster and set the appropriate serialization/deserialization classes.

## FlatMap
Create a Kafka Streams application that performs a specific transformation using the flatMap method.

- Input Topic: "input-topic"
- Output Topic: "flatmap-output-topic"
- Transformation: Utilize the flatMap method to break each input message into multiple output records. For example, if an input message's value contains a comma-separated list of words, the application should produce a separate output record for each word.
- Additional Task: Add logic to filter out any words that are less than 3 characters long before sending them to the output topic.

