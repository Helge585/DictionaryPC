package com.kuznetsov.dictionarypc;

import java.io.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class Data {

    public static void saveDictionaryGroup(String name) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("data.txt", true))) {
            bw.write(name);
            bw.newLine();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static List<String> readDictionaryGroups() {
        ArrayList<String> result = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String str;
            while ((str = br.readLine()) != null) {
                result.add(str);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return result;
    }
}
