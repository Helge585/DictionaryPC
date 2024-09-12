package com.kuznetsov.dictionarypc.data;

import com.kuznetsov.dictionarypc.entity.Wordbook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WordbookDao {

    private WordbookDao() {}

    //wordbook constructor
    //Wordbook(int id, String name, int groupId, int result, String lastDate)
    public static Wordbook selectWordbook(int wordbookId, Connection connection) throws SQLException {
        String sql = "SELECT * FROM Dicts WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordbookId);
        ResultSet resultSet = preparedStatement.executeQuery();
        Wordbook wordbook = null;
        if (resultSet.next()) {
            wordbook = new Wordbook(
                    resultSet.getInt(1),
                    resultSet.getString(3),
                    resultSet.getInt(2),
                    resultSet.getInt(4),
                    resultSet.getString(5)
            );
        }
        return wordbook;
    }

    public static List<Wordbook> selectWordbooks(int wordbookGroupId, Connection connection) throws SQLException {
        String sql = "SELECT * FROM Dicts WHERE Groupid = ?";
        ArrayList<Wordbook> wordbooks = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordbookGroupId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(3);
            int result = resultSet.getInt(4);
            String lastDate = resultSet.getString(5);
            wordbooks.add(new Wordbook(id, name, wordbookGroupId, result, lastDate));
        }
        return wordbooks;
    }

    public static int createWordbook(Wordbook wordbook, Connection connection) throws SQLException {
        String sql = "INSERT INTO Dicts(Groupid, Name) VALUES(?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, wordbook.getGroupId());
        preparedStatement.setString(2, wordbook.getName());
        preparedStatement.executeUpdate();
        ResultSet autoId = preparedStatement.getGeneratedKeys();
        if (autoId.next()) {
            return autoId.getInt(1);
        }
        return -1;
    }

    public static boolean updateWordbook(Wordbook wordbook, Connection connection) throws SQLException {
        String sql = "UPDATE Dicts SET Result = ?, LastDate = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordbook.getResult());
        preparedStatement.setString(2, wordbook.getLastDate());
        preparedStatement.setInt(3, wordbook.getId());
        return preparedStatement.executeUpdate() > 0;
    }

    public static boolean deleteWordbook(int wordbookId, Connection connection) throws SQLException {
        String sql = "DELETE FROM Dicts WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordbookId);
        return preparedStatement.executeUpdate() > 0;
    }

}
