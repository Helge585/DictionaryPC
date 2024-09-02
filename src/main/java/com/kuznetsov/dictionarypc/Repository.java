package com.kuznetsov.dictionarypc;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class Repository {
    private Repository() {}
    private static Connection connection;
    private static final ArrayList<DictGroupCreatingListener> dictGroupCreatingListeners = new ArrayList<>();
    private static final HashMap<Integer, DictCreatingListener> dictCreatingListeners = new HashMap<>();

    public static void initialize() throws SQLException, IOException {
        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("database.properties"))) {
            props.load(in);
        }
        connection =  DriverManager.getConnection(props.getProperty("url"),
                props.getProperty("username"), props.getProperty("password"));
    }

    public static void close() throws SQLException {
        connection.close();
    }

    public static void setOnDictGroupCreateListener(DictGroupCreatingListener listener) {
        dictGroupCreatingListeners.add(listener);
    }

    public static void setOnDictCreatingListener(DictCreatingListener listener) {
        dictCreatingListeners.put(listener.getDictGroupId(), listener);
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

    public static void addDictionary(Dict dict) throws SQLException {
        String sql = "INSERT INTO Dicts(Groupid, Name) VALUES(?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, dict.getGroupId());
        preparedStatement.setString(2, dict.getName());
        preparedStatement.executeUpdate();
        ResultSet autoId = preparedStatement.getGeneratedKeys();
        if (autoId.next()) {
            dict.setId(autoId.getInt(1));
            if (dictCreatingListeners.containsKey(dict.getGroupId())) {
                dictCreatingListeners.get(dict.getGroupId()).onDictCreate(dict);
            }
        }
    }

    public static List<Dict> getDictsByGroupId(int groupId) throws SQLException {
        String sql = "SELECT * FROM Dicts WHERE Groupid = ?";
        ArrayList<Dict> dicts = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, groupId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            int id = resultSet.getInt(1);
            String name = resultSet.getString(3);
            //System.out.printf("%d - %s\n", id, name);
            dicts.add(new Dict(id, name, groupId));
        }
        return dicts;
    }
}
