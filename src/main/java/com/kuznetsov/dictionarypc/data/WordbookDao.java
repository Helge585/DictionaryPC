package com.kuznetsov.dictionarypc.data;

import com.kuznetsov.dictionarypc.entity.Wordbook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WordbookDao {

    private WordbookDao() {}

    //comment added 24.09.2024
    //wordbook constructor:
    //Wordbook(int id, String name, int wGroupId, int result, String lastDate)

    //Wordbook table in DB:
    // Table name - Wordbooks
//    1. Id INT AUTO_INCREMENT PRIMARY KEY,
//    2. WGroupId INT,
//    3. Name VARCHAR(200),
//    4. Result INT,
//    5. LastDate VARCHAR(15),
    private static Wordbook getWordbookFromResultSet(ResultSet resultSet) throws SQLException {
        return new Wordbook(
                resultSet.getInt(1),
                resultSet.getString(3),
                resultSet.getInt(2),
                resultSet.getInt(4),
                resultSet.getString(5)
        );
    }

    public static Wordbook selectWordbook(int wordbookId, Connection connection) throws SQLException {
        String sql = "SELECT * FROM Wordbooks WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordbookId);
        ResultSet resultSet = preparedStatement.executeQuery();
        Wordbook wordbook = null;
        if (resultSet.next()) {
            wordbook = getWordbookFromResultSet(resultSet);
        }
        return wordbook;
    }

    public static List<Wordbook> selectWordbooks(int wGroupId, Connection connection) throws SQLException {
        String sql = "SELECT * FROM Wordbooks WHERE WGroupId = ?";
        ArrayList<Wordbook> wordbooks = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wGroupId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            wordbooks.add(getWordbookFromResultSet(resultSet));
        }
        return wordbooks;
    }

    //return auto generated key for inserted wordbook
    public static int createWordbook(Wordbook wordbook, Connection connection) throws SQLException {
        String sql = "INSERT INTO Wordbooks(WGroupId, Name, Result, LastDate) VALUES(?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, wordbook.getwGroupId());
        preparedStatement.setString(2, wordbook.getName());
        preparedStatement.setInt(3, wordbook.getResult());
        preparedStatement.setString(4, wordbook.getLastDate());
        preparedStatement.executeUpdate();
        ResultSet autoId = preparedStatement.getGeneratedKeys();
        if (autoId.next()) {
            return autoId.getInt(1);
        }
        return -1;
    }

    public static boolean updateWordbook(Wordbook wordbook, Connection connection) throws SQLException {
        String sql = "UPDATE Wordbooks SET Result = ?, LastDate = ? WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordbook.getResult());
        preparedStatement.setString(2, wordbook.getLastDate());
        preparedStatement.setInt(3, wordbook.getId());
        return preparedStatement.executeUpdate() > 0;
    }

    //In Words table:
    //FOREIGN KEY (WordbookId) REFERENCES Wordbooks(Id) ON DELETE CASCADE
    //All words of this wordbook will be deleted
    public static boolean deleteWordbook(int wordbookId, Connection connection) throws SQLException {
        String sql = "DELETE FROM Wordbooks WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordbookId);
        return preparedStatement.executeUpdate() > 0;
    }

}
