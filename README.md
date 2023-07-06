# kabusapi-wsclient
Example of a WebSocket application receiving kabu station PUSH API.

## Requirements

Building the WebSocket application requires:
1. Java 1.7+
2. Maven

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn clean install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn clean deploy
```

Refer to the [OSSRH Guide](http://central.sonatype.org/pages/ossrh-guide.html) for more information.

## Getting Started

To try example of a WebSocket application, run the following command.

```shell
mvn exec:java
```

## Reference sites
[kabuステーション PUSH APIリファレンス](https://kabucom.github.io/kabusapi/ptal/push.html)

[kabuステーション API ｜株のことならネット証券会社【auカブコム証券】](https://kabucom.github.io/kabusapi/ptal/index.html)
