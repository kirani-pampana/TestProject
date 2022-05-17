package com.company;

import java.io.*;
import java.util.Scanner;

public class FileTest {

    static File folder = new File("/Users/avinash/Documents/TestData");

    static PrintWriter pw;

    static {
        try {
            pw = new PrintWriter("/Users/avinash/Documents/TestData/Merge_File.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws FileNotFoundException, IOException {

            String[] Files = folder.list();

            for (String fileName : Files) {

                File f = new File(folder, fileName);

                if (f.isDirectory()) {

                    String[] files = f.list();
                    for (String file : files) {
                        File file1 = new File(f, file);
                        insertFile(file1);
                    }

                }

                if (f.isFile() && f.getName().contains("csv")) {
                    insertFile(f);
                }

            }

          // File newFile = new File ("/Users/avinash/Documents/TestData/Merge_File.csv");
           // BufferedReader bf1 = new BufferedReader(new FileReader(newFile));

            //String s = bf1.readLine();


        }
     static void insertFile(File f) throws IOException {
    BufferedReader bf = new BufferedReader(new FileReader(f));

    String header = bf.readLine();
    String eachLine = bf.readLine();

            while (eachLine != null) {
        pw.println(eachLine);
        eachLine = bf.readLine();

    }
            pw.flush();
}
}
