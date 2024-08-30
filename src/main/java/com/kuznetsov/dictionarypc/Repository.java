package com.kuznetsov.dictionarypc;


import javafx.scene.Group;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Repository {
    private Repository() {}
    private static Connection connection;
    private static ArrayList<DictGroupCreatingListener> dictGroupCreatingListeners = new ArrayList<>();

    public static void setOnDictGroupCreateListener(DictGroupCreatingListener listener) {
        dictGroupCreatingListeners.add(listener);
    }

    public static List<DictGroup> getAllGroups() throws SQLException {
        ArrayList<DictGroup> dictGroups = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM DictGroups");
        while(resultSet.next()){
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            //System.out.printf("%d - %s\n", id, name);
            dictGroups.add(new DictGroup(id, name));
        }
        return dictGroups;
    }

    public static void addDictGroup(DictGroup dictGroup) throws SQLException {
        String sql = "INSERT INTO DictGroups(Name) VALUES (?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, dictGroup.getName());
        preparedStatement.executeUpdate();
        for (DictGroupCreatingListener listener : dictGroupCreatingListeners) {
            listener.onDictGroupCreate(dictGroup);
        }
    }
    public static void close() throws SQLException {
        connection.close();
    }
    public static void initialize() throws SQLException, IOException {
        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("database.properties"))) {
            props.load(in);
        }
        connection =  DriverManager.getConnection(props.getProperty("url"),
                props.getProperty("username"), props.getProperty("password"));
    }
}
