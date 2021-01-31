# PokeAimPI
RPC API backend for applications made for PokeaimMD

## Development Setup Instruction
* [Install MySQL](https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/)
* Create database "pokeaimpi" and (optional) application user
* Install Eclipse (or Java IDE of choice)
* Clone and import the project as a Maven project (you may need to install the m2 pulgin for Eclipse)
* Add configuration files
     * application.properties in src/main/resources
       * spring.jpa.hibernate.ddl-auto=none
       * spring.datasource.url=
       * spring.datasource.username=
       * spring.datasource.password=
       * spring.jpa.database=
       * server.servlet.context-path=
       * server.port=9000
     * liquibase.properties in src/main/resources/liquibase.properties
       * driver=
       * url=
       * username=
       * password=
       * dropFirst=
* Run unit tests to ensure the project was set up correctly
* Run Maven with goals `clean liquibase:update spring-boot:run`

## Configure Pub-Sub
* Setup an instance of [RabbitMQ](https://www.rabbitmq.com/install-debian.html)
* Enable the `pub-sub` profile
* add the following to your application.properties:
     * spring.rabbitmq.host=
     * spring.rabbitmq.password=
     * spring.rabbitmq.port=5672 
     * spring.rabbitmq.username=