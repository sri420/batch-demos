Build
=====
mvn clean package

Run
===
mvn spring-boot:run

Endpoint
========
POST http://localhost:8080/batch

Request Body:
============
[
    {
        "firstName":"sridhar",
        "lastName":"balasubramanian"
    }
    ,
    {
        "firstName":"Dexter",
        "lastName":"Morgan"
    }

]

