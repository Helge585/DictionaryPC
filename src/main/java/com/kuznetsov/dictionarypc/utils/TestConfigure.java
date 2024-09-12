package com.kuznetsov.dictionarypc.utils;

import com.kuznetsov.dictionarypc.entity.Word;

public class TestConfigure {
    public enum TestType {
        WriteFirst("Write first"), WriteSecond("Write second");
        private final String value;
        TestType(String value) {
            this.value = value;
        }
        public String getName() {
            return value;
        }

        public static TestType getTestType(String value) {
            return switch (value) {
                case "Write first" -> TestType.WriteFirst;
                case "Write second" -> TestType.WriteSecond;
                default -> TestType.WriteFirst;
            };
        }

    }

    public enum WordType {
        New(0), Right(1), Wrong(2), All(3);
        WordType(int type) {
            this.type = type;
        }
        private final int type;
        public int getType() {
            return type;
        }
        public static WordType getWordType(int type) {
            return switch (type) {
                case 0 -> New;
                case 1 -> Right;
                default -> Wrong;
            };
        }

        public static WordType getWordType(String type) {
            return switch (type) {
                case "New" -> New;
                case "Wrong" -> Wrong;
                default -> All;
            };
        }
    }
}
