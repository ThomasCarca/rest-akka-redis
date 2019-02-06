# A simple Akka rest api with Redis storage

The Akka HTTP server has two purposes :
- listen to requests
- generate random logs to a redis server

For now, you must have :
- sbt installed
- a redis server running on localhost:6379

In the future, the instances might be running on containers.

This project is under development.

# HTTP Routes

***GET** /logs[?limit&level]*
will return a list of the logs in the redis storage.

>limit [Integer] is optional and will filter the amount of logs returned

>level [String] is optional and will filter the level of logs returned. (DEBUG, INFO, WARNING, ERROR, CRITICAL)

***GET** /logs/number*
will return the amount of logs in the redis storage