# Kafka Client Exercises with Schema Registry

## Producer
1. Erstellen Sie ein Avro-Schema für ein Pokémon, das folgende Felder enthält:
    - `name`: Der Name des Pokémon (z. B. "Pikachu").
    - `type`: Der Haupttyp des Pokémon (z. B. "Elektro").
    - `pokedexNumber`: Die eindeutige Nummer des Pokémon im Pokédex.

2. Registrieren Sie das Avro-Schema in der Schema Registry.

3. Entwickeln Sie einen Kafka-Producer, der das Avro-Schema verwendet und Pokémon-Daten in ein Kafka-Topic namens `pokemon-data` sendet.

## Consumer
Schreibe einen passenden Consumer dazu.

## Kafka-Streaming-Anwendung für Pokémon-Handel

**Hintergrund:**
In der Welt von Pokémon können Trainer ihre Pokémon mit anderen Trainern tauschen. Wir möchten ein System entwickeln, das diese Handelstransaktionen überwacht und Statistiken dazu erstellt.

**Ziel:**
Entwickeln Sie eine Kafka-Streaming-Anwendung, die Handelstransaktionen zwischen Trainern verarbeitet, den Handelswert berechnet und spezifische Alerts basierend auf den Handelswerten generiert.

### Anforderungen:

1. **Datenmodelle**:
    - Erstellen Sie ein Avro-Schema für ein Pokémon:
        - `name`: Der Name des Pokémon.
        - `type`: Der Haupttyp des Pokémon.
        - `pokedexNumber`: Die eindeutige Nummer des Pokémon im Pokédex.
        - `value`: Ein numerischer Wert, der den Handelswert des Pokémon darstellt.
    - Erstellen Sie ein weiteres Avro-Schema für eine Handelstransaktion:
        - `trainerFrom`: Der Name des Trainers, der das Pokémon gibt.
        - `trainerTo`: Der Name des Trainers, der das Pokémon erhält.
        - `pokemonGiven`: Das Pokémon, das vom `trainerFrom` gegeben wurde.
        - `pokemonReceived`: Das Pokémon, das `trainerTo` erhalten hat.

2. **Topics**:
    - Erzeugen Sie ein Kafka-Topic namens `pokemon-trades` für Handelstransaktionen.
    - Erzeugen Sie ein weiteres Kafka-Topic namens `trade-alerts` für Alerts, die basierend auf bestimmten Handelskriterien generiert werden.

3. **Streaming-Logik**:
    - Konsumieren Sie Nachrichten aus dem `pokemon-trades` Topic.
    - Für jede Handelstransaktion, berechnen Sie den Wertunterschied zwischen `pokemonGiven` und `pokemonReceived`.
    - Wenn der Wertunterschied größer als 50 ist, generieren Sie einen Alert in Form einer Nachricht:
      ```
      "Großer Handelsunterschied! Trainer [trainerFrom] handelte ein Pokémon im Wert von [pokemonGivenValue] gegen ein Pokémon im Wert von [pokemonReceivedValue]."
      ```
    - Senden Sie diese Alert-Nachrichten an das `trade-alerts` Topic.

4. **Fehlerbehandlung**:
    - Die Anwendung sollte mit Schema-Änderungen oder -Inkompatibilitäten umgehen können und dabei keine Nachrichten verlieren.
    - Bei fehlerhaften Transaktionsdaten sollte die Anwendung eine Fehlermeldung protokollieren, aber weiterhin andere Nachrichten verarbeiten.

5. **Bonus**: Implementieren Sie eine einfache Aggregationslogik, die die Top 5 am häufigsten gehandelten Pokémon in den letzten 24 Stunden ermittelt und diese Information alle 2 Stunden in ein separates Kafka-Topic `top-trades` sendet.