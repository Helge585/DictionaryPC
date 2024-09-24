package com.kuznetsov.dictionarypc.data;

import com.kuznetsov.dictionarypc.entity.WGroup;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WGroupDao {
    public static WGroup selectWGroup(int wGroupId, Connection connection) throws SQLException {
        String sql = "SELECT * FROM WGroups WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wGroupId);
        ResultSet resultSet = preparedStatement.executeQuery();
        WGroup wGroup = null;
        if (resultSet.next()) {
            wGroup = new WGroup(
                    wGroupId,
                    resultSet.getString(2)
            );
        }
        return wGroup;
    }

    public static List<WGroup> selectWGroups(Connection connection) throws SQLException {
        ArrayList<WGroup> wGroups = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM WGroups");
        while(resultSet.next()){
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            wGroups.add(new WGroup(id, name));
        }
        return wGroups;
    }

    //in Wordbooks table:
    //FOREIGN KEY (WGroupId) REFERENCES WGroups(Id) ON DELETE CASCADE
    //All wordbooks of this WGroup will be deleted
    public static boolean deleteWGroup(int wGroupId, Connection connection) throws SQLException {
        String sql = "DELETE FROM WGroups WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wGroupId);
        return preparedStatement.executeUpdate() > 0;
    }

    //return auto generated key for inserted wgroup
    public static int createWGroup(WGroup wGroup, Connection connection) throws SQLException {
        String sql = "INSERT INTO WGroups(Name) VALUES (?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, wGroup.getName());
        preparedStatement.executeUpdate();
        ResultSet autoId = preparedStatement.getGeneratedKeys();
        if (autoId.next()) {
            return autoId.getInt(1);
        }
        return -1;
    }
}
