package com.kuznetsov.dictionarypc.data;

import com.kuznetsov.dictionarypc.entity.WordbookGroup;
import com.kuznetsov.dictionarypc.listener.WordbookGroupCreatingListener;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WordbookGroupDao {
    public static WordbookGroup selectWordbook(int wordbookGroupId, Connection connection) throws SQLException {
        String sql = "SELECT * FROM DictGroups WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordbookGroupId);
        ResultSet resultSet = preparedStatement.executeQuery();
        WordbookGroup wordbookGroup = null;
        if (resultSet.next()) {
            wordbookGroup = new WordbookGroup(
                    wordbookGroupId,
                    resultSet.getString(2)
            );
        }
        return wordbookGroup;
    }

    public static List<WordbookGroup> selectWordbookGroups(Connection connection) throws SQLException {
        ArrayList<WordbookGroup> wordbookGroups = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM DictGroups");
        while(resultSet.next()){
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            //System.out.printf("%d - %s\n", id, name);
            wordbookGroups.add(new WordbookGroup(id, name));
        }
        return wordbookGroups;
    }

    public static boolean deleteWordbookGroup(int wordbookGroupId, Connection connection) throws SQLException {
        String sql = "DELETE FROM DictGroups WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordbookGroupId);
        return preparedStatement.executeUpdate() > 0;
    }

    public static int createWordbookGroup(WordbookGroup wordbookGroup, Connection connection) throws SQLException {
        String sql = "INSERT INTO DictGroups(Name) VALUES (?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, wordbookGroup.getName());
        preparedStatement.executeUpdate();
        ResultSet autoId = preparedStatement.getGeneratedKeys();
        if (autoId.next()) {
            return autoId.getInt(1);
        }
        return -1;
    }
}
