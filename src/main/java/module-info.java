module com.kuznetsov.dictionarypc {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.kuznetsov.dictionarypc to javafx.fxml;
    exports com.kuznetsov.dictionarypc;
}