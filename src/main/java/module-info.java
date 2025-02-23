module com.example.mysimplechat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.postgresql.jdbc;
    requires jbcrypt;
    requires spring.websocket;
    requires spring.context;
    requires spring.messaging;
    requires spring.beans;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires spring.data.jpa;
    requires spring.web;
    requires org.hibernate.orm.core;
    requires org.apache.tomcat.embed.core;
    requires jakarta.persistence;

//    opens com.example.mysimplechat. to javafx.fxml;
    exports com.example.security;
    exports com.example.database;
    exports com.example.validation;
    exports com.example.mysimplechat.authorization;
    exports com.example.mysimplechat.chat;
    exports com.example.mysimplechat.chat.websockets.client;
    exports com.example.mysimplechat.chat.websockets.config;
    exports com.example.mysimplechat.chat.websockets;
    exports com.example.mysimplechat.chat.chatroom;


    opens com.example.mysimplechat.chat to javafx.fxml, spring.core, spring.beans, spring.data.jpa,
            spring.context, com.fasterxml.jackson.databind, org.hibernate.orm.core;
    opens com.example.mysimplechat.authorization to javafx.fxml;
    opens com.example.mysimplechat.chat.websockets.client to javafx.fxml;
    opens com.example.mysimplechat.chat.websockets.config to com.fasterxml.jackson.databind, javafx.fxml, spring.core;
    opens com.example.mysimplechat.chat.websockets to com.fasterxml.jackson.databind, javafx.fxml, spring.core;
    opens com.example.mysimplechat.chat.chatroom to javafx.fxml, spring.core, spring.beans, spring.data.jpa,
            spring.context, com.fasterxml.jackson.databind, org.hibernate.orm.core;

//    opens com.example.mysimplechat.chat to javafx.fxml, spring.core, spring.beans, spring.context, spring.messaging;


//    opens com.example.mysimplechat.chat to spring.core;
//    opens com.example.mysimplechat.authorization to spring.core;
}