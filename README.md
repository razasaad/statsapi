# statsapi

A Service consuming and serving results in O(1) time and space complexity.

This service that serves two endpoints:
1. POST /transactions
2. GET /statistics

Payload for POST request:
```json
{
  "timestamp": 15261856678990,
  "amount": 1.8
}
```

The timestamp is supposed to be millis.

GET request returns the statistics for transactions occured in past 60s.

## Usage

To install the jar, run:
```
mvn install
```

In order to run it as a Docker Container on your machine, run:
```
mvn run-app
```
and the service will start serving on port 6000.
