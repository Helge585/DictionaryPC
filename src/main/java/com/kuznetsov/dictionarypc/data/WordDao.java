package com.kuznetsov.dictionarypc.data;

import com.kuznetsov.dictionarypc.entity.Word;
import com.kuznetsov.dictionarypc.utils.TestConfigure;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WordDao {

    private WordDao() {}
//    Word constructor:
//    (int id, int dictId, String russianWord, String foreignWord,
//    String russianExample, String foreignExample, TestConfigure.WordType wordType)
//
//    Words table in DB
//    Id INT AUTO_INCREMENT PRIMARY KEY,
//    WordbookId INT,
//    RussianWord VARCHAR(200),
//    ForeignWord VARCHAR(200),
//    RussianExample VARCHAR(400),
//    ForeignExample VARCHAR(400),
//    Type INT,
    private static Word getWordFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        int wordbookId = resultSet.getInt(2);
        String first = resultSet.getString(3);
        String second = resultSet.getString(4);
        String firstExample = resultSet.getString(5);
        String secondExample = resultSet.getString(6);
        TestConfigure.WordType wordType =
                TestConfigure.WordType.getWordType(resultSet.getInt(7));
        return new Word(id, wordbookId, first, second,
                firstExample, secondExample, wordType);
    }

// select methods
    public static Word selectWord(int wordId, Connection connection) throws SQLException {
        String sql = "SELECT * FROM Words WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return getWordFromResultSet(resultSet);
        }
        return null;
    }

    public static List<Word> selectWords(int wordbookId, TestConfigure.WordType wordType,
                                         Connection connection) throws SQLException {
        ArrayList<Word> selectedWords = new ArrayList<>();
        String sql = "SELECT * FROM Words WHERE 1=1";
        if (wordbookId != -1) {
            sql += " AND WordbookId = ?";
        }
        if (wordType != null) {
            sql += " AND Type = ?";
        }
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        if (wordbookId != -1) {
            preparedStatement.setInt(1, wordbookId);
        }
        if (wordType != null) {
            preparedStatement.setInt(2, wordType.getType());
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            selectedWords.add(getWordFromResultSet(resultSet));
        }
        return selectedWords;
    }

// delete methods
    public static boolean deleteWord(int wordId, Connection connection) throws SQLException {
        String sql = "DELETE FROM Words WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordId);
        return preparedStatement.executeUpdate() > 0;
    }

// update methods
    public static boolean updateWordValues(Word word, Connection connection) throws SQLException {
        String sql = "UPDATE Words SET RussianWord = ?, ForeignWord = ?, " +
                "RussianExample = ?, ForeignExample = ? WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, word.getRussianWord());
        preparedStatement.setString(2, word.getForeignWord());
        preparedStatement.setString(3, word.getRussianExample());
        preparedStatement.setString(4, word.getForeignExample());
        preparedStatement.setInt(5, word.getId());
        return preparedStatement.executeUpdate() > 0;
    }

    public static boolean updateWordType(int wordId, int newType,
                                         Connection connection) throws SQLException {
        String sql = "UPDATE Words SET Type = ? WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, newType);
        preparedStatement.setInt(2, wordId);
        return preparedStatement.executeUpdate() > 0;
    }

// insert methods

    //return auto generated key for inserted word
    public static int createWord(Word word, Connection connection) throws SQLException {
        String sql = "INSERT INTO Words(WordbookId, RussianWord, ForeignWord, " +
                "RussianExample, ForeignExample, Type) VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, word.getDictId());
        preparedStatement.setString(2, word.getRussianWord());
        preparedStatement.setString(3, word.getForeignWord());
        preparedStatement.setString(4, word.getRussianExample());
        preparedStatement.setString(5, word.getForeignExample());
        preparedStatement.setInt(6, word.getWordType().getType());
        preparedStatement.executeUpdate();
        ResultSet autoId = preparedStatement.getGeneratedKeys();
        if (autoId.next()) {
            return autoId.getInt(1);
        }
        return -1;
    }
//other methods
    public static int[] getWordsCountByWordbookId(int wordbookId, Connection connection) throws SQLException {
        int[] result = new int[3];
        result[0] = result[1] = result[2] = 0;
        String sql = "SELECT * FROM Words WHERE WordbookId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordbookId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            int type = resultSet.getInt(7);
            if (type == 0) {
                ++result[0];
            } else if (type == 1) {
                ++result[1];
            } else if (type == 2) {
                ++result[2];
            }
        }
        return result;
    }
}
