package com.kuznetsov.dictionarypc.data;


import com.kuznetsov.dictionarypc.entity.Wordbook;
import com.kuznetsov.dictionarypc.entity.WordbookGroup;
import com.kuznetsov.dictionarypc.entity.Word;
import com.kuznetsov.dictionarypc.listener.WordbookCreatingListener;
import com.kuznetsov.dictionarypc.listener.WordbookGroupCreatingListener;
import com.kuznetsov.dictionarypc.listener.WordCreatingListener;
import com.kuznetsov.dictionarypc.utils.TestConfigure;

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
    private static final ArrayList<WordbookGroupCreatingListener>
            wordbookGroupCreatingListeners = new ArrayList<>();
    private static final HashMap<Integer, WordbookCreatingListener>
            wordbookCreatingListeners = new HashMap<>();
    private static final HashMap<Integer, WordCreatingListener>
            wordCreatingListeners = new HashMap<>();

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

    public static void setOnWordbookGroupCreateListener(WordbookGroupCreatingListener listener) {
        wordbookGroupCreatingListeners.add(listener);
    }

    public static void setOnWordbookCreatingListener(WordbookCreatingListener listener) {
        wordbookCreatingListeners.put(listener.getWordbookGroupId(), listener);
    }

    public static void setOnWordCreatingListener(WordCreatingListener listener) {
        wordCreatingListeners.put(listener.getWordbookId(), listener);
    }

    public static List<WordbookGroup> getAllWordbookGroups() throws SQLException {
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

    public static void addWordbookGroup(WordbookGroup wordbookGroup) throws SQLException {
        String sql = "INSERT INTO DictGroups(Name) VALUES (?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, wordbookGroup.getName());
        preparedStatement.executeUpdate();
        for (WordbookGroupCreatingListener listener : wordbookGroupCreatingListeners) {
            listener.onWordbookGroupCreate(wordbookGroup);
        }
    }

    public static void addWordbook(Wordbook wordbook) throws SQLException {
        String sql = "INSERT INTO Dicts(Groupid, Name) VALUES(?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, wordbook.getGroupId());
        preparedStatement.setString(2, wordbook.getName());
        preparedStatement.executeUpdate();
        ResultSet autoId = preparedStatement.getGeneratedKeys();
        if (autoId.next()) {
            wordbook.setId(autoId.getInt(1));
            if (wordbookCreatingListeners.containsKey(wordbook.getGroupId())) {
                wordbookCreatingListeners.get(wordbook.getGroupId()).onWordbookCreate(wordbook);
            }
        }
    }

    public static List<Wordbook> getWordbooksByGroupId(int wordbookGroupId) throws SQLException {
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
            //System.out.printf("%d - %s\n", id, name);
            wordbooks.add(new Wordbook(id, name, wordbookGroupId, result, lastDate));
        }
        return wordbooks;
    }

    public static void addWord(Word word) throws SQLException {
        String sql = "INSERT INTO Words(DictId, First, Second, FirstExample, SecondExample) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, word.getDictId());
        preparedStatement.setString(2, word.getFirst());
        preparedStatement.setString(3, word.getSecond());
        preparedStatement.setString(4, word.getFirstExample());
        preparedStatement.setString(5, word.getSecondExample());
        preparedStatement.executeUpdate();
        ResultSet autoId = preparedStatement.getGeneratedKeys();
        if (autoId.next()) {
            word.setId(autoId.getInt(1));
            if (wordCreatingListeners.containsKey(word.getDictId())) {
                wordCreatingListeners.get(word.getDictId()).onWordCreate(word);
            }
        }
    }

    public static List<Word> getWordsByWordbookId(int wordbookId) throws SQLException {
        String sql = "SELECT * FROM Words WHERE Dictid = ?";
        ArrayList<Word> words = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordbookId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            int id = resultSet.getInt(1);
            String first = resultSet.getString(3);
            String second = resultSet.getString(4);
            String firstExample = resultSet.getString(5);
            String secondExample = resultSet.getString(6);
            int wordType = resultSet.getInt(7);
            words.add(new Word(id, wordbookId, first, second,
                    firstExample, secondExample, TestConfigure.WordType.getWordType(wordType)));
        }
        return words;
    }

    public static int getWordsCountByWordbookId(int wordbookId) throws SQLException {
        String sql = "SELECT * FROM Words WHERE Dictid = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordbookId);
        ResultSet resultSet = preparedStatement.executeQuery();
        int count = 0;
        while(resultSet.next()){
            ++count;
        }
        return count;
    }

    public static int[] getWordTypesCountByWordbookId(int wordbookId) throws SQLException {
        int[] result = new int[3];
        result[0] = result[1] = result[2] = 0;
        String sql = "SELECT * FROM Words WHERE Dictid = ?";
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

    public static List<Word> getWordsByWordbookIdAndWordType(int wordbookId,
                                                             TestConfigure.WordType wordType) throws SQLException {
        String sql = "SELECT * FROM Words WHERE Dictid = ? AND Type = ?";
        ArrayList<Word> words = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordbookId);
        preparedStatement.setInt(2, wordType.getType());
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            int id = resultSet.getInt(1);
            String first = resultSet.getString(3);
            String second = resultSet.getString(4);
            String firstExample = resultSet.getString(5);
            String secondExample = resultSet.getString(6);
            words.add(new Word(id, wordbookId, first, second,
                    firstExample, secondExample, wordType));
        }
        return words;
    }

    public static void updateWordbook(Wordbook wordbook) throws SQLException {
        String sql = "UPDATE Dicts SET Result = ?, LastDate = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordbook.getResult());
        preparedStatement.setString(2, wordbook.getLastDate());
        preparedStatement.setInt(3, wordbook.getId());
        preparedStatement.executeUpdate();
    }

    //wordbook constructor
    //Wordbook(int id, String name, int groupId, int result, String lastDate)
    public static Wordbook getWordbookById(int wordbookId) throws SQLException {
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

    public static void updateWordType(int wordId, TestConfigure.WordType newType) throws SQLException {
        String sql = "UPDATE Words SET Type = ? WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, newType.getType());
        preparedStatement.setInt(2, wordId);
        preparedStatement.executeUpdate();
    }

    public static void updateWordValues(Word word) throws SQLException {
        String sql = "UPDATE Words SET First = ?, Second = ?, FirstExample = ?, SecondExample = ? WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, word.getFirst());
        preparedStatement.setString(2, word.getSecond());
        preparedStatement.setString(3, word.getFirstExample());
        preparedStatement.setString(4, word.getSecondExample());
        preparedStatement.setInt(5, word.getId());
        preparedStatement.executeUpdate();
    }

    public static void deleteWordbookGroup(int wordbookGroupId) throws SQLException {
        String sql = "DELETE FROM DictGroups WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordbookGroupId);
        preparedStatement.executeUpdate();
    }

    public static void deleteWordbook(int wordbookId) throws SQLException {
        String sql = "DELETE FROM Dicts WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordbookId);
        preparedStatement.executeUpdate();
    }

    public static void deleteWord(int wordId) throws SQLException {
        String sql = "DELETE FROM Words WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, wordId);
        preparedStatement.executeUpdate();
    }
}
