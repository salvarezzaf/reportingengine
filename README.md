# Daily Trade Reporting Engine [![Build Status](https://travis-ci.org/salvarezzaf/reportingengine.svg?branch=master)](https://travis-ci.org/salvarezzaf/reportingengine)
Reportng engine processes instruction data sent by various clients to be executed in the international market. It calculates effective settlement date of trade instructions by taking into account working week of different currency markets  and creates a report that shows the following:

- Amount in USD settled incoming everyday
- Amount in USD settled outgoing everyday
- Ranking of entities based on incoming amount
- Ranking of entities based on outgoing amount

Report is formatted according to the current locale and so dates and amount are formatted according to standards. Default locale for application is US so date will have format MM/dd/YYYY and amounts $##,###.##

## Requirements

- Java 8 
- Maven

## How to run it

To run tests and build

```bash
mvn clean install
```

To run report:

```bash
mvn exec:java
```

or if you prefer Java CLI:

```bash
java -cp target/classes com.jpmc.reporting.Application
```





