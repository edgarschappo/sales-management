The problem Implement a small message processing application that satisfies the below requirements for processing sales notification messages.

Rest Service Address (POST)
http://localhost:8080/sales-management/salesService/sendMessage

Method POST - Call Examples:

E.g 1
{"message":"1", "product":"apple", "value":"10"}

E.g 2
{"message":"1", "product":"apple", "quantity":"2", "value":"10"}

E.g 3
{"message":"3", "product":"apple", "operation":"ADD", "value":"1"}

Maven Project
External Dependencies:
1. JUnit 4.4
2. Jersey 1.9

Application Server:
Apache Tomcat 8.0

Java Version: 8
