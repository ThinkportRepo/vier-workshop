# Kafka Connect Exercise

## File Source Connector
1. Read the documentation on the FileSourceConnector (https://docs.confluent.io/platform/current/connect/filestream_connector.html)
2. Start a FileSourceConnector that reads from a file inside the ./files folder (it's mounted into docker)
3. Test it by adding new lines to the file and read from the specified topic

## File Sink Connector
1. Read the documentation on the FileSinkConnector
2. Run it in the same way. You might need to specific the value.converter as "org.apache.kafka.connect.storage.StringConverter"
3. Test it by sending messages to the topic and read the specified file

## Combine those two
1. Add a source connector that reads strings from a file
2. Write a java application that reads from that topic, uppercases the string, and writes it to an output topic
3. Add a sink connector that reads from that output topic and writes into a file

## Write a JDBC Sink Connector
1. Read about the JDBC Sink Connector https://docs.confluent.io/kafka-connectors/jdbc/current/sink-connector/overview.html
2. Create a JDBC Sink Connector