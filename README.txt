A pesar de estar disponible JDK 22, se ha elegido JDK 21 por ser la última versión con soporte a largo plazo. El resto de versiones utilizadas han sido:
Apache 3.9.6
Spring Boot 3.2.5
JUnit 4.13.2
Spring Aspects 6.1.6
OpenApi 2.5.0
Kafka 3.1.4

El proyecto ha sido realizado con patrón MVC por ser el que más ha utilizado la desarrolladora a lo largo de su vida laboral. Se ha implementado un producer de Kafka con intención de mantener actualizados a posibles
sistemas externos que utilicen cachés o necesiten la última versión sobre la información disponible en la base de datos de la flota, por lo que se llama en cada creación y actualización, en un hilo diferente para no afectar
al rendimiento de la aplicación.

Mejoras futuras:
Gestión de excepciones a un nivel mayor de detalle con excepciones propias
Aumentar la diversidad de casos, tanto negativos como positivos, testeados en los tests unitarios y de integración
Seguridad de la API