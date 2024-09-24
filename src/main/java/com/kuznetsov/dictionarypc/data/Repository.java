package com.kuznetsov.dictionarypc.data;


import com.kuznetsov.dictionarypc.entity.WGroup;
import com.kuznetsov.dictionarypc.entity.Wordbook;
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

//methods for word
    public static Word selectWord(int wordId) {
        try {
            return WordDao.selectWord(wordId, connection);
        } catch (SQLException e) {
            return null;
        }
    }
    public static List<Word> selectWords(int wordbookId, TestConfigure.WordType wordType) {
        try {
            return WordDao.selectWords(wordbookId, wordType, connection);
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean deleteWord(int wordId) {
        try {
            return WordDao.deleteWord(wordId, connection);
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean updateWordType(int wordId, TestConfigure.WordType newType) {
        try {
            return WordDao.updateWordType(wordId, newType.getType(), connection);
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean updateWordValues(Word word) {
        try {
            return WordDao.updateWordValues(word, connection);
        } catch (SQLException e) {
            return false;
        }
    }

    public static int createWord(Word word) {
        try {
            int wordId = WordDao.createWord(word, connection);
            word.setId(wordId);
            if (wordCreatingListeners.containsKey(word.getDictId())) {
                wordCreatingListeners.get(word.getDictId()).onWordCreate(word);
            }
            return wordId;
        } catch (SQLException e) {
            return -1;
        }
    }
    public static int[] getWordsCountByWordbookId(int wordbookId) {
        try {
            return WordDao.getWordsCountByWordbookId(wordbookId, connection);
        } catch (SQLException e) {
            int[] result = new int[3];
            result[0] = result[1] = result[2] = 0;
            return result;
        }
    }

// methods for wordbook
    public static Wordbook selectWordbook(int wordbookId) {
        try {
            return WordbookDao.selectWordbook(wordbookId, connection);
        } catch (SQLException e) {
            return null;
        }
    }
    public static List<Wordbook> selectWordbooks(int wordbookGroupId) {
        try {
            return WordbookDao.selectWordbooks(wordbookGroupId, connection);
        } catch (SQLException e) {
            return null;
        }
    }

    public static int createWordbook(Wordbook wordbook) {
        try {
            int wordbookId = WordbookDao.createWordbook(wordbook, connection);
            wordbook.setId(wordbookId);
            if (wordbookCreatingListeners.containsKey(wordbook.getwGroupId())) {
                wordbookCreatingListeners.get(wordbook.getwGroupId()).onWordbookCreate(wordbook);
            }
            return wordbookId;
        } catch (SQLException e) {
            return -1;
        }
    }

    public static boolean updateWordbook(Wordbook wordbook) {
        try {
            return WordbookDao.updateWordbook(wordbook, connection);
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean deleteWordbook(int wordbookId) {
        try {
            return WordbookDao.deleteWordbook(wordbookId, connection);
        } catch (SQLException e) {
            return false;
        }
    }

// methods for wordbook groups
    public static WGroup selectWordbookGroup(int wordbookGroupId) {
        try {
            return WGroupDao.selectWGroup(wordbookGroupId, connection);
        } catch (SQLException e) {
            return null;
        }
    }
    public static List<WGroup> selectWordbookGroups() {
        try {
            return WGroupDao.selectWGroups(connection);
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean deleteWordbookGroup(int wordbookGroupId) {
        try {
            return WGroupDao.deleteWGroup(wordbookGroupId, connection);
        } catch (SQLException e) {
            return false;
        }
    }

    public static int createWordbookGroup(WGroup wGroup) {
        try {
            int wordGroupId = WGroupDao.createWGroup(wGroup, connection);
            if (wordGroupId == -1) {
                return -1;
            }
            for (WordbookGroupCreatingListener listener : wordbookGroupCreatingListeners) {
                listener.onWordbookGroupCreate(wGroup);
            }
            return wordGroupId;
        } catch (SQLException e) {
            return -1;
        }
    }
}
