module com.kuznetsov.dictionarypc {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;


    opens com.kuznetsov.dictionarypc to javafx.fxml;
    exports com.kuznetsov.dictionarypc;
    exports com.kuznetsov.dictionarypc.listener;
    opens com.kuznetsov.dictionarypc.listener to javafx.fxml;
    exports com.kuznetsov.dictionarypc.data;
    opens com.kuznetsov.dictionarypc.data to javafx.fxml;
    exports com.kuznetsov.dictionarypc.entity;
    opens com.kuznetsov.dictionarypc.entity to javafx.fxml;
    exports com.kuznetsov.dictionarypc.controller;
    opens com.kuznetsov.dictionarypc.controller to javafx.fxml;
}