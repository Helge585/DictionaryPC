package com.kuznetsov.dictionarypc.data;


import com.kuznetsov.dictionarypc.entity.Wordbook;
import com.kuznetsov.dictionarypc.entity.WordbookGroup;
import com.kuznetsov.dictionarypc.entity.Word;
import com.kuznetsov.dictionarypc.listener.WordbookCreatingListener;
import com.kuznetsov.dictionarypc.listener.WordbookGroupCreatingListener;
import com.kuznetsov.dictionarypc.listener.WordCreatingListener;

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
        while(resultSet.next()){
            int id = resultSet.getInt(1);
            String name = resultSet.getString(3);
            //System.out.printf("%d - %s\n", id, name);
            wordbooks.add(new Wordbook(id, name, wordbookGroupId));
        }
        return wordbooks;
    }

    public static List<Word> getTestWords() {
        ArrayList<Word> words = new ArrayList<>();
        for (int i = 0; i < 100; ++i) {
            String buf = (i + "").repeat(10);
            Word word = new Word(-1, -1, buf, buf, buf, buf);
            words.add(word);
        }
        return words;
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
            words.add(new Word(id, wordbookId, first, second, firstExample, secondExample));
        }
        return words;
    }
}
