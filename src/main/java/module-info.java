module com.kuznetsov.dictionarypc {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;


    opens com.kuznetsov.dictionarypc to javafx.fxml;
    exports com.kuznetsov.dictionarypc;
}